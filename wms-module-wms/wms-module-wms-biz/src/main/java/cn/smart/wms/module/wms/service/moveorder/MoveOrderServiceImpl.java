package cn.smart.wms.module.wms.service.moveorder;

import cn.smart.wms.framework.common.exception.ErrorCode;
import cn.smart.wms.framework.common.pojo.PageParam;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.util.object.BeanUtils;
import cn.smart.wms.module.wms.controller.admin.moveorder.vo.MoveOperationReqVO;
import cn.smart.wms.module.wms.controller.admin.moveorder.vo.MoveOrderPageReqVO;
import cn.smart.wms.module.wms.controller.admin.moveorder.vo.MoveOrderSaveReqVO;
import cn.smart.wms.module.wms.controller.admin.moverecord.vo.MoveRecordSaveReqVO;
import cn.smart.wms.module.wms.dal.dataobject.moveorder.MoveOrderDO;
import cn.smart.wms.module.wms.dal.dataobject.moveorderdetail.MoveOrderDetailDO;
import cn.smart.wms.module.wms.dal.mysql.moveorder.MoveOrderMapper;
import cn.smart.wms.module.wms.dal.mysql.moveorderdetail.MoveOrderDetailMapper;
import cn.smart.wms.module.wms.enums.move.MoveStatusEnum;
import cn.smart.wms.module.wms.service.batchinventory.BatchInventoryService;
import cn.smart.wms.module.wms.service.inventory.InventoryService;
import cn.smart.wms.module.wms.service.moveorderdetail.MoveOrderDetailService;
import cn.smart.wms.module.wms.service.moverecord.MoveRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static cn.smart.wms.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.smart.wms.module.wms.enums.ErrorCodeConstants.*;

