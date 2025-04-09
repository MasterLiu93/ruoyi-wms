package cn.smart.wms.module.wms.controller.admin.receiptorderdetail;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.List;
import cn.hutool.core.util.StrUtil;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.smart.wms.framework.common.pojo.PageParam;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.pojo.CommonResult;
import static cn.smart.wms.framework.common.pojo.CommonResult.success;

import cn.smart.wms.framework.excel.core.util.ExcelUtils;

import cn.smart.wms.module.wms.controller.admin.receiptorderdetail.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.receiptorderdetail.ReceiptOrderDetailDO;
import cn.smart.wms.module.wms.service.receiptorderdetail.ReceiptOrderDetailService;

import cn.smart.wms.framework.common.util.object.BeanUtils;

@Tag(name = "管理后台 - 入库单明细")
@RestController
@RequestMapping("/wms/receipt-order-detail")
@Validated
public class ReceiptOrderDetailController {

    private static final Logger log = LoggerFactory.getLogger(ReceiptOrderDetailController.class);

    @Resource
    private ReceiptOrderDetailService receiptOrderDetailService;

    @PostMapping("/create")
    @Operation(summary = "创建入库单明细")
    @PreAuthorize("@ss.hasPermission('wms:receipt-order-detail:create')")
    public CommonResult<Long> createReceiptOrderDetail(@Valid @RequestBody ReceiptOrderDetailSaveReqVO createReqVO) {
        return success(receiptOrderDetailService.createReceiptOrderDetail(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新入库单明细")
    @PreAuthorize("@ss.hasPermission('wms:receipt-order-detail:update')")
    public CommonResult<Boolean> updateReceiptOrderDetail(@Valid @RequestBody ReceiptOrderDetailSaveReqVO updateReqVO) {
        receiptOrderDetailService.updateReceiptOrderDetail(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除入库单明细")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:receipt-order-detail:delete')")
    public CommonResult<Boolean> deleteReceiptOrderDetail(@RequestParam("id") Long id) {
        receiptOrderDetailService.deleteReceiptOrderDetail(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得入库单明细")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:receipt-order-detail:query')")
    public CommonResult<ReceiptOrderDetailRespVO> getReceiptOrderDetail(@RequestParam("id") Long id) {
        ReceiptOrderDetailDO receiptOrderDetail = receiptOrderDetailService.getReceiptOrderDetail(id);
        return success(BeanUtils.toBean(receiptOrderDetail, ReceiptOrderDetailRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得入库单明细分页")
    @PreAuthorize("@ss.hasPermission('wms:receipt-order-detail:query')")
    public CommonResult<PageResult<ReceiptOrderDetailRespVO>> getReceiptOrderDetailPage(@Valid ReceiptOrderDetailPageReqVO pageReqVO) {
        PageResult<ReceiptOrderDetailDO> pageResult = receiptOrderDetailService.getReceiptOrderDetailPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ReceiptOrderDetailRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出入库单明细 Excel")
    @PreAuthorize("@ss.hasPermission('wms:receipt-order-detail:export')")
    public void exportReceiptOrderDetailExcel(@Valid ReceiptOrderDetailPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ReceiptOrderDetailDO> list = receiptOrderDetailService.getReceiptOrderDetailPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "入库单明细.xls", "数据", ReceiptOrderDetailRespVO.class,
                        BeanUtils.toBean(list, ReceiptOrderDetailRespVO.class));
    }
    

    @GetMapping("/export-enhanced-excel-template")
    @Operation(summary = "导出入库单明细增强版导入模板（带数据筛选功能）")
    @Parameter(name = "warehouseId", description = "仓库ID，可选，用于筛选特定仓库的货区、货架和库位")
    @PreAuthorize("@ss.hasPermission('wms:receipt-order-detail:import')")
    public void exportEnhancedReceiptOrderDetailImportTemplate(
            @RequestParam(value = "warehouseId", required = false) Long warehouseId,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        
        log.info("收到导出请求: {}", request.getRequestURL().toString() + (request.getQueryString() != null ? "?" + request.getQueryString() : ""));
        log.info("请求参数: warehouseId={}", warehouseId);
        
        try {
            // 设置响应头
            response.setContentType("application/vnd.ms-excel");
            String fileName = URLEncoder.encode("入库单明细导入模板", "UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xls");
            
            // 直接转发到service处理导出
            log.info("开始调用service层导出Excel，warehouseId={}", warehouseId);
            receiptOrderDetailService.exportEnhancedTemplate(warehouseId, response);
            
            log.info("Excel导出完成");
        } catch (Exception e) {
            log.error("导出Excel模板发生错误", e);
            throw new IOException("导出失败: " + e.getMessage(), e);
        }
    }
    
    @Operation(summary = "解析上传的Excel数据", description = "将文件内容以Base64格式传输")
    @PostMapping("/parse-excel-data")
    @PreAuthorize("@ss.hasPermission('wms:receipt-order-detail:import')")
    public CommonResult<List<ReceiptOrderDetailRespVO>> parseExcelData(@RequestBody Map<String, String> requestParams) {
        try {
            // 解码Base64文件数据
            String fileData = requestParams.get("fileData");
            if (StrUtil.isBlank(fileData)) {
                return CommonResult.error(400, "文件数据为空");
            }
            
            byte[] bytes = Base64.getDecoder().decode(fileData);
            log.info("[parseExcelData][文件大小: {} 字节]", bytes.length);
            
            // 使用ByteArrayInputStream将字节数组转换为输入流
            try (InputStream inputStream = new ByteArrayInputStream(bytes)) {
                // 调用服务方法解析Excel数据
                List<ReceiptOrderDetailDO> details = receiptOrderDetailService.importReceiptOrderDetails(inputStream, false);
                List<ReceiptOrderDetailRespVO> respList = BeanUtils.toBean(details, ReceiptOrderDetailRespVO.class);
                return success(respList);
            }
        } catch (Exception e) {
            log.error("[parseExcelData][解析Excel异常]", e);
            return CommonResult.error(500, "Excel解析失败: " + e.getMessage());
        }
    }

    /**
     * 开放版导出接口，无需认证
     */
    @GetMapping("/open/export-excel-template")
    @Operation(summary = "导出入库单明细增强版导入模板（公开版，不需要认证）")
    @Parameter(name = "warehouseId", description = "仓库ID，可选，用于筛选特定仓库的货区、货架和库位")
    public void exportOpenExcelTemplate(
            @RequestParam(value = "warehouseId", required = false) Long warehouseId,
            @RequestParam(value = "tenant-id", required = false) String tenantId,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        
        log.info("收到公开导出请求: {}", request.getRequestURL().toString() + (request.getQueryString() != null ? "?" + request.getQueryString() : ""));
        log.info("请求参数: warehouseId={}, tenant-id={}", warehouseId, tenantId);
        
        // 简化租户ID处理，直接使用硬编码值
        String fixedTenantId = "1";
        log.info("使用固定租户ID: {}", fixedTenantId);
        
        // 为请求设置固定的租户ID属性
        request.setAttribute("TENANT_ID_ATTR", fixedTenantId);
        
        try {
            // 设置响应头（纯二进制响应，不含charset）
            response.reset(); // 重置所有响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            
            // 设置下载文件名
            String fileName = "入库单明细导入模板_" + System.currentTimeMillis() + ".xlsx";
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name()).replaceAll("\\+", "%20");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");
            
            // 添加租户ID到响应头中，以便调试
            response.setHeader("X-Tenant-Id", fixedTenantId);
            
            // 设置其他通用响应头
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "0");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, tenant-id");
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition, X-Tenant-Id");
            
            log.info("开始调用service层导出Excel，warehouseId={}", warehouseId);
            
            // 调用service层导出Excel
            receiptOrderDetailService.exportEnhancedTemplate(warehouseId, response);
            
            log.info("Excel导出完成");
        } catch (Exception e) {
            log.error("公开导出Excel模板发生错误", e);
            
            try {
                // 重置响应
                response.reset();
                
                // 设置响应头
                response.setHeader("Content-Type", "text/plain;charset=utf-8");
                response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                response.setHeader("Pragma", "no-cache");
                response.setHeader("Expires", "0");
                response.setHeader("Access-Control-Allow-Origin", "*");
                
                // 写入错误消息
                response.getWriter().println("导出Excel模板失败: " + e.getMessage());
                log.error("已发送错误响应");
            } catch (Exception ex) {
                // 如果重置响应也失败，记录错误但不再尝试处理
                log.error("无法发送错误响应", ex);
            }
        }
    }

    @PostMapping("/save-batch")
    @Operation(summary = "批量保存入库单明细")
    @PreAuthorize("@ss.hasPermission('wms:receipt-order-detail:create')")
    public CommonResult<Boolean> saveReceiptOrderDetails(@RequestParam("receiptOrderId") Long receiptOrderId,
                                                   @RequestBody List<ReceiptOrderDetailDO> details) {
        receiptOrderDetailService.saveReceiptOrderDetails(receiptOrderId, details);
        return success(true);
    }

    @Operation(summary = "导入入库单明细 Excel")
    @PostMapping(value = "/import-excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("@ss.hasPermission('wms:receipt-order-detail:import')")
    public CommonResult<List<ReceiptOrderDetailRespVO>> importReceiptOrderDetail(@RequestParam("file") MultipartFile file) throws IOException {
        try {
            // 记录上传文件信息
            String filename = file.getOriginalFilename();
            long fileSize = file.getSize();
            log.info("[importReceiptOrderDetail][上传文件: {}, 大小: {} 字节]", filename, fileSize);
            
            // 调用服务接口解析Excel内容
            List<ReceiptOrderDetailDO> details = receiptOrderDetailService.importReceiptOrderDetails(file.getInputStream(), false);
            List<ReceiptOrderDetailRespVO> respList = BeanUtils.toBean(details, ReceiptOrderDetailRespVO.class);
            return success(respList);
        } catch (Exception e) {
            log.error("[importReceiptOrderDetail][Excel解析异常]", e);
            return CommonResult.error(500, "Excel解析失败: " + e.getMessage());
        }
    }

    @GetMapping("/list-by-order-id")
    @Operation(summary = "获取指定入库单ID的明细列表")
    @Parameter(name = "receiptOrderId", description = "入库单ID", required = true)
    @PreAuthorize("@ss.hasPermission('wms:receipt-order-detail:query')")
    public CommonResult<List<ReceiptOrderDetailRespVO>> getReceiptOrderDetailList(@RequestParam("receiptOrderId") Long receiptOrderId) {
        // 获取指定入库单的明细列表
        List<ReceiptOrderDetailDO> details = receiptOrderDetailService.getReceiptOrderDetailListByOrderId(receiptOrderId);
        // 转换为响应VO并返回
        return success(BeanUtils.toBean(details, ReceiptOrderDetailRespVO.class));
    }
}