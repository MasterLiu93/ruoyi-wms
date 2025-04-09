package cn.smart.wms.module.wms.enums.receipt;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 入库单据状态枚举
 * 
 * @author 芋道源码
 */
@Getter
@AllArgsConstructor
public enum ReceiptOrderStatusEnum {

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
    APPROVED(2, "已审核"),
    
    /**
     * 已拒绝
     */
    REJECTED(3, "已拒绝"),
    
    /**
     * 已取消
     */
    CANCELLED(4, "已取消");

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
    public static ReceiptOrderStatusEnum getByStatus(Integer status) {
        if (status == null) {
            return null;
        }
        for (ReceiptOrderStatusEnum value : ReceiptOrderStatusEnum.values()) {
            if (value.getStatus().equals(status)) {
                return value;
            }
        }
        return null;
    }
} 