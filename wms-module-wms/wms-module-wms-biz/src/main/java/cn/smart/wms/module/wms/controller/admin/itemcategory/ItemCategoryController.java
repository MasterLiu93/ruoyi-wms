package cn.smart.wms.module.wms.controller.admin.itemcategory;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.IOException;

import cn.smart.wms.framework.common.pojo.PageParam;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.pojo.CommonResult;
import cn.smart.wms.framework.common.util.object.BeanUtils;
import cn.smart.wms.framework.idgenerator.core.IdGeneratorFactory;
import static cn.smart.wms.framework.common.pojo.CommonResult.success;

import cn.smart.wms.framework.excel.core.util.ExcelUtils;

import cn.smart.wms.framework.apilog.core.annotation.ApiAccessLog;
import static cn.smart.wms.framework.apilog.core.enums.OperateTypeEnum.*;

import cn.smart.wms.module.wms.controller.admin.itemcategory.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.itemcategory.ItemCategoryDO;
import cn.smart.wms.module.wms.service.itemcategory.ItemCategoryService;

@Tag(name = "管理后台 - 物料分类")
@RestController
@RequestMapping("/wms/item-category")
@Validated
public class ItemCategoryController {

    @Resource
    private ItemCategoryService itemCategoryService;
    
    @Resource
    private IdGeneratorFactory idGeneratorFactory;

    @PostMapping("/create")
    @Operation(summary = "创建物料分类")
    @PreAuthorize("@ss.hasPermission('wms:item-category:create')")
    public CommonResult<Long> createItemCategory(@Valid @RequestBody ItemCategorySaveReqVO createReqVO) {
        // 自动生成物料分类编码，不再依赖前端传入
        if (createReqVO.getCategoryCode() == null || createReqVO.getCategoryCode().isEmpty()) {
            createReqVO.setCategoryCode(idGeneratorFactory.generateCustomCode("WLFL", 1));
        }
        return success(itemCategoryService.createItemCategory(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新物料分类")
    @PreAuthorize("@ss.hasPermission('wms:item-category:update')")
    public CommonResult<Boolean> updateItemCategory(@Valid @RequestBody ItemCategorySaveReqVO updateReqVO) {
        itemCategoryService.updateItemCategory(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除物料分类")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:item-category:delete')")
    public CommonResult<Boolean> deleteItemCategory(@RequestParam("id") Long id) {
        itemCategoryService.deleteItemCategory(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得物料分类")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:item-category:query')")
    public CommonResult<ItemCategoryRespVO> getItemCategory(@RequestParam("id") Long id) {
        ItemCategoryDO itemCategory = itemCategoryService.getItemCategory(id);
        return success(BeanUtils.toBean(itemCategory, ItemCategoryRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得物料分类分页")
    @PreAuthorize("@ss.hasPermission('wms:item-category:query')")
    public CommonResult<PageResult<ItemCategoryRespVO>> getItemCategoryPage(@Valid ItemCategoryPageReqVO pageReqVO) {
        PageResult<ItemCategoryDO> pageResult = itemCategoryService.getItemCategoryPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ItemCategoryRespVO.class));
    }

    @GetMapping("/tree")
    @Operation(summary = "获得物料分类树形结构")
    @PreAuthorize("@ss.hasPermission('wms:item-category:query')")
    public CommonResult<List<ItemCategoryTreeRespVO>> getItemCategoryTree(
            @RequestParam(value = "categoryName", required = false) String categoryName,
            @RequestParam(value = "categoryCode", required = false) String categoryCode,
            @RequestParam(value = "status", required = false) Integer status) {
        List<ItemCategoryTreeRespVO> tree = itemCategoryService.getItemCategoryTree(categoryName, categoryCode, status);
        return success(tree);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出物料分类 Excel")
    @PreAuthorize("@ss.hasPermission('wms:item-category:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportItemCategoryExcel(@Valid ItemCategoryPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ItemCategoryDO> list = itemCategoryService.getItemCategoryPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "物料分类.xls", "数据", ItemCategoryRespVO.class,
                        BeanUtils.toBean(list, ItemCategoryRespVO.class));
    }

}