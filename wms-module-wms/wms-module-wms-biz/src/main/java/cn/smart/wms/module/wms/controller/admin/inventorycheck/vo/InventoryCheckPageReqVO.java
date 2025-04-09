package cn.smart.wms.module.wms.controller.admin.inventorycheck.vo;

import cn.smart.wms.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.smart.wms.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 库存盘点单分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InventoryCheckPageReqVO extends PageParam {

    @Schema(description = "盘点单号")
    private String checkNo;

    @Schema(description = "仓库ID", example = "1135")
    private Long warehouseId;

    @Schema(description = "盘点类型(0-全部盘点 1-部分盘点)", example = "2")
    private Integer checkType;

    @Schema(description = "盘点状态(0-新建 1-盘点中 2-已完成)", example = "2")
    private Integer checkStatus;

    @Schema(description = "盘点总数", example = "13748")
    private Integer totalCount;

    @Schema(description = "已盘点数", example = "8727")
    private Integer checkedCount;

    @Schema(description = "差异数", example = "32025")
    private Integer differenceCount;

    @Schema(description = "备注", example = "你猜")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}