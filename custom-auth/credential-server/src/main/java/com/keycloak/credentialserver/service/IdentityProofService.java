package com.keycloak.credentialserver.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper; // 新增导入
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.keycloak.credentialserver.entity.IdentityProof;
import com.keycloak.credentialserver.mapper.IdentityProofMapper;

@Service
public class IdentityProofService {

    private static final Logger logger = LoggerFactory.getLogger(IdentityProofService.class);

    @Resource
    private IdentityProofMapper identityProofMapper;

    public IdentityProof verifyFinalCredentialHash(String finalCredentialHash) {
        logger.info("开始比对最终凭证哈希：{}", finalCredentialHash);

        if (finalCredentialHash == null || finalCredentialHash.trim().isEmpty()) {
            logger.warn("传入的哈希字符串为空");
            return null;
        }

        // 核心修改：显式指定查询字段，强制包含user_id
        QueryWrapper<IdentityProof> queryWrapper = new QueryWrapper<>();
        // 指定要查询的所有字段（必须包含user_id）
        queryWrapper.select("id", "final_credential_hash", "phone_number_hash", "user_id")
                   .eq("final_credential_hash", finalCredentialHash);

        IdentityProof identityProof = identityProofMapper.selectOne(queryWrapper);

        if (identityProof != null) {
            logger.info("查询到的完整记录：id={}, finalCredentialHash={}, userId={}", 
                        identityProof.getId(), 
                        identityProof.getFinalCredentialHash(), 
                        identityProof.getUserId()); // 打印详细信息
            logger.info("比对成功，关联userId：{}", identityProof.getUserId());
            return identityProof;
        } else {
            logger.warn("比对失败，未查询到匹配记录");
            return null;
        }
    }
}

