package cn.smart.wms.module.wms.enums.move;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 移库状态枚举
 * 
 * @author 芋道源码
 */
@Getter
@AllArgsConstructor
public enum MoveStatusEnum {

    /**
     * 待移库
     */
    PENDING(0, "待移库"),
    
    /**
     * 部分移库
     */
    PARTIAL(1, "部分移库"),
    
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
    public static MoveStatusEnum getByStatus(Integer status) {
        if (status == null) {
            return null;
        }
        for (MoveStatusEnum value : MoveStatusEnum.values()) {
            if (value.getStatus().equals(status)) {
                return value;
            }
        }
        return null;
    }
} 