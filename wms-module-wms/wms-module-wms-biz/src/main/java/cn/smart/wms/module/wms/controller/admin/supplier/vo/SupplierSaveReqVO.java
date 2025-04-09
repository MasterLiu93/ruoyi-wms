package cn.smart.wms.module.wms.controller.admin.supplier.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.*;

@Schema(description = "管理后台 - 供应商新增/修改 Request VO")
@Data
public class SupplierSaveReqVO {

    @Schema(description = "供应商ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "14386")
    private Long id;

    @Schema(description = "供应商编码")
    private String supplierCode;

    @Schema(description = "供应商名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotEmpty(message = "供应商名称不能为空")
    private String supplierName;

    @Schema(description = "供应商级别(0-普通 1-重要 2-战略)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "供应商级别(0-普通 1-重要 2-战略)不能为空")
    private Integer supplierLevel;

    @Schema(description = "联系人", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "联系人不能为空")
    private String contact;

    @Schema(description = "联系电话", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "联系电话不能为空")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "开户行", example = "赵六")
    private String bankName;

    @Schema(description = "银行账号", example = "27149")
    private String bankAccount;

    @Schema(description = "状态(0-正常 1-禁用)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态(0-正常 1-禁用)不能为空")
    private Integer status;

    @Schema(description = "备注", example = "随便")
    private String remark;

}