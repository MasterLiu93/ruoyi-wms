package cn.smart.wms.module.wms.service.shipmentorderdetail;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.smart.wms.module.wms.controller.admin.shipmentorderdetail.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.shipmentorderdetail.ShipmentOrderDetailDO;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.pojo.PageParam;
import cn.smart.wms.framework.common.util.object.BeanUtils;

import cn.smart.wms.module.wms.dal.mysql.shipmentorderdetail.ShipmentOrderDetailMapper;

import static cn.smart.wms.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.smart.wms.module.wms.enums.ErrorCodeConstants.*;

/**
 * 出库单明细 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ShipmentOrderDetailServiceImpl implements ShipmentOrderDetailService {

    @Resource
    private ShipmentOrderDetailMapper shipmentOrderDetailMapper;

    @Override
    public Long createShipmentOrderDetail(ShipmentOrderDetailSaveReqVO createReqVO) {
        // 插入
        ShipmentOrderDetailDO shipmentOrderDetail = BeanUtils.toBean(createReqVO, ShipmentOrderDetailDO.class);
        shipmentOrderDetailMapper.insert(shipmentOrderDetail);
        // 返回
        return shipmentOrderDetail.getId();
    }

    @Override
    public void updateShipmentOrderDetail(ShipmentOrderDetailSaveReqVO updateReqVO) {
        // 校验存在
        validateShipmentOrderDetailExists(updateReqVO.getId());
        // 更新
        ShipmentOrderDetailDO updateObj = BeanUtils.toBean(updateReqVO, ShipmentOrderDetailDO.class);
        shipmentOrderDetailMapper.updateById(updateObj);
    }

    @Override
    public void deleteShipmentOrderDetail(Long id) {
        // 校验存在
        validateShipmentOrderDetailExists(id);
        // 删除
        shipmentOrderDetailMapper.deleteById(id);
    }

    private void validateShipmentOrderDetailExists(Long id) {
        if (shipmentOrderDetailMapper.selectById(id) == null) {
            throw exception(SHIPMENT_ORDER_DETAIL_NOT_EXISTS);
        }
    }

    @Override
    public ShipmentOrderDetailDO getShipmentOrderDetail(Long id) {
        return shipmentOrderDetailMapper.selectById(id);
    }

    @Override
    public PageResult<ShipmentOrderDetailDO> getShipmentOrderDetailPage(ShipmentOrderDetailPageReqVO pageReqVO) {
        return shipmentOrderDetailMapper.selectPage(pageReqVO);
    }

}