package cn.smart.wms.module.wms.controller.admin.supplier.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 供应商 Response VO")
@Data
@ExcelIgnoreUnannotated
public class SupplierRespVO {

    @Schema(description = "供应商ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "14386")
    @ExcelProperty("供应商ID")
    private Long id;

    @Schema(description = "供应商编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("供应商编码")
    private String supplierCode;

    @Schema(description = "供应商名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @ExcelProperty("供应商名称")
    private String supplierName;

    @Schema(description = "供应商级别(0-普通 1-重要 2-战略)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("供应商级别(0-普通 1-重要 2-战略)")
    private Integer supplierLevel;

    @Schema(description = "联系人", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("联系人")
    private String contact;

    @Schema(description = "联系电话", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("联系电话")
    private String phone;

    @Schema(description = "邮箱")
    @ExcelProperty("邮箱")
    private String email;

    @Schema(description = "地址")
    @ExcelProperty("地址")
    private String address;

    @Schema(description = "开户行", example = "赵六")
    @ExcelProperty("开户行")
    private String bankName;

    @Schema(description = "银行账号", example = "27149")
    @ExcelProperty("银行账号")
    private String bankAccount;

    @Schema(description = "状态(0-正常 1-禁用)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("状态(0-正常 1-禁用)")
    private Integer status;

    @Schema(description = "备注", example = "随便")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}