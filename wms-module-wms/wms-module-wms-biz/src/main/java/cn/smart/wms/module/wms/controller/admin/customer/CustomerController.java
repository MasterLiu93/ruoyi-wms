package cn.smart.wms.module.wms.controller.admin.customer;

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

import cn.smart.wms.module.wms.controller.admin.customer.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.customer.CustomerDO;
import cn.smart.wms.module.wms.service.customer.CustomerService;

@Tag(name = "管理后台 - 客户")
@RestController
@RequestMapping("/wms/customer")
@Validated
public class CustomerController {

    @Resource
    private CustomerService customerService;

    @PostMapping("/create")
    @Operation(summary = "创建客户")
    @PreAuthorize("@ss.hasPermission('wms:customer:create')")
    public CommonResult<Long> createCustomer(@Valid @RequestBody CustomerSaveReqVO createReqVO) {
        return success(customerService.createCustomer(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新客户")
    @PreAuthorize("@ss.hasPermission('wms:customer:update')")
    public CommonResult<Boolean> updateCustomer(@Valid @RequestBody CustomerSaveReqVO updateReqVO) {
        customerService.updateCustomer(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除客户")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:customer:delete')")
    public CommonResult<Boolean> deleteCustomer(@RequestParam("id") Long id) {
        customerService.deleteCustomer(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得客户")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:customer:query')")
    public CommonResult<CustomerRespVO> getCustomer(@RequestParam("id") Long id) {
        CustomerDO customer = customerService.getCustomer(id);
        return success(BeanUtils.toBean(customer, CustomerRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得客户分页")
    @PreAuthorize("@ss.hasPermission('wms:customer:query')")
    public CommonResult<PageResult<CustomerRespVO>> getCustomerPage(@Valid CustomerPageReqVO pageReqVO) {
        PageResult<CustomerDO> pageResult = customerService.getCustomerPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, CustomerRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出客户 Excel")
    @PreAuthorize("@ss.hasPermission('wms:customer:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportCustomerExcel(@Valid CustomerPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<CustomerDO> list = customerService.getCustomerPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "客户.xls", "数据", CustomerRespVO.class,
                        BeanUtils.toBean(list, CustomerRespVO.class));
    }

}