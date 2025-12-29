package com.keycloak.credentialserver.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class KeycloakUserUtil {

    @Value("${keycloak.server-url}")
    private String serverUrl;
    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.client-id}")
    private String clientId;
    @Value("${keycloak.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    // 构造函数初始化 RestTemplate 和 ObjectMapper
    public KeycloakUserUtil() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * 获取 Keycloak Access Token（修复 MultiValueMap 类型问题）
     */
    private String getAccessToken() {
        try {
            // 1. 正确构建 MultiValueMap 类型的参数（核心修复）
            String tokenUrl = String.format("%s/realms/%s/protocol/openid-connect/token", serverUrl, realm);
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "client_credentials");
            params.add("client_id", clientId);
            params.add("client_secret", clientSecret);

            // 2. 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

            // 3. 发送请求并解析 Token
            ResponseEntity<String> response = restTemplate.postForEntity(tokenUrl, request, String.class);
            Map<String, Object> tokenMap = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
            return tokenMap.get("access_token").toString();
        } catch (Exception e) {
            throw new RuntimeException("获取 Keycloak Token 失败：" + e.getMessage(), e);
        }
    }

    /**
     * 创建 Keycloak 用户（纯 RestTemplate，无任何类型错误）
     */
    public String createKeycloakUser(String finalCredentialHash) {
        try {
            String accessToken = getAccessToken();
            String userApiUrl = String.format("%s/admin/realms/%s/users", serverUrl, realm);

            // 第一步：检查用户是否已存在
            String searchUrl = String.format("%s/admin/realms/%s/users?username=%s", serverUrl, realm, finalCredentialHash);
            HttpHeaders searchHeaders = new HttpHeaders();
            searchHeaders.set("Authorization", "Bearer " + accessToken);
            HttpEntity<Void> searchRequest = new HttpEntity<>(searchHeaders);
            
            ResponseEntity<String> searchResponse = restTemplate.exchange(
                    searchUrl,
                    HttpMethod.GET,
                    searchRequest,
                    String.class
            );
            List<Map<String, Object>> userList = objectMapper.readValue(searchResponse.getBody(), new TypeReference<List<Map<String, Object>>>() {});
            if (!userList.isEmpty()) {
                return userList.get(0).get("id").toString();
            }

            // 第二步：构建用户数据（避免 Map 类型错误）
            String userJson = String.format("{\n" +
                    "  \"username\": \"%s\",\n" +
                    "  \"email\": \"%s@auto-generated.com\",\n" +
                    "  \"enabled\": true,\n" +
                    "  \"emailVerified\": true,\n" +
                    "  \"credentials\": [\n" +
                    "    {\n" +
                    "      \"type\": \"password\",\n" +
                    "      \"value\": \"AutoPass%s\",\n" +
                    "      \"temporary\": false\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}", finalCredentialHash, finalCredentialHash, System.currentTimeMillis());

            // 第三步：发送创建用户请求
            HttpHeaders createHeaders = new HttpHeaders();
            createHeaders.set("Authorization", "Bearer " + accessToken);
            createHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> createRequest = new HttpEntity<>(userJson, createHeaders);

            ResponseEntity<Void> createResponse = restTemplate.exchange(
                    userApiUrl,
                    HttpMethod.POST,
                    createRequest,
                    Void.class
            );
            String location = createResponse.getHeaders().getLocation().toString();
            return location.substring(location.lastIndexOf("/") + 1);

        } catch (Exception e) {
            throw new RuntimeException("创建 Keycloak 用户失败：" + e.getMessage(), e);
        }
    }
}

