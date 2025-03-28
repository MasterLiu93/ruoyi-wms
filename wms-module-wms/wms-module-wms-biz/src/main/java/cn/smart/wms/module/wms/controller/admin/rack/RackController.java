package cn.smart.wms.module.wms.controller.admin.rack;

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

import cn.smart.wms.module.wms.controller.admin.rack.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.rack.RackDO;
import cn.smart.wms.module.wms.service.rack.RackService;

@Tag(name = "管理后台 - 货架")
@RestController
@RequestMapping("/wms/rack")
@Validated
public class RackController {

    @Resource
    private RackService rackService;

    @PostMapping("/create")
    @Operation(summary = "创建货架")
    @PreAuthorize("@ss.hasPermission('wms:rack:create')")
    public CommonResult<Long> createRack(@Valid @RequestBody RackSaveReqVO createReqVO) {
        return success(rackService.createRack(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新货架")
    @PreAuthorize("@ss.hasPermission('wms:rack:update')")
    public CommonResult<Boolean> updateRack(@Valid @RequestBody RackSaveReqVO updateReqVO) {
        rackService.updateRack(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除货架")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:rack:delete')")
    public CommonResult<Boolean> deleteRack(@RequestParam("id") Long id) {
        rackService.deleteRack(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得货架")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:rack:query')")
    public CommonResult<RackRespVO> getRack(@RequestParam("id") Long id) {
        RackDO rack = rackService.getRack(id);
        return success(BeanUtils.toBean(rack, RackRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得货架分页")
    @PreAuthorize("@ss.hasPermission('wms:rack:query')")
    public CommonResult<PageResult<RackRespVO>> getRackPage(@Valid RackPageReqVO pageReqVO) {
        PageResult<RackDO> pageResult = rackService.getRackPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, RackRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出货架 Excel")
    @PreAuthorize("@ss.hasPermission('wms:rack:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportRackExcel(@Valid RackPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<RackDO> list = rackService.getRackPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "货架.xls", "数据", RackRespVO.class,
                        BeanUtils.toBean(list, RackRespVO.class));
    }

}