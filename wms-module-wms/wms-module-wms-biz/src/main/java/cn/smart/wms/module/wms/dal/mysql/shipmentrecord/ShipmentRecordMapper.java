package cn.smart.wms.module.wms.dal.mysql.shipmentrecord;

import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.mybatis.core.mapper.BaseMapperX;
import cn.smart.wms.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.smart.wms.module.wms.controller.admin.shipmentrecord.vo.ShipmentRecordPageReqVO;
import cn.smart.wms.module.wms.dal.dataobject.shipmentrecord.ShipmentRecordDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 出库操作记录 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ShipmentRecordMapper extends BaseMapperX<ShipmentRecordDO> {

    default PageResult<ShipmentRecordDO> selectPage(ShipmentRecordPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ShipmentRecordDO>()
                .eqIfPresent(ShipmentRecordDO::getShipmentOrderId, reqVO.getShipmentOrderId())
                .eqIfPresent(ShipmentRecordDO::getShipmentOrderDetailId, reqVO.getShipmentOrderDetailId())
                .eqIfPresent(ShipmentRecordDO::getItemId, reqVO.getItemId())
                .eqIfPresent(ShipmentRecordDO::getLocationId, reqVO.getLocationId())
                .eqIfPresent(ShipmentRecordDO::getBatchId, reqVO.getBatchId())
                .eqIfPresent(ShipmentRecordDO::getCount, reqVO.getCount())
                .eqIfPresent(ShipmentRecordDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(ShipmentRecordDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ShipmentRecordDO::getId));
    }

}