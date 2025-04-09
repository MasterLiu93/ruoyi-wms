package cn.smart.wms.module.wms.enums.receipt;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 入库明细状态枚举
 * 
 * @author 芋道源码
 */
@Getter
@AllArgsConstructor
public enum ReceiptDetailStatusEnum {

    /**
     * 待入库
     */
    PENDING(0, "待入库"),
    
    /**
     * 部分入库
     */
    PARTIAL(1, "部分入库"),
    
    /**
     * 已入库
     */
    COMPLETED(2, "已入库");

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
    public static ReceiptDetailStatusEnum getByStatus(Integer status) {
        if (status == null) {
            return null;
        }
        for (ReceiptDetailStatusEnum value : ReceiptDetailStatusEnum.values()) {
            if (value.getStatus().equals(status)) {
                return value;
            }
        }
        return null;
    }
} 