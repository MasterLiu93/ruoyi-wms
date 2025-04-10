package cn.smart.wms.module.wms.dal.dataobject.inventory;

import lombok.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.smart.wms.framework.mybatis.core.dataobject.BaseDO;

/**
 * 库存 DO
 *
 * @author 芋道源码
 */
@TableName("wms_inventory")
@KeySequence("wms_inventory_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDO extends BaseDO {

    /**
     * 库存ID
     */
    @TableId
    private Long id;
    /**
     * 仓库ID
     */
    private Long warehouseId;
    /**
     * 仓库名称
     */
    @TableField(exist = false)
    private String warehouseName;
    /**
     * 库位ID
     */
    private Long locationId;
    /**
     * 库位名称
     */
    @TableField(exist = false)
    private String locationName;
    /**
     * 货区ID
     */
    @TableField(exist = false)
    private Long areaId;
    /**
     * 货架ID
     */
    @TableField(exist = false)
    private Long rackId;
    /**
     * 物料ID
     */
    private Long itemId;
    /**
     * 物料名称
     */
    @TableField(exist = false)
    private String itemName;
    /**
     * 物料编码
     */
    @TableField(exist = false)
    private String itemCode;
    /**
     * 物料类型
     */
    @TableField(exist = false)
    private Integer itemType;
    /**
     * 规格型号
     */
    @TableField(exist = false)
    private String spec;
    /**
     * 单位
     */
    @TableField(exist = false)
    private String unit;
    /**
     * 库存数量
     */
    private Integer stockCount;
    /**
     * 可用数量
     */
    private Integer availableCount;
    /**
     * 锁定数量
     */
    private Integer lockedCount;
    /**
     * 状态(0-正常 1-禁用)
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;

}