package cn.smart.wms.module.wms.dal.mysql.moveorder;

import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.mybatis.core.mapper.BaseMapperX;
import cn.smart.wms.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.smart.wms.module.wms.controller.admin.moveorder.vo.MoveOrderPageReqVO;
import cn.smart.wms.module.wms.dal.dataobject.moveorder.MoveOrderDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 移库单 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface MoveOrderMapper extends BaseMapperX<MoveOrderDO> {

    default PageResult<MoveOrderDO> selectPage(MoveOrderPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MoveOrderDO>()
                .eqIfPresent(MoveOrderDO::getMoveOrderNo, reqVO.getMoveOrderNo())
                .eqIfPresent(MoveOrderDO::getMoveType, reqVO.getMoveType())
                .eqIfPresent(MoveOrderDO::getFromWarehouseId, reqVO.getFromWarehouseId())
                .eqIfPresent(MoveOrderDO::getToWarehouseId, reqVO.getToWarehouseId())
                .eqIfPresent(MoveOrderDO::getFromLocationId, reqVO.getFromLocationId())
                .eqIfPresent(MoveOrderDO::getToLocationId, reqVO.getToLocationId())
                .eqIfPresent(MoveOrderDO::getOrderStatus, reqVO.getOrderStatus())
                .eqIfPresent(MoveOrderDO::getMoveStatus, reqVO.getMoveStatus())
                .eqIfPresent(MoveOrderDO::getTotalCount, reqVO.getTotalCount())
                .eqIfPresent(MoveOrderDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(MoveOrderDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(MoveOrderDO::getId));
    }

}