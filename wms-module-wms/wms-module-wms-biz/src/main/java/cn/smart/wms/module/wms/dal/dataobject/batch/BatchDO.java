package cn.smart.wms.module.wms.dal.dataobject.batch;

import cn.smart.wms.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDate;

/**
 * 批次信息 DO
 *
 * @author 芋道源码
 */
@TableName("wms_batch")
@KeySequence("wms_batch_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchDO extends BaseDO {

    /**
     * 批次ID
     */
    @TableId
    private Long id;
    /**
     * 批次号
     */
    private String batchNo;
    /**
     * 物料ID
     */
    private Long itemId;
    /**
     * 仓库ID
     */
    private Long warehouseId;
    /**
     * 供应商ID
     */
    private Long supplierId;
    /**
     * 生产日期
     */
    private LocalDate productionDate;
    /**
     * 过期日期
     */
    private LocalDate expiryDate;
    /**
     * 批次属性1
     */
    private String batchAttr1;
    /**
     * 批次属性2
     */
    private String batchAttr2;
    /**
     * 批次属性3
     */
    private String batchAttr3;
    /**
     * 批次属性4
     */
    private String batchAttr4;
    /**
     * 入库总数量
     */
    private Integer totalCount;
    /**
     * 可用数量
     */
    private Integer availableCount;
    /**
     * 锁定数量
     */
    private Integer lockedCount;
    /**
     * 状态(0-正常 1-冻结 2-禁用)
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;

}