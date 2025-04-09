package cn.smart.wms.module.wms.service.batch;

import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.util.object.BeanUtils;
import cn.smart.wms.module.wms.controller.admin.batch.vo.BatchPageReqVO;
import cn.smart.wms.module.wms.controller.admin.batch.vo.BatchSaveReqVO;
import cn.smart.wms.module.wms.dal.dataobject.batch.BatchDO;
import cn.smart.wms.module.wms.dal.mysql.batch.BatchMapper;
import cn.smart.wms.module.wms.enums.batch.BatchStatusEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.LocalDate;

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
        // 校验批次号是否重复
        validateBatchNoUnique(createReqVO.getBatchNo(), null);
        
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
        // 校验批次号是否重复
        validateBatchNoUnique(updateReqVO.getBatchNo(), updateReqVO.getId());
        
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
    
    /**
     * 校验批次号是否唯一
     * 
     * @param batchNo 批次号
     * @param id 批次ID
     */
    private void validateBatchNoUnique(String batchNo, Long id) {
        BatchDO batch = batchMapper.selectByBatchNo(batchNo);
        if (batch == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的批次
        if (id == null) {
            throw exception(BATCH_CODE_EXISTS);
        }
        if (!batch.getId().equals(id)) {
            throw exception(BATCH_CODE_EXISTS);
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
    
    @Override
    public BatchDO getBatchByBatchNo(String batchNo) {
        return batchMapper.selectByBatchNo(batchNo);
    }
    
    @Override
    public boolean isBatchExpired(Long batchId) {
        BatchDO batch = getBatch(batchId);
        if (batch == null) {
            throw exception(BATCH_NOT_EXISTS);
        }
        
        // 如果没有设置过期日期，则认为不会过期
        if (batch.getExpiryDate() == null) {
            return false;
        }
        
        // 判断当前日期是否超过过期日期
        return LocalDate.now().isAfter(batch.getExpiryDate());
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void freezeBatch(Long id, String remark) {
        // 校验存在
        BatchDO batch = getBatch(id);
        if (batch == null) {
            throw exception(BATCH_NOT_EXISTS);
        }
        
        // 校验状态
        if (BatchStatusEnum.FROZEN.getStatus().equals(batch.getStatus())) {
            throw exception(BATCH_STATUS_ERROR, "批次已被冻结");
        }
        
        // 更新状态为冻结
        batch.setStatus(BatchStatusEnum.FROZEN.getStatus());
        batch.setRemark(remark);
        batchMapper.updateById(batch);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unfreezeBatch(Long id, String remark) {
        // 校验存在
        BatchDO batch = getBatch(id);
        if (batch == null) {
            throw exception(BATCH_NOT_EXISTS);
        }
        
        // 校验状态
        if (!BatchStatusEnum.FROZEN.getStatus().equals(batch.getStatus())) {
            throw exception(BATCH_STATUS_ERROR, "批次未被冻结");
        }
        
        // 更新状态为正常
        batch.setStatus(BatchStatusEnum.NORMAL.getStatus());
        batch.setRemark(remark);
        batchMapper.updateById(batch);
    }
}