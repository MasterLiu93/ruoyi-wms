package cn.smart.wms.module.wms.service.inventory;

import cn.smart.wms.framework.common.exception.ErrorCode;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.util.object.BeanUtils;
import cn.smart.wms.module.wms.controller.admin.inventory.vo.InventoryPageReqVO;
import cn.smart.wms.module.wms.controller.admin.inventory.vo.InventorySaveReqVO;
import cn.smart.wms.module.wms.controller.admin.inventorymovement.vo.InventoryMovementSaveReqVO;
import cn.smart.wms.module.wms.dal.dataobject.inventory.InventoryDO;
import cn.smart.wms.module.wms.dal.mysql.inventory.InventoryMapper;
import cn.smart.wms.module.wms.enums.inventory.InventoryMovementTypeEnum;
import cn.smart.wms.module.wms.enums.inventory.InventoryStatusEnum;
import cn.smart.wms.module.wms.service.inventorymovement.InventoryMovementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static cn.smart.wms.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.smart.wms.module.wms.enums.ErrorCodeConstants.*;

/**
 * 库存 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    @Resource
    private InventoryMapper inventoryMapper;
    
    @Resource
    private InventoryMovementService inventoryMovementService;

    @Override
    public Long createInventory(InventorySaveReqVO createReqVO) {
        // 插入
        InventoryDO inventory = BeanUtils.toBean(createReqVO, InventoryDO.class);
        inventoryMapper.insert(inventory);
        // 返回
        return inventory.getId();
    }

    @Override
    public void updateInventory(InventorySaveReqVO updateReqVO) {
        // 校验存在
        validateInventoryExists(updateReqVO.getId());
        // 更新
        InventoryDO updateObj = BeanUtils.toBean(updateReqVO, InventoryDO.class);
        inventoryMapper.updateById(updateObj);
    }

    @Override
    public void deleteInventory(Long id) {
        // 校验存在
        validateInventoryExists(id);
        // 删除
        inventoryMapper.deleteById(id);
    }

    private void validateInventoryExists(Long id) {
        if (inventoryMapper.selectById(id) == null) {
            throw exception(INVENTORY_NOT_EXISTS);
        }
    }

    @Override
    public InventoryDO getInventory(Long id) {
        return inventoryMapper.selectById(id);
    }

    @Override
    public PageResult<InventoryDO> getInventoryPage(InventoryPageReqVO pageReqVO) {
        return inventoryMapper.selectPage(pageReqVO);
    }
    
    @Override
    public InventoryDO getInventoryByWarehouseAndItemAndLocation(Long warehouseId, Long itemId, Long locationId) {
        return inventoryMapper.selectByWarehouseAndItemAndLocation(warehouseId, itemId, locationId);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void lockInventory(Long warehouseId, Long itemId, Long locationId, Integer count, 
                           String businessType, Long businessId, Long businessDetailId, String remark) {
        // 校验参数                   
        if (count <= 0) {
            throw exception(INVENTORY_INSUFFICIENT);
        }
        
        // 获取库存
        InventoryDO inventory = getInventoryByWarehouseAndItemAndLocation(warehouseId, itemId, locationId);
        if (inventory == null) {
            throw exception(INVENTORY_NOT_EXISTS);
        }
        
        // 校验库存状态
        if (InventoryStatusEnum.DISABLED.getStatus().equals(inventory.getStatus())) {
            throw exception(INVENTORY_FROZEN);
        }
        
        // 校验可用库存是否足够
        if (inventory.getAvailableCount() < count) {
            throw exception(INVENTORY_INSUFFICIENT);
        }
        
        // 锁定库存
        int beforeAvailableCount = inventory.getAvailableCount();
        int beforeLockedCount = inventory.getLockedCount();
        
        inventory.setAvailableCount(inventory.getAvailableCount() - count);
        inventory.setLockedCount(inventory.getLockedCount() + count);
        
        // 更新库存
        inventoryMapper.updateById(inventory);
        
        // 创建库存移动记录
        createInventoryMovementRecord(
            InventoryMovementTypeEnum.ADJUSTMENT.getType(),
            warehouseId, locationId, itemId,
            count, beforeAvailableCount, inventory.getAvailableCount(),
            businessType, businessId, businessDetailId,
            "锁定库存: " + remark
        );
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unlockInventory(Long warehouseId, Long itemId, Long locationId, Integer count, 
                             String businessType, Long businessId, Long businessDetailId, String remark) {
        // 校验参数                     
        if (count <= 0) {
            throw exception(INVENTORY_INSUFFICIENT);
        }
        
        // 获取库存
        InventoryDO inventory = getInventoryByWarehouseAndItemAndLocation(warehouseId, itemId, locationId);
        if (inventory == null) {
            throw exception(INVENTORY_NOT_EXISTS);
        }
        
        // 校验库存状态
        if (InventoryStatusEnum.DISABLED.getStatus().equals(inventory.getStatus())) {
            throw exception(INVENTORY_FROZEN);
        }
        
        // 校验锁定库存是否足够
        if (inventory.getLockedCount() < count) {
            throw exception(INVENTORY_INSUFFICIENT);
        }
        
        // 解锁库存
        int beforeAvailableCount = inventory.getAvailableCount();
        int beforeLockedCount = inventory.getLockedCount();
        
        inventory.setAvailableCount(inventory.getAvailableCount() + count);
        inventory.setLockedCount(inventory.getLockedCount() - count);
        
        // 更新库存
        inventoryMapper.updateById(inventory);
        
        // 创建库存移动记录
        createInventoryMovementRecord(
            InventoryMovementTypeEnum.ADJUSTMENT.getType(),
            warehouseId, locationId, itemId,
            count, beforeAvailableCount, inventory.getAvailableCount(),
            businessType, businessId, businessDetailId,
            "解锁库存: " + remark
        );
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public InventoryDO increaseInventory(Long warehouseId, Long itemId, Long locationId, Integer count, 
                                      String businessType, Long businessId, Long businessDetailId, String remark) {
        // 校验参数
        if (count <= 0) {
            log.error("增加库存失败：数量必须大于0 - warehouseId={}, itemId={}, locationId={}, count={}", 
                     warehouseId, itemId, locationId, count);
            throw exception(INVENTORY_INSUFFICIENT);
        }
        
        log.info("开始增加库存: warehouseId={}, itemId={}, locationId={}, count={}, businessType={}, businessId={}, businessDetailId={}", 
                warehouseId, itemId, locationId, count, businessType, businessId, businessDetailId);
        
        // 获取库存，如果不存在则创建
        InventoryDO inventory = getInventoryByWarehouseAndItemAndLocation(warehouseId, itemId, locationId);
        int beforeCount = 0;
        
        if (inventory == null) {
            // 创建新库存记录
            log.info("库存记录不存在，创建新库存记录: warehouseId={}, itemId={}, locationId={}, count={}", 
                    warehouseId, itemId, locationId, count);
            
            try {
                inventory = new InventoryDO();
                inventory.setWarehouseId(warehouseId);
                inventory.setItemId(itemId);
                inventory.setLocationId(locationId);
                inventory.setStockCount(count);
                inventory.setAvailableCount(count);
                inventory.setLockedCount(0);
                inventory.setStatus(InventoryStatusEnum.NORMAL.getStatus());
                inventory.setRemark(remark);
                
                inventoryMapper.insert(inventory);
                log.info("新库存记录创建成功: inventoryId={}, stockCount={}, availableCount={}", 
                        inventory.getId(), inventory.getStockCount(), inventory.getAvailableCount());
            } catch (Exception e) {
                log.error("创建新库存记录失败: warehouseId={}, itemId={}, locationId={}, 错误信息={}", 
                         warehouseId, itemId, locationId, e.getMessage(), e);
                throw e;
            }
        } else {
            // 校验库存状态
            if (InventoryStatusEnum.DISABLED.getStatus().equals(inventory.getStatus())) {
                log.error("增加库存失败：库存已冻结 - warehouseId={}, itemId={}, locationId={}, status={}", 
                        warehouseId, itemId, locationId, inventory.getStatus());
                throw exception(INVENTORY_FROZEN);
            }
            
            // 增加库存
            beforeCount = inventory.getStockCount();
            inventory.setStockCount(inventory.getStockCount() + count);
            inventory.setAvailableCount(inventory.getAvailableCount() + count);
            
            log.info("更新现有库存: inventoryId={}, beforeStockCount={}, afterStockCount={}, beforeAvailableCount={}, afterAvailableCount={}", 
                    inventory.getId(), beforeCount, inventory.getStockCount(), 
                    inventory.getAvailableCount() - count, inventory.getAvailableCount());
            
            try {
                // 更新库存 - 使用乐观锁确保不会并发更新问题
                int rows = inventoryMapper.updateById(inventory);
                log.info("库存更新结果: inventoryId={}, updatedRows={}", inventory.getId(), rows);
                
                // 确认更新成功
                if (rows <= 0) {
                    log.error("库存更新失败，可能是并发冲突: inventoryId={}", inventory.getId());
                    
                    // 重新获取最新的库存记录，然后重试一次
                    InventoryDO freshInventory = inventoryMapper.selectById(inventory.getId());
                    if (freshInventory != null) {
                        log.info("重新获取库存记录: inventoryId={}, currentStockCount={}, currentAvailableCount={}",
                                freshInventory.getId(), freshInventory.getStockCount(), freshInventory.getAvailableCount());
                        
                        // 重新计算增加后的值
                        freshInventory.setStockCount(freshInventory.getStockCount() + count);
                        freshInventory.setAvailableCount(freshInventory.getAvailableCount() + count);
                        
                        rows = inventoryMapper.updateById(freshInventory);
                        log.info("重试更新库存结果: inventoryId={}, updatedRows={}", freshInventory.getId(), rows);
                        
                        if (rows > 0) {
                            inventory = freshInventory; // 使用更新成功的库存记录
                        } else {
                            log.error("库存更新第二次尝试仍然失败: inventoryId={}", freshInventory.getId());
                            throw exception(new ErrorCode(1002008099, "库存更新失败，请重试"));
                        }
                    } else {
                        log.error("无法找到库存记录: inventoryId={}", inventory.getId());
                        throw exception(new ErrorCode(1002008099, "库存记录不存在，请重试"));
                    }
                }
            } catch (Exception e) {
                log.error("更新库存失败: inventoryId={}, 错误信息={}", inventory.getId(), e.getMessage(), e);
                throw e;
            }
        }
        
        try {
            // 创建库存移动记录
            log.info("创建库存移动记录: movementType={}, warehouseId={}, locationId={}, itemId={}, count={}, beforeCount={}, afterCount={}", 
                    InventoryMovementTypeEnum.IN.getType(), warehouseId, locationId, itemId, count, beforeCount, inventory.getStockCount());
            
            createInventoryMovementRecord(
                InventoryMovementTypeEnum.IN.getType(),
                warehouseId, locationId, itemId,
                count, beforeCount, inventory.getStockCount(),
                businessType, businessId, businessDetailId,
                remark
            );
            
            log.info("库存移动记录创建成功");
        } catch (Exception e) {
            log.error("创建库存移动记录失败: 错误信息={}", e.getMessage(), e);
            throw e; // 重新抛出异常，确保事务回滚
        }
        
        // 重新从数据库加载库存以确保数据一致性
        InventoryDO freshInventory = getInventoryByWarehouseAndItemAndLocation(warehouseId, itemId, locationId);
        if (freshInventory != null) {
            log.info("库存增加操作完成: inventoryId={}, currentStockCount={}, currentAvailableCount={}", 
                    freshInventory.getId(), freshInventory.getStockCount(), freshInventory.getAvailableCount());
            
            // 额外验证库存是否正确更新
            boolean stockCountCorrect = (beforeCount + count) == freshInventory.getStockCount();
            if (!stockCountCorrect) {
                log.warn("库存数量更新异常: inventoryId={}, 预期库存={}, 实际库存={}, 差额={}", 
                        freshInventory.getId(), beforeCount + count, freshInventory.getStockCount(), 
                        (beforeCount + count) - freshInventory.getStockCount());
            } else {
                log.info("库存更新验证成功: 原始库存={}, 增加数量={}, 最终库存={}", 
                        beforeCount, count, freshInventory.getStockCount());
            }
            
            return freshInventory;
        } else {
            log.error("无法获取更新后的库存记录: warehouseId={}, itemId={}, locationId={}", 
                     warehouseId, itemId, locationId);
            
            // 如果无法获取更新后的库存，则返回原来的库存记录
            return inventory;
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public InventoryDO decreaseInventory(Long warehouseId, Long itemId, Long locationId, Integer count, 
                                      String businessType, Long businessId, Long businessDetailId, String remark) {
        // 校验参数
        if (count <= 0) {
            log.error("减少库存失败：数量必须大于0 - warehouseId={}, itemId={}, locationId={}, count={}", 
                     warehouseId, itemId, locationId, count);
            throw exception(INVENTORY_INSUFFICIENT);
        }
        
        log.info("开始减少库存: warehouseId={}, itemId={}, locationId={}, count={}, businessType={}, businessId={}, businessDetailId={}", 
                 warehouseId, itemId, locationId, count, businessType, businessId, businessDetailId);
        
        // 获取库存
        InventoryDO inventory = getInventoryByWarehouseAndItemAndLocation(warehouseId, itemId, locationId);
        if (inventory == null) {
            log.error("减少库存失败：库存不存在 - warehouseId={}, itemId={}, locationId={}", 
                     warehouseId, itemId, locationId);
            throw exception(INVENTORY_NOT_EXISTS);
        }
        
        // 校验库存状态
        if (InventoryStatusEnum.DISABLED.getStatus().equals(inventory.getStatus())) {
            log.error("减少库存失败：库存已冻结 - warehouseId={}, itemId={}, locationId={}, status={}", 
                     warehouseId, itemId, locationId, inventory.getStatus());
            throw exception(INVENTORY_FROZEN);
        }
        
        // 校验可用库存是否足够
        if (inventory.getAvailableCount() < count) {
            log.error("减少库存失败：可用库存不足 - warehouseId={}, itemId={}, locationId={}, availableCount={}, requestCount={}", 
                     warehouseId, itemId, locationId, inventory.getAvailableCount(), count);
            throw exception(INVENTORY_INSUFFICIENT);
        }
        
        // 减少库存
        int beforeCount = inventory.getStockCount();
        inventory.setStockCount(inventory.getStockCount() - count);
        inventory.setAvailableCount(inventory.getAvailableCount() - count);
        
        log.info("更新现有库存: inventoryId={}, beforeStockCount={}, afterStockCount={}, beforeAvailableCount={}, afterAvailableCount={}", 
                inventory.getId(), beforeCount, inventory.getStockCount(), 
                inventory.getAvailableCount() + count, inventory.getAvailableCount());
        
        // 更新库存
        int rows = inventoryMapper.updateById(inventory);
        log.info("库存更新结果: inventoryId={}, updatedRows={}", inventory.getId(), rows);
        
        // 确认更新成功
        if (rows <= 0) {
            log.error("库存更新失败: inventoryId={}", inventory.getId());
            throw exception(new ErrorCode(1002008099, "库存更新失败，请重试"));
        }
        
        try {
            // 创建库存移动记录
            log.info("创建库存移动记录: movementType={}, warehouseId={}, locationId={}, itemId={}, count={}, beforeCount={}, afterCount={}", 
                    InventoryMovementTypeEnum.OUT.getType(), warehouseId, locationId, itemId, count, beforeCount, inventory.getStockCount());
            
            createInventoryMovementRecord(
                InventoryMovementTypeEnum.OUT.getType(),
                warehouseId, locationId, itemId,
                count, beforeCount, inventory.getStockCount(),
                businessType, businessId, businessDetailId,
                remark
            );
            
            log.info("库存移动记录创建成功");
        } catch (Exception e) {
            log.error("创建库存移动记录失败", e);
            throw e; // 重新抛出异常，确保事务回滚
        }
        
        // 重新从数据库加载库存以确保数据一致性
        InventoryDO freshInventory = getInventoryByWarehouseAndItemAndLocation(warehouseId, itemId, locationId);
        log.info("库存减少操作完成: inventoryId={}, currentStockCount={}, currentAvailableCount={}", 
                freshInventory.getId(), freshInventory.getStockCount(), freshInventory.getAvailableCount());
        
        return freshInventory;
    }
    
    @Override
    public boolean isInventorySufficient(Long warehouseId, Long itemId, Long locationId, Integer count) {
        // 获取库存
        InventoryDO inventory = getInventoryByWarehouseAndItemAndLocation(warehouseId, itemId, locationId);
        if (inventory == null) {
            return false;
        }
        
        // 校验库存状态
        if (InventoryStatusEnum.DISABLED.getStatus().equals(inventory.getStatus())) {
            return false;
        }
        
        // 校验可用库存是否足够
        return inventory.getAvailableCount() >= count;
    }
    
    @Override
    public Integer getTotalAvailableCountByItemId(Long itemId) {
        List<InventoryDO> inventories = inventoryMapper.selectListByItemId(itemId);
        return inventories.stream()
                .filter(inv -> InventoryStatusEnum.NORMAL.getStatus().equals(inv.getStatus()))
                .mapToInt(InventoryDO::getAvailableCount)
                .sum();
    }
    
    @Override
    public Integer getAvailableCountByWarehouseAndItem(Long warehouseId, Long itemId) {
        List<InventoryDO> inventories = inventoryMapper.selectListByWarehouseId(warehouseId);
        return inventories.stream()
                .filter(inv -> itemId.equals(inv.getItemId()) 
                        && InventoryStatusEnum.NORMAL.getStatus().equals(inv.getStatus()))
                .mapToInt(InventoryDO::getAvailableCount)
                .sum();
    }
    
    @Override
    public List<InventoryDO> getInventoryList(Long warehouseId, List<Long> locationIds, List<Long> itemIds) {
        if (warehouseId == null) {
            throw exception(INVENTORY_NOT_EXISTS);
        }
        
        return inventoryMapper.selectListByConditions(warehouseId, locationIds, itemIds);
    }
    
    @Override
    public List<InventoryDO> getInventoryListByWarehouse(Long warehouseId) {
        if (warehouseId == null) {
            throw exception(INVENTORY_NOT_EXISTS);
        }
        
        return inventoryMapper.selectListByWarehouseId(warehouseId);
    }
    
    @Override
    public List<InventoryDO> getInventoryListByWarehouseAndLocationIds(Long warehouseId, List<Long> locationIds) {
        if (warehouseId == null) {
            throw exception(INVENTORY_NOT_EXISTS);
        }
        
        if (locationIds == null || locationIds.isEmpty()) {
            return new ArrayList<>();
        }
        
        return inventoryMapper.selectListByConditions(warehouseId, locationIds, null);
    }
    
    @Override
    public List<InventoryDO> getInventoryListByWarehouseAndItemIds(Long warehouseId, List<Long> itemIds) {
        if (warehouseId == null) {
            throw exception(INVENTORY_NOT_EXISTS);
        }
        
        if (itemIds == null || itemIds.isEmpty()) {
            return new ArrayList<>();
        }
        
        return inventoryMapper.selectListByConditions(warehouseId, null, itemIds);
    }
    
    @Override
    public List<InventoryDO> getInventoryListByWarehouseAndLocationIdsAndItemIds(Long warehouseId, List<Long> locationIds, List<Long> itemIds) {
        if (warehouseId == null) {
            throw exception(INVENTORY_NOT_EXISTS);
        }
        
        if ((locationIds == null || locationIds.isEmpty()) || (itemIds == null || itemIds.isEmpty())) {
            return new ArrayList<>();
        }
        
        return inventoryMapper.selectListByConditions(warehouseId, locationIds, itemIds);
    }
    
    /**
     * 创建库存移动记录
     */
    private void createInventoryMovementRecord(Integer movementType, Long warehouseId, Long locationId, Long itemId, 
                                         Integer count, Integer beforeCount, Integer afterCount,
                                         String businessType, Long businessId, Long businessDetailId, String remark) {
        // 生成移动单号
        String movementNo = generateMovementNo();
        
        // 创建库存移动记录
        InventoryMovementSaveReqVO movementReqVO = new InventoryMovementSaveReqVO();
        movementReqVO.setMovementType(movementType);
        movementReqVO.setMovementNo(movementNo);
        movementReqVO.setWarehouseId(warehouseId);
        movementReqVO.setLocationId(locationId);
        movementReqVO.setItemId(itemId);
        movementReqVO.setCount(count);
        movementReqVO.setBeforeCount(beforeCount);
        movementReqVO.setAfterCount(afterCount);
        movementReqVO.setBusinessType(businessType);
        movementReqVO.setBusinessId(businessId);
        movementReqVO.setBusinessDetailId(businessDetailId);
        movementReqVO.setRemark(remark);
        
        inventoryMovementService.createInventoryMovement(movementReqVO);
    }
    
    /**
     * 生成移动单号
     */
    private String generateMovementNo() {
        // 前缀 + 时间戳 + 随机数
        return "MOV" + System.currentTimeMillis() + String.format("%04d", (int) (Math.random() * 10000));
    }
}