package cn.smart.wms.module.wms.dal.mysql.itemcategory;

import java.util.*;

import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.smart.wms.framework.mybatis.core.mapper.BaseMapperX;
import cn.smart.wms.module.wms.dal.dataobject.itemcategory.ItemCategoryDO;
import org.apache.ibatis.annotations.Mapper;
import cn.smart.wms.module.wms.controller.admin.itemcategory.vo.*;

/**
 * 物料分类 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ItemCategoryMapper extends BaseMapperX<ItemCategoryDO> {

    default PageResult<ItemCategoryDO> selectPage(ItemCategoryPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ItemCategoryDO>()
                .eqIfPresent(ItemCategoryDO::getCategoryCode, reqVO.getCategoryCode())
                .likeIfPresent(ItemCategoryDO::getCategoryName, reqVO.getCategoryName())
                .eqIfPresent(ItemCategoryDO::getParentId, reqVO.getParentId())
                .eqIfPresent(ItemCategoryDO::getSort, reqVO.getSort())
                .eqIfPresent(ItemCategoryDO::getStatus, reqVO.getStatus())
                .eqIfPresent(ItemCategoryDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(ItemCategoryDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ItemCategoryDO::getId));
    }

}