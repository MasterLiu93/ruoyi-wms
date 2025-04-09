package cn.smart.wms.module.wms.controller.admin.customer.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 客户 Response VO")
@Data
@ExcelIgnoreUnannotated
public class CustomerRespVO {

    @Schema(description = "客户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "29593")
    @ExcelProperty("客户ID")
    private Long id;

    @Schema(description = "客户编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("客户编码")
    private String customerCode;

    @Schema(description = "客户名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @ExcelProperty("客户名称")
    private String customerName;

    @Schema(description = "客户级别(0-普通客户 1-重要客户 2-VIP客户)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("客户级别(0-普通客户 1-重要客户 2-VIP客户)")
    private Integer customerLevel;

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

    @Schema(description = "开户行", example = "王五")
    @ExcelProperty("开户行")
    private String bankName;

    @Schema(description = "银行账号", example = "27769")
    @ExcelProperty("银行账号")
    private String bankAccount;

    @Schema(description = "状态(0-正常 1-禁用)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("状态(0-正常 1-禁用)")
    private Integer status;

    @Schema(description = "备注", example = "你猜")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}