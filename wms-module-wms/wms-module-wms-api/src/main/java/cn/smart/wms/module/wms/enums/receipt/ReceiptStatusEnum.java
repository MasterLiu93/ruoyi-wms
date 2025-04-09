package cn.smart.wms.module.wms.enums.receipt;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 入库状态枚举
 * 
 * @author 芋道源码
 */
@Getter
@AllArgsConstructor
public enum ReceiptStatusEnum {

    /**
     * 待入库
     */
    PENDING(0, "待入库"),
    
    /**
     * 部分入库
     */
    PARTIAL(1, "部分入库"),
    
    /**
     * 已完成
     */
    COMPLETED(2, "已完成");

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
    public static ReceiptStatusEnum getByStatus(Integer status) {
        if (status == null) {
            return null;
        }
        for (ReceiptStatusEnum value : ReceiptStatusEnum.values()) {
            if (value.getStatus().equals(status)) {
                return value;
            }
        }
        return null;
    }
} 