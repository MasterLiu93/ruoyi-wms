package cn.smart.wms.module.wms.controller.admin.warehouse.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 仓库新增/修改 Request VO")
@Data
public class WarehouseSaveReqVO {

    @Schema(description = "仓库ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "7817")
    private Long id;

    @Schema(description = "仓库编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "仓库编码不能为空")
    private String warehouseCode;

    @Schema(description = "仓库名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @NotEmpty(message = "仓库名称不能为空")
    private String warehouseName;

    @Schema(description = "仓库类型(0-原材料 1-半成品 2-成品 3-退货)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "仓库类型(0-原材料 1-半成品 2-成品 3-退货)不能为空")
    private Integer warehouseType;

    @Schema(description = "面积(平方米)")
    private BigDecimal area;

    @Schema(description = "地址", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "地址不能为空")
    private String address;

    @Schema(description = "负责人", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "负责人不能为空")
    private String chargePerson;

    @Schema(description = "联系电话", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "联系电话不能为空")
    private String phone;

    @Schema(description = "状态(0-正常 1-禁用)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "状态(0-正常 1-禁用)不能为空")
    private Integer status;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

}