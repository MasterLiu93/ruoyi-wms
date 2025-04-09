package cn.smart.wms.module.wms.service.inventory;

import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.module.wms.controller.admin.inventory.vo.InventoryPageReqVO;
import cn.smart.wms.module.wms.controller.admin.inventory.vo.InventorySaveReqVO;
import cn.smart.wms.module.wms.dal.dataobject.inventory.InventoryDO;

import javax.validation.Valid;
import java.util.List;

/**
 * 库存 Service 接口
 *
 * @author 芋道源码
 */
public interface InventoryService {

    /**
     * 创建库存
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createInventory(@Valid InventorySaveReqVO createReqVO);

    /**
     * 更新库存
     *
     * @param updateReqVO 更新信息
     */
    void updateInventory(@Valid InventorySaveReqVO updateReqVO);

    /**
     * 删除库存
     *
     * @param id 编号
     */
    void deleteInventory(Long id);

    /**
     * 获得库存
     *
     * @param id 编号
     * @return 库存
     */
    InventoryDO getInventory(Long id);

    /**
     * 获得库存分页
     *
     * @param pageReqVO 分页查询
     * @return 库存分页
     */
    PageResult<InventoryDO> getInventoryPage(InventoryPageReqVO pageReqVO);
    
    /**
     * 根据仓库ID、物料ID和库位ID获取库存
     *
     * @param warehouseId 仓库ID
     * @param itemId 物料ID
     * @param locationId 库位ID
     * @return 库存对象，如果不存在返回null
     */
    InventoryDO getInventoryByWarehouseAndItemAndLocation(Long warehouseId, Long itemId, Long locationId);
    
    /**
     * 锁定库存
     *
     * @param warehouseId 仓库ID
     * @param itemId 物料ID
     * @param locationId 库位ID
     * @param count 锁定数量
     * @param businessType 业务类型
     * @param businessId 业务单ID
     * @param businessDetailId 业务明细ID
     * @param remark 备注
     */
    void lockInventory(Long warehouseId, Long itemId, Long locationId, Integer count, 
                     String businessType, Long businessId, Long businessDetailId, String remark);
    
    /**
     * 解锁库存
     *
     * @param warehouseId 仓库ID
     * @param itemId 物料ID
     * @param locationId 库位ID
     * @param count 解锁数量
     * @param businessType 业务类型
     * @param businessId 业务单ID
     * @param businessDetailId 业务明细ID
     * @param remark 备注
     */
    void unlockInventory(Long warehouseId, Long itemId, Long locationId, Integer count, 
                       String businessType, Long businessId, Long businessDetailId, String remark);
    
    /**
     * 增加库存
     *
     * @param warehouseId 仓库ID
     * @param itemId 物料ID
     * @param locationId 库位ID
     * @param count 增加数量
     * @param businessType 业务类型
     * @param businessId 业务单ID
     * @param businessDetailId 业务明细ID
     * @param remark 备注
     * @return 库存对象
     */
    InventoryDO increaseInventory(Long warehouseId, Long itemId, Long locationId, Integer count, 
                               String businessType, Long businessId, Long businessDetailId, String remark);
    
    /**
     * 减少库存
     *
     * @param warehouseId 仓库ID
     * @param itemId 物料ID
     * @param locationId 库位ID
     * @param count 减少数量
     * @param businessType 业务类型
     * @param businessId 业务单ID
     * @param businessDetailId 业务明细ID
     * @param remark 备注
     * @return 库存对象
     */
    InventoryDO decreaseInventory(Long warehouseId, Long itemId, Long locationId, Integer count, 
                               String businessType, Long businessId, Long businessDetailId, String remark);
    
    /**
     * 校验库存是否足够
     *
     * @param warehouseId 仓库ID
     * @param itemId 物料ID
     * @param locationId 库位ID
     * @param count 需要的数量
     * @return 是否足够
     */
    boolean isInventorySufficient(Long warehouseId, Long itemId, Long locationId, Integer count);
    
    /**
     * 获取物料在所有库位的可用库存总量
     *
     * @param itemId 物料ID
     * @return 可用库存总量
     */
    Integer getTotalAvailableCountByItemId(Long itemId);
    
    /**
     * 获取物料在指定仓库的可用库存总量
     *
     * @param warehouseId 仓库ID
     * @param itemId 物料ID
     * @return 可用库存总量
     */
    Integer getAvailableCountByWarehouseAndItem(Long warehouseId, Long itemId);

    /**
     * 获取符合条件的库存列表
     *
     * @param warehouseId 仓库ID
     * @param locationIds 库位ID列表，如果为空则不限制库位
     * @param itemIds 物料ID列表，如果为空则不限制物料
     * @return 库存列表
     */
    List<InventoryDO> getInventoryList(Long warehouseId, List<Long> locationIds, List<Long> itemIds);
    
    /**
     * 获取指定仓库的所有库存
     *
     * @param warehouseId 仓库ID
     * @return 库存列表
     */
    List<InventoryDO> getInventoryListByWarehouse(Long warehouseId);
    
    /**
     * 获取指定仓库和指定库位的库存
     *
     * @param warehouseId 仓库ID
     * @param locationIds 库位ID列表
     * @return 库存列表
     */
    List<InventoryDO> getInventoryListByWarehouseAndLocationIds(Long warehouseId, List<Long> locationIds);
    
    /**
     * 获取指定仓库和指定物料的库存
     *
     * @param warehouseId 仓库ID
     * @param itemIds 物料ID列表
     * @return 库存列表
     */
    List<InventoryDO> getInventoryListByWarehouseAndItemIds(Long warehouseId, List<Long> itemIds);
    
    /**
     * 获取指定仓库、指定库位和指定物料的库存
     *
     * @param warehouseId 仓库ID
     * @param locationIds 库位ID列表
     * @param itemIds 物料ID列表
     * @return 库存列表
     */
    List<InventoryDO> getInventoryListByWarehouseAndLocationIdsAndItemIds(Long warehouseId, List<Long> locationIds, List<Long> itemIds);
}