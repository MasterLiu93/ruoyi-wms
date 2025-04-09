package cn.smart.wms.module.wms.enums.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 单据状态枚举
 * 
 * @author 芋道源码
 */
@Getter
@AllArgsConstructor
public enum OrderStatusEnum {

    /**
     * 草稿
     */
    DRAFT(0, "草稿"),
    
    /**
     * 待审核
     */
    PENDING_APPROVAL(1, "待审核"),
    
    /**
     * 审核通过
     */
    APPROVED(2, "审核通过"),
    
    /**
     * 审核拒绝
     */
    REJECTED(3, "审核拒绝");

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
    public static OrderStatusEnum getByStatus(Integer status) {
        if (status == null) {
            return null;
        }
        for (OrderStatusEnum value : OrderStatusEnum.values()) {
            if (value.getStatus().equals(status)) {
                return value;
            }
        }
        return null;
    }
} 