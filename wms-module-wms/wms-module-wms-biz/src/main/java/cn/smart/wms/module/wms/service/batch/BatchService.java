package cn.smart.wms.module.wms.service.batch;

import java.util.*;
import javax.validation.*;
import cn.smart.wms.module.wms.controller.admin.batch.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.batch.BatchDO;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.pojo.PageParam;

/**
 * 批次信息 Service 接口
 *
 * @author 芋道源码
 */
public interface BatchService {

    /**
     * 创建批次信息
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createBatch(@Valid BatchSaveReqVO createReqVO);

    /**
     * 更新批次信息
     *
     * @param updateReqVO 更新信息
     */
    void updateBatch(@Valid BatchSaveReqVO updateReqVO);

    /**
     * 删除批次信息
     *
     * @param id 编号
     */
    void deleteBatch(Long id);

    /**
     * 获得批次信息
     *
     * @param id 编号
     * @return 批次信息
     */
    BatchDO getBatch(Long id);

    /**
     * 获得批次信息分页
     *
     * @param pageReqVO 分页查询
     * @return 批次信息分页
     */
    PageResult<BatchDO> getBatchPage(BatchPageReqVO pageReqVO);

}