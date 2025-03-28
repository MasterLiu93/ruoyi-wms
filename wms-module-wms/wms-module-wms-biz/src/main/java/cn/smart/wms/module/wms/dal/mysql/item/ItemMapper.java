package cn.smart.wms.module.wms.dal.mysql.item;

import java.util.*;

import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.smart.wms.framework.mybatis.core.mapper.BaseMapperX;
import cn.smart.wms.module.wms.dal.dataobject.item.ItemDO;
import org.apache.ibatis.annotations.Mapper;
import cn.smart.wms.module.wms.controller.admin.item.vo.*;

/**
 * 物料 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ItemMapper extends BaseMapperX<ItemDO> {

    default PageResult<ItemDO> selectPage(ItemPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ItemDO>()
                .eqIfPresent(ItemDO::getItemCode, reqVO.getItemCode())
                .likeIfPresent(ItemDO::getItemName, reqVO.getItemName())
                .eqIfPresent(ItemDO::getCategoryId, reqVO.getCategoryId())
                .eqIfPresent(ItemDO::getItemType, reqVO.getItemType())
                .eqIfPresent(ItemDO::getUnit, reqVO.getUnit())
                .eqIfPresent(ItemDO::getSpec, reqVO.getSpec())
                .eqIfPresent(ItemDO::getPrice, reqVO.getPrice())
                .eqIfPresent(ItemDO::getSafetyStock, reqVO.getSafetyStock())
                .eqIfPresent(ItemDO::getStatus, reqVO.getStatus())
                .eqIfPresent(ItemDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(ItemDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ItemDO::getId));
    }

}