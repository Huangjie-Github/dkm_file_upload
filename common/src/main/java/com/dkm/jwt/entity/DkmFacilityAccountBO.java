package com.dkm.jwt.entity;

import lombok.Data;

/**
 * @Author: HuangJie
 * @Date: 2020/3/25 13:36
 * @Version: 1.0V
 */
@Data
public class DkmFacilityAccountBO {
    /**
     * 账户ID
     */
    private Double id;
    /**
     * 账户名称
     */
    private String accountName;
    /**
     * 账户密码
     */
    private String accountPassWord;
    /**
     * 账户关联的设备名称
     */
    private String facilityName;
    /**
     * 账户关联设备的描述
     */
    private String facilityDescription;
}
