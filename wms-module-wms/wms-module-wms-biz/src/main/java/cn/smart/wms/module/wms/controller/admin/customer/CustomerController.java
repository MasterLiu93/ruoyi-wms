package cn.smart.wms.module.wms.controller.admin.customer;

import cn.smart.wms.framework.apilog.core.annotation.ApiAccessLog;
import cn.smart.wms.framework.common.pojo.CommonResult;
import cn.smart.wms.framework.common.pojo.PageParam;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.util.object.BeanUtils;
import cn.smart.wms.framework.excel.core.util.ExcelUtils;
import cn.smart.wms.framework.idgenerator.core.IdGeneratorFactory;
import cn.smart.wms.module.wms.controller.admin.customer.vo.CustomerPageReqVO;
import cn.smart.wms.module.wms.controller.admin.customer.vo.CustomerRespVO;
import cn.smart.wms.module.wms.controller.admin.customer.vo.CustomerSaveReqVO;
import cn.smart.wms.module.wms.dal.dataobject.customer.CustomerDO;
import cn.smart.wms.module.wms.service.customer.CustomerService;
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

@Tag(name = "管理后台 - 客户")
@RestController
@RequestMapping("/wms/customer")
@Validated
public class CustomerController {

    @Resource
    private CustomerService customerService;
    
    @Resource
    private IdGeneratorFactory idGeneratorFactory;

    @PostMapping("/create")
    @Operation(summary = "创建客户")
    @PreAuthorize("@ss.hasPermission('wms:customer:create')")
    public CommonResult<Long> createCustomer(@Valid @RequestBody CustomerSaveReqVO createReqVO) {
        // 自动生成客户编码，不再依赖前端传入
        if (createReqVO.getCustomerCode() == null || createReqVO.getCustomerCode().isEmpty()) {
            createReqVO.setCustomerCode(idGeneratorFactory.generateCustomerCode());
        }
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
    public CommonResult<PageResult<CustomerRespVO>> getCustomerPage(@Valid CustomerPageReqVO pageVO) {
        PageResult<CustomerDO> pageResult = customerService.getCustomerPage(pageVO);
        return success(BeanUtils.toBean(pageResult, CustomerRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出客户 Excel")
    @PreAuthorize("@ss.hasPermission('wms:customer:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportCustomerExcel(@Valid CustomerPageReqVO pageVO,
              HttpServletResponse response) throws IOException {
        pageVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<CustomerDO> list = customerService.getCustomerPage(pageVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "客户数据.xls", "数据", CustomerRespVO.class,
                BeanUtils.toBean(list, CustomerRespVO.class));
    }

}