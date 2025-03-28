package cn.smart.wms.module.wms.dal.dataobject.moverecord;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.smart.wms.framework.mybatis.core.dataobject.BaseDO;

/**
 * 移库操作记录 DO
 *
 * @author 芋道源码
 */
@TableName("wms_move_record")
@KeySequence("wms_move_record_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoveRecordDO extends BaseDO {

    /**
     * 记录ID
     */
    @TableId
    private Long id;
    /**
     * 移库单ID
     */
    private Long moveOrderId;
    /**
     * 移库单明细ID
     */
    private Long moveOrderDetailId;
    /**
     * 物料ID
     */
    private Long itemId;
    /**
     * 批次ID
     */
    private Long batchId;
    /**
     * 源库位ID
     */
    private Long fromLocationId;
    /**
     * 目标库位ID
     */
    private Long toLocationId;
    /**
     * 移动数量
     */
    private Integer count;
    /**
     * 备注
     */
    private String remark;

}