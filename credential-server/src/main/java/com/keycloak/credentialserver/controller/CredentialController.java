package com.keycloak.credentialserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.keycloak.credentialserver.entity.IdentityProof;
import com.keycloak.credentialserver.service.IdentityProofService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CredentialController {

    private static final Logger logger = LoggerFactory.getLogger(CredentialController.class);

    @Autowired
    private IdentityProofService identityProofService;

    // ========== 原有验证接口（无需修改） ==========
    @PostMapping("/verify")
    public ResponseEntity<Map<String, Object>> verifyCredential(@RequestBody Map<String, String> request) {
        logger.info("收到凭证比对请求：{}", request);
        Map<String, Object> response = new HashMap<>();

        try {
            String finalCredentialHash = request.get("finalCredentialHash");
            IdentityProof validProof = identityProofService.verifyFinalCredentialHash(finalCredentialHash);

            if (validProof != null) {
                response.put("success", true);
                response.put("userId", validProof.getUserId());
                response.put("message", "凭证比对成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "凭证无效，请使用账号密码登录");
                return ResponseEntity.status(401).body(response);
            }

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "服务异常：" + e.getMessage());
            logger.error("比对过程异常", e);
            return ResponseEntity.status(500).body(response);
        }
    }

    // ========== 新数据入库接口（适配你的表） ==========
    @PostMapping("/save-auto-user")
    public ResponseEntity<Map<String, Object>> saveAutoUser(@RequestBody IdentityProof identityProof) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean success = identityProofService.saveCredentialWithAutoUser(identityProof);
            if (success) {
                response.put("success", true);
                response.put("message", "凭证保存成功，已自动创建Keycloak用户");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "凭证保存失败");
                return ResponseEntity.status(500).body(response);
            }
        } catch (Exception e) {
            logger.error("保存凭证异常", e);
            response.put("success", false);
            response.put("message", "服务异常：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // ========== 新增：补全所有空user_id的接口 ==========
    @PostMapping("/fill-empty-userid")
    public ResponseEntity<Map<String, Object>> fillEmptyUserId() {
        Map<String, Object> response = new HashMap<>();
        try {
            identityProofService.fillEmptyUserId();

            response.put("success", true);
            response.put("message", "空user_id补全完成");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("补全空user_id异常", e);
            response.put("success", false);
            response.put("message", "补全失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}

