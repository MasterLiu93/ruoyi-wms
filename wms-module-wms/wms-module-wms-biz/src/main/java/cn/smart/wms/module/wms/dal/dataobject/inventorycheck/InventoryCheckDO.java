package cn.smart.wms.module.wms.dal.dataobject.inventorycheck;

import lombok.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.smart.wms.framework.mybatis.core.dataobject.BaseDO;

/**
 * 库存盘点单 DO
 *
 * @author 芋道源码
 */
@TableName("wms_inventory_check")
@KeySequence("wms_inventory_check_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryCheckDO extends BaseDO {

    /**
     * 盘点单ID
     */
    @TableId
    private Long id;
    /**
     * 盘点单号
     */
    private String checkNo;
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
     * 盘点类型(0-全部盘点 1-部分盘点)
     */
    private Integer checkType;
    /**
     * 盘点状态(0-新建 1-盘点中 2-已完成)
     */
    private Integer checkStatus;
    /**
     * 盘点总数
     */
    private Integer totalCount;
    /**
     * 已盘点数
     */
    private Integer checkedCount;
    /**
     * 差异数
     */
    private Integer differenceCount;
    /**
     * 备注
     */
    private String remark;

}