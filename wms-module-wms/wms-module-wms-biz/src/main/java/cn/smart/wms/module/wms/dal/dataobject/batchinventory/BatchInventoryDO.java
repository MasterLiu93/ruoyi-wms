package cn.smart.wms.module.wms.dal.dataobject.batchinventory;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.smart.wms.framework.mybatis.core.dataobject.BaseDO;

/**
 * 批次库存 DO
 *
 * @author 芋道源码
 */
@TableName("wms_batch_inventory")
@KeySequence("wms_batch_inventory_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchInventoryDO extends BaseDO {

    /**
     * 批次库存ID
     */
    @TableId
    private Long id;
    /**
     * 批次ID
     */
    private Long batchId;
    /**
     * 物料ID
     */
    private Long itemId;
    /**
     * 仓库ID
     */
    private Long warehouseId;
    /**
     * 库位ID
     */
    private Long locationId;
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
     * 状态(0-正常 1-冻结)
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;

}