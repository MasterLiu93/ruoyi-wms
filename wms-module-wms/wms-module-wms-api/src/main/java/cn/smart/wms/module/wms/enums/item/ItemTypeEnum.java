package cn.smart.wms.module.wms.enums.item;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 物料类型枚举
 * 
 * @author 芋道源码
 */
@Getter
@AllArgsConstructor
public enum ItemTypeEnum {

    /**
     * 原材料
     */
    RAW_MATERIAL(0, "原材料"),
    
    /**
     * 半成品
     */
    SEMI_FINISHED(1, "半成品"),
    
    /**
     * 成品
     */
    FINISHED_PRODUCT(2, "成品"),
    
    /**
     * 包装材料
     */
    PACKAGING(3, "包装材料");

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
    public static ItemTypeEnum getByType(Integer type) {
        if (type == null) {
            return null;
        }
        for (ItemTypeEnum value : ItemTypeEnum.values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return null;
    }
} 