package cn.smart.wms.module.wms.dal.mysql.moveorderdetail;

import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.mybatis.core.mapper.BaseMapperX;
import cn.smart.wms.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.smart.wms.module.wms.controller.admin.moveorderdetail.vo.MoveOrderDetailPageReqVO;
import cn.smart.wms.module.wms.dal.dataobject.moveorderdetail.MoveOrderDetailDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 移库单明细 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface MoveOrderDetailMapper extends BaseMapperX<MoveOrderDetailDO> {

    default PageResult<MoveOrderDetailDO> selectPage(MoveOrderDetailPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MoveOrderDetailDO>()
                .eqIfPresent(MoveOrderDetailDO::getMoveOrderId, reqVO.getMoveOrderId())
                .eqIfPresent(MoveOrderDetailDO::getItemId, reqVO.getItemId())
                .eqIfPresent(MoveOrderDetailDO::getBatchId, reqVO.getBatchId())
                .eqIfPresent(MoveOrderDetailDO::getPlanCount, reqVO.getPlanCount())
                .eqIfPresent(MoveOrderDetailDO::getRealCount, reqVO.getRealCount())
                .eqIfPresent(MoveOrderDetailDO::getStatus, reqVO.getStatus())
                .eqIfPresent(MoveOrderDetailDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(MoveOrderDetailDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(MoveOrderDetailDO::getId));
    }

}