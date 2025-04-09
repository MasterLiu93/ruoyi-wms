package cn.smart.wms.module.wms.dal.dataobject.receiptorder;

import lombok.*;

import java.time.LocalDateTime;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;
import cn.smart.wms.framework.mybatis.core.dataobject.BaseDO;

/**
 * 入库单 DO
 *
 * @author 芋道源码
 */
@TableName("wms_receipt_order")
@KeySequence("wms_receipt_order_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptOrderDO extends BaseDO {

    /**
     * 入库单ID
     */
    @TableId
    private Long id;
    /**
     * 入库单号
     */
    private String receiptOrderNo;
    /**
     * 入库类型(0-采购入库 1-生产入库 2-退货入库 3-调拨入库)
     */
    private Integer receiptType;
    /**
     * 供应商ID
     */
    private Long supplierId;
    /**
     * 供应商名称
     */
    @TableField(exist = false)
    private String supplierName;
    /**
     * 仓库ID
     */
    private Long warehouseId;
    /**
     * 单据状态(0-草稿 1-待审核 2-审核通过 3-审核拒绝)
     */
    private Integer orderStatus;
    /**
     * 入库状态(0-待入库 1-部分入库 2-已完成)
     */
    private Integer receiptStatus;
    /**
     * 预计到货时间
     */
    private LocalDateTime expectTime;
    /**
     * 实际到货时间
     */
    private LocalDateTime arrivalTime;
    /**
     * 完成时间
     */
    private LocalDateTime completionTime;
    /**
     * 商品数量
     */
    private Integer totalCount;
    /**
     * 商品金额
     */
    private BigDecimal totalAmount;
    /**
     * 备注
     */
    private String remark;

}