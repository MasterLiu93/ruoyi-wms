package cn.smart.wms.module.wms.dal.dataobject.batchinventory;

import cn.smart.wms.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 批次库存 DO
 */
@TableName("wms_batch_inventory")
@KeySequence("wms_batch_inventory_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BatchInventoryDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;

    /**
     * 批次编号
     */
    private Long batchId;

    /**
     * 物料编号
     */
    private Long itemId;

    /**
     * 仓库编号
     */
    private Long warehouseId;

    /**
     * 库位编号
     */
    private Long locationId;

    /**
     * 库存数量
     */
    private Integer stockCount;

    /**
     * 锁定库存数量
     */
    private Integer lockedCount;

    /**
     * 可用库存数量
     */
    private Integer availableCount;

    /**
     * 租户编号
     */
    private Long tenantId;

} 