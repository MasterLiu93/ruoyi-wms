package cn.smart.wms.module.wms.service.shipmentorder;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.smart.wms.module.wms.controller.admin.shipmentorder.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.shipmentorder.ShipmentOrderDO;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.pojo.PageParam;
import cn.smart.wms.framework.common.util.object.BeanUtils;

import cn.smart.wms.module.wms.dal.mysql.shipmentorder.ShipmentOrderMapper;

import static cn.smart.wms.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.smart.wms.module.wms.enums.ErrorCodeConstants.*;

/**
 * 出库单 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ShipmentOrderServiceImpl implements ShipmentOrderService {

    @Resource
    private ShipmentOrderMapper shipmentOrderMapper;

    @Override
    public Long createShipmentOrder(ShipmentOrderSaveReqVO createReqVO) {
        // 插入
        ShipmentOrderDO shipmentOrder = BeanUtils.toBean(createReqVO, ShipmentOrderDO.class);
        shipmentOrderMapper.insert(shipmentOrder);
        // 返回
        return shipmentOrder.getId();
    }

    @Override
    public void updateShipmentOrder(ShipmentOrderSaveReqVO updateReqVO) {
        // 校验存在
        validateShipmentOrderExists(updateReqVO.getId());
        // 更新
        ShipmentOrderDO updateObj = BeanUtils.toBean(updateReqVO, ShipmentOrderDO.class);
        shipmentOrderMapper.updateById(updateObj);
    }

    @Override
    public void deleteShipmentOrder(Long id) {
        // 校验存在
        validateShipmentOrderExists(id);
        // 删除
        shipmentOrderMapper.deleteById(id);
    }

    private void validateShipmentOrderExists(Long id) {
        if (shipmentOrderMapper.selectById(id) == null) {
            throw exception(SHIPMENT_ORDER_NOT_EXISTS);
        }
    }

    @Override
    public ShipmentOrderDO getShipmentOrder(Long id) {
        return shipmentOrderMapper.selectById(id);
    }

    @Override
    public PageResult<ShipmentOrderDO> getShipmentOrderPage(ShipmentOrderPageReqVO pageReqVO) {
        return shipmentOrderMapper.selectPage(pageReqVO);
    }

}