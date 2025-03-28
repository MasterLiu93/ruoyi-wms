package cn.smart.wms.module.wms.controller.admin.receiptrecord.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.smart.wms.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.smart.wms.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 入库操作记录分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ReceiptRecordPageReqVO extends PageParam {

    @Schema(description = "入库单ID", example = "14146")
    private Long receiptOrderId;

    @Schema(description = "入库单明细ID", example = "9171")
    private Long receiptOrderDetailId;

    @Schema(description = "物料ID", example = "561")
    private Long itemId;

    @Schema(description = "入库库位ID", example = "19637")
    private Long locationId;

    @Schema(description = "批次ID", example = "4382")
    private Long batchId;

    @Schema(description = "入库数量", example = "49")
    private Integer count;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}