package com.keycloak.credentialserver.controller; // 包名与路径完全一致

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.keycloak.credentialserver.entity.IdentityProof; // 导入当前包下的entity
import com.keycloak.credentialserver.service.IdentityProofService; // 导入当前包下的service

import java.util.HashMap;
import java.util.Map;

/**
 * 凭证比对接口（对外暴露）
 */
@RestController
@RequestMapping("/api")
public class CredentialController {

    private static final Logger logger = LoggerFactory.getLogger(CredentialController.class);

    @Autowired
    private IdentityProofService identityProofService;

    @PostMapping("/verify")
    public ResponseEntity<Map<String, Object>> verifyCredential(@RequestBody Map<String, String> request) {
        logger.info("收到凭证比对请求：{}", request);
        Map<String, Object> response = new HashMap<>();

        try {
            String finalCredentialHash = request.get("finalCredentialHash");
            IdentityProof validProof = identityProofService.verifyFinalCredentialHash(finalCredentialHash);

            if (validProof != null) {
                response.put("success", true);
                response.put("userId", validProof.getPhoneNumberHash()); //  返回phoneNumberHash作为用户ID
                response.put("message", "凭证比对成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "凭证无效，请使用账号密码登录");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "服务异常：" + e.getMessage());
            logger.error("比对过程异常", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}

