package cn.smart.wms.module.wms.dal.dataobject.shipmentorder;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.smart.wms.framework.mybatis.core.dataobject.BaseDO;

/**
 * 出库单 DO
 *
 * @author 芋道源码
 */
@TableName("wms_shipment_order")
@KeySequence("wms_shipment_order_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentOrderDO extends BaseDO {

    /**
     * 出库单ID
     */
    @TableId
    private Long id;
    /**
     * 出库单号
     */
    private String shipmentOrderNo;
    /**
     * 出库类型(0-销售出库 1-生产出库 2-调拨出库 3-其他出库)
     */
    private Integer shipmentType;
    /**
     * 客户ID
     */
    private Long customerId;
    /**
     * 仓库ID
     */
    private Long warehouseId;
    /**
     * 单据状态(0-草稿 1-待审核 2-审核通过 3-审核拒绝)
     */
    private Integer orderStatus;
    /**
     * 出库状态(0-待出库 1-部分出库 2-已完成)
     */
    private Integer shipmentStatus;
    /**
     * 预计出库时间
     */
    private LocalDateTime expectTime;
    /**
     * 实际完成时间
     */
    private LocalDateTime completeTime;
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