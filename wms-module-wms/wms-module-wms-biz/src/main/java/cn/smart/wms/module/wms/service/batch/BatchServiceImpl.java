package cn.smart.wms.module.wms.service.batch;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.smart.wms.module.wms.controller.admin.batch.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.batch.BatchDO;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.pojo.PageParam;
import cn.smart.wms.framework.common.util.object.BeanUtils;

import cn.smart.wms.module.wms.dal.mysql.batch.BatchMapper;

import static cn.smart.wms.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.smart.wms.module.wms.enums.ErrorCodeConstants.*;

/**
 * 批次信息 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class BatchServiceImpl implements BatchService {

    @Resource
    private BatchMapper batchMapper;

    @Override
    public Long createBatch(BatchSaveReqVO createReqVO) {
        // 插入
        BatchDO batch = BeanUtils.toBean(createReqVO, BatchDO.class);
        batchMapper.insert(batch);
        // 返回
        return batch.getId();
    }

    @Override
    public void updateBatch(BatchSaveReqVO updateReqVO) {
        // 校验存在
        validateBatchExists(updateReqVO.getId());
        // 更新
        BatchDO updateObj = BeanUtils.toBean(updateReqVO, BatchDO.class);
        batchMapper.updateById(updateObj);
    }

    @Override
    public void deleteBatch(Long id) {
        // 校验存在
        validateBatchExists(id);
        // 删除
        batchMapper.deleteById(id);
    }

    private void validateBatchExists(Long id) {
        if (batchMapper.selectById(id) == null) {
            throw exception(BATCH_NOT_EXISTS);
        }
    }

    @Override
    public BatchDO getBatch(Long id) {
        return batchMapper.selectById(id);
    }

    @Override
    public PageResult<BatchDO> getBatchPage(BatchPageReqVO pageReqVO) {
        return batchMapper.selectPage(pageReqVO);
    }

}