package cn.smart.wms.module.wms.dal.dataobject.shipmentorderdetail;

import cn.smart.wms.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;

/**
 * 出库单明细 DO
 *
 * @author 芋道源码
 */
@TableName("wms_shipment_order_detail")
@KeySequence("wms_shipment_order_detail_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentOrderDetailDO extends BaseDO {

    /**
     * 出库单明细ID
     */
    @TableId
    private Long id;
    /**
     * 出库单ID
     */
    private Long shipmentOrderId;
    /**
     * 仓库ID
     */
    private Long warehouseId;
    /**
     * 货区ID
     */
    private Long areaId;
    /**
     * 货架ID
     */
    private Long rackId;
    /**
     * 物料ID
     */
    private Long itemId;
    /**
     * 计划数量
     */
    private Integer planCount;
    /**
     * 实际出库数量
     */
    private Integer realCount;
    /**
     * 出库库位ID
     */
    private Long locationId;
    /**
     * 批次ID
     */
    private Long batchId;
    /**
     * 出库单价
     */
    private BigDecimal price;
    /**
     * 状态(0-未出库 1-部分出库 2-已出库)
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;

}