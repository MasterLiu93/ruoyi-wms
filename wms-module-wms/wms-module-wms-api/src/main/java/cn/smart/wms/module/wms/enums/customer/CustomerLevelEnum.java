package cn.smart.wms.module.wms.enums.customer;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 客户级别枚举
 * 
 * @author 芋道源码
 */
@Getter
@AllArgsConstructor
public enum CustomerLevelEnum {

    /**
     * 普通客户
     */
    NORMAL(0, "普通客户"),
    
    /**
     * 重要客户
     */
    IMPORTANT(1, "重要客户"),
    
    /**
     * VIP客户
     */
    VIP(2, "VIP客户");

    /**
     * 级别
     */
    private final Integer level;
    
    /**
     * 描述
     */
    private final String description;
    
    /**
     * 根据level获取枚举
     * 
     * @param level 级别
     * @return 枚举
     */
    public static CustomerLevelEnum getByLevel(Integer level) {
        if (level == null) {
            return null;
        }
        for (CustomerLevelEnum value : CustomerLevelEnum.values()) {
            if (value.getLevel().equals(level)) {
                return value;
            }
        }
        return null;
    }
} 