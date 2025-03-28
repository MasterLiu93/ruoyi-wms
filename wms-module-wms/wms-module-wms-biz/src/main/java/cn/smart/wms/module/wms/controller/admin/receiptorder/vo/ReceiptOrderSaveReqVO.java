package cn.smart.wms.module.wms.controller.admin.receiptorder.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 入库单新增/修改 Request VO")
@Data
public class ReceiptOrderSaveReqVO {

    @Schema(description = "入库单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "9636")
    private Long id;

    @Schema(description = "入库单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "入库单号不能为空")
    private String receiptOrderNo;

    @Schema(description = "入库类型(0-采购入库 1-生产入库 2-退货入库 3-调拨入库)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "入库类型(0-采购入库 1-生产入库 2-退货入库 3-调拨入库)不能为空")
    private Integer receiptType;

    @Schema(description = "供应商ID", example = "9921")
    private Long supplierId;

    @Schema(description = "仓库ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "24988")
    @NotNull(message = "仓库ID不能为空")
    private Long warehouseId;

    @Schema(description = "单据状态(0-草稿 1-待审核 2-审核通过 3-审核拒绝)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "单据状态(0-草稿 1-待审核 2-审核通过 3-审核拒绝)不能为空")
    private Integer orderStatus;

    @Schema(description = "入库状态(0-待入库 1-部分入库 2-已完成)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "入库状态(0-待入库 1-部分入库 2-已完成)不能为空")
    private Integer receiptStatus;

    @Schema(description = "预计到货时间")
    private LocalDateTime expectTime;

    @Schema(description = "实际到货时间")
    private LocalDateTime arrivalTime;

    @Schema(description = "商品数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "21888")
    @NotNull(message = "商品数量不能为空")
    private Integer totalCount;

    @Schema(description = "商品金额", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "商品金额不能为空")
    private BigDecimal totalAmount;

    @Schema(description = "备注", example = "你猜")
    private String remark;

}