package cn.smart.wms.module.wms.dal.dataobject.moveorder;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.smart.wms.framework.mybatis.core.dataobject.BaseDO;

/**
 * 移库单 DO
 *
 * @author 芋道源码
 */
@TableName("wms_move_order")
@KeySequence("wms_move_order_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoveOrderDO extends BaseDO {

    /**
     * 移库单ID
     */
    @TableId
    private Long id;
    /**
     * 移库单号
     */
    private String moveOrderNo;
    /**
     * 移库类型(0-仓库间移库 1-库区间移库 2-库位间移库)
     */
    private Integer moveType;
    /**
     * 源仓库ID
     */
    private Long fromWarehouseId;
    /**
     * 目标仓库ID
     */
    private Long toWarehouseId;
    /**
     * 源库位ID
     */
    private Long fromLocationId;
    /**
     * 目标库位ID
     */
    private Long toLocationId;
    /**
     * 单据状态(0-草稿 1-待审核 2-审核通过 3-审核拒绝)
     */
    private Integer orderStatus;
    /**
     * 移库状态(0-待移库 1-部分移库 2-已完成)
     */
    private Integer moveStatus;
    /**
     * 商品数量
     */
    private Integer totalCount;
    /**
     * 备注
     */
    private String remark;

}