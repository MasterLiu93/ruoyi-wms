package cn.smart.wms.module.wms.dal.dataobject.warehouse;

import cn.smart.wms.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;

/**
 * 仓库 DO
 *
 * @author 芋道源码
 */
@TableName("wms_warehouse")
@KeySequence("wms_warehouse_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseDO extends BaseDO {

    /**
     * 仓库ID
     */
    @TableId
    private Long id;
    /**
     * 仓库编码
     */
    private String warehouseCode;
    /**
     * 仓库名称
     */
    private String warehouseName;
    /**
     * 仓库类型(0-原材料 1-半成品 2-成品 3-退货)
     */
    private Integer warehouseType;
    /**
     * 面积(平方米)
     */
    private BigDecimal area;
    /**
     * 地址
     */
    private String address;
    /**
     * 负责人
     */
    private String chargePerson;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 状态(0-正常 1-禁用)
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;

}