package cn.smart.wms.module.wms.service.batchrecord;

import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.util.object.BeanUtils;
import cn.smart.wms.module.wms.controller.admin.batchrecord.vo.BatchRecordPageReqVO;
import cn.smart.wms.module.wms.controller.admin.batchrecord.vo.BatchRecordSaveReqVO;
import cn.smart.wms.module.wms.dal.dataobject.batchrecord.BatchRecordDO;
import cn.smart.wms.module.wms.dal.mysql.batchrecord.BatchRecordMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

import static cn.smart.wms.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.smart.wms.module.wms.enums.ErrorCodeConstants.BATCH_RECORD_NOT_EXISTS;

/**
 * 批次操作记录 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class BatchRecordServiceImpl implements BatchRecordService {

    @Resource
    private BatchRecordMapper batchRecordMapper;

    @Override
    public Long createBatchRecord(BatchRecordSaveReqVO createReqVO) {
        // 插入
        BatchRecordDO batchRecord = BeanUtils.toBean(createReqVO, BatchRecordDO.class);
        batchRecordMapper.insert(batchRecord);
        // 返回
        return batchRecord.getId();
    }

    @Override
    public void updateBatchRecord(BatchRecordSaveReqVO updateReqVO) {
        // 校验存在
        validateBatchRecordExists(updateReqVO.getId());
        // 更新
        BatchRecordDO updateObj = BeanUtils.toBean(updateReqVO, BatchRecordDO.class);
        batchRecordMapper.updateById(updateObj);
    }

    @Override
    public void deleteBatchRecord(Long id) {
        // 校验存在
        validateBatchRecordExists(id);
        // 删除
        batchRecordMapper.deleteById(id);
    }

    private void validateBatchRecordExists(Long id) {
        if (batchRecordMapper.selectById(id) == null) {
            throw exception(BATCH_RECORD_NOT_EXISTS);
        }
    }

    @Override
    public BatchRecordDO getBatchRecord(Long id) {
        return batchRecordMapper.selectById(id);
    }

    @Override
    public PageResult<BatchRecordDO> getBatchRecordPage(BatchRecordPageReqVO pageReqVO) {
        return batchRecordMapper.selectPage(pageReqVO);
    }

}