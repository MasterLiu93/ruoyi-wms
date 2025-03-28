package cn.smart.wms.module.wms.dal.mysql.shipmentorderdetail;

import java.util.*;

import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.smart.wms.framework.mybatis.core.mapper.BaseMapperX;
import cn.smart.wms.module.wms.dal.dataobject.shipmentorderdetail.ShipmentOrderDetailDO;
import org.apache.ibatis.annotations.Mapper;
import cn.smart.wms.module.wms.controller.admin.shipmentorderdetail.vo.*;

/**
 * 出库单明细 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ShipmentOrderDetailMapper extends BaseMapperX<ShipmentOrderDetailDO> {

    default PageResult<ShipmentOrderDetailDO> selectPage(ShipmentOrderDetailPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ShipmentOrderDetailDO>()
                .eqIfPresent(ShipmentOrderDetailDO::getShipmentOrderId, reqVO.getShipmentOrderId())
                .eqIfPresent(ShipmentOrderDetailDO::getItemId, reqVO.getItemId())
                .eqIfPresent(ShipmentOrderDetailDO::getPlanCount, reqVO.getPlanCount())
                .eqIfPresent(ShipmentOrderDetailDO::getRealCount, reqVO.getRealCount())
                .eqIfPresent(ShipmentOrderDetailDO::getLocationId, reqVO.getLocationId())
                .eqIfPresent(ShipmentOrderDetailDO::getBatchId, reqVO.getBatchId())
                .eqIfPresent(ShipmentOrderDetailDO::getPrice, reqVO.getPrice())
                .eqIfPresent(ShipmentOrderDetailDO::getStatus, reqVO.getStatus())
                .eqIfPresent(ShipmentOrderDetailDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(ShipmentOrderDetailDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ShipmentOrderDetailDO::getId));
    }

}