package cn.smart.wms.module.wms.dal.mysql.batchinventory;

import java.util.*;

import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.smart.wms.framework.mybatis.core.mapper.BaseMapperX;
import cn.smart.wms.module.wms.dal.dataobject.batchinventory.BatchInventoryDO;
import org.apache.ibatis.annotations.Mapper;
import cn.smart.wms.module.wms.controller.admin.batchinventory.vo.*;

/**
 * 批次库存 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface BatchInventoryMapper extends BaseMapperX<BatchInventoryDO> {

    default PageResult<BatchInventoryDO> selectPage(BatchInventoryPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BatchInventoryDO>()
                .eqIfPresent(BatchInventoryDO::getBatchId, reqVO.getBatchId())
                .eqIfPresent(BatchInventoryDO::getItemId, reqVO.getItemId())
                .eqIfPresent(BatchInventoryDO::getWarehouseId, reqVO.getWarehouseId())
                .eqIfPresent(BatchInventoryDO::getLocationId, reqVO.getLocationId())
                .eqIfPresent(BatchInventoryDO::getStockCount, reqVO.getStockCount())
                .eqIfPresent(BatchInventoryDO::getAvailableCount, reqVO.getAvailableCount())
                .eqIfPresent(BatchInventoryDO::getLockedCount, reqVO.getLockedCount())
                .eqIfPresent(BatchInventoryDO::getStatus, reqVO.getStatus())
                .eqIfPresent(BatchInventoryDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(BatchInventoryDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(BatchInventoryDO::getId));
    }
    
    /**
     * 根据批次ID、物料ID、仓库ID和库位ID查询批次库存
     *
     * @param batchId 批次ID
     * @param itemId 物料ID
     * @param warehouseId 仓库ID
     * @param locationId 库位ID
     * @return 批次库存对象
     */
    default BatchInventoryDO selectByInfo(Long batchId, Long itemId, Long warehouseId, Long locationId) {
        return selectOne(new LambdaQueryWrapperX<BatchInventoryDO>()
                .eq(BatchInventoryDO::getBatchId, batchId)
                .eq(BatchInventoryDO::getItemId, itemId)
                .eq(BatchInventoryDO::getWarehouseId, warehouseId)
                .eq(BatchInventoryDO::getLocationId, locationId));
    }
    
    /**
     * 根据批次ID查询所有批次库存
     *
     * @param batchId 批次ID
     * @return 批次库存列表
     */
    default List<BatchInventoryDO> selectListByBatchId(Long batchId) {
        return selectList(BatchInventoryDO::getBatchId, batchId);
    }
    
    /**
     * 根据物料ID查询所有批次库存
     *
     * @param itemId 物料ID
     * @return 批次库存列表
     */
    default List<BatchInventoryDO> selectListByItemId(Long itemId) {
        return selectList(BatchInventoryDO::getItemId, itemId);
    }
    
    /**
     * 根据仓库ID查询所有批次库存
     *
     * @param warehouseId 仓库ID
     * @return 批次库存列表
     */
    default List<BatchInventoryDO> selectListByWarehouseId(Long warehouseId) {
        return selectList(BatchInventoryDO::getWarehouseId, warehouseId);
    }
    
    /**
     * 根据库位ID查询所有批次库存
     *
     * @param locationId 库位ID
     * @return 批次库存列表
     */
    default List<BatchInventoryDO> selectListByLocationId(Long locationId) {
        return selectList(BatchInventoryDO::getLocationId, locationId);
    }
}