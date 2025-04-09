package cn.smart.wms.module.wms.dal.mysql.shipmentorder;

import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.smart.wms.framework.mybatis.core.mapper.BaseMapperX;
import cn.smart.wms.module.wms.dal.dataobject.shipmentorder.ShipmentOrderDO;
import org.apache.ibatis.annotations.Mapper;
import cn.smart.wms.module.wms.controller.admin.shipmentorder.vo.*;

/**
 * 出库单 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ShipmentOrderMapper extends BaseMapperX<ShipmentOrderDO> {

    default PageResult<ShipmentOrderDO> selectPage(ShipmentOrderPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ShipmentOrderDO>()
                .eqIfPresent(ShipmentOrderDO::getShipmentOrderNo, reqVO.getShipmentOrderNo())
                .eqIfPresent(ShipmentOrderDO::getShipmentType, reqVO.getShipmentType())
                .eqIfPresent(ShipmentOrderDO::getCustomerId, reqVO.getCustomerId())
                .eqIfPresent(ShipmentOrderDO::getWarehouseId, reqVO.getWarehouseId())
                .eqIfPresent(ShipmentOrderDO::getOrderStatus, reqVO.getOrderStatus())
                .eqIfPresent(ShipmentOrderDO::getShipmentStatus, reqVO.getShipmentStatus())
                .betweenIfPresent(ShipmentOrderDO::getExpectTime, reqVO.getExpectTime())
                .betweenIfPresent(ShipmentOrderDO::getCompleteTime, reqVO.getCompleteTime())
                .eqIfPresent(ShipmentOrderDO::getTotalCount, reqVO.getTotalCount())
                .eqIfPresent(ShipmentOrderDO::getTotalAmount, reqVO.getTotalAmount())
                .eqIfPresent(ShipmentOrderDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(ShipmentOrderDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ShipmentOrderDO::getId));
    }

}