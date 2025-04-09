package cn.smart.wms.module.wms.controller.admin.inventorymovement.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.smart.wms.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.smart.wms.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 库存移动记录分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InventoryMovementPageReqVO extends PageParam {

    @Schema(description = "移动类型(0-入库 1-出库 2-库存调整)", example = "2")
    private Integer movementType;

    @Schema(description = "移动单号")
    private String movementNo;

    @Schema(description = "仓库ID", example = "11381")
    private Long warehouseId;

    @Schema(description = "库位ID", example = "9215")
    private Long locationId;

    @Schema(description = "物料ID", example = "14832")
    private Long itemId;

    @Schema(description = "物料名称")
    private String itemName;

    @Schema(description = "物料编码")
    private String itemCode;

    @Schema(description = "移动数量", example = "2819")
    private Integer count;

    @Schema(description = "移动前数量", example = "19001")
    private Integer beforeCount;

    @Schema(description = "移动后数量", example = "4870")
    private Integer afterCount;

    @Schema(description = "业务类型", example = "1")
    private String businessType;

    @Schema(description = "业务单ID", example = "1117")
    private Long businessId;

    @Schema(description = "业务明细ID", example = "20164")
    private Long businessDetailId;

    @Schema(description = "备注", example = "随便")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}