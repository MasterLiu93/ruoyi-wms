package cn.smart.wms.module.wms.controller.admin.batchinventory.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 批次库存操作 Request VO，用于批次库存增减、锁定和解锁等操作
 */
@Schema(description = "管理后台 - 批次库存操作 Request VO")
@Data
public class BatchInventoryOperationReqVO {

    @Schema(description = "批次ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "20347")
    @NotNull(message = "批次ID不能为空")
    private Long batchId;

    @Schema(description = "物料ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "27051")
    @NotNull(message = "物料ID不能为空")
    private Long itemId;

    @Schema(description = "仓库ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2731")
    @NotNull(message = "仓库ID不能为空")
    private Long warehouseId;

    @Schema(description = "库位ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "16585")
    @NotNull(message = "库位ID不能为空")
    private Long locationId;

    @Schema(description = "操作数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    @NotNull(message = "操作数量不能为空")
    @Min(value = 1, message = "操作数量必须大于0")
    private Integer count;
    
    @Schema(description = "业务类型", example = "采购入库")
    private String businessType;
    
    @Schema(description = "业务单ID", example = "1001")
    private Long businessId;
    
    @Schema(description = "业务明细ID", example = "2001")
    private Long businessDetailId;

    @Schema(description = "备注", example = "手动增加批次库存")
    private String remark;
} 