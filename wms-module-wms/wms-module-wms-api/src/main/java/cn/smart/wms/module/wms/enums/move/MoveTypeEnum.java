package cn.smart.wms.module.wms.enums.move;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 移库类型枚举
 * 
 * @author 芋道源码
 */
@Getter
@AllArgsConstructor
public enum MoveTypeEnum {

    /**
     * 仓库间移库
     */
    WAREHOUSE(0, "仓库间移库"),
    
    /**
     * 库区间移库
     */
    AREA(1, "库区间移库"),
    
    /**
     * 库位间移库
     */
    LOCATION(2, "库位间移库");

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
    public static MoveTypeEnum getByType(Integer type) {
        if (type == null) {
            return null;
        }
        for (MoveTypeEnum value : MoveTypeEnum.values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return null;
    }
} 