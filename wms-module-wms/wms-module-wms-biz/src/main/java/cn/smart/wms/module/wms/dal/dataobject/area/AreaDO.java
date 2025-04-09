package cn.smart.wms.module.wms.dal.dataobject.area;

import cn.smart.wms.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 货区 DO
 *
 * @author 芋道源码
 */
@TableName("wms_area")
@KeySequence("wms_area_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AreaDO extends BaseDO {

    /**
     * 货区ID
     */
    @TableId
    private Long id;
    /**
     * 货区编码
     */
    private String areaCode;
    /**
     * 货区名称
     */
    private String areaName;
    /**
     * 所属仓库ID
     */
    private Long warehouseId;
    /**
     * 货区类型(0-存储区 1-暂存区 2-拣货区 3-出货区)
     */
    private Integer areaType;
    /**
     * 状态(0-正常 1-禁用)
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;

}