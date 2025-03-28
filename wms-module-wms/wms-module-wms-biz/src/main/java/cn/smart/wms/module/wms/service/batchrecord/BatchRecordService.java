package cn.smart.wms.module.wms.service.batchrecord;

import java.util.*;
import javax.validation.*;
import cn.smart.wms.module.wms.controller.admin.batchrecord.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.batchrecord.BatchRecordDO;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.pojo.PageParam;

/**
 * 批次操作记录 Service 接口
 *
 * @author 芋道源码
 */
public interface BatchRecordService {

    /**
     * 创建批次操作记录
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createBatchRecord(@Valid BatchRecordSaveReqVO createReqVO);

    /**
     * 更新批次操作记录
     *
     * @param updateReqVO 更新信息
     */
    void updateBatchRecord(@Valid BatchRecordSaveReqVO updateReqVO);

    /**
     * 删除批次操作记录
     *
     * @param id 编号
     */
    void deleteBatchRecord(Long id);

    /**
     * 获得批次操作记录
     *
     * @param id 编号
     * @return 批次操作记录
     */
    BatchRecordDO getBatchRecord(Long id);

    /**
     * 获得批次操作记录分页
     *
     * @param pageReqVO 分页查询
     * @return 批次操作记录分页
     */
    PageResult<BatchRecordDO> getBatchRecordPage(BatchRecordPageReqVO pageReqVO);

}