/**
 * 移库单 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class MoveOrderServiceImpl implements MoveOrderService {

    @Resource
    private MoveOrderMapper moveOrderMapper;
    
    @Resource
    private MoveOrderDetailMapper moveOrderDetailMapper;
    
    @Resource
    private MoveOrderDetailService moveOrderDetailService;
    
    @Resource
    private MoveRecordService moveRecordService;
    
    @Resource
    private InventoryService inventoryService;
    
    @Resource
    private BatchInventoryService batchInventoryService;

    @Override
    public Long createMoveOrder(MoveOrderSaveReqVO createReqVO) {
        // 插入
        MoveOrderDO moveOrder = BeanUtils.toBean(createReqVO, MoveOrderDO.class);
        // 设置默认值
        if (moveOrder.getOrderStatus() == null) {
            moveOrder.setOrderStatus(0); // 默认为草稿状态
        }
        moveOrder.setMoveStatus(MoveStatusEnum.PENDING.getStatus()); // 待移库状态
        moveOrderMapper.insert(moveOrder);
        // 返回
        return moveOrder.getId();
    }

    @Override
    public void updateMoveOrder(MoveOrderSaveReqVO updateReqVO) {
        // 校验存在
        MoveOrderDO moveOrder = validateMoveOrderExists(updateReqVO.getId());
        // 校验状态，只有草稿和待审核状态可以修改
        if (moveOrder.getOrderStatus() > 1) {
            throw exception(MOVE_ORDER_STATUS_ERROR);
        }
        // 更新
        MoveOrderDO updateObj = BeanUtils.toBean(updateReqVO, MoveOrderDO.class);
        moveOrderMapper.updateById(updateObj);
    }

    @Override
    public void deleteMoveOrder(Long id) {
        // 校验存在
        MoveOrderDO moveOrder = validateMoveOrderExists(id);
        // 校验状态，只有草稿状态可以删除
        if (moveOrder.getOrderStatus() != 0) {
            throw exception(MOVE_ORDER_STATUS_ERROR);
        }
        // 删除
        moveOrderMapper.deleteById(id);
    }

    private MoveOrderDO validateMoveOrderExists(Long id) {
        MoveOrderDO moveOrder = moveOrderMapper.selectById(id);
        if (moveOrder == null) {
            throw exception(MOVE_ORDER_NOT_EXISTS);
        }
        return moveOrder;
    }

    @Override
    public MoveOrderDO getMoveOrder(Long id) {
        return moveOrderMapper.selectById(id);
    }

    @Override
    public PageResult<MoveOrderDO> getMoveOrderPage(MoveOrderPageReqVO pageReqVO) {
        return moveOrderMapper.selectPage(pageReqVO);
    }
    
    @Override
    public List<MoveOrderDO> getMoveOrderList(MoveOrderPageReqVO pageReqVO) {
        // 复用分页方法，但设置无需分页
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        return moveOrderMapper.selectPage(pageReqVO).getList();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approveMoveOrder(Long id, Boolean approved, String remark) {
        // 校验存在
        MoveOrderDO moveOrder = validateMoveOrderExists(id);
        // 校验状态，只有待审核状态可以审核
        if (moveOrder.getOrderStatus() != 1) { // 待审核状态
            throw exception(MOVE_ORDER_STATUS_ERROR);
        }
        
        // 更新状态
        MoveOrderDO updateObj = new MoveOrderDO();
        updateObj.setId(id);
        updateObj.setOrderStatus(approved ? 2 : 3); // 2-审核通过，3-审核拒绝
        updateObj.setRemark(remark);
        
        moveOrderMapper.updateById(updateObj);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long executeMoveByDetail(Long detailId, Integer count, String remark) {
        // 校验移库单明细存在
        MoveOrderDetailDO detail = moveOrderDetailMapper.selectById(detailId);
        if (detail == null) {
            throw exception(MOVE_ORDER_DETAIL_NOT_EXISTS);
        }
        
        // 校验移库单
        MoveOrderDO moveOrder = validateMoveOrderExists(detail.getMoveOrderId());
        // 校验移库单状态，只有审核通过的可以执行移库
        if (moveOrder.getOrderStatus() != 2) { // 审核通过状态
            throw exception(MOVE_ORDER_STATUS_ERROR);
        }
        
        // 校验移库数量
        int remainCount = detail.getPlanCount() - detail.getRealCount();
        if (count > remainCount) {
            throw exception(new ErrorCode(1002007003, "移库数量不能大于剩余计划数量"));
        }
        
        // 校验源库位库存是否足够
        if (detail.getBatchId() != null) {
            // 批次库存校验
            boolean sufficient = batchInventoryService.isBatchInventorySufficient(
                detail.getBatchId(), detail.getItemId(), moveOrder.getFromWarehouseId(), 
                moveOrder.getFromLocationId(), count);
            if (!sufficient) {
                throw exception(INVENTORY_INSUFFICIENT);
            }
        } else {
            // 普通库存校验
            boolean sufficient = inventoryService.isInventorySufficient(
                moveOrder.getFromWarehouseId(), detail.getItemId(), 
                moveOrder.getFromLocationId(), count);
            if (!sufficient) {
                throw exception(INVENTORY_INSUFFICIENT);
            }
        }
        
        // 创建移库记录
        MoveRecordSaveReqVO recordReqVO = new MoveRecordSaveReqVO();
        recordReqVO.setMoveOrderId(moveOrder.getId());
        recordReqVO.setMoveOrderDetailId(detailId);
        recordReqVO.setItemId(detail.getItemId());
        recordReqVO.setBatchId(detail.getBatchId());
        recordReqVO.setFromWarehouseId(moveOrder.getFromWarehouseId());
        recordReqVO.setToWarehouseId(moveOrder.getToWarehouseId());
        recordReqVO.setFromLocationId(moveOrder.getFromLocationId());
        recordReqVO.setToLocationId(moveOrder.getToLocationId());
        recordReqVO.setCount(count);
        recordReqVO.setRemark(remark);
        Long recordId = moveRecordService.createMoveRecord(recordReqVO);
        
        // 更新移库单明细的实际移库数量
        MoveOrderDetailDO updateDetail = new MoveOrderDetailDO();
        updateDetail.setId(detailId);
        updateDetail.setRealCount(detail.getRealCount() + count);
        // 更新明细状态
        if (updateDetail.getRealCount() >= detail.getPlanCount()) {
            updateDetail.setStatus(2); // 已移库状态
        } else if (updateDetail.getRealCount() > 0) {
            updateDetail.setStatus(1); // 部分移库状态
        }
        moveOrderDetailMapper.updateById(updateDetail);
        
        // 更新移库单状态
        updateMoveOrderStatus(moveOrder.getId());
        
        // 执行库存的移动操作
        if (detail.getBatchId() != null) {
            // 批次库存移动
            batchInventoryService.moveBatchInventory(
                detail.getBatchId(), detail.getItemId(), 
                moveOrder.getFromWarehouseId(), moveOrder.getToWarehouseId(),
                moveOrder.getFromLocationId(), moveOrder.getToLocationId(),
                count, "MOVE", moveOrder.getId(), detailId, remark
            );
        } else {
            // 普通库存移动
            // 在源库位减少库存
            inventoryService.decreaseInventory(
                moveOrder.getFromWarehouseId(), detail.getItemId(), 
                moveOrder.getFromLocationId(), count, 
                "MOVE", moveOrder.getId(), detailId, "移库出库: " + remark
            );
            
            // 在目标库位增加库存
            inventoryService.increaseInventory(
                moveOrder.getToWarehouseId(), detail.getItemId(), 
                moveOrder.getToLocationId(), count, 
                "MOVE", moveOrder.getId(), detailId, "移库入库: " + remark
            );
        }
        
        return recordId;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Long> executeMove(Long id, List<MoveOperationReqVO> moveDetails) {
        // 校验移库单
        MoveOrderDO moveOrder = validateMoveOrderExists(id);
        // 校验移库单状态，只有审核通过的可以执行移库
        if (moveOrder.getOrderStatus() != 2) { // 审核通过状态
            throw exception(MOVE_ORDER_STATUS_ERROR);
        }
        
        List<Long> recordIds = new ArrayList<>();
        for (MoveOperationReqVO detail : moveDetails) {
            // 执行每个明细的移库操作
            Long recordId = executeMoveByDetail(
                detail.getDetailId(), detail.getCount(), detail.getRemark()
            );
            recordIds.add(recordId);
        }
        
        return recordIds;
    }
    
    /**
     * 更新移库单状态
     * 
     * @param moveOrderId 移库单ID
     */
    private void updateMoveOrderStatus(Long moveOrderId) {
        // 查询移库单所有明细
        List<MoveOrderDetailDO> details = moveOrderDetailMapper.selectList(
            MoveOrderDetailDO::getMoveOrderId, moveOrderId
        );
        
        boolean allCompleted = true;
        boolean anyMoved = false;
        
        int totalPlanCount = 0;
        int totalRealCount = 0;
        
        for (MoveOrderDetailDO detail : details) {
            totalPlanCount += detail.getPlanCount();
            totalRealCount += detail.getRealCount();
            
            if (detail.getRealCount() > 0) {
                anyMoved = true;
            }
            
            if (detail.getRealCount() < detail.getPlanCount()) {
                allCompleted = false;
            }
        }
        
        // 更新移库单状态
        MoveOrderDO updateOrder = new MoveOrderDO();
        updateOrder.setId(moveOrderId);
        updateOrder.setTotalCount(totalPlanCount);
        
        if (allCompleted) {
            updateOrder.setMoveStatus(MoveStatusEnum.COMPLETED.getStatus()); // 已完成
        } else if (anyMoved) {
            updateOrder.setMoveStatus(MoveStatusEnum.PARTIAL.getStatus()); // 部分移库
        }
        
        moveOrderMapper.updateById(updateOrder);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelMoveOrder(Long id, String remark) {
        // 校验存在
        MoveOrderDO moveOrder = validateMoveOrderExists(id);
        // 校验状态，已完成的移库单不能取消
        if (moveOrder.getMoveStatus() == MoveStatusEnum.COMPLETED.getStatus()) {
            throw exception(MOVE_ORDER_STATUS_ERROR);
        }
        
        // 更新状态
        MoveOrderDO updateObj = new MoveOrderDO();
        updateObj.setId(id);
        updateObj.setOrderStatus(3); // 审核拒绝状态
        updateObj.setRemark(remark);
        
        moveOrderMapper.updateById(updateObj);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completeMoveOrder(Long id) {
        // 校验存在
        MoveOrderDO moveOrder = validateMoveOrderExists(id);
        // 校验状态，只有审核通过且部分移库的可以手动完成
        if (moveOrder.getOrderStatus() != 2 || 
            moveOrder.getMoveStatus() != MoveStatusEnum.PARTIAL.getStatus()) {
            throw exception(MOVE_ORDER_STATUS_ERROR);
        }
        
        // 更新状态
        MoveOrderDO updateObj = new MoveOrderDO();
        updateObj.setId(id);
        updateObj.setMoveStatus(MoveStatusEnum.COMPLETED.getStatus()); // 已完成
        
        moveOrderMapper.updateById(updateObj);
    }
}