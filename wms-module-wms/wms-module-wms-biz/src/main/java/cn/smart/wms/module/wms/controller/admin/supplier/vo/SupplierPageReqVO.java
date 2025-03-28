package cn.smart.wms.module.wms.controller.admin.supplier.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.smart.wms.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.smart.wms.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 供应商分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SupplierPageReqVO extends PageParam {

    @Schema(description = "供应商编码")
    private String supplierCode;

    @Schema(description = "供应商名称", example = "张三")
    private String supplierName;

    @Schema(description = "供应商级别(0-普通 1-重要 2-战略)")
    private Integer supplierLevel;

    @Schema(description = "联系人")
    private String contact;

    @Schema(description = "联系电话")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "开户行", example = "赵六")
    private String bankName;

    @Schema(description = "银行账号", example = "27149")
    private String bankAccount;

    @Schema(description = "状态(0-正常 1-禁用)", example = "1")
    private Integer status;

    @Schema(description = "备注", example = "随便")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}