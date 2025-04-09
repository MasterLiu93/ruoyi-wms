package cn.smart.wms.module.wms.dal.dataobject.item;

import lombok.*;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;
import cn.smart.wms.framework.mybatis.core.dataobject.BaseDO;

/**
 * 物料 DO
 *
 * @author 芋道源码
 */
@TableName("wms_item")
@KeySequence("wms_item_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDO extends BaseDO {

    /**
     * 物料ID
     */
    @TableId
    private Long id;
    /**
     * 物料编码
     */
    private String itemCode;
    /**
     * 物料名称
     */
    private String itemName;
    /**
     * 分类ID
     */
    private Long categoryId;
    /**
     * 物料类型(0-原材料 1-半成品 2-成品 3-包装材料)
     */
    private Integer itemType;
    /**
     * 单位
     */
    private String unit;
    /**
     * 规格
     */
    private String spec;
    /**
     * 参考价格
     */
    private BigDecimal price;
    /**
     * 安全库存
     */
    private Integer safetyStock;
    /**
     * 状态(0-正常 1-禁用)
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;

}