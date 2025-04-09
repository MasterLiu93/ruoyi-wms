package cn.smart.wms.module.wms.dal.dataobject.inventorymovement;

import lombok.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.smart.wms.framework.mybatis.core.dataobject.BaseDO;

/**
 * 库存移动记录 DO
 *
 * @author 芋道源码
 */
@TableName("wms_inventory_movement")
@KeySequence("wms_inventory_movement_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryMovementDO extends BaseDO {

    /**
     * 移动ID
     */
    @TableId
    private Long id;
    /**
     * 移动类型(0-入库 1-出库 2-库存调整)
     */
    private Integer movementType;
    /**
     * 移动单号
     */
    private String movementNo;
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
     * 移动数量
     */
    private Integer count;
    /**
     * 移动前数量
     */
    private Integer beforeCount;
    /**
     * 移动后数量
     */
    private Integer afterCount;
    /**
     * 业务类型
     */
    private String businessType;
    /**
     * 业务单ID
     */
    private Long businessId;
    /**
     * 业务明细ID
     */
    private Long businessDetailId;
    /**
     * 备注
     */
    private String remark;

}