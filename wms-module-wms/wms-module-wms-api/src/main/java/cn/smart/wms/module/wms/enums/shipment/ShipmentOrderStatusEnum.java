package cn.smart.wms.module.wms.enums.shipment;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 出库单状态枚举
 * 
 * @author 芋道源码
 */
@Getter
@AllArgsConstructor
public enum ShipmentOrderStatusEnum {

    /**
     * 草稿
     */
    DRAFT(0, "草稿"),
    
    /**
     * 待审核
     */
    PENDING_APPROVAL(1, "待审核"),
    
    /**
     * 已审核
     */
    APPROVED(2, "审核通过"),
    
    /**
     * 已拒绝
     */
    REJECTED(3, "审核拒绝"),
    
    /**
     * 已取消
     */
    CANCELLED(4, "已取消");

    /**
     * 状态值
     */
    private final Integer status;
    
    /**
     * 状态名
     */
    private final String name;
    
    /**
     * 根据status获取枚举
     * 
     * @param status 状态
     * @return 枚举
     */
    public static ShipmentOrderStatusEnum getByStatus(Integer status) {
        if (status == null) {
            return null;
        }
        for (ShipmentOrderStatusEnum value : ShipmentOrderStatusEnum.values()) {
            if (value.getStatus().equals(status)) {
                return value;
            }
        }
        return null;
    }
} 