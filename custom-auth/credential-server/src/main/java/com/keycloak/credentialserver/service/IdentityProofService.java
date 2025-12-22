package com.keycloak.credentialserver.service; // 包名与路径完全一致

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.keycloak.credentialserver.entity.IdentityProof; // 导入当前包下的entity
import com.keycloak.credentialserver.mapper.IdentityProofMapper; // 导入当前包下的mapper

/**
 * 身份凭证业务层（核心比对逻辑）
 */
@Service
public class IdentityProofService {

    private static final Logger logger = LoggerFactory.getLogger(IdentityProofService.class);

    @Resource
    private IdentityProofMapper identityProofMapper;

    /**
     * 比对最终凭证哈希
     * @param finalCredentialHash 认证器传入的哈希字符串
     * @return 匹配的实体（null表示失败）
     */
    public IdentityProof verifyFinalCredentialHash(String finalCredentialHash) {
        logger.info("开始比对最终凭证哈希：{}", finalCredentialHash);

        // 参数校验
        if (finalCredentialHash == null || finalCredentialHash.trim().isEmpty()) {
            logger.warn("传入的哈希字符串为空");
            return null;
        }

        // 数据库查询
        IdentityProof identityProof = identityProofMapper.selectByFinalCredentialHash(finalCredentialHash);

        if (identityProof != null) {
            logger.info("比对成功，关联phoneNumberHash：{}", identityProof.getPhoneNumberHash());
            return identityProof;
        } else {
            logger.warn("比对失败，未查询到匹配记录");
            return null;
        }
    }
}

