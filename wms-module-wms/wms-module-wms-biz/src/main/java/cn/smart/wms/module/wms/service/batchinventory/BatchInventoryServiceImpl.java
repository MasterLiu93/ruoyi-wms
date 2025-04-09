package cn.smart.wms.module.wms.service.batchinventory;

import cn.smart.wms.framework.common.exception.ErrorCode;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.util.object.BeanUtils;
import cn.smart.wms.module.wms.controller.admin.batch.vo.BatchSaveReqVO;
import cn.smart.wms.module.wms.controller.admin.batchinventory.vo.BatchInventoryPageReqVO;
import cn.smart.wms.module.wms.controller.admin.batchinventory.vo.BatchInventorySaveReqVO;
import cn.smart.wms.module.wms.controller.admin.batchrecord.vo.BatchRecordSaveReqVO;
import cn.smart.wms.module.wms.dal.dataobject.batch.BatchDO;
import cn.smart.wms.module.wms.dal.dataobject.batchinventory.BatchInventoryDO;
import cn.smart.wms.module.wms.dal.mysql.batchinventory.BatchInventoryMapper;
import cn.smart.wms.module.wms.enums.batch.BatchMovementTypeEnum;
import cn.smart.wms.module.wms.enums.batch.BatchStatusEnum;
import cn.smart.wms.module.wms.service.batch.BatchService;
import cn.smart.wms.module.wms.service.batchrecord.BatchRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;

import static cn.smart.wms.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.smart.wms.module.wms.enums.ErrorCodeConstants.*;

