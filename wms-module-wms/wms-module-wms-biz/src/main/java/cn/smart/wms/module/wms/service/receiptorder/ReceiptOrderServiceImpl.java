package cn.smart.wms.module.wms.service.receiptorder;

import cn.smart.wms.framework.common.exception.ErrorCode;
import cn.smart.wms.framework.common.pojo.PageParam;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.util.object.BeanUtils;
import cn.smart.wms.framework.lock.core.annotation.DistributedLock;
import cn.smart.wms.module.wms.controller.admin.receiptorder.vo.ReceiptOrderPageReqVO;
import cn.smart.wms.module.wms.controller.admin.receiptorder.vo.ReceiptOrderSaveReqVO;
import cn.smart.wms.module.wms.controller.admin.receiptrecord.vo.ReceiptRecordSaveReqVO;
import cn.smart.wms.module.wms.dal.dataobject.inventory.InventoryDO;
import cn.smart.wms.module.wms.dal.dataobject.receiptorder.ReceiptOrderDO;
import cn.smart.wms.module.wms.dal.dataobject.receiptorderdetail.ReceiptOrderDetailDO;
import cn.smart.wms.module.wms.dal.mysql.receiptorder.ReceiptOrderMapper;
import cn.smart.wms.module.wms.dal.mysql.receiptorderdetail.ReceiptOrderDetailMapper;
import cn.smart.wms.module.wms.enums.receipt.ReceiptDetailStatusEnum;
import cn.smart.wms.module.wms.enums.receipt.ReceiptOrderStatusEnum;
import cn.smart.wms.module.wms.enums.receipt.ReceiptStatusEnum;
import cn.smart.wms.module.wms.service.batchinventory.BatchInventoryService;
import cn.smart.wms.module.wms.service.inventory.InventoryService;
import cn.smart.wms.module.wms.service.receiptorderdetail.ReceiptOrderDetailService;
import cn.smart.wms.module.wms.service.receiptrecord.ReceiptRecordService;
import cn.smart.wms.module.wms.service.audit.OrderAuditService;
import cn.smart.wms.module.wms.enums.OrderTypeEnum;
import cn.smart.wms.module.wms.enums.AuditStatusEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

import static cn.smart.wms.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.smart.wms.module.wms.enums.ErrorCodeConstants.*;

