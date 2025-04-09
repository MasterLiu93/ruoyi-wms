package cn.smart.wms.module.wms.controller.admin.supplier;

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
import static cn.smart.wms.framework.common.pojo.CommonResult.success;

import cn.smart.wms.framework.excel.core.util.ExcelUtils;

import cn.smart.wms.framework.apilog.core.annotation.ApiAccessLog;
import static cn.smart.wms.framework.apilog.core.enums.OperateTypeEnum.*;

import cn.smart.wms.module.wms.controller.admin.supplier.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.supplier.SupplierDO;
import cn.smart.wms.module.wms.service.supplier.SupplierService;
import cn.smart.wms.framework.idgenerator.core.IdGeneratorFactory;

@Tag(name = "管理后台 - 供应商")
@RestController
@RequestMapping("/wms/supplier")
@Validated
public class SupplierController {

    @Resource
    private SupplierService supplierService;
    
    @Resource
    private IdGeneratorFactory idGeneratorFactory;

    @PostMapping("/create")
    @Operation(summary = "创建供应商")
    @PreAuthorize("@ss.hasPermission('wms:supplier:create')")
    public CommonResult<Long> createSupplier(@Valid @RequestBody SupplierSaveReqVO createReqVO) {
        // 自动生成供应商编码，不再依赖前端传入
        if (createReqVO.getSupplierCode() == null || createReqVO.getSupplierCode().isEmpty()) {
            createReqVO.setSupplierCode(idGeneratorFactory.generateSupplierCode());
        }
        return success(supplierService.createSupplier(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新供应商")
    @PreAuthorize("@ss.hasPermission('wms:supplier:update')")
    public CommonResult<Boolean> updateSupplier(@Valid @RequestBody SupplierSaveReqVO updateReqVO) {
        supplierService.updateSupplier(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除供应商")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:supplier:delete')")
    public CommonResult<Boolean> deleteSupplier(@RequestParam("id") Long id) {
        supplierService.deleteSupplier(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得供应商")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:supplier:query')")
    public CommonResult<SupplierRespVO> getSupplier(@RequestParam("id") Long id) {
        SupplierDO supplier = supplierService.getSupplier(id);
        return success(BeanUtils.toBean(supplier, SupplierRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得供应商分页")
    @PreAuthorize("@ss.hasPermission('wms:supplier:query')")
    public CommonResult<PageResult<SupplierRespVO>> getSupplierPage(@Valid SupplierPageReqVO pageVO) {
        PageResult<SupplierDO> pageResult = supplierService.getSupplierPage(pageVO);
        return success(BeanUtils.toBean(pageResult, SupplierRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出供应商 Excel")
    @PreAuthorize("@ss.hasPermission('wms:supplier:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportSupplierExcel(@Valid SupplierPageReqVO pageVO,
              HttpServletResponse response) throws IOException {
        pageVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<SupplierDO> list = supplierService.getSupplierPage(pageVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "供应商数据.xls", "数据", SupplierRespVO.class,
                BeanUtils.toBean(list, SupplierRespVO.class));
    }
    
    @GetMapping("/simple-list")
    @Operation(summary = "获得简单供应商列表")
    @PreAuthorize("@ss.hasPermission('wms:supplier:query')")
    public CommonResult<List<SupplierSimpleRespVO>> getSimpleSupplierList() {
        // 获取所有启用状态的供应商
        SupplierPageReqVO reqVO = new SupplierPageReqVO();
        reqVO.setStatus(0); // 获取状态为正常的供应商
        reqVO.setPageSize(PageParam.PAGE_SIZE_NONE); // 不分页
        List<SupplierDO> list = supplierService.getSupplierPage(reqVO).getList();
        return success(BeanUtils.toBean(list, SupplierSimpleRespVO.class));
    }

}