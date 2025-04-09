package cn.smart.wms.module.wms.enums.inventory;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 盘点状态枚举
 * 
 * @author 芋道源码
 */
@Getter
@AllArgsConstructor
public enum InventoryCheckStatusEnum {

    /**
     * 新建
     */
    NEW(0, "新建"),
    
    /**
     * 盘点中
     */
    CHECKING(1, "盘点中"),
    
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
    public static InventoryCheckStatusEnum getByStatus(Integer status) {
        if (status == null) {
            return null;
        }
        for (InventoryCheckStatusEnum value : InventoryCheckStatusEnum.values()) {
            if (value.getStatus().equals(status)) {
                return value;
            }
        }
        return null;
    }
} 