/**
 * 入库单 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class ReceiptOrderServiceImpl implements ReceiptOrderService {

    @Resource
    private ReceiptOrderMapper receiptOrderMapper;
    
    @Resource
    private ReceiptOrderDetailMapper receiptOrderDetailMapper;
    
    @Resource
    private ReceiptOrderDetailService receiptOrderDetailService;
    
    @Resource
    private ReceiptRecordService receiptRecordService;
    
    @Resource
    private InventoryService inventoryService;
    
    @Resource
    private BatchInventoryService batchInventoryService;
    
    @Resource
    private OrderAuditService orderAuditService;

    @Override
    public Long createReceiptOrder(ReceiptOrderSaveReqVO createReqVO) {
        // 插入
        ReceiptOrderDO receiptOrder = BeanUtils.toBean(createReqVO, ReceiptOrderDO.class);
        // 设置默认值
        if (receiptOrder.getOrderStatus() == null) {
            receiptOrder.setOrderStatus(0); // 默认为草稿状态
        }
        receiptOrder.setReceiptStatus(ReceiptStatusEnum.PENDING.getStatus()); // 待入库状态
        receiptOrderMapper.insert(receiptOrder);
        // 返回
        return receiptOrder.getId();
    }

    @Override
    public void updateReceiptOrder(ReceiptOrderSaveReqVO updateReqVO) {
        // 校验存在
        ReceiptOrderDO receiptOrder = validateReceiptOrderExists(updateReqVO.getId());
        // 校验状态，只有草稿和待审核状态可以修改
        if (receiptOrder.getOrderStatus() > 1) {
            throw exception(RECEIPT_ORDER_STATUS_ERROR);
        }
        // 更新
        ReceiptOrderDO updateObj = BeanUtils.toBean(updateReqVO, ReceiptOrderDO.class);
        receiptOrderMapper.updateById(updateObj);
    }

    @Override
    public void deleteReceiptOrder(Long id) {
        // 校验存在
        ReceiptOrderDO receiptOrder = validateReceiptOrderExists(id);
        // 校验状态，只有草稿状态可以删除
        if (receiptOrder.getOrderStatus() != 0) {
            throw exception(RECEIPT_ORDER_STATUS_ERROR);
        }
        // 删除
        receiptOrderMapper.deleteById(id);
    }

    private ReceiptOrderDO validateReceiptOrderExists(Long id) {
        ReceiptOrderDO receiptOrder = receiptOrderMapper.selectById(id);
        if (receiptOrder == null) {
            throw exception(RECEIPT_ORDER_NOT_EXISTS);
        }
        return receiptOrder;
    }

    @Override
    public ReceiptOrderDO getReceiptOrder(Long id) {
        ReceiptOrderDO receiptOrder = receiptOrderMapper.selectById(id);
        if (receiptOrder != null) {
            // 设置供应商名称（示例实现，实际应该关联供应商表查询）
            if (receiptOrder.getSupplierId() != null) {
                // 实际业务中，这里应该调用供应商服务获取供应商名称
                // 示例：receiptOrder.setSupplierName(supplierService.getSupplierName(receiptOrder.getSupplierId()));
            }
            
            // 设置仓库名称（示例实现，实际应该关联仓库表查询）
            if (receiptOrder.getWarehouseId() != null) {
                // 实际业务中，这里应该调用仓库服务获取仓库名称
                // 示例：receiptOrder.setWarehouseName(warehouseService.getWarehouseName(receiptOrder.getWarehouseId()));
            }
        }
        return receiptOrder;
    }

    @Override
    public PageResult<ReceiptOrderDO> getReceiptOrderPage(ReceiptOrderPageReqVO pageReqVO) {
        return receiptOrderMapper.selectPage(pageReqVO);
    }
    
    @Override
    public List<ReceiptOrderDO> getReceiptOrderList(ReceiptOrderPageReqVO pageReqVO) {
        // 复用分页方法，但设置无需分页
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        return receiptOrderMapper.selectPage(pageReqVO).getList();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitReceiptOrder(Long id) {
        // 校验存在
        ReceiptOrderDO receiptOrder = validateReceiptOrderExists(id);
        // 校验状态，只有草稿状态可以提交审核
        if (receiptOrder.getOrderStatus() != ReceiptOrderStatusEnum.DRAFT.getStatus()) {
            throw exception(RECEIPT_ORDER_STATUS_ERROR);
        }
        
        // 检查是否有明细
        List<ReceiptOrderDetailDO> details = receiptOrderDetailMapper.selectByReceiptOrderId(id);
        if (details == null || details.isEmpty()) {
            log.warn("入库单没有明细，无法提交审核: 入库单ID={}", id);
            throw exception(new ErrorCode(1002005006, "入库单没有明细，请先添加明细后再提交审核"));
        }
        
        // 记录明细情况，便于调试
        int totalPlanCount = 0;
        int totalRealCount = 0;
        
        StringBuilder detailLog = new StringBuilder("提交前明细状态: ");
        for (ReceiptOrderDetailDO detail : details) {
            int planCount = detail.getPlanCount() != null ? detail.getPlanCount() : 0;
            int realCount = detail.getRealCount() != null ? detail.getRealCount() : 0;
            
            totalPlanCount += planCount;
            totalRealCount += realCount;
            
            detailLog.append("明细ID=").append(detail.getId())
                    .append(", 计划=").append(planCount)
                    .append(", 实际=").append(realCount)
                    .append("; ");
        }
        
        log.info("开始提交入库单: ID={}, 当前状态={}, 入库状态={}, 总计划数量={}, 总实际数量={}", 
                 id, receiptOrder.getOrderStatus(), receiptOrder.getReceiptStatus(), 
                 totalPlanCount, totalRealCount);
        log.info(detailLog.toString());
        
        // 更新状态为待审核
        ReceiptOrderDO updateObj = new ReceiptOrderDO();
        updateObj.setId(id);
        updateObj.setOrderStatus(ReceiptOrderStatusEnum.PENDING_APPROVAL.getStatus()); // 待审核状态
        
        receiptOrderMapper.updateById(updateObj);
        
        // 根据明细的实际入库数量计算和更新入库状态
        // 不再设置默认的入库状态，而是使用updateReceiptOrderStatus根据实际情况计算
        updateReceiptOrderStatus(id);
        
        // 获取更新后的入库单状态
        ReceiptOrderDO updatedOrder = receiptOrderMapper.selectById(id);
        log.info("入库单提交完成: ID={}, 更新后单据状态={}, 更新后入库状态={}", 
                id, updatedOrder.getOrderStatus(), updatedOrder.getReceiptStatus());
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approveReceiptOrder(Long id, Boolean approved, String remark) {
        // 校验存在
        ReceiptOrderDO receiptOrder = validateReceiptOrderExists(id);
        // 校验状态，只有待审核状态可以审核
        if (receiptOrder.getOrderStatus() != ReceiptOrderStatusEnum.PENDING_APPROVAL.getStatus()) { // 待审核状态
            throw exception(RECEIPT_ORDER_STATUS_ERROR);
        }
        
        // 检查是否有明细（只有审核通过时才检查）
        List<ReceiptOrderDetailDO> details = null;
        if (approved) {
            details = receiptOrderDetailMapper.selectByReceiptOrderId(id);
            if (details == null || details.isEmpty()) {
                log.warn("入库单没有明细，无法审核通过: 入库单ID={}", id);
                throw exception(new ErrorCode(1002005007, "入库单没有明细，无法审核通过"));
            }
            
            // 额外记录明细情况，便于调试
            log.info("审批前入库单明细数据: 入库单ID={}, 明细数量={}", id, details.size());
            for (ReceiptOrderDetailDO detail : details) {
                log.info("明细ID={}, 物料ID={}, 计划数量={}, 实际数量={}, 状态={}", 
                        detail.getId(), detail.getItemId(), detail.getPlanCount(), 
                        detail.getRealCount(), detail.getStatus());
            }
        }
        
        log.info("开始审批入库单: ID={}, 是否通过={}", id, approved);
        
        // 更新状态
        ReceiptOrderDO updateObj = new ReceiptOrderDO();
        updateObj.setId(id);
        updateObj.setOrderStatus(approved ? 
            ReceiptOrderStatusEnum.APPROVED.getStatus() : 
            ReceiptOrderStatusEnum.REJECTED.getStatus());
        updateObj.setRemark(remark);
        
        int rows = receiptOrderMapper.updateById(updateObj);
        log.info("审批单据状态更新结果: id={}, rows={}, status={}", id, rows, updateObj.getOrderStatus());
        
        if (rows <= 0) {
            throw exception(new ErrorCode(1002005099, "入库单状态更新失败"));
        }
        
        // 创建审核记录
        orderAuditService.createOrderAudit(
            id, 
            OrderTypeEnum.RECEIPT.getType(), 
            Boolean.TRUE.equals(approved) ? AuditStatusEnum.APPROVED.getStatus() : AuditStatusEnum.REJECTED.getStatus(), 
            remark
        );
        
        // 如果审批通过，确保入库状态为待入库
        if (approved) {
            // 在这里不再自动设置入库状态，而是通过updateReceiptOrderStatus来更新
            // 这样可以确保入库状态是基于实际情况计算的
            
            // 更新明细状态为待入库（如果之前没有状态）
            if (details != null && !details.isEmpty()) {
                for (ReceiptOrderDetailDO detail : details) {
                    if (detail.getStatus() == null) {
                        ReceiptOrderDetailDO updateDetail = new ReceiptOrderDetailDO();
                        updateDetail.setId(detail.getId());
                        updateDetail.setStatus(ReceiptDetailStatusEnum.PENDING.getStatus());
                        receiptOrderDetailMapper.updateById(updateDetail);
                        log.info("更新明细初始状态: 明细ID={}, 状态=待入库", detail.getId());
                    }
                }
            }
            
            // 检查是否有明细已经有实际入库数量 - 修复判断逻辑
            boolean hasRealCounts = false;
            int totalRealCount = 0;
            StringBuilder realCountLog = new StringBuilder("检查明细实际入库数量: ");
            
            if (details != null) {
                for (ReceiptOrderDetailDO detail : details) {
                    Integer realCount = detail.getRealCount();
                    realCountLog.append("明细ID=").append(detail.getId())
                            .append(", 计划=").append(detail.getPlanCount())
                            .append(", 实际=").append(realCount)
                            .append("; ");
                    
                    if (realCount != null && realCount > 0) {
                        hasRealCounts = true;
                        totalRealCount += realCount;
                        log.info("明细已有实际入库数量: 明细ID={}, 计划数量={}, 已入库数量={}", 
                                detail.getId(), detail.getPlanCount(), realCount);
                    }
                }
            }
            
            log.info(realCountLog.toString());
            log.info("是否存在已有实际入库数量: {}, 总实际入库数量: {}", hasRealCounts, totalRealCount);
            
            // 更新入库单状态 - 基于实际情况
            updateReceiptOrderStatus(id);
            
            // 关键修改：发现已有实际入库数量时，直接跳过批量入库
            if (hasRealCounts) {
                log.info("跳过自动批量入库，因为明细已有实际入库数量: 入库单ID={}, 总实际数量={}", id, totalRealCount);
                return; // 直接返回，不执行批量入库
            }
            
            // 只有当没有任何明细有实际入库数量时，才执行自动批量入库
            try {
                // 审批通过且没有手动设置过实际入库数量，自动执行批量入库操作
                log.info("审批通过且无已有入库数量，开始自动执行批量入库操作: 入库单ID={}", id);
                batchExecuteReceiptByOrderId(id);
                log.info("自动批量入库操作完成: 入库单ID={}", id);
                
                // 再次获取明细数据，验证入库结果
                List<ReceiptOrderDetailDO> updatedDetails = receiptOrderDetailMapper.selectByReceiptOrderId(id);
                log.info("批量入库后明细数据: 入库单ID={}, 明细数量={}", id, updatedDetails.size());
                for (ReceiptOrderDetailDO detail : updatedDetails) {
                    log.info("明细ID={}, 物料ID={}, 计划数量={}, 实际数量={}, 状态={}", 
                            detail.getId(), detail.getItemId(), detail.getPlanCount(), 
                            detail.getRealCount(), detail.getStatus());
                }
            } catch (Exception e) {
                // 批量入库操作失败，但不影响审批结果，记录异常日志
                log.error("自动批量入库操作失败: 入库单ID={}, 错误信息={}", id, e.getMessage(), e);
            }
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    @DistributedLock(prefix = "receipt", key = "#detailId", errorMessage = "入库处理中，请稍后再试")
    public Long executeReceiptByDetail(Long detailId, Long locationId, Long batchId, Integer count, String remark) {
        // 校验入库单明细存在
        ReceiptOrderDetailDO detail = receiptOrderDetailMapper.selectById(detailId);
        if (detail == null) {
            throw exception(RECEIPT_ORDER_DETAIL_NOT_EXISTS);
        }
        
        // 校验入库单
        ReceiptOrderDO receiptOrder = validateReceiptOrderExists(detail.getReceiptOrderId());
        // 校验入库单状态，只有审核通过的可以执行入库
        if (receiptOrder.getOrderStatus() != ReceiptOrderStatusEnum.APPROVED.getStatus()) {
            throw exception(RECEIPT_ORDER_STATUS_ERROR);
        }
        
        // 校验入库数量 - 修复计算逻辑，处理realCount为null的情况
        int realCount = detail.getRealCount() != null ? detail.getRealCount() : 0;
        int remainCount = detail.getPlanCount() - realCount;
        if (count > remainCount) {
            throw exception(new ErrorCode(1002005003, "入库数量不能大于剩余计划数量"));
        }
        
        log.info("开始执行入库操作: 明细ID={}, 库位ID={}, 批次ID={}, 数量={}, 已入库数量={}, 计划数量={}", 
                 detailId, locationId, batchId, count, realCount, detail.getPlanCount());
        
        // 先检查当前库存情况
        Long warehouseId = receiptOrder.getWarehouseId();
        Long itemId = detail.getItemId();
        
        // 尝试查询现有库存记录
        InventoryDO beforeInventory = inventoryService.getInventoryByWarehouseAndItemAndLocation(
            warehouseId, itemId, locationId
        );
        
        int beforeStockCount = 0;
        if (beforeInventory != null) {
            beforeStockCount = beforeInventory.getStockCount() != null ? beforeInventory.getStockCount() : 0;
            log.info("入库前库存: 仓库ID={}, 物料ID={}, 库位ID={}, 当前库存={}", 
                    warehouseId, itemId, locationId, beforeStockCount);
        } else {
            log.info("入库前库存记录不存在: 仓库ID={}, 物料ID={}, 库位ID={}", 
                    warehouseId, itemId, locationId);
        }
        
        // 创建入库记录
        ReceiptRecordSaveReqVO recordReqVO = new ReceiptRecordSaveReqVO();
        recordReqVO.setReceiptOrderId(receiptOrder.getId());
        recordReqVO.setReceiptOrderDetailId(detailId);
        recordReqVO.setItemId(detail.getItemId());
        recordReqVO.setLocationId(locationId);
        recordReqVO.setBatchId(batchId);
        recordReqVO.setCount(count);
        recordReqVO.setRemark(remark);
        Long recordId = receiptRecordService.createReceiptRecord(recordReqVO);
        
        log.info("创建入库记录成功: 记录ID={}", recordId);
        
        // 更新入库单明细的实际入库数量和状态 - 修复计算逻辑，处理realCount为null的情况
        ReceiptOrderDetailDO updateDetail = new ReceiptOrderDetailDO();
        updateDetail.setId(detailId);
        int newRealCount = realCount + count;
        updateDetail.setRealCount(newRealCount);
        updateDetail.setLocationId(locationId);
        updateDetail.setBatchId(batchId);
        
        // 更新明细状态
        if (newRealCount >= detail.getPlanCount()) {
            updateDetail.setStatus(ReceiptDetailStatusEnum.COMPLETED.getStatus()); // 已入库状态
            log.info("明细已完全入库: 明细ID={}, 实际入库数量={}, 计划数量={}", detailId, newRealCount, detail.getPlanCount());
        } else if (newRealCount > 0) {
            updateDetail.setStatus(ReceiptDetailStatusEnum.PARTIAL.getStatus()); // 部分入库状态
            log.info("明细部分入库: 明细ID={}, 实际入库数量={}, 计划数量={}", detailId, newRealCount, detail.getPlanCount());
        }
        
        int updatedRows = receiptOrderDetailMapper.updateById(updateDetail);
        log.info("更新入库单明细结果: 明细ID={}, 更新行数={}", detailId, updatedRows);
        
        if (updatedRows <= 0) {
            log.error("入库单明细更新失败: 明细ID={}", detailId);
            throw exception(new ErrorCode(1002005099, "入库单明细更新失败，请重试"));
        }
        
        // 强制从数据库中重新加载明细，确保数据一致性
        detail = receiptOrderDetailMapper.selectById(detailId);
        log.info("重新加载的明细状态: 明细ID={}, 状态={}, 实际入库数量={}", detailId, detail.getStatus(), detail.getRealCount());
        
        InventoryDO updatedInventory = null;
        try {
            // 更新库存
            if (batchId != null) {
                // 批次库存增加
                log.info("开始更新批次库存: 批次ID={}, 物料ID={}, 仓库ID={}, 库位ID={}, 数量={}", 
                        batchId, detail.getItemId(), receiptOrder.getWarehouseId(), locationId, count);
                batchInventoryService.increaseBatchInventory(
                    batchId, 
                    detail.getItemId(), 
                    receiptOrder.getWarehouseId(), 
                    locationId, 
                    count, 
                    "RECEIPT", // 业务类型
                    receiptOrder.getId(), // 业务单ID
                    detailId, // 业务明细ID
                    remark
                );
            } else {
                // 普通库存增加
                log.info("开始更新普通库存: 仓库ID={}, 物料ID={}, 库位ID={}, 数量={}", 
                        receiptOrder.getWarehouseId(), detail.getItemId(), locationId, count);
                updatedInventory = inventoryService.increaseInventory(
                    receiptOrder.getWarehouseId(),
                    detail.getItemId(), 
                    locationId, 
                    count, 
                    "RECEIPT", // 业务类型
                    receiptOrder.getId(), // 业务单ID
                    detailId, // 业务明细ID
                    remark
                );
                
                // 记录库存更新后的结果
                if (updatedInventory != null) {
                    log.info("库存更新成功: 库存ID={}, 更新前库存={}, 更新后库存={}, 增加数量={}", 
                            updatedInventory.getId(), beforeStockCount, updatedInventory.getStockCount(), count);
                    
                    // 验证库存是否真的增加了指定数量
                    int expectedStockCount = beforeStockCount + count;
                    if (updatedInventory.getStockCount() != expectedStockCount) {
                        log.warn("库存更新后数量与预期不符: 预期库存={}, 实际库存={}, 差额={}", 
                                 expectedStockCount, updatedInventory.getStockCount(), 
                                 expectedStockCount - updatedInventory.getStockCount());
                    }
                } else {
                    log.warn("库存更新后返回为空: 仓库ID={}, 物料ID={}, 库位ID={}, 增加数量={}", 
                             receiptOrder.getWarehouseId(), detail.getItemId(), locationId, count);
                }
            }
            
            // 强制从数据库再次查询库存，确认更新已生效
            InventoryDO finalInventory = inventoryService.getInventoryByWarehouseAndItemAndLocation(
                warehouseId, itemId, locationId
            );
            
            if (finalInventory != null) {
                log.info("最终确认库存: 库存ID={}, 当前库存={}, 预期增加={}, 入库前库存={}", 
                        finalInventory.getId(), finalInventory.getStockCount(), count, beforeStockCount);
                
                // 进行最终的库存验证
                if (beforeStockCount + count != finalInventory.getStockCount()) {
                    log.error("库存更新异常: 入库前={}, 入库数量={}, 预期入库后={}, 实际入库后={}", 
                             beforeStockCount, count, beforeStockCount + count, finalInventory.getStockCount());
                } else {
                    log.info("库存更新成功并验证一致: 入库前={}, 入库数量={}, 入库后={}", 
                            beforeStockCount, count, finalInventory.getStockCount());
                }
            } else {
                log.error("库存记录仍不存在: 仓库ID={}, 物料ID={}, 库位ID={}", 
                         warehouseId, itemId, locationId);
            }
            
            log.info("库存更新成功");
        } catch (Exception e) {
            log.error("库存更新失败: 错误信息={}", e.getMessage(), e);
            throw e; // 重新抛出异常，确保事务回滚
        }
        
        // 更新入库单状态 - 确保在同一事务中执行
        try {
            // 使用更新后的updateReceiptOrderStatus方法，而不是内部方法
            // 这样可以确保状态计算是一致的
            updateReceiptOrderStatus(receiptOrder.getId());
            
            // 再次确认入库单状态已更新
            ReceiptOrderDO updatedOrder = receiptOrderMapper.selectById(receiptOrder.getId());
            log.info("入库单状态更新后的结果: 入库单ID={}, 入库状态={}", 
                    receiptOrder.getId(), updatedOrder.getReceiptStatus());
        } catch (Exception e) {
            log.error("入库单状态更新失败", e);
            throw e; // 重新抛出异常，确保事务回滚
        }
        
        return recordId;
    }
    
    /**
     * 内部方法，确保在同一事务中调用
     */
    private void updateReceiptOrderStatusInternal(Long id) {
        // 查询入库单
        ReceiptOrderDO receiptOrder = validateReceiptOrderExists(id);
        log.info("内部方法开始更新入库单状态: 入库单ID={}, 当前入库状态={}, 当前单据状态={}", 
                id, receiptOrder.getReceiptStatus(), receiptOrder.getOrderStatus());
        
        // 查询入库单明细
        List<ReceiptOrderDetailDO> details = receiptOrderDetailMapper.selectByReceiptOrderId(id);
        
        // 判断入库单的入库状态
        if (details == null || details.isEmpty()) {
            log.warn("入库单没有明细，无法更新状态: 入库单ID={}", id);
            return;
        }
        
        int pendingCount = 0;  // 待入库
        int partialCount = 0;  // 部分入库
        int completedCount = 0; // 已入库
        
        for (ReceiptOrderDetailDO detail : details) {
            if (detail.getStatus() == null || detail.getStatus() == 0) { // 待入库
                pendingCount++;
            } else if (detail.getStatus() == 1) { // 部分入库
                partialCount++;
            } else if (detail.getStatus() == 2) { // 已入库
                completedCount++;
            }
        }
        
        log.info("入库单明细状态统计(内部方法): 待入库={}, 部分入库={}, 已入库={}, 总计={}",
                pendingCount, partialCount, completedCount, details.size());
        
        // 更新入库单状态
        ReceiptOrderDO updateOrder = new ReceiptOrderDO();
        updateOrder.setId(id);
        
        // 设置新状态
        int newStatus;
        if (completedCount == details.size() && completedCount > 0) {
            // 全部入库完成
            newStatus = ReceiptStatusEnum.COMPLETED.getStatus();
            updateOrder.setCompletionTime(LocalDateTime.now()); // 设置完成时间
            log.info("所有明细已完成入库(内部方法)，更新入库单为已完成状态: 入库单ID={}", id);
        } else if (partialCount > 0 || completedCount > 0) {
            // 部分入库
            newStatus = ReceiptStatusEnum.PARTIAL.getStatus();
            log.info("部分明细已入库(内部方法)，更新入库单为部分入库状态: 入库单ID={}", id);
        } else {
            // 所有明细都未入库
            newStatus = ReceiptStatusEnum.PENDING.getStatus();
            log.info("所有明细都未入库(内部方法)，保持入库单为待入库状态: 入库单ID={}", id);
        }
        
        // 设置新状态
        updateOrder.setReceiptStatus(newStatus);
        
        // 执行更新操作
        int updatedRows = receiptOrderMapper.updateById(updateOrder);
        log.info("内部方法更新入库单状态结果: 入库单ID={}, 更新行数={}, 新状态={}", 
                id, updatedRows, newStatus);
    }
    
    @Override
    public Long getIdByNo(String receiptOrderNo) {
        ReceiptOrderDO receiptOrder = receiptOrderMapper.selectByReceiptOrderNo(receiptOrderNo);
        return receiptOrder != null ? receiptOrder.getId() : null;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateReceiptOrderStatus(Long id) {
        // 查询入库单
        ReceiptOrderDO receiptOrder = validateReceiptOrderExists(id);
        log.info("开始更新入库单状态: 入库单ID={}, 当前入库状态={}, 当前单据状态={}", 
                id, receiptOrder.getReceiptStatus(), receiptOrder.getOrderStatus());
        
        // 查询入库单明细
        List<ReceiptOrderDetailDO> details = receiptOrderDetailMapper.selectByReceiptOrderId(id);
        
        // 判断入库单的入库状态
        if (details == null || details.isEmpty()) {
            log.warn("入库单没有明细，无法更新状态: 入库单ID={}", id);
            return;
        }
        
        int pendingCount = 0;  // 待入库（无实际入库数量）
        int partialCount = 0;  // 部分入库（有实际入库数量，但小于计划数量）
        int completedCount = 0; // 已入库（实际入库数量等于或大于计划数量）
        int totalPlanCount = 0; // 总计划数量
        int totalRealCount = 0; // 总实际入库数量
        
        StringBuilder detailStatusLog = new StringBuilder("明细状态清单: ");
        for (ReceiptOrderDetailDO detail : details) {
            // 确保数量不为null
            int planCount = detail.getPlanCount() != null ? detail.getPlanCount() : 0;
            int realCount = detail.getRealCount() != null ? detail.getRealCount() : 0;
            
            totalPlanCount += planCount;
            totalRealCount += realCount;
            
            detailStatusLog.append("明细ID=").append(detail.getId())
                    .append(", 状态=").append(detail.getStatus())
                    .append(", 计划数量=").append(planCount)
                    .append(", 实际数量=").append(realCount)
                    .append("; ");
            
            // 先更新明细状态
            boolean needUpdateDetailStatus = false;
            ReceiptOrderDetailDO updateDetail = new ReceiptOrderDetailDO();
            updateDetail.setId(detail.getId());
            
            // 根据实际入库数量与计划数量的比较来确定状态
            if (planCount > 0 && realCount >= planCount) {
                // 已完全入库
                if (detail.getStatus() == null || !detail.getStatus().equals(ReceiptDetailStatusEnum.COMPLETED.getStatus())) {
                    updateDetail.setStatus(ReceiptDetailStatusEnum.COMPLETED.getStatus());
                    needUpdateDetailStatus = true;
                    log.info("更新明细为已完成: 明细ID={}, 计划数量={}, 实际数量={}", 
                            detail.getId(), planCount, realCount);
                }
                completedCount++;
            } else if (realCount > 0) {
                // 部分入库
                if (detail.getStatus() == null || !detail.getStatus().equals(ReceiptDetailStatusEnum.PARTIAL.getStatus())) {
                    updateDetail.setStatus(ReceiptDetailStatusEnum.PARTIAL.getStatus());
                    needUpdateDetailStatus = true;
                    log.info("更新明细为部分入库: 明细ID={}, 计划数量={}, 实际数量={}", 
                            detail.getId(), planCount, realCount);
                }
                partialCount++;
            } else {
                // 未入库
                if (detail.getStatus() == null || !detail.getStatus().equals(ReceiptDetailStatusEnum.PENDING.getStatus())) {
                    updateDetail.setStatus(ReceiptDetailStatusEnum.PENDING.getStatus());
                    needUpdateDetailStatus = true;
                    log.info("更新明细为待入库: 明细ID={}, 计划数量={}, 实际数量={}", 
                            detail.getId(), planCount, realCount);
                }
                pendingCount++;
            }
            
            // 更新明细状态
            if (needUpdateDetailStatus) {
                receiptOrderDetailMapper.updateById(updateDetail);
                log.info("更新明细状态: 明细ID={}, 新状态={}, 计划数量={}, 实际数量={}", 
                        detail.getId(), updateDetail.getStatus(), planCount, realCount);
            }
        }
        
        log.info(detailStatusLog.toString());
        log.info("入库单明细状态统计: 待入库={}, 部分入库={}, 已入库={}, 总计={}, 总计划数量={}, 总实际数量={}",
                pendingCount, partialCount, completedCount, details.size(), totalPlanCount, totalRealCount);
        
        // 更新入库单状态
        ReceiptOrderDO updateOrder = new ReceiptOrderDO();
        updateOrder.setId(id);
        
        // 确定入库单状态的逻辑：
        // 1. 如果所有明细都已完成入库(已入库数>=计划数)，则整单完成
        // 2. 如果有任何实际入库数量>0，则部分入库
        // 3. 否则为待入库
        if (completedCount == details.size() && completedCount > 0) {
            // 全部明细都已完成入库
            log.info("所有明细都已完成入库，更新入库单为已完成状态: 入库单ID={}", id);
            updateOrder.setReceiptStatus(ReceiptStatusEnum.COMPLETED.getStatus()); // 已入库
            updateOrder.setCompletionTime(LocalDateTime.now()); // 设置完成时间
        } 
        else if (totalRealCount > 0) {
            // 有任何实际入库数量，则为部分入库
            log.info("有明细已部分入库或完成入库，更新入库单为部分入库状态: 入库单ID={}, 总实际数量={}, 总计划数量={}", 
                    id, totalRealCount, totalPlanCount);
            updateOrder.setReceiptStatus(ReceiptStatusEnum.PARTIAL.getStatus()); // 部分入库
        }
        else {
            // 所有明细都未入库
            log.info("所有明细都未入库，保持入库单为待入库状态: 入库单ID={}", id);
            updateOrder.setReceiptStatus(ReceiptStatusEnum.PENDING.getStatus()); // 待入库
        }
        
        // 执行更新操作
        int updatedRows = receiptOrderMapper.updateById(updateOrder);
        log.info("更新入库单状态结果: 入库单ID={}, 更新行数={}, 新状态={}", 
                id, updatedRows, updateOrder.getReceiptStatus());
        
        // 验证更新是否成功
        if (updatedRows > 0) {
            // 强制从数据库重新加载入库单，确保状态已更新
            ReceiptOrderDO updatedOrder = receiptOrderMapper.selectById(id);
            log.info("更新后的入库单状态: 入库单ID={}, 入库状态={}", 
                    id, updatedOrder.getReceiptStatus());
            
            // 确保更新成功
            if (!updateOrder.getReceiptStatus().equals(updatedOrder.getReceiptStatus())) {
                log.warn("入库单状态更新不一致: 预期状态={}, 实际状态={}", 
                        updateOrder.getReceiptStatus(), updatedOrder.getReceiptStatus());
                
                // 再次尝试更新
                ReceiptOrderDO retryUpdate = new ReceiptOrderDO();
                retryUpdate.setId(id);
                retryUpdate.setReceiptStatus(updateOrder.getReceiptStatus());
                if (updateOrder.getCompletionTime() != null) {
                    retryUpdate.setCompletionTime(updateOrder.getCompletionTime());
                }
                
                updatedRows = receiptOrderMapper.updateById(retryUpdate);
                log.info("再次尝试更新入库单状态: 入库单ID={}, 更新行数={}", id, updatedRows);
            }
        } else {
            log.warn("入库单状态更新失败: 入库单ID={}", id);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void executeReceiptOrder(Long id) {
        // 校验入库单存在
        ReceiptOrderDO receiptOrder = validateReceiptOrderExists(id);
        
        // 校验入库单状态，只有审核通过的可以执行入库
        if (receiptOrder.getOrderStatus() != ReceiptOrderStatusEnum.APPROVED.getStatus()) {
            throw exception(RECEIPT_ORDER_STATUS_ERROR);
        }
        
        // 查询入库单所有明细是否已全部入库
        List<ReceiptOrderDetailDO> details = receiptOrderDetailMapper.selectByReceiptOrderId(id);
        if (details == null || details.isEmpty()) {
            throw exception(new ErrorCode(1002005004, "入库单没有明细，无法完成入库"));
        }
        
        boolean allCompleted = true;
        for (ReceiptOrderDetailDO detail : details) {
            if (detail.getStatus() != ReceiptDetailStatusEnum.COMPLETED.getStatus()) {
                allCompleted = false;
                break;
            }
        }
        
        if (!allCompleted) {
            throw exception(new ErrorCode(1002005005, "入库单明细尚未全部入库，无法完成入库"));
        }
        
        // 更新入库单状态
        ReceiptOrderDO updateOrder = new ReceiptOrderDO();
        updateOrder.setId(id);
        updateOrder.setReceiptStatus(ReceiptStatusEnum.COMPLETED.getStatus());
        updateOrder.setCompletionTime(LocalDateTime.now());
        
        receiptOrderMapper.updateById(updateOrder);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelReceiptOrder(Long id) {
        // 校验入库单存在
        ReceiptOrderDO receiptOrder = validateReceiptOrderExists(id);
        
        // 校验状态，只有草稿和待审核状态可以取消
        if (receiptOrder.getOrderStatus() > ReceiptOrderStatusEnum.PENDING_APPROVAL.getStatus()) {
            throw exception(RECEIPT_ORDER_STATUS_ERROR);
        }
        
        // 更新状态为已取消
        ReceiptOrderDO updateObj = new ReceiptOrderDO();
        updateObj.setId(id);
        updateObj.setOrderStatus(ReceiptOrderStatusEnum.CANCELLED.getStatus());
        
        receiptOrderMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createReceiptOrderWithDetails(ReceiptOrderSaveReqVO createReqVO) {
        // 1. 创建入库单
        Long receiptOrderId = createReceiptOrder(createReqVO);
        
        // 2. 获取并处理明细列表
        List<ReceiptOrderDetailDO> details = createReqVO.getDetails();
        if (details != null && !details.isEmpty()) {
            // 设置入库单ID
            details.forEach(detail -> detail.setReceiptOrderId(receiptOrderId));
            
            // 3. 使用明细服务保存所有明细
            receiptOrderDetailService.saveReceiptOrderDetails(receiptOrderId, details);
        }
        
        return receiptOrderId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateReceiptOrderWithDetails(ReceiptOrderSaveReqVO updateReqVO) {
        Long receiptOrderId = updateReqVO.getId();
        if (receiptOrderId == null) {
            throw exception(RECEIPT_ORDER_NOT_EXISTS);
        }
        
        // 1. 更新入库单主信息
        updateReceiptOrder(updateReqVO);
        
        // 2. 获取明细列表
        List<ReceiptOrderDetailDO> details = updateReqVO.getDetails();
        if (details == null || details.isEmpty()) {
            return;
        }
        
        // 3. 获取数据库中现有的明细记录
        List<ReceiptOrderDetailDO> existingDetails = receiptOrderDetailMapper.selectList(
            new LambdaQueryWrapper<ReceiptOrderDetailDO>()
                .eq(ReceiptOrderDetailDO::getReceiptOrderId, receiptOrderId)
        );
        
        // 创建现有明细的ID映射，用于快速查找
        Map<Long, ReceiptOrderDetailDO> existingDetailMap = existingDetails.stream()
            .collect(java.util.stream.Collectors.toMap(
                ReceiptOrderDetailDO::getId, 
                java.util.function.Function.identity(), 
                (k1, k2) -> k1
            ));
        
        // 分离需要新增、更新和删除的明细
        List<ReceiptOrderDetailDO> detailsToAdd = new ArrayList<>();
        List<ReceiptOrderDetailDO> detailsToUpdate = new ArrayList<>();
        Set<Long> updatedDetailIds = new HashSet<>();
        
        // 4. 处理新增和更新
        for (ReceiptOrderDetailDO detail : details) {
            detail.setReceiptOrderId(receiptOrderId); // 确保关联到正确的入库单
            
            if (detail.getId() != null && existingDetailMap.containsKey(detail.getId())) {
                // 更新已存在的明细
                detailsToUpdate.add(detail);
                updatedDetailIds.add(detail.getId());
            } else {
                // 新明细，确保无ID，让数据库自动生成
                detail.setId(null);
                detailsToAdd.add(detail);
            }
        }
        
        // 5. 找出需要删除的明细（在数据库中存在但不在提交的列表中）
        List<Long> detailsToDelete = existingDetailMap.keySet().stream()
            .filter(id -> !updatedDetailIds.contains(id))
            .collect(java.util.stream.Collectors.toList());
        
        // 6. 执行批量操作
        // 6.1 新增明细
        if (!detailsToAdd.isEmpty()) {
            for (ReceiptOrderDetailDO detail : detailsToAdd) {
                receiptOrderDetailMapper.insert(detail);
                log.info("新增入库单明细: 入库单ID={}, 明细={}", receiptOrderId, detail);
            }
        }
        
        // 6.2 更新明细
        if (!detailsToUpdate.isEmpty()) {
            for (ReceiptOrderDetailDO detail : detailsToUpdate) {
                receiptOrderDetailMapper.updateById(detail);
                log.info("更新入库单明细: 入库单ID={}, 明细ID={}", receiptOrderId, detail.getId());
            }
        }
        
        // 6.3 删除不再需要的明细
        if (!detailsToDelete.isEmpty()) {
            receiptOrderDetailMapper.deleteBatchIds(detailsToDelete);
            log.info("删除入库单明细: 入库单ID={}, 删除的明细IDs={}", receiptOrderId, detailsToDelete);
        }
        
        // 7. 更新入库单总量和总金额
        receiptOrderDetailService.updateReceiptOrderTotal(receiptOrderId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long submitAndReceiptOrder(ReceiptOrderSaveReqVO reqVO) {
        // 1. 先创建或更新入库单和明细
        Long receiptOrderId;
        if (reqVO.getId() != null) {
            updateReceiptOrderWithDetails(reqVO);
            receiptOrderId = reqVO.getId();
        } else {
            receiptOrderId = createReceiptOrderWithDetails(reqVO);
        }
        
        // 2. 将入库单状态设置为已提交（跳过审核环节）
        ReceiptOrderDO updateObj = new ReceiptOrderDO();
        updateObj.setId(receiptOrderId);
        updateObj.setOrderStatus(ReceiptOrderStatusEnum.APPROVED.getStatus());
        receiptOrderMapper.updateById(updateObj);
        
        // 3. 执行入库操作
        executeReceiptOrder(receiptOrderId);
        
        return receiptOrderId;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    @DistributedLock(prefix = "receipt", key = "#orderId", errorMessage = "入库单处理中，请稍后再试")
    public void batchExecuteReceiptByOrderId(Long orderId) {
        // 校验入库单
        ReceiptOrderDO receiptOrder = validateReceiptOrderExists(orderId);
        
        // 校验入库单状态，只有审核通过的可以执行入库
        if (receiptOrder.getOrderStatus() != ReceiptOrderStatusEnum.APPROVED.getStatus()) {
            throw exception(RECEIPT_ORDER_STATUS_ERROR);
        }
        
        log.info("开始批量执行入库操作: 入库单ID={}", orderId);
        
        // 获取所有明细
        List<ReceiptOrderDetailDO> details = receiptOrderDetailMapper.selectByReceiptOrderId(orderId);
        if (details == null || details.isEmpty()) {
            log.warn("入库单没有明细，无法执行批量入库: 入库单ID={}", orderId);
            throw exception(new ErrorCode(1002005004, "入库单没有明细，无法执行入库"));
        }
        
        log.info("找到入库单明细数量: {}", details.size());
        
        // 获取已经有入库记录的明细ID列表
        List<Long> detailIdsWithRecords = getDetailIdsWithReceiptRecords(orderId);
        log.info("已有入库记录的明细ID: {}", detailIdsWithRecords);
        
        // 统计明细状态
        int pendingCount = 0;  // 待入库
        int partialCount = 0;  // 部分入库
        int completedCount = 0; // 已入库
        int errorCount = 0;     // 入库失败
        int totalProcessedCount = 0; // 总处理数量
        int totalPlanCount = 0;      // 总计划数量
        boolean hasActualUpdated = false; // 是否有实际入库数量更新
        
        // 批量入库的核心逻辑：对于每个明细，处理尚未入库的部分
        for (ReceiptOrderDetailDO detail : details) {
            // 记录明细当前状态，便于调试
            log.info("处理前明细状态: 明细ID={}, 计划数量={}, 实际数量={}, 明细状态={}", 
                     detail.getId(), detail.getPlanCount(), detail.getRealCount(), detail.getStatus());
                    
            // 确保数量不为null
            int planCount = detail.getPlanCount() != null ? detail.getPlanCount() : 0;
            int realCount = detail.getRealCount() != null ? detail.getRealCount() : 0;
            totalPlanCount += planCount;
            
            // 跳过计划数量为0或null的明细
            if (planCount <= 0) {
                log.info("明细计划数量为0，跳过: 明细ID={}", detail.getId());
                continue;
            }
            
            // 检查明细是否设置了必要信息
            if (detail.getLocationId() == null) {
                log.warn("明细未设置库位，跳过: 明细ID={}, 计划数量={}", 
                         detail.getId(), planCount);
                errorCount++;
                continue;
            }
            
            // 计算需要入库的数量 - 已有数量加上尚未入库的部分
            int remainingCount = planCount - realCount;
            boolean needToUpdateInventory = remainingCount > 0 || 
                                            (realCount > 0 && !detailIdsWithRecords.contains(detail.getId()));
            
            // 如果这个明细不需要更新库存，则跳过
            if (!needToUpdateInventory) {
                log.info("明细无需更新库存，跳过: 明细ID={}, 计划数量={}, 实际数量={}, 剩余数量={}, 已有入库记录={}",
                        detail.getId(), planCount, realCount, remainingCount, detailIdsWithRecords.contains(detail.getId()));
                continue;
            }
            
            try {
                // 确定本次入库数量：如果还没有实际入库记录，则使用实际数量(realCount)；否则使用剩余数量(remainingCount)
                int inboundCount = detailIdsWithRecords.contains(detail.getId()) ? remainingCount : 
                                   (realCount > 0 ? realCount : planCount);
                
                // 如果已经有实际入库数量，但没有对应的入库记录，我们需要更新库存
                if (realCount > 0 && !detailIdsWithRecords.contains(detail.getId())) {
                    log.info("明细有实际入库数量但没有入库记录，创建入库记录并更新库存: 明细ID={}, 计划数量={}, 实际数量={}", 
                            detail.getId(), planCount, realCount);
                    
                    // 创建入库记录
                    ReceiptRecordSaveReqVO recordReqVO = new ReceiptRecordSaveReqVO();
                    recordReqVO.setReceiptOrderId(receiptOrder.getId());
                    recordReqVO.setReceiptOrderDetailId(detail.getId());
                    recordReqVO.setItemId(detail.getItemId());
                    recordReqVO.setLocationId(detail.getLocationId());
                    recordReqVO.setBatchId(detail.getBatchId());
                    recordReqVO.setCount(realCount);
                    recordReqVO.setRemark("系统批量入库-已有实际入库数量");
                    Long recordId = receiptRecordService.createReceiptRecord(recordReqVO);
                    
                    log.info("创建入库记录成功: 记录ID={}, 入库数量={}", recordId, realCount);
                    
                    // 更新库存
                    updateInventory(receiptOrder, detail, realCount, "系统批量入库-已有实际入库数量");
                    
                    totalProcessedCount += realCount;
                    hasActualUpdated = true;
                    
                    // 已更新库存，进入下一个明细
                    continue;
                }
                
                // 只有还有剩余需要入库的数量时，才更新明细的实际入库数量和状态
                if (remainingCount > 0) {
                    // 更新明细的实际入库数量
                    ReceiptOrderDetailDO updateDetail = new ReceiptOrderDetailDO();
                    updateDetail.setId(detail.getId());
                    int newRealCount = realCount + remainingCount;
                    updateDetail.setRealCount(newRealCount);
                    
                    // 更新明细状态
                    if (newRealCount >= planCount) {
                        updateDetail.setStatus(ReceiptDetailStatusEnum.COMPLETED.getStatus());
                    } else if (newRealCount > 0) {
                        updateDetail.setStatus(ReceiptDetailStatusEnum.PARTIAL.getStatus());
                    }
                    
                    int updatedRows = receiptOrderDetailMapper.updateById(updateDetail);
                    if (updatedRows <= 0) {
                        throw new RuntimeException("更新明细实际入库数量失败");
                    }
                    
                    log.info("更新明细入库数量: 明细ID={}, 原实际数量={}, 增加数量={}, 新实际数量={}", 
                             detail.getId(), realCount, remainingCount, newRealCount);
                    
                    // 创建入库记录
                    ReceiptRecordSaveReqVO recordReqVO = new ReceiptRecordSaveReqVO();
                    recordReqVO.setReceiptOrderId(receiptOrder.getId());
                    recordReqVO.setReceiptOrderDetailId(detail.getId());
                    recordReqVO.setItemId(detail.getItemId());
                    recordReqVO.setLocationId(detail.getLocationId());
                    recordReqVO.setBatchId(detail.getBatchId());
                    recordReqVO.setCount(remainingCount);
                    recordReqVO.setRemark("系统批量入库-剩余数量");
                    Long recordId = receiptRecordService.createReceiptRecord(recordReqVO);
                    
                    log.info("创建入库记录成功: 记录ID={}, 入库数量={}", recordId, remainingCount);
                    
                    // 更新库存
                    updateInventory(receiptOrder, detail, remainingCount, "系统批量入库-剩余数量");
                    
                    totalProcessedCount += remainingCount;
                    hasActualUpdated = true;
                }
                
                completedCount++;
                
            } catch (Exception e) {
                log.error("明细入库失败: 明细ID={}, 计划数量={}, 错误信息={}", 
                          detail.getId(), detail.getPlanCount(), e.getMessage(), e);
                errorCount++;
                // 不抛出异常，继续处理其他明细
            }
        }
        
        // 总结入库操作结果
        if (hasActualUpdated) {
            log.info("批量入库操作完成: 入库单ID={}, 处理入库数量={}, 失败明细数={}", 
                     orderId, totalProcessedCount, errorCount);
            
            // 更新入库单整体状态
            updateReceiptOrderStatus(orderId);
        } else {
            log.info("批量入库操作未执行任何入库: 入库单ID={}, 所有明细都已入库或无需入库", orderId);
        }
        
        // 如果有入库失败的明细，记录警告
        if (errorCount > 0) {
            log.warn("部分明细入库失败: 入库单ID={}, 失败明细数={}", orderId, errorCount);
        }
    }
    
    /**
     * 获取已经有入库记录的明细ID列表
     */
    private List<Long> getDetailIdsWithReceiptRecords(Long receiptOrderId) {
        // 实际项目中应该通过ReceiptRecordService查询
        // 这里简化实现，直接从数据库查询
        try {
            return receiptRecordService.getDetailIdsWithRecords(receiptOrderId);
        } catch (Exception e) {
            log.error("获取已有入库记录的明细ID失败: 入库单ID={}, 错误={}", receiptOrderId, e.getMessage(), e);
            return new ArrayList<>();
        }
    }
    
    /**
     * 更新库存的辅助方法
     */
    private void updateInventory(ReceiptOrderDO receiptOrder, ReceiptOrderDetailDO detail, int count, String remark) {
        if (detail.getBatchId() != null) {
            // 批次库存增加
            log.info("开始更新批次库存: 批次ID={}, 物料ID={}, 仓库ID={}, 库位ID={}, 数量={}", 
                    detail.getBatchId(), detail.getItemId(), receiptOrder.getWarehouseId(), 
                    detail.getLocationId(), count);
            batchInventoryService.increaseBatchInventory(
                detail.getBatchId(), 
                detail.getItemId(), 
                receiptOrder.getWarehouseId(), 
                detail.getLocationId(), 
                count,
                "RECEIPT", 
                receiptOrder.getId(), 
                detail.getId(),
                remark
            );
        } else {
            // 普通库存增加
            log.info("开始更新普通库存: 仓库ID={}, 物料ID={}, 库位ID={}, 数量={}", 
                    receiptOrder.getWarehouseId(), detail.getItemId(), 
                    detail.getLocationId(), count);
            inventoryService.increaseInventory(
                receiptOrder.getWarehouseId(),
                detail.getItemId(), 
                detail.getLocationId(), 
                count,
                "RECEIPT", 
                receiptOrder.getId(), 
                detail.getId(),
                remark
            );
        }
    }
}