package cn.smart.wms.module.wms.dal.dataobject.rack;

import lombok.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.smart.wms.framework.mybatis.core.dataobject.BaseDO;

/**
 * 货架 DO
 *
 * @author 芋道源码
 */
@TableName("wms_rack")
@KeySequence("wms_rack_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RackDO extends BaseDO {

    /**
     * 货架ID
     */
    @TableId
    private Long id;
    /**
     * 货架编码
     */
    private String rackCode;
    /**
     * 货架名称
     */
    private String rackName;
    /**
     * 所属货区ID
     */
    private Long areaId;
    /**
     * 货架类型(0-标准货架 1-重型货架 2-悬臂货架)
     */
    private Integer rackType;
    /**
     * 状态(0-正常 1-禁用)
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;

}