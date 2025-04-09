package cn.smart.wms.module.wms.dal.dataobject.location;

import lombok.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.smart.wms.framework.mybatis.core.dataobject.BaseDO;

/**
 * 库位 DO
 *
 * @author 芋道源码
 */
@TableName("wms_location")
@KeySequence("wms_location_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationDO extends BaseDO {

    /**
     * 库位ID
     */
    @TableId
    private Long id;

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
     * 库位编号
     */
    private String locationCode;

    /**
     * 库位名称
     */
    private String locationName;

    /**
     * 库位类型
     */
    private Integer locationType;

    /**
     * 库位状态（0正常 1停用）
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

}