/**
 * 批次库存 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class BatchInventoryServiceImpl implements BatchInventoryService {

    @Resource
    private BatchInventoryMapper batchInventoryMapper;
    
    @Resource
    private BatchRecordService batchRecordService;
    
    @Resource
    private BatchService batchService;

    @Override
    public Long createBatchInventory(BatchInventorySaveReqVO createReqVO) {
        // 插入
        BatchInventoryDO batchInventory = BeanUtils.toBean(createReqVO, BatchInventoryDO.class);
        batchInventoryMapper.insert(batchInventory);
        // 返回
        return batchInventory.getId();
    }

    @Override
    public void updateBatchInventory(BatchInventorySaveReqVO updateReqVO) {
        // 校验存在
        validateBatchInventoryExists(updateReqVO.getId());
        // 更新
        BatchInventoryDO updateObj = BeanUtils.toBean(updateReqVO, BatchInventoryDO.class);
        batchInventoryMapper.updateById(updateObj);
    }

    @Override
    public void deleteBatchInventory(Long id) {
        // 校验存在
        validateBatchInventoryExists(id);
        // 删除
        batchInventoryMapper.deleteById(id);
    }

    private void validateBatchInventoryExists(Long id) {
        if (batchInventoryMapper.selectById(id) == null) {
            throw exception(BATCH_INVENTORY_NOT_EXISTS);
        }
    }

    @Override
    public BatchInventoryDO getBatchInventory(Long id) {
        return batchInventoryMapper.selectById(id);
    }

    @Override
    public PageResult<BatchInventoryDO> getBatchInventoryPage(BatchInventoryPageReqVO pageReqVO) {
        return batchInventoryMapper.selectPage(pageReqVO);
    }
    
    @Override
    public BatchInventoryDO getBatchInventoryByInfo(Long batchId, Long itemId, Long warehouseId, Long locationId) {
        return batchInventoryMapper.selectByInfo(batchId, itemId, warehouseId, locationId);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void lockBatchInventory(Long batchId, Long itemId, Long warehouseId, Long locationId, Integer count, 
                           String businessType, Long businessId, Long businessDetailId, String remark) {
        // 校验参数                   
        if (count <= 0) {
            throw exception(INVENTORY_INSUFFICIENT);
        }
        
        // 校验批次是否存在
        BatchDO batch = batchService.getBatch(batchId);
        if (batch == null) {
            throw exception(BATCH_NOT_EXISTS);
        }
        
        // 校验批次状态
        if (!BatchStatusEnum.NORMAL.getStatus().equals(batch.getStatus())) {
            throw exception(BATCH_STATUS_ERROR);
        }
        
        // 获取批次库存
        BatchInventoryDO batchInventory = getBatchInventoryByInfo(batchId, itemId, warehouseId, locationId);
        if (batchInventory == null) {
            throw exception(BATCH_INVENTORY_NOT_EXISTS);
        }
        
        // 校验可用库存是否足够
        if (batchInventory.getAvailableCount() < count) {
            throw exception(INVENTORY_INSUFFICIENT);
        }
        
        // 锁定库存
        int beforeAvailableCount = batchInventory.getAvailableCount();
        int beforeLockedCount = batchInventory.getLockedCount();
        
        batchInventory.setAvailableCount(batchInventory.getAvailableCount() - count);
        batchInventory.setLockedCount(batchInventory.getLockedCount() + count);
        
        // 更新批次库存
        batchInventoryMapper.updateById(batchInventory);
        
        // 创建批次操作记录
        createBatchRecord(
            batchId, BatchMovementTypeEnum.ADJUSTMENT.getType(),
            count, beforeAvailableCount, batchInventory.getAvailableCount(),
            businessId, "锁定批次库存: " + remark
        );
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unlockBatchInventory(Long batchId, Long itemId, Long warehouseId, Long locationId, Integer count, 
                             String businessType, Long businessId, Long businessDetailId, String remark) {
        // 校验参数                     
        if (count <= 0) {
            throw exception(INVENTORY_INSUFFICIENT);
        }
        
        // 校验批次是否存在
        BatchDO batch = batchService.getBatch(batchId);
        if (batch == null) {
            throw exception(BATCH_NOT_EXISTS);
        }
        
        // 校验批次状态
        if (!BatchStatusEnum.NORMAL.getStatus().equals(batch.getStatus())) {
            throw exception(BATCH_STATUS_ERROR);
        }
        
        // 获取批次库存
        BatchInventoryDO batchInventory = getBatchInventoryByInfo(batchId, itemId, warehouseId, locationId);
        if (batchInventory == null) {
            throw exception(BATCH_INVENTORY_NOT_EXISTS);
        }
        
        // 校验锁定库存是否足够
        if (batchInventory.getLockedCount() < count) {
            throw exception(INVENTORY_INSUFFICIENT);
        }
        
        // 解锁库存
        int beforeAvailableCount = batchInventory.getAvailableCount();
        int beforeLockedCount = batchInventory.getLockedCount();
        
        batchInventory.setAvailableCount(batchInventory.getAvailableCount() + count);
        batchInventory.setLockedCount(batchInventory.getLockedCount() - count);
        
        // 更新批次库存
        batchInventoryMapper.updateById(batchInventory);
        
        // 创建批次操作记录
        createBatchRecord(
            batchId, BatchMovementTypeEnum.ADJUSTMENT.getType(),
            count, beforeAvailableCount, batchInventory.getAvailableCount(),
            businessId, "解锁批次库存: " + remark
        );
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BatchInventoryDO increaseBatchInventory(Long batchId, Long itemId, Long warehouseId, Long locationId, Integer count, 
                           String businessType, Long businessId, Long businessDetailId, String remark) {
        // 校验参数
        if (count <= 0) {
            log.error("增加批次库存失败：数量必须大于0 - batchId={}, itemId={}, warehouseId={}, locationId={}, count={}", 
                     batchId, itemId, warehouseId, locationId, count);
            throw exception(INVENTORY_INSUFFICIENT);
        }
        
        log.info("开始增加批次库存: batchId={}, itemId={}, warehouseId={}, locationId={}, count={}, businessType={}, businessId={}, businessDetailId={}", 
                batchId, itemId, warehouseId, locationId, count, businessType, businessId, businessDetailId);
        
        // 校验批次是否存在
        BatchDO batch = batchService.getBatch(batchId);
        if (batch == null) {
            log.error("增加批次库存失败：批次不存在 - batchId={}", batchId);
            throw exception(BATCH_NOT_EXISTS);
        }
        
        // 校验批次状态
        if (!BatchStatusEnum.NORMAL.getStatus().equals(batch.getStatus())) {
            log.error("增加批次库存失败：批次状态错误 - batchId={}, status={}", batchId, batch.getStatus());
            throw exception(BATCH_STATUS_ERROR);
        }
        
        // 获取批次库存，如果不存在则创建
        BatchInventoryDO batchInventory = getBatchInventoryByInfo(batchId, itemId, warehouseId, locationId);
        int beforeCount = 0;
        
        if (batchInventory == null) {
            // 创建新批次库存记录
            log.info("批次库存记录不存在，创建新批次库存记录: batchId={}, itemId={}, warehouseId={}, locationId={}, count={}", 
                    batchId, itemId, warehouseId, locationId, count);
            
            batchInventory = new BatchInventoryDO();
            batchInventory.setBatchId(batchId);
            batchInventory.setItemId(itemId);
            batchInventory.setWarehouseId(warehouseId);
            batchInventory.setLocationId(locationId);
            batchInventory.setStockCount(count);
            batchInventory.setAvailableCount(count);
            batchInventory.setLockedCount(0);
            batchInventory.setStatus(0); // 正常状态
            batchInventory.setRemark(remark);
            
            batchInventoryMapper.insert(batchInventory);
            log.info("新批次库存记录创建成功: batchInventoryId={}, stockCount={}, availableCount={}", 
                    batchInventory.getId(), batchInventory.getStockCount(), batchInventory.getAvailableCount());
        } else {
            // 增加库存
            beforeCount = batchInventory.getStockCount();
            batchInventory.setStockCount(batchInventory.getStockCount() + count);
            batchInventory.setAvailableCount(batchInventory.getAvailableCount() + count);
            
            log.info("更新现有批次库存: batchInventoryId={}, beforeStockCount={}, afterStockCount={}, beforeAvailableCount={}, afterAvailableCount={}", 
                    batchInventory.getId(), beforeCount, batchInventory.getStockCount(), 
                    beforeCount, batchInventory.getAvailableCount());
            
            // 更新批次库存
            int rows = batchInventoryMapper.updateById(batchInventory);
            log.info("批次库存更新结果: batchInventoryId={}, updatedRows={}", batchInventory.getId(), rows);
            
            // 确认更新成功
            if (rows <= 0) {
                log.error("批次库存更新失败: batchInventoryId={}", batchInventory.getId());
                throw exception(new ErrorCode(1002008099, "批次库存更新失败，请重试"));
            }
        }
        
        try {
            // 更新批次总库存
            int beforeBatchTotalCount = batch.getTotalCount();
            int beforeBatchAvailableCount = batch.getAvailableCount();
            
            batch.setTotalCount(batch.getTotalCount() + count);
            batch.setAvailableCount(batch.getAvailableCount() + count);
            
            log.info("更新批次总库存: batchId={}, beforeTotalCount={}, afterTotalCount={}, beforeAvailableCount={}, afterAvailableCount={}", 
                    batch.getId(), beforeBatchTotalCount, batch.getTotalCount(), 
                    beforeBatchAvailableCount, batch.getAvailableCount());
            
            // 使用正确的转换方式
            BatchSaveReqVO batchUpdateReqVO = new BatchSaveReqVO();
            batchUpdateReqVO.setId(batch.getId());
            batchUpdateReqVO.setBatchNo(batch.getBatchNo());
            batchUpdateReqVO.setItemId(batch.getItemId());
            batchUpdateReqVO.setWarehouseId(batch.getWarehouseId());
            batchUpdateReqVO.setSupplierId(batch.getSupplierId());
            batchUpdateReqVO.setProductionDate(batch.getProductionDate());
            batchUpdateReqVO.setExpiryDate(batch.getExpiryDate());
            batchUpdateReqVO.setBatchAttr1(batch.getBatchAttr1());
            batchUpdateReqVO.setBatchAttr2(batch.getBatchAttr2());
            batchUpdateReqVO.setBatchAttr3(batch.getBatchAttr3());
            batchUpdateReqVO.setBatchAttr4(batch.getBatchAttr4());
            batchUpdateReqVO.setTotalCount(batch.getTotalCount());
            batchUpdateReqVO.setAvailableCount(batch.getAvailableCount());
            batchUpdateReqVO.setLockedCount(batch.getLockedCount());
            
            batchService.updateBatch(batchUpdateReqVO);
            log.info("批次总库存更新成功: batchId={}", batch.getId());
        } catch (Exception e) {
            log.error("批次总库存更新失败", e);
            throw e; // 重新抛出异常，确保事务回滚
        }
        
        try {
            // 创建批次操作记录
            log.info("创建批次操作记录: batchId={}, movementType={}, count={}, beforeCount={}, afterCount={}", 
                    batchId, BatchMovementTypeEnum.RECEIPT.getType(), count, beforeCount, batchInventory.getStockCount());
            
            createBatchRecord(
                batchId, BatchMovementTypeEnum.RECEIPT.getType(),
                count, beforeCount, batchInventory.getStockCount(),
                businessId, remark
            );
            
            log.info("批次操作记录创建成功: batchId={}", batchId);
        } catch (Exception e) {
            log.error("创建批次操作记录失败", e);
            throw e; // 重新抛出异常，确保事务回滚
        }
        
        // 重新从数据库加载批次库存以确保数据一致性
        BatchInventoryDO freshBatchInventory = getBatchInventoryByInfo(batchId, itemId, warehouseId, locationId);
        log.info("批次库存增加操作完成: batchInventoryId={}, currentStockCount={}, currentAvailableCount={}", 
                freshBatchInventory.getId(), freshBatchInventory.getStockCount(), freshBatchInventory.getAvailableCount());
        
        return freshBatchInventory;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BatchInventoryDO decreaseBatchInventory(Long batchId, Long itemId, Long warehouseId, Long locationId, Integer count, 
                           String businessType, Long businessId, Long businessDetailId, String remark) {
        // 校验参数
        if (count <= 0) {
            log.error("减少批次库存失败：数量必须大于0 - batchId={}, itemId={}, warehouseId={}, locationId={}, count={}", 
                     batchId, itemId, warehouseId, locationId, count);
            throw exception(INVENTORY_INSUFFICIENT);
        }
        
        log.info("开始减少批次库存: batchId={}, itemId={}, warehouseId={}, locationId={}, count={}, businessType={}, businessId={}, businessDetailId={}", 
                batchId, itemId, warehouseId, locationId, count, businessType, businessId, businessDetailId);
        
        // 校验批次是否存在
        BatchDO batch = batchService.getBatch(batchId);
        if (batch == null) {
            log.error("减少批次库存失败：批次不存在 - batchId={}", batchId);
            throw exception(BATCH_NOT_EXISTS);
        }
        
        // 校验批次状态
        if (!BatchStatusEnum.NORMAL.getStatus().equals(batch.getStatus())) {
            log.error("减少批次库存失败：批次状态错误 - batchId={}, status={}", batchId, batch.getStatus());
            throw exception(BATCH_STATUS_ERROR);
        }
        
        // 获取批次库存
        BatchInventoryDO batchInventory = getBatchInventoryByInfo(batchId, itemId, warehouseId, locationId);
        if (batchInventory == null) {
            log.error("减少批次库存失败：批次库存不存在 - batchId={}, itemId={}, warehouseId={}, locationId={}", 
                     batchId, itemId, warehouseId, locationId);
            throw exception(BATCH_INVENTORY_NOT_EXISTS);
        }
        
        // 校验可用库存是否足够
        if (batchInventory.getAvailableCount() < count) {
            log.error("减少批次库存失败：可用库存不足 - batchId={}, itemId={}, warehouseId={}, locationId={}, availableCount={}, requestCount={}", 
                     batchId, itemId, warehouseId, locationId, batchInventory.getAvailableCount(), count);
            throw exception(INVENTORY_INSUFFICIENT);
        }
        
        // 减少库存
        int beforeCount = batchInventory.getStockCount();
        batchInventory.setStockCount(batchInventory.getStockCount() - count);
        batchInventory.setAvailableCount(batchInventory.getAvailableCount() - count);
        
        log.info("更新现有批次库存: batchInventoryId={}, beforeStockCount={}, afterStockCount={}, beforeAvailableCount={}, afterAvailableCount={}", 
                batchInventory.getId(), beforeCount, batchInventory.getStockCount(), 
                batchInventory.getAvailableCount() + count, batchInventory.getAvailableCount());
        
        // 更新批次库存
        int rows = batchInventoryMapper.updateById(batchInventory);
        log.info("批次库存更新结果: batchInventoryId={}, updatedRows={}", batchInventory.getId(), rows);
        
        // 确认更新成功
        if (rows <= 0) {
            log.error("批次库存更新失败: batchInventoryId={}", batchInventory.getId());
            throw exception(new ErrorCode(1002008099, "批次库存更新失败，请重试"));
        }
        
        try {
            // 更新批次总库存
            int beforeBatchAvailableCount = batch.getAvailableCount();
            batch.setAvailableCount(Math.max(0, batch.getAvailableCount() - count));
            
            log.info("更新批次总库存: batchId={}, beforeAvailableCount={}, afterAvailableCount={}", 
                    batch.getId(), beforeBatchAvailableCount, batch.getAvailableCount());
            
            // 使用正确的转换方式
            BatchSaveReqVO batchUpdateReqVO = new BatchSaveReqVO();
            batchUpdateReqVO.setId(batch.getId());
            batchUpdateReqVO.setBatchNo(batch.getBatchNo());
            batchUpdateReqVO.setItemId(batch.getItemId());
            batchUpdateReqVO.setWarehouseId(batch.getWarehouseId());
            batchUpdateReqVO.setSupplierId(batch.getSupplierId());
            batchUpdateReqVO.setProductionDate(batch.getProductionDate());
            batchUpdateReqVO.setExpiryDate(batch.getExpiryDate());
            batchUpdateReqVO.setBatchAttr1(batch.getBatchAttr1());
            batchUpdateReqVO.setBatchAttr2(batch.getBatchAttr2());
            batchUpdateReqVO.setBatchAttr3(batch.getBatchAttr3());
            batchUpdateReqVO.setBatchAttr4(batch.getBatchAttr4());
            batchUpdateReqVO.setTotalCount(batch.getTotalCount());
            batchUpdateReqVO.setAvailableCount(batch.getAvailableCount());
            batchUpdateReqVO.setLockedCount(batch.getLockedCount());
            
            batchService.updateBatch(batchUpdateReqVO);
            log.info("批次总库存更新成功: batchId={}", batch.getId());
        } catch (Exception e) {
            log.error("批次总库存更新失败", e);
            throw e; // 重新抛出异常，确保事务回滚
        }
        
        try {
            // 创建批次操作记录
            log.info("创建批次操作记录: batchId={}, movementType={}, count={}, beforeCount={}, afterCount={}", 
                    batchId, BatchMovementTypeEnum.SHIPMENT.getType(), count, beforeCount, batchInventory.getStockCount());
            
            createBatchRecord(
                batchId, BatchMovementTypeEnum.SHIPMENT.getType(),
                count, beforeCount, batchInventory.getStockCount(),
                businessId, remark
            );
            
            log.info("批次操作记录创建成功: batchId={}", batchId);
        } catch (Exception e) {
            log.error("创建批次操作记录失败", e);
            throw e; // 重新抛出异常，确保事务回滚
        }
        
        // 重新从数据库加载批次库存以确保数据一致性
        BatchInventoryDO freshBatchInventory = getBatchInventoryByInfo(batchId, itemId, warehouseId, locationId);
        log.info("批次库存减少操作完成: batchInventoryId={}, currentStockCount={}, currentAvailableCount={}", 
                freshBatchInventory.getId(), freshBatchInventory.getStockCount(), freshBatchInventory.getAvailableCount());
        
        return freshBatchInventory;
    }
    
    @Override
    public boolean isBatchInventorySufficient(Long batchId, Long itemId, Long warehouseId, Long locationId, Integer count) {
        // 获取批次库存
        BatchInventoryDO batchInventory = getBatchInventoryByInfo(batchId, itemId, warehouseId, locationId);
        if (batchInventory == null) {
            return false;
        }
        
        // 校验批次库存状态
        if (batchInventory.getStatus() != 0) { // 0-正常
            return false;
        }
        
        // 校验可用库存是否足够
        return batchInventory.getAvailableCount() >= count;
    }
    
    @Override
    public Integer getTotalAvailableCountByBatchId(Long batchId) {
        List<BatchInventoryDO> batchInventories = batchInventoryMapper.selectListByBatchId(batchId);
        return batchInventories.stream()
                .filter(inv -> inv.getStatus() == 0) // 0-正常
                .mapToInt(BatchInventoryDO::getAvailableCount)
                .sum();
    }
    
    @Override
    public Integer getAvailableCountByBatchAndWarehouse(Long batchId, Long warehouseId) {
        List<BatchInventoryDO> batchInventories = batchInventoryMapper.selectListByWarehouseId(warehouseId);
        return batchInventories.stream()
                .filter(inv -> batchId.equals(inv.getBatchId()) && inv.getStatus() == 0) // 0-正常
                .mapToInt(BatchInventoryDO::getAvailableCount)
                .sum();
    }
    
    /**
     * 创建批次操作记录
     */
    private void createBatchRecord(Long batchId, Integer movementType, Integer count, 
                            Integer beforeCount, Integer afterCount, 
                            Long businessId, String remark) {
        // 创建批次操作记录
        BatchRecordSaveReqVO recordReqVO = new BatchRecordSaveReqVO();
        recordReqVO.setBatchId(batchId);
        recordReqVO.setMovementType(movementType);
        recordReqVO.setMovementId(businessId);
        recordReqVO.setCount(count);
        recordReqVO.setBeforeCount(beforeCount);
        recordReqVO.setAfterCount(afterCount);
        recordReqVO.setRemark(remark);
        
        batchRecordService.createBatchRecord(recordReqVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BatchInventoryDO moveBatchInventory(Long batchId, Long itemId, 
                                       Long fromWarehouseId, Long toWarehouseId,
                                       Long fromLocationId, Long toLocationId,
                                       Integer count, String businessType, Long businessId, 
                                       Long businessDetailId, String remark) {
        // 校验参数
        if (count <= 0) {
            throw exception(INVENTORY_INSUFFICIENT);
        }
        
        // 校验批次是否存在
        BatchDO batch = batchService.getBatch(batchId);
        if (batch == null) {
            throw exception(BATCH_NOT_EXISTS);
        }
        
        // 校验批次状态
        if (!BatchStatusEnum.NORMAL.getStatus().equals(batch.getStatus())) {
            throw exception(BATCH_STATUS_ERROR);
        }
        
        // 获取源库位批次库存
        BatchInventoryDO sourceBatchInventory = getBatchInventoryByInfo(batchId, itemId, fromWarehouseId, fromLocationId);
        if (sourceBatchInventory == null) {
            throw exception(BATCH_INVENTORY_NOT_EXISTS);
        }
        
        // 校验源库位可用库存是否足够
        if (sourceBatchInventory.getAvailableCount() < count) {
            throw exception(INVENTORY_INSUFFICIENT);
        }
        
        // 减少源库位批次库存
        int sourceBeforeCount = sourceBatchInventory.getStockCount();
        sourceBatchInventory.setStockCount(sourceBatchInventory.getStockCount() - count);
        sourceBatchInventory.setAvailableCount(sourceBatchInventory.getAvailableCount() - count);
        
        // 更新源库位批次库存
        batchInventoryMapper.updateById(sourceBatchInventory);
        
        // 创建源库位批次操作记录
        createBatchRecord(
            batchId, BatchMovementTypeEnum.MOVE_OUT.getType(),
            count, sourceBeforeCount, sourceBatchInventory.getStockCount(),
            businessId, "移库出库: " + remark
        );
        
        // 获取目标库位批次库存，如果不存在则创建
        BatchInventoryDO targetBatchInventory = getBatchInventoryByInfo(batchId, itemId, toWarehouseId, toLocationId);
        int targetBeforeCount = 0;
        
        if (targetBatchInventory == null) {
            // 创建新批次库存记录
            targetBatchInventory = new BatchInventoryDO();
            targetBatchInventory.setBatchId(batchId);
            targetBatchInventory.setItemId(itemId);
            targetBatchInventory.setWarehouseId(toWarehouseId);
            targetBatchInventory.setLocationId(toLocationId);
            targetBatchInventory.setStockCount(count);
            targetBatchInventory.setAvailableCount(count);
            targetBatchInventory.setLockedCount(0);
            targetBatchInventory.setStatus(0); // 正常状态
            targetBatchInventory.setRemark(remark);
            
            batchInventoryMapper.insert(targetBatchInventory);
        } else {
            // 增加目标库位批次库存
            targetBeforeCount = targetBatchInventory.getStockCount();
            targetBatchInventory.setStockCount(targetBatchInventory.getStockCount() + count);
            targetBatchInventory.setAvailableCount(targetBatchInventory.getAvailableCount() + count);
            
            // 更新目标库位批次库存
            batchInventoryMapper.updateById(targetBatchInventory);
        }
        
        // 创建目标库位批次操作记录
        createBatchRecord(
            batchId, BatchMovementTypeEnum.MOVE_IN.getType(),
            count, targetBeforeCount, targetBatchInventory.getStockCount(),
            businessId, "移库入库: " + remark
        );
        
        // 批次总库存不变，只是位置变化，所以不需要更新批次总库存
        
        return targetBatchInventory;
    }
}