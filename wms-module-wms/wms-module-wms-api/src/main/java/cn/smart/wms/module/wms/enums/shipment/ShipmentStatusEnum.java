package cn.smart.wms.module.wms.enums.shipment;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 出库状态枚举
 * 
 * @author 芋道源码
 */
@Getter
@AllArgsConstructor
public enum ShipmentStatusEnum {

    /**
     * 待出库
     */
    PENDING(0, "待出库"),
    
    /**
     * 部分出库
     */
    PARTIAL(1, "部分出库"),
    
    /**
     * 已完成
     */
    COMPLETED(2, "已完成");

    /**
     * 状态值
     */
    private final Integer status;
    
    /**
     * 状态名
     */
    private final String name;
    
    /**
     * 根据状态获取枚举
     * 
     * @param status 状态
     * @return 枚举对象
     */
    public static ShipmentStatusEnum getByStatus(Integer status) {
        if (status == null) {
            return null;
        }
        
        for (ShipmentStatusEnum statusEnum : values()) {
            if (statusEnum.getStatus().equals(status)) {
                return statusEnum;
            }
        }
        
        return null;
    }
} 