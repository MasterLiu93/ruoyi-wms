package cn.smart.wms.module.wms.controller.admin.inventory;

import cn.smart.wms.framework.apilog.core.annotation.ApiAccessLog;
import cn.smart.wms.framework.common.pojo.CommonResult;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.util.object.BeanUtils;
import cn.smart.wms.framework.excel.core.util.ExcelUtils;
import cn.smart.wms.module.wms.controller.admin.inventory.vo.InventoryOperationReqVO;
import cn.smart.wms.module.wms.controller.admin.inventory.vo.InventoryPageReqVO;
import cn.smart.wms.module.wms.controller.admin.inventory.vo.InventoryRespVO;
import cn.smart.wms.module.wms.controller.admin.inventory.vo.InventorySaveReqVO;
import cn.smart.wms.module.wms.dal.dataobject.inventory.InventoryDO;
import cn.smart.wms.module.wms.service.inventory.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static cn.smart.wms.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.smart.wms.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 库存")
@RestController
@RequestMapping("/wms/inventory")
@Validated
public class InventoryController {

    @Resource
    private InventoryService inventoryService;

    @PostMapping("/create")
    @Operation(summary = "创建库存")
    @PreAuthorize("@ss.hasPermission('wms:inventory:create')")
    public CommonResult<Long> createInventory(@Valid @RequestBody InventorySaveReqVO createReqVO) {
        return success(inventoryService.createInventory(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新库存")
    @PreAuthorize("@ss.hasPermission('wms:inventory:update')")
    public CommonResult<Boolean> updateInventory(@Valid @RequestBody InventorySaveReqVO updateReqVO) {
        inventoryService.updateInventory(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除库存")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:inventory:delete')")
    public CommonResult<Boolean> deleteInventory(@RequestParam("id") Long id) {
        inventoryService.deleteInventory(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得库存")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:inventory:query')")
    public CommonResult<InventoryRespVO> getInventory(@RequestParam("id") Long id) {
        InventoryDO inventory = inventoryService.getInventory(id);
        return success(BeanUtils.toBean(inventory, InventoryRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得库存分页")
    @PreAuthorize("@ss.hasPermission('wms:inventory:query')")
    public CommonResult<PageResult<InventoryRespVO>> getInventoryPage(@Valid InventoryPageReqVO pageVO) {
        PageResult<InventoryDO> pageResult = inventoryService.getInventoryPage(pageVO);
        return success(BeanUtils.toBean(pageResult, InventoryRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出库存 Excel")
    @PreAuthorize("@ss.hasPermission('wms:inventory:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportInventoryExcel(@Valid InventoryPageReqVO pageVO,
              HttpServletResponse response) throws IOException {
        // 设置为导出全部数据
        pageVO.setPageNo(1);
        pageVO.setPageSize(Integer.MAX_VALUE);
        
        // 查询数据
        List<InventoryDO> list = inventoryService.getInventoryPage(pageVO).getList();
        
        // 转换为 VO 对象
        List<InventoryRespVO> voList = new ArrayList<>();
        for (InventoryDO inventory : list) {
            voList.add(BeanUtils.toBean(inventory, InventoryRespVO.class));
        }
        
        // 导出 Excel
        ExcelUtils.write(response, "库存数据.xls", "数据", InventoryRespVO.class, voList);
    }
    
    @GetMapping("/get-by-location")
    @Operation(summary = "根据仓库、物料和库位获取库存")
    @Parameter(name = "warehouseId", description = "仓库ID", required = true, example = "1001")
    @Parameter(name = "itemId", description = "物料ID", required = true, example = "2001")
    @Parameter(name = "locationId", description = "库位ID", required = true, example = "3001")
    @PreAuthorize("@ss.hasPermission('wms:inventory:query')")
    public CommonResult<InventoryRespVO> getInventoryByLocation(@RequestParam("warehouseId") Long warehouseId,
                                                          @RequestParam("itemId") Long itemId,
                                                          @RequestParam("locationId") Long locationId) {
        InventoryDO inventory = inventoryService.getInventoryByWarehouseAndItemAndLocation(warehouseId, itemId, locationId);
        return success(BeanUtils.toBean(inventory, InventoryRespVO.class));
    }
    
    @GetMapping("/check-sufficient")
    @Operation(summary = "检查库存是否足够")
    @Parameter(name = "warehouseId", description = "仓库ID", required = true, example = "1001")
    @Parameter(name = "itemId", description = "物料ID", required = true, example = "2001")
    @Parameter(name = "locationId", description = "库位ID", required = true, example = "3001")
    @Parameter(name = "count", description = "数量", required = true, example = "100")
    @PreAuthorize("@ss.hasPermission('wms:inventory:query')")
    public CommonResult<Boolean> checkInventorySufficient(@RequestParam("warehouseId") Long warehouseId,
                                                   @RequestParam("itemId") Long itemId,
                                                   @RequestParam("locationId") Long locationId,
                                                   @RequestParam("count") Integer count) {
        boolean sufficient = inventoryService.isInventorySufficient(warehouseId, itemId, locationId, count);
        return success(sufficient);
    }
    
    @GetMapping("/get-total-available")
    @Operation(summary = "获取物料的总可用库存")
    @Parameter(name = "itemId", description = "物料ID", required = true, example = "2001")
    @PreAuthorize("@ss.hasPermission('wms:inventory:query')")
    public CommonResult<Integer> getTotalAvailableCount(@RequestParam("itemId") Long itemId) {
        Integer availableCount = inventoryService.getTotalAvailableCountByItemId(itemId);
        return success(availableCount);
    }
    
    @GetMapping("/get-warehouse-available")
    @Operation(summary = "获取物料在指定仓库的可用库存")
    @Parameter(name = "warehouseId", description = "仓库ID", required = true, example = "1001")
    @Parameter(name = "itemId", description = "物料ID", required = true, example = "2001")
    @PreAuthorize("@ss.hasPermission('wms:inventory:query')")
    public CommonResult<Integer> getWarehouseAvailableCount(@RequestParam("warehouseId") Long warehouseId,
                                                     @RequestParam("itemId") Long itemId) {
        Integer availableCount = inventoryService.getAvailableCountByWarehouseAndItem(warehouseId, itemId);
        return success(availableCount);
    }
    
    @PostMapping("/increase")
    @Operation(summary = "增加库存")
    @PreAuthorize("@ss.hasPermission('wms:inventory:update')")
    public CommonResult<InventoryRespVO> increaseInventory(@Valid @RequestBody InventoryOperationReqVO reqVO) {
        InventoryDO inventory = inventoryService.increaseInventory(
            reqVO.getWarehouseId(), reqVO.getItemId(), reqVO.getLocationId(), reqVO.getCount(),
            reqVO.getBusinessType(), reqVO.getBusinessId(), reqVO.getBusinessDetailId(), reqVO.getRemark()
        );
        return success(BeanUtils.toBean(inventory, InventoryRespVO.class));
    }
    
    @PostMapping("/decrease")
    @Operation(summary = "减少库存")
    @PreAuthorize("@ss.hasPermission('wms:inventory:update')")
    public CommonResult<InventoryRespVO> decreaseInventory(@Valid @RequestBody InventoryOperationReqVO reqVO) {
        InventoryDO inventory = inventoryService.decreaseInventory(
            reqVO.getWarehouseId(), reqVO.getItemId(), reqVO.getLocationId(), reqVO.getCount(),
            reqVO.getBusinessType(), reqVO.getBusinessId(), reqVO.getBusinessDetailId(), reqVO.getRemark()
        );
        return success(BeanUtils.toBean(inventory, InventoryRespVO.class));
    }
    
    @PostMapping("/lock")
    @Operation(summary = "锁定库存")
    @PreAuthorize("@ss.hasPermission('wms:inventory:update')")
    public CommonResult<Boolean> lockInventory(@Valid @RequestBody InventoryOperationReqVO reqVO) {
        inventoryService.lockInventory(
            reqVO.getWarehouseId(), reqVO.getItemId(), reqVO.getLocationId(), reqVO.getCount(),
            reqVO.getBusinessType(), reqVO.getBusinessId(), reqVO.getBusinessDetailId(), reqVO.getRemark()
        );
        return success(true);
    }
    
    @PostMapping("/unlock")
    @Operation(summary = "解锁库存")
    @PreAuthorize("@ss.hasPermission('wms:inventory:update')")
    public CommonResult<Boolean> unlockInventory(@Valid @RequestBody InventoryOperationReqVO reqVO) {
        inventoryService.unlockInventory(
            reqVO.getWarehouseId(), reqVO.getItemId(), reqVO.getLocationId(), reqVO.getCount(),
            reqVO.getBusinessType(), reqVO.getBusinessId(), reqVO.getBusinessDetailId(), reqVO.getRemark()
        );
        return success(true);
    }

}