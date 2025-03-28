package cn.smart.wms.module.wms.controller.admin.item;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.constraints.*;
import javax.validation.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.IOException;

import cn.smart.wms.framework.common.pojo.PageParam;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.pojo.CommonResult;
import cn.smart.wms.framework.common.util.object.BeanUtils;
import static cn.smart.wms.framework.common.pojo.CommonResult.success;

import cn.smart.wms.framework.excel.core.util.ExcelUtils;

import cn.smart.wms.framework.apilog.core.annotation.ApiAccessLog;
import static cn.smart.wms.framework.apilog.core.enums.OperateTypeEnum.*;

import cn.smart.wms.module.wms.controller.admin.item.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.item.ItemDO;
import cn.smart.wms.module.wms.service.item.ItemService;

@Tag(name = "管理后台 - 物料")
@RestController
@RequestMapping("/wms/item")
@Validated
public class ItemController {

    @Resource
    private ItemService itemService;

    @PostMapping("/create")
    @Operation(summary = "创建物料")
    @PreAuthorize("@ss.hasPermission('wms:item:create')")
    public CommonResult<Long> createItem(@Valid @RequestBody ItemSaveReqVO createReqVO) {
        return success(itemService.createItem(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新物料")
    @PreAuthorize("@ss.hasPermission('wms:item:update')")
    public CommonResult<Boolean> updateItem(@Valid @RequestBody ItemSaveReqVO updateReqVO) {
        itemService.updateItem(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除物料")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:item:delete')")
    public CommonResult<Boolean> deleteItem(@RequestParam("id") Long id) {
        itemService.deleteItem(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得物料")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:item:query')")
    public CommonResult<ItemRespVO> getItem(@RequestParam("id") Long id) {
        ItemDO item = itemService.getItem(id);
        return success(BeanUtils.toBean(item, ItemRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得物料分页")
    @PreAuthorize("@ss.hasPermission('wms:item:query')")
    public CommonResult<PageResult<ItemRespVO>> getItemPage(@Valid ItemPageReqVO pageReqVO) {
        PageResult<ItemDO> pageResult = itemService.getItemPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ItemRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出物料 Excel")
    @PreAuthorize("@ss.hasPermission('wms:item:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportItemExcel(@Valid ItemPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ItemDO> list = itemService.getItemPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "物料.xls", "数据", ItemRespVO.class,
                        BeanUtils.toBean(list, ItemRespVO.class));
    }

}