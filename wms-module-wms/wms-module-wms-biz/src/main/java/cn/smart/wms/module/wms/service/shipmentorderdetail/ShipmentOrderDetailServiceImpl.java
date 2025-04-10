package cn.smart.wms.module.wms.service.shipmentorderdetail;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import cn.smart.wms.module.wms.controller.admin.shipmentorderdetail.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.shipmentorderdetail.ShipmentOrderDetailDO;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.util.object.BeanUtils;

import cn.smart.wms.module.wms.dal.mysql.shipmentorderdetail.ShipmentOrderDetailMapper;

import static cn.smart.wms.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.smart.wms.module.wms.enums.ErrorCodeConstants.*;

import java.util.List;

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

    private ShipmentOrderDetailDO validateShipmentOrderDetailExists(Long id) {
        ShipmentOrderDetailDO detail = shipmentOrderDetailMapper.selectById(id);
        if (detail == null) {
            throw exception(SHIPMENT_ORDER_DETAIL_NOT_EXISTS);
        }
        return detail;
    }

    @Override
    public ShipmentOrderDetailDO getShipmentOrderDetail(Long id) {
        return shipmentOrderDetailMapper.selectById(id);
    }

    @Override
    public PageResult<ShipmentOrderDetailDO> getShipmentOrderDetailPage(ShipmentOrderDetailPageReqVO pageReqVO) {
        return shipmentOrderDetailMapper.selectPage(pageReqVO);
    }
    
    @Override
    public List<ShipmentOrderDetailDO> getShipmentOrderDetailListByOrderId(Long orderId) {
        return shipmentOrderDetailMapper.selectListByOrderId(orderId);
    }

    @Override
    public void updateShipmentOrderDetailStatus(Long id, Integer status) {
        // 校验存在
        validateShipmentOrderDetailExists(id);
        
        // 更新状态
        ShipmentOrderDetailDO updateObj = new ShipmentOrderDetailDO();
        updateObj.setId(id);
        updateObj.setStatus(status);
        shipmentOrderDetailMapper.updateById(updateObj);
    }

    @Override
    public boolean isAllShipped(Long orderId) {
        // 获取出库单所有明细
        List<ShipmentOrderDetailDO> details = getShipmentOrderDetailListByOrderId(orderId);
        if (details.isEmpty()) {
            return false; // 没有明细，返回false
        }
        
        // 检查是否所有明细都已完成出库（所有明细状态都为已出库）
        // 假设状态2表示已出库状态
        for (ShipmentOrderDetailDO detail : details) {
            if (detail.getStatus() != 2) { // 如果有任何一个明细未出库，返回false
                return false;
            }
        }
        
        // 所有明细都已出库
        return true;
    }

    @Override
    public void updateRealCount(Long id, Integer quantity) {
        // 校验存在
        ShipmentOrderDetailDO detail = validateShipmentOrderDetailExists(id);
        
        // 计算新的实际出库数量
        Integer newRealCount = (detail.getRealCount() != null ? detail.getRealCount() : 0) + quantity;
        
        // 确保不超过计划数量
        if (newRealCount > detail.getPlanCount()) {
            throw exception(INVENTORY_NOT_ENOUGH); // 使用合适的错误码
        }
        
        // 更新实际出库数量
        ShipmentOrderDetailDO updateObj = new ShipmentOrderDetailDO();
        updateObj.setId(id);
        updateObj.setRealCount(newRealCount);
        
        // 同时更新状态
        // 0-未出库 1-部分出库 2-已出库
        int status;
        if (newRealCount == 0) {
            status = 0; // 未出库
        } else if (newRealCount.equals(detail.getPlanCount())) {
            status = 2; // 已出库
        } else {
            status = 1; // 部分出库
        }
        updateObj.setStatus(status);
        
        shipmentOrderDetailMapper.updateById(updateObj);
    }
}