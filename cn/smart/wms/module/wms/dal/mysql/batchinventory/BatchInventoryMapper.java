package cn.smart.wms.module.wms.dal.mysql.batchinventory;

import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.mybatis.core.mapper.BaseMapperX;
import cn.smart.wms.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.smart.wms.module.wms.controller.admin.batchinventory.vo.BatchInventoryPageReqVO;
import cn.smart.wms.module.wms.dal.dataobject.batchinventory.BatchInventoryDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 批次库存 Mapper
 */
@Mapper
public interface BatchInventoryMapper extends BaseMapperX<BatchInventoryDO> {

    default PageResult<BatchInventoryDO> selectPage(BatchInventoryPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BatchInventoryDO>()
                .eqIfPresent(BatchInventoryDO::getBatchId, reqVO.getBatchId())
                .eqIfPresent(BatchInventoryDO::getItemId, reqVO.getItemId())
                .eqIfPresent(BatchInventoryDO::getWarehouseId, reqVO.getWarehouseId())
                .eqIfPresent(BatchInventoryDO::getLocationId, reqVO.getLocationId())
                .betweenIfPresent(BatchInventoryDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(BatchInventoryDO::getId));
    }

} 