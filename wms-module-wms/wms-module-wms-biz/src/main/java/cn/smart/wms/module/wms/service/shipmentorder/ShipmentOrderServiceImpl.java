package cn.smart.wms.module.wms.service.shipmentorder;

import cn.smart.wms.framework.lock.core.annotation.DistributedLock;
import cn.smart.wms.module.wms.enums.ErrorCodeConstants;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import cn.smart.wms.module.wms.controller.admin.shipmentorder.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.shipmentorder.ShipmentOrderDO;
import cn.smart.wms.module.wms.dal.dataobject.shipmentorderdetail.ShipmentOrderDetailDO;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.util.object.BeanUtils;

import cn.smart.wms.module.wms.dal.mysql.shipmentorder.ShipmentOrderMapper;
import cn.smart.wms.module.wms.service.shipmentorderdetail.ShipmentOrderDetailService;
import cn.smart.wms.framework.idgenerator.core.IdGeneratorFactory;
import cn.smart.wms.framework.security.core.util.SecurityFrameworkUtils;

import static cn.smart.wms.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.smart.wms.module.wms.enums.ErrorCodeConstants.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.smart.wms.module.wms.service.audit.OrderAuditService;
import cn.smart.wms.module.wms.enums.OrderTypeEnum;
import cn.smart.wms.module.wms.enums.AuditStatusEnum;
import cn.smart.wms.module.wms.service.inventory.InventoryService;
import cn.smart.wms.module.wms.service.batchinventory.BatchInventoryService;
import cn.smart.wms.module.wms.service.shipmentrecord.ShipmentRecordService;
import cn.smart.wms.module.wms.controller.admin.shipmentrecord.vo.ShipmentRecordSaveReqVO;
import cn.smart.wms.module.wms.dal.dataobject.inventory.InventoryDO;

import java.math.BigDecimal;
import org.springframework.util.ObjectUtils;

import cn.smart.wms.module.wms.enums.shipment.ShipmentStatusEnum;
import cn.smart.wms.module.wms.enums.shipment.ShipmentOrderStatusEnum;
import cn.smart.wms.module.wms.controller.admin.shipmentorderdetail.vo.ShipmentOrderDetailSaveReqVO;
import cn.smart.wms.module.wms.enums.shipment.ShipmentDetailStatusEnum;

