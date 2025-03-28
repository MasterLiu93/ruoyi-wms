package cn.smart.wms.module.wms.dal.dataobject.location;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
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
     * 库位编码
     */
    private String locationCode;
    /**
     * 库位名称
     */
    private String locationName;
    /**
     * 所属货架ID
     */
    private Long rackId;
    /**
     * 库位类型(0-普通 1-快检 2-退货)
     */
    private Integer locationType;
    /**
     * 状态(0-空闲 1-占用 2-锁定 3-禁用)
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;

}