package cn.smart.wms.module.wms.dal.dataobject.moveorderdetail;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.smart.wms.framework.mybatis.core.dataobject.BaseDO;

/**
 * 移库单明细 DO
 *
 * @author 芋道源码
 */
@TableName("wms_move_order_detail")
@KeySequence("wms_move_order_detail_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoveOrderDetailDO extends BaseDO {

    /**
     * 移库单明细ID
     */
    @TableId
    private Long id;
    /**
     * 移库单ID
     */
    private Long moveOrderId;
    /**
     * 物料ID
     */
    private Long itemId;
    /**
     * 批次ID
     */
    private Long batchId;
    /**
     * 计划数量
     */
    private Integer planCount;
    /**
     * 实际移库数量
     */
    private Integer realCount;
    /**
     * 状态(0-未移库 1-部分移库 2-已移库)
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;

}