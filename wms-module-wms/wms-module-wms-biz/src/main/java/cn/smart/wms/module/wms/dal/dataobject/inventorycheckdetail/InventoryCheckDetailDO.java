package cn.smart.wms.module.wms.dal.dataobject.inventorycheckdetail;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.smart.wms.framework.mybatis.core.dataobject.BaseDO;

/**
 * 库存盘点明细 DO
 *
 * @author 芋道源码
 */
@TableName("wms_inventory_check_detail")
@KeySequence("wms_inventory_check_detail_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryCheckDetailDO extends BaseDO {

    /**
     * 盘点明细ID
     */
    @TableId
    private Long id;
    /**
     * 盘点单ID
     */
    private Long checkId;
    /**
     * 物料ID
     */
    private Long itemId;
    /**
     * 库位ID
     */
    private Long locationId;
    /**
     * 账面数量
     */
    private Integer bookCount;
    /**
     * 盘点数量
     */
    private Integer checkCount;
    /**
     * 差异数量
     */
    private Integer differenceCount;
    /**
     * 盘点状态(0-未盘点 1-已盘点)
     */
    private Integer checkStatus;
    /**
     * 备注
     */
    private String remark;

}