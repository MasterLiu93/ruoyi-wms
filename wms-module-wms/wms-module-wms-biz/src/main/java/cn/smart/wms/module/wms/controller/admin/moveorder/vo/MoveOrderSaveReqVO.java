package cn.smart.wms.module.wms.controller.admin.moveorder.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 移库单新增/修改 Request VO")
@Data
public class MoveOrderSaveReqVO {

    @Schema(description = "移库单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "30146")
    private Long id;

    @Schema(description = "移库单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "移库单号不能为空")
    private String moveOrderNo;

    @Schema(description = "移库类型(0-仓库间移库 1-库区间移库 2-库位间移库)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "移库类型(0-仓库间移库 1-库区间移库 2-库位间移库)不能为空")
    private Integer moveType;

    @Schema(description = "源仓库ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "12260")
    @NotNull(message = "源仓库ID不能为空")
    private Long fromWarehouseId;

    @Schema(description = "目标仓库ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "9332")
    @NotNull(message = "目标仓库ID不能为空")
    private Long toWarehouseId;

    @Schema(description = "源库位ID", example = "18298")
    private Long fromLocationId;

    @Schema(description = "目标库位ID", example = "10638")
    private Long toLocationId;

    @Schema(description = "单据状态(0-草稿 1-待审核 2-审核通过 3-审核拒绝)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "单据状态(0-草稿 1-待审核 2-审核通过 3-审核拒绝)不能为空")
    private Integer orderStatus;

    @Schema(description = "移库状态(0-待移库 1-部分移库 2-已完成)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "移库状态(0-待移库 1-部分移库 2-已完成)不能为空")
    private Integer moveStatus;

    @Schema(description = "商品数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "2244")
    @NotNull(message = "商品数量不能为空")
    private Integer totalCount;

    @Schema(description = "备注", example = "随便")
    private String remark;

}