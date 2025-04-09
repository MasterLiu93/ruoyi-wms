package cn.smart.wms.module.wms.service.batch;

import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.module.wms.controller.admin.batch.vo.BatchPageReqVO;
import cn.smart.wms.module.wms.controller.admin.batch.vo.BatchSaveReqVO;
import cn.smart.wms.module.wms.dal.dataobject.batch.BatchDO;

import javax.validation.Valid;

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
    
    /**
     * 根据批次号获取批次信息
     *
     * @param batchNo 批次号
     * @return 批次信息
     */
    BatchDO getBatchByBatchNo(String batchNo);
    
    /**
     * 检查批次是否过期
     *
     * @param batchId 批次ID
     * @return 是否过期
     */
    boolean isBatchExpired(Long batchId);
    
    /**
     * 冻结批次
     *
     * @param id 批次ID
     * @param remark 备注
     */
    void freezeBatch(Long id, String remark);
    
    /**
     * 解冻批次
     *
     * @param id 批次ID
     * @param remark 备注
     */
    void unfreezeBatch(Long id, String remark);
}