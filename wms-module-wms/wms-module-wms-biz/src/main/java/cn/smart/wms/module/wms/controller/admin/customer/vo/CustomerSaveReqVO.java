package cn.smart.wms.module.wms.controller.admin.customer.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 客户新增/修改 Request VO")
@Data
public class CustomerSaveReqVO {

    @Schema(description = "客户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "29593")
    private Long id;

    @Schema(description = "客户编码")
    private String customerCode;

    @Schema(description = "客户名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @NotEmpty(message = "客户名称不能为空")
    private String customerName;

    @Schema(description = "客户级别(0-普通客户 1-重要客户 2-VIP客户)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "客户级别(0-普通客户 1-重要客户 2-VIP客户)不能为空")
    private Integer customerLevel;

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

    @Schema(description = "开户行", example = "王五")
    private String bankName;

    @Schema(description = "银行账号", example = "27769")
    private String bankAccount;

    @Schema(description = "状态(0-正常 1-禁用)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "状态(0-正常 1-禁用)不能为空")
    private Integer status;

    @Schema(description = "备注", example = "你猜")
    private String remark;

}