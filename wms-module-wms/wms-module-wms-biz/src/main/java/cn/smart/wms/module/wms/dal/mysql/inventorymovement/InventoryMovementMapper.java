package cn.smart.wms.module.wms.dal.mysql.inventorymovement;

import java.util.*;

import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.smart.wms.framework.mybatis.core.mapper.BaseMapperX;
import cn.smart.wms.module.wms.dal.dataobject.inventorymovement.InventoryMovementDO;
import org.apache.ibatis.annotations.Mapper;
import cn.smart.wms.module.wms.controller.admin.inventorymovement.vo.*;

/**
 * 库存移动记录 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface InventoryMovementMapper extends BaseMapperX<InventoryMovementDO> {

    default PageResult<InventoryMovementDO> selectPage(InventoryMovementPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<InventoryMovementDO>()
                .eqIfPresent(InventoryMovementDO::getMovementType, reqVO.getMovementType())
                .eqIfPresent(InventoryMovementDO::getMovementNo, reqVO.getMovementNo())
                .eqIfPresent(InventoryMovementDO::getWarehouseId, reqVO.getWarehouseId())
                .eqIfPresent(InventoryMovementDO::getLocationId, reqVO.getLocationId())
                .eqIfPresent(InventoryMovementDO::getItemId, reqVO.getItemId())
                .eqIfPresent(InventoryMovementDO::getCount, reqVO.getCount())
                .eqIfPresent(InventoryMovementDO::getBeforeCount, reqVO.getBeforeCount())
                .eqIfPresent(InventoryMovementDO::getAfterCount, reqVO.getAfterCount())
                .eqIfPresent(InventoryMovementDO::getBusinessType, reqVO.getBusinessType())
                .eqIfPresent(InventoryMovementDO::getBusinessId, reqVO.getBusinessId())
                .eqIfPresent(InventoryMovementDO::getBusinessDetailId, reqVO.getBusinessDetailId())
                .eqIfPresent(InventoryMovementDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(InventoryMovementDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(InventoryMovementDO::getId));
    }

}