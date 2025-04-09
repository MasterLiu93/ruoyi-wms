package cn.smart.wms.module.wms.enums.batch;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 批次移动类型枚举
 * 
 * @author 芋道源码
 */
@Getter
@AllArgsConstructor
public enum BatchMovementTypeEnum {

    RECEIPT(0, "入库"),
    SHIPMENT(1, "出库"),
    MOVE_OUT(2, "移出"),
    MOVE_IN(3, "移入"),
    ADJUSTMENT(4, "调整");

    /**
     * 类型值
     */
    private final Integer type;
    /**
     * 类型名
     */
    private final String name;

    /**
     * 根据类型获取枚举
     */
    public static BatchMovementTypeEnum getByType(Integer type) {
        if (type == null) {
            return null;
        }
        for (BatchMovementTypeEnum value : BatchMovementTypeEnum.values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return null;
    }
} 