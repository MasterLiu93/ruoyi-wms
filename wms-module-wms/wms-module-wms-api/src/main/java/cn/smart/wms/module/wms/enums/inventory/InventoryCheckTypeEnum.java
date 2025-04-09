package cn.smart.wms.module.wms.enums.inventory;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 盘点类型枚举
 * 
 * @author 芋道源码
 */
@Getter
@AllArgsConstructor
public enum InventoryCheckTypeEnum {

    /**
     * 全部盘点
     */
    FULL(0, "全部盘点"),
    
    /**
     * 部分盘点
     */
    PARTIAL(1, "部分盘点");

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
    public static InventoryCheckTypeEnum getByType(Integer type) {
        if (type == null) {
            return null;
        }
        for (InventoryCheckTypeEnum value : InventoryCheckTypeEnum.values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return null;
    }
} 