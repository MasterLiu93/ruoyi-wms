package cn.smart.wms.module.wms.dal.dataobject.batchrecord;

import lombok.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.smart.wms.framework.mybatis.core.dataobject.BaseDO;

/**
 * 批次操作记录 DO
 *
 * @author 芋道源码
 */
@TableName("wms_batch_record")
@KeySequence("wms_batch_record_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchRecordDO extends BaseDO {

    /**
     * 记录ID
     */
    @TableId
    private Long id;
    /**
     * 批次ID
     */
    private Long batchId;
    /**
     * 移动类型(0-入库 1-出库 2-库存调整)
     */
    private Integer movementType;
    /**
     * 移动记录ID
     */
    private Long movementId;
    /**
     * 操作数量
     */
    private Integer count;
    /**
     * 操作前数量
     */
    private Integer beforeCount;
    /**
     * 操作后数量
     */
    private Integer afterCount;
    /**
     * 备注
     */
    private String remark;

}