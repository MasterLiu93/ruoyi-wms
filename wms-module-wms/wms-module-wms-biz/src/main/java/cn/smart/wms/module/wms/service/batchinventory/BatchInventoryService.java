package cn.smart.wms.module.wms.service.batchinventory;

import javax.validation.*;
import cn.smart.wms.module.wms.controller.admin.batchinventory.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.batchinventory.BatchInventoryDO;
import cn.smart.wms.framework.common.pojo.PageResult;

/**
 * 批次库存 Service 接口
 *
 * @author 芋道源码
 */
public interface BatchInventoryService {

    /**
     * 创建批次库存
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createBatchInventory(@Valid BatchInventorySaveReqVO createReqVO);

    /**
     * 更新批次库存
     *
     * @param updateReqVO 更新信息
     */
    void updateBatchInventory(@Valid BatchInventorySaveReqVO updateReqVO);

    /**
     * 删除批次库存
     *
     * @param id 编号
     */
    void deleteBatchInventory(Long id);

    /**
     * 获得批次库存
     *
     * @param id 编号
     * @return 批次库存
     */
    BatchInventoryDO getBatchInventory(Long id);

    /**
     * 获得批次库存分页
     *
     * @param pageReqVO 分页查询
     * @return 批次库存分页
     */
    PageResult<BatchInventoryDO> getBatchInventoryPage(BatchInventoryPageReqVO pageReqVO);
    
    /**
     * 根据批次ID、物料ID、仓库ID和库位ID获取批次库存
     *
     * @param batchId 批次ID
     * @param itemId 物料ID
     * @param warehouseId 仓库ID
     * @param locationId 库位ID
     * @return 批次库存对象，如果不存在返回null
     */
    BatchInventoryDO getBatchInventoryByInfo(Long batchId, Long itemId, Long warehouseId, Long locationId);
    
    /**
     * 锁定批次库存
     *
     * @param batchId 批次ID
     * @param itemId 物料ID
     * @param warehouseId 仓库ID
     * @param locationId 库位ID
     * @param count 锁定数量
     * @param businessType 业务类型
     * @param businessId 业务单ID
     * @param businessDetailId 业务明细ID
     * @param remark 备注
     */
    void lockBatchInventory(Long batchId, Long itemId, Long warehouseId, Long locationId, Integer count, 
                     String businessType, Long businessId, Long businessDetailId, String remark);
    
    /**
     * 解锁批次库存
     *
     * @param batchId 批次ID
     * @param itemId 物料ID
     * @param warehouseId 仓库ID
     * @param locationId 库位ID
     * @param count 解锁数量
     * @param businessType 业务类型
     * @param businessId 业务单ID
     * @param businessDetailId 业务明细ID
     * @param remark 备注
     */
    void unlockBatchInventory(Long batchId, Long itemId, Long warehouseId, Long locationId, Integer count, 
                       String businessType, Long businessId, Long businessDetailId, String remark);
    
    /**
     * 增加批次库存
     *
     * @param batchId 批次ID
     * @param itemId 物料ID
     * @param warehouseId 仓库ID
     * @param locationId 库位ID
     * @param count 增加数量
     * @param businessType 业务类型
     * @param businessId 业务单ID
     * @param businessDetailId 业务明细ID
     * @param remark 备注
     * @return 批次库存对象
     */
    BatchInventoryDO increaseBatchInventory(Long batchId, Long itemId, Long warehouseId, Long locationId, Integer count, 
                               String businessType, Long businessId, Long businessDetailId, String remark);
    
    /**
     * 减少批次库存
     *
     * @param batchId 批次ID
     * @param itemId 物料ID
     * @param warehouseId 仓库ID
     * @param locationId 库位ID
     * @param count 减少数量
     * @param businessType 业务类型
     * @param businessId 业务单ID
     * @param businessDetailId 业务明细ID
     * @param remark 备注
     * @return 批次库存对象
     */
    BatchInventoryDO decreaseBatchInventory(Long batchId, Long itemId, Long warehouseId, Long locationId, Integer count, 
                               String businessType, Long businessId, Long businessDetailId, String remark);
    
    /**
     * 校验批次库存是否足够
     *
     * @param batchId 批次ID
     * @param itemId 物料ID
     * @param warehouseId 仓库ID
     * @param locationId 库位ID
     * @param count 需要的数量
     * @return 是否足够
     */
    boolean isBatchInventorySufficient(Long batchId, Long itemId, Long warehouseId, Long locationId, Integer count);
    
    /**
     * 获取批次在所有库位的可用库存总量
     *
     * @param batchId 批次ID
     * @return 可用库存总量
     */
    Integer getTotalAvailableCountByBatchId(Long batchId);
    
    /**
     * 获取批次在指定仓库的可用库存总量
     *
     * @param batchId 批次ID
     * @param warehouseId 仓库ID
     * @return 可用库存总量
     */
    Integer getAvailableCountByBatchAndWarehouse(Long batchId, Long warehouseId);

    /**
     * 移动批次库存
     *
     * @param batchId 批次ID
     * @param itemId 物料ID
     * @param fromWarehouseId 源仓库ID
     * @param toWarehouseId 目标仓库ID
     * @param fromLocationId 源库位ID
     * @param toLocationId 目标库位ID
     * @param count 移动数量
     * @param businessType 业务类型
     * @param businessId 业务单ID
     * @param businessDetailId 业务明细ID
     * @param remark 备注
     * @return 目标批次库存对象
     */
    BatchInventoryDO moveBatchInventory(Long batchId, Long itemId, 
                                      Long fromWarehouseId, Long toWarehouseId,
                                      Long fromLocationId, Long toLocationId,
                                      Integer count, String businessType, Long businessId, 
                                      Long businessDetailId, String remark);
}