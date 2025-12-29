package com.keycloak.credentialserver.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.keycloak.credentialserver.entity.IdentityProof;
import com.keycloak.credentialserver.mapper.IdentityProofMapper;
import com.keycloak.credentialserver.util.KeycloakUserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class IdentityProofService {

    private static final Logger logger = LoggerFactory.getLogger(IdentityProofService.class);

    @Resource
    private IdentityProofMapper identityProofMapper;
    @Resource
    private KeycloakUserUtil keycloakUserUtil;

    // ========== 新增：补全所有空user_id的记录 ==========
    // 正确代码：英文方法名 + void 返回类型
@Transactional
public void fillEmptyUserId() { // 中文→英文，补充void返回类型
    // 1. 查询所有user_id为空的记录
    QueryWrapper<IdentityProof> queryWrapper = new QueryWrapper<>();
    queryWrapper.isNull("user_id").or().eq("user_id", "");
    List<IdentityProof> emptyUserIdList = identityProofMapper.selectList(queryWrapper);

    // 2. 逐个创建Keycloak用户并更新user_id
    for (IdentityProof proof : emptyUserIdList) {
        String finalCredentialHash = proof.getFinalCredentialHash();
        try {
            // 创建Keycloak用户
            String keycloakUserId = keycloakUserUtil.createKeycloakUser(finalCredentialHash);
            // 更新数据库user_id和最后更新时间
            UpdateWrapper<IdentityProof> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", proof.getId())
                         .set("user_id", keycloakUserId)
                         .set("last_update_timestamp", LocalDateTime.now());
            identityProofMapper.update(null, updateWrapper);
            logger.info("补全id={}的user_id：{}", proof.getId(), keycloakUserId);
        } catch (Exception e) {
            logger.error("补全id={}的user_id失败：{}", proof.getId(), e.getMessage());
        }
    }
}


    // ========== 适配现有表：新数据入库自动创建用户 ==========
    @Transactional
    public boolean saveCredentialWithAutoUser(IdentityProof identityProof) {
        String finalCredentialHash = identityProof.getFinalCredentialHash();
        if (finalCredentialHash == null || finalCredentialHash.trim().isEmpty()) {
            logger.warn("finalCredentialHash为空，入库失败");
            return false;
        }

        // 1. 检查该凭证是否已存在（避免重复）
        QueryWrapper<IdentityProof> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("final_credential_hash", finalCredentialHash);
        IdentityProof existProof = identityProofMapper.selectOne(queryWrapper);

        if (existProof != null) {
            // 若已存在但user_id为空，自动补全
            if (existProof.getUserId() == null || existProof.getUserId().isEmpty()) {
                String keycloakUserId = keycloakUserUtil.createKeycloakUser(finalCredentialHash);
                existProof.setUserId(keycloakUserId);
                existProof.setLastUpdateTimestamp(LocalDateTime.now());
                identityProofMapper.updateById(existProof);
                logger.info("更新已有记录的user_id：{}", keycloakUserId);
            }
            logger.info("凭证已存在，userId：{}", existProof.getUserId());
            return true;
        }

        // 2. 新记录：创建Keycloak用户并入库
        String keycloakUserId = keycloakUserUtil.createKeycloakUser(finalCredentialHash);
        logger.info("创建Keycloak用户成功，ID：{}", keycloakUserId);

        // 3. 赋值（严格匹配你的表字段）
        identityProof.setUserId(keycloakUserId);
        // 若前端未传时间，自动填充
        if (identityProof.getCreationTimestamp() == null) {
            identityProof.setCreationTimestamp(LocalDateTime.now());
        }
        identityProof.setLastUpdateTimestamp(LocalDateTime.now());

        // 4. 插入数据库（匹配你的表结构）
        return identityProofMapper.insert(identityProof) > 0;
    }

    // ========== 保留原有验证方法（适配你的表） ==========
    public IdentityProof verifyFinalCredentialHash(String finalCredentialHash) {
        logger.info("开始比对最终凭证哈希：{}", finalCredentialHash);

        if (finalCredentialHash == null || finalCredentialHash.trim().isEmpty()) {
            logger.warn("传入的哈希字符串为空");
            return null;
        }

        // 显式指定查询字段，严格匹配你的表
        QueryWrapper<IdentityProof> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "final_credential_hash", "phone_number_hash", "user_id")
                   .eq("final_credential_hash", finalCredentialHash);

        IdentityProof identityProof = identityProofMapper.selectOne(queryWrapper);

        if (identityProof != null) {
            logger.info("比对成功，关联userId：{}", identityProof.getUserId());
            return identityProof;
        } else {
            logger.warn("比对失败，未查询到匹配记录");
            return null;
        }
    }
}

