package cn.smart.wms.module.wms.controller.admin.item;

import cn.smart.wms.framework.apilog.core.annotation.ApiAccessLog;
import cn.smart.wms.framework.common.pojo.CommonResult;
import cn.smart.wms.framework.common.pojo.PageParam;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.util.object.BeanUtils;
import cn.smart.wms.framework.excel.core.util.ExcelUtils;
import cn.smart.wms.framework.idgenerator.core.IdGeneratorFactory;
import cn.smart.wms.module.wms.controller.admin.item.vo.ItemPageReqVO;
import cn.smart.wms.module.wms.controller.admin.item.vo.ItemRespVO;
import cn.smart.wms.module.wms.controller.admin.item.vo.ItemSaveReqVO;
import cn.smart.wms.module.wms.dal.dataobject.item.ItemDO;
import cn.smart.wms.module.wms.service.item.ItemService;
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
import java.util.List;

import static cn.smart.wms.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.smart.wms.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 物料")
@RestController
@RequestMapping("/wms/item")
@Validated
public class ItemController {

    @Resource
    private ItemService itemService;
    
    @Resource
    private IdGeneratorFactory idGeneratorFactory;

    @PostMapping("/create")
    @Operation(summary = "创建物料")
    @PreAuthorize("@ss.hasPermission('wms:item:create')")
    public CommonResult<Long> createItem(@Valid @RequestBody ItemSaveReqVO createReqVO) {
        // 自动生成物料编码，不再依赖前端传入
        if (createReqVO.getItemCode() == null || createReqVO.getItemCode().isEmpty()) {
            createReqVO.setItemCode(idGeneratorFactory.generateItemCode());
        }
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
    public CommonResult<PageResult<ItemRespVO>> getItemPage(@Valid ItemPageReqVO pageVO) {
        PageResult<ItemDO> pageResult = itemService.getItemPage(pageVO);
        return success(BeanUtils.toBean(pageResult, ItemRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出物料 Excel")
    @PreAuthorize("@ss.hasPermission('wms:item:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportItemExcel(@Valid ItemPageReqVO pageVO,
              HttpServletResponse response) throws IOException {
        pageVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ItemDO> list = itemService.getItemPage(pageVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "物料数据.xls", "数据", ItemRespVO.class,
                        BeanUtils.toBean(list, ItemRespVO.class));
    }

}