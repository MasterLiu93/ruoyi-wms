package cn.smart.wms.module.wms.service.receiptrecord;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.smart.wms.module.wms.controller.admin.receiptrecord.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.receiptrecord.ReceiptRecordDO;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.pojo.PageParam;
import cn.smart.wms.framework.common.util.object.BeanUtils;

import cn.smart.wms.module.wms.dal.mysql.receiptrecord.ReceiptRecordMapper;

import static cn.smart.wms.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.smart.wms.module.wms.enums.ErrorCodeConstants.*;

/**
 * 入库操作记录 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ReceiptRecordServiceImpl implements ReceiptRecordService {

    @Resource
    private ReceiptRecordMapper receiptRecordMapper;

    @Override
    public Long createReceiptRecord(ReceiptRecordSaveReqVO createReqVO) {
        // 插入
        ReceiptRecordDO receiptRecord = BeanUtils.toBean(createReqVO, ReceiptRecordDO.class);
        receiptRecordMapper.insert(receiptRecord);
        // 返回
        return receiptRecord.getId();
    }

    @Override
    public void updateReceiptRecord(ReceiptRecordSaveReqVO updateReqVO) {
        // 校验存在
        validateReceiptRecordExists(updateReqVO.getId());
        // 更新
        ReceiptRecordDO updateObj = BeanUtils.toBean(updateReqVO, ReceiptRecordDO.class);
        receiptRecordMapper.updateById(updateObj);
    }

    @Override
    public void deleteReceiptRecord(Long id) {
        // 校验存在
        validateReceiptRecordExists(id);
        // 删除
        receiptRecordMapper.deleteById(id);
    }

    private void validateReceiptRecordExists(Long id) {
        if (receiptRecordMapper.selectById(id) == null) {
            throw exception(RECEIPT_RECORD_NOT_EXISTS);
        }
    }

    @Override
    public ReceiptRecordDO getReceiptRecord(Long id) {
        return receiptRecordMapper.selectById(id);
    }

    @Override
    public PageResult<ReceiptRecordDO> getReceiptRecordPage(ReceiptRecordPageReqVO pageReqVO) {
        return receiptRecordMapper.selectPage(pageReqVO);
    }

}