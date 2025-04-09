package cn.smart.wms.module.wms.enums.shipment;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 出库类型枚举
 * 
 * @author 芋道源码
 */
@Getter
@AllArgsConstructor
public enum ShipmentTypeEnum {

    /**
     * 销售出库
     */
    SALES(0, "销售出库"),
    
    /**
     * 生产出库
     */
    PRODUCTION(1, "生产出库"),
    
    /**
     * 调拨出库
     */
    TRANSFER(2, "调拨出库"),
    
    /**
     * 其他出库
     */
    OTHER(3, "其他出库");

    /**
     * 类型值
     */
    private final Integer type;
    
    /**
     * 类型名
     */
    private final String name;
    
    /**
     * 根据type获取枚举
     * 
     * @param type 类型
     * @return 枚举
     */
    public static ShipmentTypeEnum getByType(Integer type) {
        if (type == null) {
            return null;
        }
        for (ShipmentTypeEnum value : ShipmentTypeEnum.values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return null;
    }
} 