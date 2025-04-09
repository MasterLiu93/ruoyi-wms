package cn.smart.wms.module.wms.enums.inventory;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 库存移动类型枚举
 * 
 * @author 芋道源码
 */
@Getter
@AllArgsConstructor
public enum InventoryMovementTypeEnum {

    /**
     * 入库
     */
    IN(0, "入库"),
    
    /**
     * 出库
     */
    OUT(1, "出库"),
    
    /**
     * 库存调整
     */
    ADJUSTMENT(2, "库存调整");

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
    public static InventoryMovementTypeEnum getByType(Integer type) {
        if (type == null) {
            return null;
        }
        for (InventoryMovementTypeEnum value : InventoryMovementTypeEnum.values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return null;
    }
} 