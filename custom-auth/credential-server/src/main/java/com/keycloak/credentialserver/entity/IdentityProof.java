package com.keycloak.credentialserver.entity; // 包名与路径完全一致

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 身份凭证实体类（对应数据库identity_proof表）
 */
@Data // Lombok注解：自动生成getter/setter/toString（解决getPhoneNumberHash()找不到问题）
@TableName("identity_proof") // 绑定数据库表名（必须正确）
public class IdentityProof {

    @TableId(type = IdType.AUTO) // 主键自增（对应表中id字段）
    private Long id;

    // 字段名：驼峰命名，对应数据库下划线字段（phone_number_hash）
    private String phoneNumberHash;
    private String deviceFingerprintHash;
    private String finalCredentialHash;
    private LocalDateTime creationTimestamp;
    private LocalDateTime lastUpdateTimestamp;
}

