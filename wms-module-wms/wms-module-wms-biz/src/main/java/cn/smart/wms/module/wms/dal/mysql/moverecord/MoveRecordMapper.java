package cn.smart.wms.module.wms.dal.mysql.moverecord;

import java.util.*;

import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.smart.wms.framework.mybatis.core.mapper.BaseMapperX;
import cn.smart.wms.module.wms.dal.dataobject.moverecord.MoveRecordDO;
import org.apache.ibatis.annotations.Mapper;
import cn.smart.wms.module.wms.controller.admin.moverecord.vo.*;

/**
 * 移库操作记录 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface MoveRecordMapper extends BaseMapperX<MoveRecordDO> {

    default PageResult<MoveRecordDO> selectPage(MoveRecordPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MoveRecordDO>()
                .eqIfPresent(MoveRecordDO::getMoveOrderId, reqVO.getMoveOrderId())
                .eqIfPresent(MoveRecordDO::getMoveOrderDetailId, reqVO.getMoveOrderDetailId())
                .eqIfPresent(MoveRecordDO::getItemId, reqVO.getItemId())
                .eqIfPresent(MoveRecordDO::getBatchId, reqVO.getBatchId())
                .eqIfPresent(MoveRecordDO::getFromLocationId, reqVO.getFromLocationId())
                .eqIfPresent(MoveRecordDO::getToLocationId, reqVO.getToLocationId())
                .eqIfPresent(MoveRecordDO::getCount, reqVO.getCount())
                .eqIfPresent(MoveRecordDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(MoveRecordDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(MoveRecordDO::getId));
    }

}