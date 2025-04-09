package cn.smart.wms.module.wms.enums.shipment;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 出库明细状态枚举
 * 
 * @author 芋道源码
 */
@Getter
@AllArgsConstructor
public enum ShipmentDetailStatusEnum {

    /**
     * 待出库
     */
    PENDING(0, "待出库"),
    
    /**
     * 部分出库
     */
    PARTIAL(1, "部分出库"),
    
    /**
     * 已出库
     */
    COMPLETED(2, "已出库");

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
    public static ShipmentDetailStatusEnum getByStatus(Integer status) {
        if (status == null) {
            return null;
        }
        for (ShipmentDetailStatusEnum value : ShipmentDetailStatusEnum.values()) {
            if (value.getStatus().equals(status)) {
                return value;
            }
        }
        return null;
    }
} 