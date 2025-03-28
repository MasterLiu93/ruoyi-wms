package cn.smart.wms.module.wms.controller.admin.inventorycheckdetail.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.smart.wms.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.smart.wms.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 库存盘点明细分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InventoryCheckDetailPageReqVO extends PageParam {

    @Schema(description = "盘点单ID", example = "23284")
    private Long checkId;

    @Schema(description = "物料ID", example = "6328")
    private Long itemId;

    @Schema(description = "库位ID", example = "14363")
    private Long locationId;

    @Schema(description = "账面数量", example = "27620")
    private Integer bookCount;

    @Schema(description = "盘点数量", example = "26450")
    private Integer checkCount;

    @Schema(description = "差异数量", example = "30553")
    private Integer differenceCount;

    @Schema(description = "盘点状态(0-未盘点 1-已盘点)", example = "2")
    private Integer checkStatus;

    @Schema(description = "备注", example = "你猜")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}