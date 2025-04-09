package cn.smart.wms.module.wms.enums.inventory;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 库存状态枚举
 * 
 * @author 芋道源码
 */
@Getter
@AllArgsConstructor
public enum InventoryStatusEnum {

    /**
     * 正常
     */
    NORMAL(0, "正常"),
    
    /**
     * 禁用
     */
    DISABLED(1, "禁用");

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
    public static InventoryStatusEnum getByStatus(Integer status) {
        if (status == null) {
            return null;
        }
        for (InventoryStatusEnum value : InventoryStatusEnum.values()) {
            if (value.getStatus().equals(status)) {
                return value;
            }
        }
        return null;
    }
} 