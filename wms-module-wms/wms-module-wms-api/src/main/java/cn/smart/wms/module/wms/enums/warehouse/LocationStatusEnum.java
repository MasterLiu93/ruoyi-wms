package cn.smart.wms.module.wms.enums.warehouse;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 库位状态枚举
 * 
 * @author 芋道源码
 */
@Getter
@AllArgsConstructor
public enum LocationStatusEnum {

    /**
     * 空闲
     */
    IDLE(0, "空闲"),
    
    /**
     * 占用
     */
    OCCUPIED(1, "占用"),
    
    /**
     * 锁定
     */
    LOCKED(2, "锁定"),
    
    /**
     * 禁用
     */
    DISABLED(3, "禁用");

    /**
     * 状态
     */
    private final Integer status;
    
    /**
     * 描述
     */
    private final String description;
    
    /**
     * 根据status获取枚举
     * 
     * @param status 状态
     * @return 枚举
     */
    public static LocationStatusEnum getByStatus(Integer status) {
        if (status == null) {
            return null;
        }
        for (LocationStatusEnum value : LocationStatusEnum.values()) {
            if (value.getStatus().equals(status)) {
                return value;
            }
        }
        return null;
    }
} 