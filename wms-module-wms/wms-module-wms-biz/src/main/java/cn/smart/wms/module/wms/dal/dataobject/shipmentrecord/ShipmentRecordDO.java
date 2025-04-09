package cn.smart.wms.module.wms.dal.dataobject.shipmentrecord;

import cn.smart.wms.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 出库操作记录 DO
 *
 * @author 芋道源码
 */
@TableName("wms_shipment_record")
@KeySequence("wms_shipment_record_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentRecordDO extends BaseDO {

    /**
     * 记录ID
     */
    @TableId
    private Long id;
    /**
     * 出库单ID
     */
    private Long shipmentOrderId;
    /**
     * 出库单明细ID
     */
    private Long shipmentOrderDetailId;
    /**
     * 物料ID
     */
    private Long itemId;
    /**
     * 出库库位ID
     */
    private Long locationId;
    /**
     * 批次ID
     */
    private Long batchId;
    /**
     * 出库数量
     */
    private Integer count;
    /**
     * 备注
     */
    private String remark;

}