package cn.smart.wms.module.wms.enums.supplier;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 供应商级别枚举
 * 
 * @author 芋道源码
 */
@Getter
@AllArgsConstructor
public enum SupplierLevelEnum {

    /**
     * 普通供应商
     */
    NORMAL(0, "普通供应商"),
    
    /**
     * 重要供应商
     */
    IMPORTANT(1, "重要供应商"),
    
    /**
     * 战略供应商
     */
    STRATEGIC(2, "战略供应商");

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
    public static SupplierLevelEnum getByLevel(Integer level) {
        if (level == null) {
            return null;
        }
        for (SupplierLevelEnum value : SupplierLevelEnum.values()) {
            if (value.getLevel().equals(level)) {
                return value;
            }
        }
        return null;
    }
} 