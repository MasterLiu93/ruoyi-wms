package cn.smart.wms.module.wms.controller.admin.batchinventory.vo;

import cn.smart.wms.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.smart.wms.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 批次库存分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BatchInventoryPageReqVO extends PageParam {

    @Schema(description = "批次编号")
    private Long batchId;

    @Schema(description = "物料编号")
    private Long itemId;

    @Schema(description = "仓库编号")
    private Long warehouseId;

    @Schema(description = "库位编号")
    private Long locationId;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

} 