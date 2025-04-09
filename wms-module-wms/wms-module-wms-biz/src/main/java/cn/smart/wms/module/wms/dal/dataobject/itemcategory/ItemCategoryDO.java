package cn.smart.wms.module.wms.dal.dataobject.itemcategory;

import cn.smart.wms.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 物料分类 DO
 *
 * @author 芋道源码
 */
@TableName("wms_item_category")
@KeySequence("wms_item_category_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemCategoryDO extends BaseDO {

    /**
     * 分类ID
     */
    @TableId
    private Long id;
    /**
     * 分类编码
     */
    private String categoryCode;
    /**
     * 分类名称
     */
    private String categoryName;
    /**
     * 父分类ID
     */
    private Long parentId;
    /**
     * 显示顺序
     */
    private Integer sort;
    /**
     * 状态(0-正常 1-禁用)
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;

}