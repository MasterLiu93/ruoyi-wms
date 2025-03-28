package cn.smart.wms.module.wms.dal.mysql.inventory;

import java.util.*;

import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.smart.wms.framework.mybatis.core.mapper.BaseMapperX;
import cn.smart.wms.module.wms.dal.dataobject.inventory.InventoryDO;
import org.apache.ibatis.annotations.Mapper;
import cn.smart.wms.module.wms.controller.admin.inventory.vo.*;

/**
 * 库存 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface InventoryMapper extends BaseMapperX<InventoryDO> {

    default PageResult<InventoryDO> selectPage(InventoryPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<InventoryDO>()
                .eqIfPresent(InventoryDO::getWarehouseId, reqVO.getWarehouseId())
                .eqIfPresent(InventoryDO::getLocationId, reqVO.getLocationId())
                .eqIfPresent(InventoryDO::getItemId, reqVO.getItemId())
                .eqIfPresent(InventoryDO::getStockCount, reqVO.getStockCount())
                .eqIfPresent(InventoryDO::getAvailableCount, reqVO.getAvailableCount())
                .eqIfPresent(InventoryDO::getLockedCount, reqVO.getLockedCount())
                .eqIfPresent(InventoryDO::getStatus, reqVO.getStatus())
                .eqIfPresent(InventoryDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(InventoryDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(InventoryDO::getId));
    }

}