package cn.smart.wms.module.wms.controller.admin.batch.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 批次信息新增/修改 Request VO")
@Data
public class BatchSaveReqVO {

    @Schema(description = "批次ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "30442")
    private Long id;

    @Schema(description = "批次号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "批次号不能为空")
    private String batchNo;

    @Schema(description = "物料ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "26918")
    @NotNull(message = "物料ID不能为空")
    private Long itemId;

    @Schema(description = "仓库ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "10449")
    @NotNull(message = "仓库ID不能为空")
    private Long warehouseId;

    @Schema(description = "供应商ID", example = "7591")
    private Long supplierId;

    @Schema(description = "生产日期")
    private LocalDate productionDate;

    @Schema(description = "过期日期")
    private LocalDate expiryDate;

    @Schema(description = "批次属性1")
    private String batchAttr1;

    @Schema(description = "批次属性2")
    private String batchAttr2;

    @Schema(description = "批次属性3")
    private String batchAttr3;

    @Schema(description = "批次属性4")
    private String batchAttr4;

    @Schema(description = "入库总数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "7693")
    @NotNull(message = "入库总数量不能为空")
    private Integer totalCount;

    @Schema(description = "可用数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "20352")
    @NotNull(message = "可用数量不能为空")
    private Integer availableCount;

    @Schema(description = "锁定数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "10915")
    @NotNull(message = "锁定数量不能为空")
    private Integer lockedCount;

    @Schema(description = "状态(0-正常 1-冻结 2-禁用)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "状态(0-正常 1-冻结 2-禁用)不能为空")
    private Integer status;

    @Schema(description = "备注", example = "随便")
    private String remark;

}