/**
 * 出库单 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ShipmentOrderServiceImpl implements ShipmentOrderService {

    private static final Logger log = LoggerFactory.getLogger(ShipmentOrderServiceImpl.class);

    @Resource
    private ShipmentOrderMapper shipmentOrderMapper;
    
    @Resource
    private ShipmentOrderDetailService shipmentOrderDetailService;
    
    @Resource
    private IdGeneratorFactory idGeneratorFactory;

    @Resource
    private OrderAuditService orderAuditService;

    @Resource
    private InventoryService inventoryService;

    @Resource
    private BatchInventoryService batchInventoryService;

    @Resource
    private ShipmentRecordService shipmentRecordService;

    @Override
    public Long createShipmentOrder(ShipmentOrderSaveReqVO createReqVO) {
        // 插入
        ShipmentOrderDO shipmentOrder = BeanUtils.toBean(createReqVO, ShipmentOrderDO.class);
        // 如果没有指定状态，默认设置为草稿状态
        if (shipmentOrder.getOrderStatus() == null) {
            shipmentOrder.setOrderStatus(0); // 0-草稿状态
        }
        if (shipmentOrder.getShipmentStatus() == null) {
            shipmentOrder.setShipmentStatus(0); // 0-待出库
        }
        // 生成出库单号
        if (shipmentOrder.getShipmentOrderNo() == null || shipmentOrder.getShipmentOrderNo().isEmpty()) {
            shipmentOrder.setShipmentOrderNo(idGeneratorFactory.generateShipmentOrderNo());
        }
        shipmentOrderMapper.insert(shipmentOrder);
        // 返回
        return shipmentOrder.getId();
    }

    @Override
    public void updateShipmentOrder(ShipmentOrderSaveReqVO updateReqVO) {
        // 校验存在
        validateShipmentOrderExists(updateReqVO.getId());
        ShipmentOrderDO shipmentOrder = shipmentOrderMapper.selectById(updateReqVO.getId());
        // 状态判断：只有草稿状态可以更新
        if (!Objects.equals(shipmentOrder.getOrderStatus(), 0)) {
            throw exception(SHIPMENT_ORDER_UPDATE_ONLY_DRAFT_STATUS);
        }
        // 更新
        ShipmentOrderDO updateObj = BeanUtils.toBean(updateReqVO, ShipmentOrderDO.class);
        shipmentOrderMapper.updateById(updateObj);
    }

    @Override
    public void deleteShipmentOrder(Long id) {
        // 校验存在
        ShipmentOrderDO shipmentOrder = validateShipmentOrderExists(id);
        // 状态判断：只有草稿状态可以删除
        if (!Objects.equals(shipmentOrder.getOrderStatus(), 0)) {
            throw exception(SHIPMENT_ORDER_DELETE_ONLY_DRAFT_STATUS);
        }
        // 删除
        shipmentOrderMapper.deleteById(id);
        // 删除明细
        List<ShipmentOrderDetailDO> details = shipmentOrderDetailService.getShipmentOrderDetailListByOrderId(id);
        for (ShipmentOrderDetailDO detail : details) {
            shipmentOrderDetailService.deleteShipmentOrderDetail(detail.getId());
        }
    }

    private ShipmentOrderDO validateShipmentOrderExists(Long id) {
        ShipmentOrderDO shipmentOrder = shipmentOrderMapper.selectById(id);
        if (shipmentOrder == null) {
            throw exception(SHIPMENT_ORDER_NOT_EXISTS);
        }
        return shipmentOrder;
    }

    @Override
    public ShipmentOrderDO getShipmentOrder(Long id) {
        return shipmentOrderMapper.selectById(id);
    }

    @Override
    public PageResult<ShipmentOrderDO> getShipmentOrderPage(ShipmentOrderPageReqVO pageReqVO) {
        return shipmentOrderMapper.selectPage(pageReqVO);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createShipmentOrderWithDetails(ShipmentOrderSaveReqVO createReqVO) {
        // 先创建出库单
        Long orderId = createShipmentOrder(createReqVO);
        
        // 如果有明细，保存明细
        if (createReqVO.getDetails() != null && !createReqVO.getDetails().isEmpty()) {
            // 设置明细中的出库单ID
            createReqVO.getDetails().forEach(detail -> {
                detail.setShipmentOrderId(orderId);
                // 设置仓库ID，与主表一致
                detail.setWarehouseId(createReqVO.getWarehouseId());
            });
            
            // 逐个保存明细
            for (ShipmentOrderDetailDO detail : createReqVO.getDetails()) {
                // 设置默认状态
                if (detail.getStatus() == null) {
                    detail.setStatus(0); // 0-待出库
                }
                shipmentOrderDetailService.createShipmentOrderDetail(BeanUtils.toBean(detail, cn.smart.wms.module.wms.controller.admin.shipmentorderdetail.vo.ShipmentOrderDetailSaveReqVO.class));
            }
        }
        
        return orderId;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateShipmentOrderWithDetails(ShipmentOrderSaveReqVO updateReqVO) {
        // 校验出库单存在
        ShipmentOrderDO existingOrder = validateShipmentOrderExists(updateReqVO.getId());
        
        // 状态判断：只有草稿状态可以更新
        if (!Objects.equals(existingOrder.getOrderStatus(), 0)) {
            throw exception(SHIPMENT_ORDER_UPDATE_ONLY_DRAFT_STATUS);
        }
        
        // 先更新出库单
        updateShipmentOrder(updateReqVO);
        
        // 如果有明细，处理明细
        if (updateReqVO.getDetails() != null) {
            // 1. 获取当前出库单下所有明细
            List<ShipmentOrderDetailDO> existingDetails = shipmentOrderDetailService.getShipmentOrderDetailListByOrderId(updateReqVO.getId());
            
            // 2. 获取需要保留的明细ID集合
            Set<Long> retainDetailIds = updateReqVO.getDetails().stream()
                    .map(ShipmentOrderDetailDO::getId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
                    
            // 3. 删除不再需要的明细
            existingDetails.stream()
                    .filter(detail -> !retainDetailIds.contains(detail.getId()))
                    .forEach(detail -> shipmentOrderDetailService.deleteShipmentOrderDetail(detail.getId()));
                    
            // 4. 更新或新增明细
            for (ShipmentOrderDetailDO detail : updateReqVO.getDetails()) {
                cn.smart.wms.module.wms.controller.admin.shipmentorderdetail.vo.ShipmentOrderDetailSaveReqVO detailVO = BeanUtils.toBean(detail, cn.smart.wms.module.wms.controller.admin.shipmentorderdetail.vo.ShipmentOrderDetailSaveReqVO.class);
                
                // 设置出库单ID
                detailVO.setShipmentOrderId(updateReqVO.getId());
                
                // 设置仓库ID，与主表一致
                detailVO.setWarehouseId(updateReqVO.getWarehouseId());
                
                if (detail.getId() != null) {
                    // 更新已有明细
                    shipmentOrderDetailService.updateShipmentOrderDetail(detailVO);
                } else {
                    // 新增明细
                    // 设置默认状态
                    if (detailVO.getStatus() == null) {
                        detailVO.setStatus(0); // 0-待出库
                    }
                    shipmentOrderDetailService.createShipmentOrderDetail(detailVO);
                }
            }
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitShipmentOrder(Long id) {
        // 校验存在
        ShipmentOrderDO shipmentOrder = validateShipmentOrderExists(id);
        // 校验状态
        if (!ObjectUtils.nullSafeEquals(shipmentOrder.getOrderStatus(), ShipmentOrderStatusEnum.DRAFT.getStatus())) {
            throw exception(SHIPMENT_ORDER_STATUS_ERROR);
        }
        
        // 更新提交状态
        shipmentOrderMapper.updateById(new ShipmentOrderDO().setId(id)
                .setOrderStatus(ShipmentOrderStatusEnum.PENDING_APPROVAL.getStatus()));
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approveShipmentOrder(Long id, Boolean approved, String remark) {
        // 校验存在
        ShipmentOrderDO shipmentOrder = validateShipmentOrderExists(id);
        // 校验状态
        if (!ObjectUtils.nullSafeEquals(shipmentOrder.getOrderStatus(), ShipmentOrderStatusEnum.PENDING_APPROVAL.getStatus())) {
            throw exception(SHIPMENT_ORDER_STATUS_ERROR);
        }
        
        // 设置审核人信息
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        String loginUserNickname = SecurityFrameworkUtils.getLoginUserNickname();
        
        // 更新订单状态
        Integer orderStatus = approved 
            ? ShipmentOrderStatusEnum.APPROVED.getStatus() 
            : ShipmentOrderStatusEnum.REJECTED.getStatus();
            
        shipmentOrderMapper.updateById(new ShipmentOrderDO()
                .setId(id)
                .setOrderStatus(orderStatus));
        
        // 创建审核记录
        orderAuditService.createOrderAudit(
            id,
            OrderTypeEnum.SHIPMENT.getType(),
            approved ? AuditStatusEnum.APPROVED.getStatus() : AuditStatusEnum.REJECTED.getStatus(),
            remark
        );
        
        // 如果审核通过，则自动执行出库操作（减库存）
        if (approved) {
            executeShipmentAfterApproval(id);
        }
        
        log.info("出库单审核完成, 单号: {}, 审核结果: {}", shipmentOrder.getShipmentOrderNo(), 
                approved ? "通过" : "拒绝");
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelShipmentOrder(Long id) {
        // 校验存在
        ShipmentOrderDO shipmentOrder = validateShipmentOrderExists(id);
        // 校验是否为审核通过的状态
        if (!Objects.equals(ShipmentOrderStatusEnum.APPROVED.getStatus(), shipmentOrder.getOrderStatus())) {
            throw exception(ErrorCodeConstants.SHIPMENT_ORDER_STATUS_ERROR);
        }
        // 更新
        shipmentOrderMapper.updateById(new ShipmentOrderDO().setId(id).setOrderStatus(ShipmentOrderStatusEnum.CANCELLED.getStatus()));
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completeShipmentOrder(Long id) {
        // 校验存在
        ShipmentOrderDO shipmentOrder = validateShipmentOrderExists(id);
        // 校验是否为审核通过的状态
        if (!Objects.equals(ShipmentOrderStatusEnum.APPROVED.getStatus(), shipmentOrder.getOrderStatus())) {
            throw exception(ErrorCodeConstants.SHIPMENT_ORDER_STATUS_ERROR);
        }
        // 校验是否已经全部出库完成
        List<ShipmentOrderDetailDO> details = shipmentOrderDetailService.getShipmentOrderDetailListByOrderId(id);
        for (ShipmentOrderDetailDO detail : details) {
            if (detail.getRealCount() < detail.getPlanCount()) {
                throw exception(ErrorCodeConstants.SHIPMENT_ORDER_NOT_COMPLETE);
            }
        }
        // 更新出库单状态为已完成
        shipmentOrderMapper.updateById(new ShipmentOrderDO()
                .setId(id)
                .setCompleteTime(LocalDateTime.now())
                .setShipmentStatus(ShipmentStatusEnum.COMPLETED.getStatus()));
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    @DistributedLock(prefix = "shipment", key = "#reqVO.shipmentOrderId", errorMessage = "出库处理中，请稍后再试")
    public void executeShipment(ShipmentOperationReqVO reqVO) {
        // 校验出库单存在
        ShipmentOrderDO shipmentOrder = validateShipmentOrderExists(reqVO.getShipmentOrderId());
        // 校验出库单状态是否为审核通过
        if (!Objects.equals(ShipmentOrderStatusEnum.APPROVED.getStatus(), shipmentOrder.getOrderStatus())) {
            throw exception(SHIPMENT_ORDER_STATUS_ERROR);
        }
        
        // 校验明细存在
        ShipmentOrderDetailDO detail = shipmentOrderDetailService.getShipmentOrderDetail(reqVO.getDetailId());
        if (detail == null) {
            throw exception(SHIPMENT_ORDER_DETAIL_NOT_EXISTS);
        }
        
        // 校验出库数量
        if (reqVO.getQuantity() <= 0 || reqVO.getQuantity() > (detail.getPlanCount() - detail.getRealCount())) {
            throw exception(SHIPMENT_ORDER_QUANTITY_ERROR);
        }
        
        // 执行库存出库操作
        inventoryService.decreaseInventory(
            reqVO.getWarehouseId(),           // 仓库ID
            detail.getItemId(),              // 物料ID
            reqVO.getLocationId(),           // 库位ID，可能为null
            reqVO.getQuantity(),             // 出库数量
            "SHIPMENT",                      // 业务类型
            reqVO.getShipmentOrderId(),      // 业务单ID
            reqVO.getDetailId(),             // 业务明细ID
            reqVO.getRemark() != null ? reqVO.getRemark() : "手动执行出库" // 备注
        );
        
        // 更新明细已出库数量
        shipmentOrderDetailService.updateRealCount(reqVO.getDetailId(), reqVO.getQuantity());
        
        // 记录出库记录
        // TODO: 如果需要保存出库记录，请根据ShipmentRecordSaveReqVO的具体字段实现
        
        // 判断是否全部出库完成
        boolean isAllShipped = true;
        List<ShipmentOrderDetailDO> details = shipmentOrderDetailService.getShipmentOrderDetailListByOrderId(reqVO.getShipmentOrderId());
        for (ShipmentOrderDetailDO d : details) {
            if (d.getRealCount() < d.getPlanCount()) {
                isAllShipped = false;
                break;
            }
        }
        
        // 如果全部出库完成，更新出库单状态
        if (isAllShipped) {
            shipmentOrderMapper.updateById(new ShipmentOrderDO()
                    .setId(reqVO.getShipmentOrderId())
                    .setCompleteTime(LocalDateTime.now())
                    .setShipmentStatus(ShipmentStatusEnum.COMPLETED.getStatus()));
        } else {
            // 更新出库单状态为部分出库
            shipmentOrderMapper.updateById(new ShipmentOrderDO()
                    .setId(reqVO.getShipmentOrderId())
                    .setShipmentStatus(ShipmentStatusEnum.PARTIAL.getStatus()));
        }
    }
    
    /**
     * 审核通过后自动执行的出库操作（批量操作）
     * 
     * @param orderId 出库单ID
     */
    @DistributedLock(prefix = "shipment", key = "#orderId", errorMessage = "出库处理中，请稍后再试")
    private void executeShipmentAfterApproval(Long orderId) {
        // 获取出库单
        ShipmentOrderDO shipmentOrder = shipmentOrderMapper.selectById(orderId);
        if (shipmentOrder == null) {
            return;
        }
        
        try {
            log.info("执行出库操作，出库单号：{}", shipmentOrder.getShipmentOrderNo());
            
            // 获取所有明细
            List<ShipmentOrderDetailDO> details = shipmentOrderDetailService.getShipmentOrderDetailListByOrderId(orderId);
            if (details.isEmpty()) {
                return;
            }
            
            // 标记是否所有明细都完全出库
            boolean isAllCompleted = true;
            
            // 减少库存（按明细批量减库存）
            for (ShipmentOrderDetailDO detail : details) {
                try {
                    // 获取计划数量和实际出库数量
                    Integer planCount = detail.getPlanCount();
                    Integer realCount = detail.getRealCount();
                    
                    if (planCount == null || planCount <= 0) {
                        continue; // 跳过计划数量为空或小于等于0的明细
                    }
                    
                    // 使用明细上的实际出库数量
                    // 如果实际出库数量为null或0，说明是首次出库，则使用计划数量
                    Integer actualShipmentCount = (realCount != null && realCount > 0) ? realCount : planCount;
                    
                    // 获取库位ID
                    Long locationId = detail.getLocationId() != null && detail.getLocationId() > 0 ? 
                                     detail.getLocationId() : null;
                    
                    // 获取当前库存以便于日志记录
                    InventoryDO currentInventory = inventoryService.getInventoryByWarehouseAndItemAndLocation(
                        shipmentOrder.getWarehouseId(), 
                        detail.getItemId(),
                        locationId
                    );
                    
                    Integer currentStock = 0;
                    if (currentInventory != null) {
                        currentStock = currentInventory.getStockCount() != null ? currentInventory.getStockCount() : 0;
                    }
                    
                    // 执行库存减少，减去实际出库数量
                    try {
                        inventoryService.decreaseInventory(
                            shipmentOrder.getWarehouseId(),  // 仓库ID
                            detail.getItemId(),              // 物料ID
                            locationId,                      // 库位ID，可能为null
                            actualShipmentCount,             // 实际出库数量
                            "SHIPMENT",                      // 业务类型
                            orderId,                         // 业务单ID
                            detail.getId(),                  // 业务明细ID
                            "审核通过自动出库"                 // 备注
                        );
                        
                        // 打印库存变化日志
                        log.info("库存减少，物料ID={}，原库存={}，出库数量={}，剩余库存={}", 
                              detail.getItemId(), currentStock, actualShipmentCount, (currentStock - actualShipmentCount));
                    } catch (Exception e) {
                        log.error("库存减少失败，明细ID={}，物料ID={}，出库数量={}，错误：{}", 
                                detail.getId(), detail.getItemId(), actualShipmentCount, e.getMessage());
                        isAllCompleted = false;
                        continue; // 继续处理其他明细
                    }
                    
                    // 更新明细状态为已出库
                    shipmentOrderDetailService.updateShipmentOrderDetailStatus(detail.getId(), ShipmentDetailStatusEnum.COMPLETED.getStatus());
                    
                    // 创建出库记录
                    try {
                        ShipmentRecordSaveReqVO recordVO = new ShipmentRecordSaveReqVO();
                        recordVO.setShipmentOrderId(orderId);
                        recordVO.setShipmentOrderDetailId(detail.getId());
                        recordVO.setItemId(detail.getItemId());
                        recordVO.setLocationId(locationId != null ? locationId : 0L);
                        if (detail.getBatchId() != null) {
                            recordVO.setBatchId(detail.getBatchId());
                        }
                        recordVO.setCount(actualShipmentCount);
                        recordVO.setRemark("审核通过自动出库");
                        shipmentRecordService.createShipmentRecord(recordVO);
                    } catch (Exception e) {
                        // 记录出错但不影响主流程
                        log.error("创建出库记录失败，明细ID={}, 错误：{}", detail.getId(), e.getMessage(), e);
                    }
                } catch (Exception e) {
                    log.error("明细出库失败，ID={}, 错误：{}", detail.getId(), e.getMessage(), e);
                    isAllCompleted = false;
                    // 继续处理其他明细，不中断流程
                }
            }
            
            // 更新出库单状态
            ShipmentOrderDO updateObj = new ShipmentOrderDO().setId(orderId);
            if (isAllCompleted) {
                // 全部完成出库
                updateObj.setShipmentStatus(ShipmentStatusEnum.COMPLETED.getStatus())
                         .setCompleteTime(LocalDateTime.now());
            } else {
                // 部分出库
                updateObj.setShipmentStatus(ShipmentStatusEnum.PARTIAL.getStatus());
            }
            shipmentOrderMapper.updateById(updateObj);
                
            log.info("出库操作完成，出库单号：{}，出库状态：{}", 
                    shipmentOrder.getShipmentOrderNo(),
                    isAllCompleted ? "已完成" : "部分出库");
        } catch (Exception e) {
            log.error("出库单处理失败，ID={}, 错误：{}", orderId, e.getMessage(), e);
            throw e; // 重新抛出异常，触发事务回滚
        }
    }
}