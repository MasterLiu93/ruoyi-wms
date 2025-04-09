package cn.smart.wms.module.wms.enums.receipt;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 入库类型枚举
 * 
 * @author 芋道源码
 */
@Getter
@AllArgsConstructor
public enum ReceiptTypeEnum {

    /**
     * 采购入库
     */
    PURCHASE(0, "采购入库"),
    
    /**
     * 生产入库
     */
    PRODUCTION(1, "生产入库"),
    
    /**
     * 退货入库
     */
    RETURN(2, "退货入库"),
    
    /**
     * 调拨入库
     */
    TRANSFER(3, "调拨入库");

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
    public static ReceiptTypeEnum getByType(Integer type) {
        if (type == null) {
            return null;
        }
        for (ReceiptTypeEnum value : ReceiptTypeEnum.values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return null;
    }
} 