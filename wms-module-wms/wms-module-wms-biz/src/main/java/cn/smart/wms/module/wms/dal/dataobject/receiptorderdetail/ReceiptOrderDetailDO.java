package cn.smart.wms.module.wms.dal.dataobject.receiptorderdetail;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
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
     * 计划数量
     */
    private Integer planCount;
    /**
     * 实际入库数量
     */
    private Integer realCount;
    /**
     * 入库库位ID
     */
    private Long locationId;
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