package cn.smart.wms.module.wms.enums.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通用状态枚举
 * 
 * @author 芋道源码
 */
@Getter
@AllArgsConstructor
public enum StatusEnum {

    /**
     * 正常
     */
    ENABLE(0, "正常"),
    
    /**
     * 禁用
     */
    DISABLE(1, "禁用");

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
     * @param status 状态值
     * @return 枚举
     */
    public static StatusEnum getByStatus(Integer status) {
        if (status == null) {
            return null;
        }
        for (StatusEnum value : StatusEnum.values()) {
            if (value.getStatus().equals(status)) {
                return value;
            }
        }
        return null;
    }
} 