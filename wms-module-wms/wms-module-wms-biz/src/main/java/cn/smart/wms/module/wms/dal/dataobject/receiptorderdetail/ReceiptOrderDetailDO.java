package cn.smart.wms.module.wms.dal.dataobject.receiptorderdetail;

import lombok.*;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;
import cn.smart.wms.framework.mybatis.core.dataobject.BaseDO;

/**
 * 入库单明细 DO
 *
 * @author 芋道源码
 */
@TableName("wms_receipt_order_detail")
@KeySequence("wms_receipt_order_detail_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptOrderDetailDO extends BaseDO {

    /**
     * 入库单明细ID
     */
    @TableId
    private Long id;
    /**
     * 入库单ID
     */
    private Long receiptOrderId;
    /**
     * 物料ID
     */
    private Long itemId;
    /**
     * 物料编码
     */
    @TableField(exist = false)
    private String itemCode;
    /**
     * 物料名称
     */
    @TableField(exist = false)
    private String itemName;
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
     * 计划数量
     */
    private Integer planCount;
    /**
     * 实际入库数量
     */
    private Integer realCount;
    /**
     * 仓库ID
     */
    @TableField(exist = false)
    private Long warehouseId;
    /**
     * 仓库名称
     */
    @TableField(exist = false)
    private String warehouseName;
    /**
     * 货区ID
     */
    private Long areaId;
    /**
     * 货区名称
     */
    @TableField(exist = false)
    private String areaName;
    /**
     * 货架ID
     */
    private Long rackId;
    /**
     * 货架名称
     */
    @TableField(exist = false)
    private String rackName;
    /**
     * 入库库位ID
     */
    private Long locationId;
    /**
     * 库位名称
     */
    @TableField(exist = false)
    private String locationName;
    /**
     * 批次ID
     */
    private Long batchId;
    /**
     * 入库单价
     */
    private BigDecimal price;
    /**
     * 质检状态(0-未检验 1-已检验)
     */
    private Integer qualityStatus;
    /**
     * 状态(0-未入库 1-部分入库 2-已入库)
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;

}