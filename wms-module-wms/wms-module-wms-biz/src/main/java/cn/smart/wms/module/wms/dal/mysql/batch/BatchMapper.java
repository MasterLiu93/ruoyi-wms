package cn.smart.wms.module.wms.dal.mysql.batch;

import java.util.*;

import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.smart.wms.framework.mybatis.core.mapper.BaseMapperX;
import cn.smart.wms.module.wms.dal.dataobject.batch.BatchDO;
import org.apache.ibatis.annotations.Mapper;
import cn.smart.wms.module.wms.controller.admin.batch.vo.*;

/**
 * 批次信息 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface BatchMapper extends BaseMapperX<BatchDO> {

    default PageResult<BatchDO> selectPage(BatchPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BatchDO>()
                .eqIfPresent(BatchDO::getBatchNo, reqVO.getBatchNo())
                .eqIfPresent(BatchDO::getItemId, reqVO.getItemId())
                .eqIfPresent(BatchDO::getWarehouseId, reqVO.getWarehouseId())
                .eqIfPresent(BatchDO::getSupplierId, reqVO.getSupplierId())
                .betweenIfPresent(BatchDO::getProductionDate, reqVO.getProductionDate())
                .betweenIfPresent(BatchDO::getExpiryDate, reqVO.getExpiryDate())
                .eqIfPresent(BatchDO::getBatchAttr1, reqVO.getBatchAttr1())
                .eqIfPresent(BatchDO::getBatchAttr2, reqVO.getBatchAttr2())
                .eqIfPresent(BatchDO::getBatchAttr3, reqVO.getBatchAttr3())
                .eqIfPresent(BatchDO::getBatchAttr4, reqVO.getBatchAttr4())
                .eqIfPresent(BatchDO::getTotalCount, reqVO.getTotalCount())
                .eqIfPresent(BatchDO::getAvailableCount, reqVO.getAvailableCount())
                .eqIfPresent(BatchDO::getLockedCount, reqVO.getLockedCount())
                .eqIfPresent(BatchDO::getStatus, reqVO.getStatus())
                .eqIfPresent(BatchDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(BatchDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(BatchDO::getId));
    }

}