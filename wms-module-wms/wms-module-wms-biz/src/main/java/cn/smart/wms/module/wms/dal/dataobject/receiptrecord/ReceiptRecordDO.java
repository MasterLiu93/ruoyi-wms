package cn.smart.wms.module.wms.dal.dataobject.receiptrecord;

import lombok.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.smart.wms.framework.mybatis.core.dataobject.BaseDO;

/**
 * 入库操作记录 DO
 *
 * @author 芋道源码
 */
@TableName("wms_receipt_record")
@KeySequence("wms_receipt_record_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptRecordDO extends BaseDO {

    /**
     * 记录ID
     */
    @TableId
    private Long id;
    /**
     * 入库单ID
     */
    private Long receiptOrderId;
    /**
     * 入库单明细ID
     */
    private Long receiptOrderDetailId;
    /**
     * 物料ID
     */
    private Long itemId;
    /**
     * 入库库位ID
     */
    private Long locationId;
    /**
     * 批次ID
     */
    private Long batchId;
    /**
     * 入库数量
     */
    private Integer count;
    /**
     * 备注
     */
    private String remark;

}