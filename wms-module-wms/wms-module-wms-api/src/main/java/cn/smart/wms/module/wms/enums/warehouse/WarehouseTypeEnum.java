package cn.smart.wms.module.wms.enums.warehouse;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 仓库类型枚举
 * 
 * @author 芋道源码
 */
@Getter
@AllArgsConstructor
public enum WarehouseTypeEnum {

    /**
     * 原材料仓
     */
    RAW_MATERIAL(0, "原材料仓"),
    
    /**
     * 成品仓
     */
    FINISHED_PRODUCT(1, "成品仓"),
    
    /**
     * 半成品仓
     */
    SEMI_FINISHED(2, "半成品仓"),
    
    /**
     * 退货仓
     */
    RETURN(3, "退货仓");

    /**
     * 类型
     */
    private final Integer type;
    
    /**
     * 描述
     */
    private final String description;
    
    /**
     * 根据type获取枚举
     * 
     * @param type 类型
     * @return 枚举
     */
    public static WarehouseTypeEnum getByType(Integer type) {
        if (type == null) {
            return null;
        }
        for (WarehouseTypeEnum value : WarehouseTypeEnum.values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return null;
    }
} 