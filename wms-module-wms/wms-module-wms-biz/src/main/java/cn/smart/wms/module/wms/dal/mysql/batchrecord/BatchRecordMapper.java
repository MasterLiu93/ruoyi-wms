package cn.smart.wms.module.wms.dal.mysql.batchrecord;

import java.util.*;

import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.smart.wms.framework.mybatis.core.mapper.BaseMapperX;
import cn.smart.wms.module.wms.dal.dataobject.batchrecord.BatchRecordDO;
import org.apache.ibatis.annotations.Mapper;
import cn.smart.wms.module.wms.controller.admin.batchrecord.vo.*;

/**
 * 批次操作记录 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface BatchRecordMapper extends BaseMapperX<BatchRecordDO> {

    default PageResult<BatchRecordDO> selectPage(BatchRecordPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BatchRecordDO>()
                .eqIfPresent(BatchRecordDO::getBatchId, reqVO.getBatchId())
                .eqIfPresent(BatchRecordDO::getMovementType, reqVO.getMovementType())
                .eqIfPresent(BatchRecordDO::getMovementId, reqVO.getMovementId())
                .eqIfPresent(BatchRecordDO::getCount, reqVO.getCount())
                .eqIfPresent(BatchRecordDO::getBeforeCount, reqVO.getBeforeCount())
                .eqIfPresent(BatchRecordDO::getAfterCount, reqVO.getAfterCount())
                .eqIfPresent(BatchRecordDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(BatchRecordDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(BatchRecordDO::getId));
    }

}