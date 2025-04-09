package cn.smart.wms.module.wms.service.receiptrecord;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Collectors;

import cn.smart.wms.module.wms.controller.admin.receiptrecord.vo.ReceiptRecordSaveReqVO;
import cn.smart.wms.module.wms.controller.admin.receiptrecord.vo.ReceiptRecordPageReqVO;
import cn.smart.wms.module.wms.dal.dataobject.receiptrecord.ReceiptRecordDO;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.util.object.BeanUtils;

import cn.smart.wms.module.wms.dal.mysql.receiptrecord.ReceiptRecordMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import static cn.smart.wms.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.smart.wms.module.wms.enums.ErrorCodeConstants.*;
import lombok.extern.slf4j.Slf4j;

/**
 * 入库操作记录 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
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
    
    @Override
    public List<Long> getDetailIdsWithRecords(Long receiptOrderId) {
        if (receiptOrderId == null) {
            log.warn("获取已有入库记录的明细ID列表失败: 入库单ID为空");
            return new ArrayList<>();
        }
        
        try {
            // 查询入库单对应的所有入库记录
            List<ReceiptRecordDO> records = receiptRecordMapper.selectList(
                new LambdaQueryWrapper<ReceiptRecordDO>()
                    .eq(ReceiptRecordDO::getReceiptOrderId, receiptOrderId)
            );
            
            if (records == null || records.isEmpty()) {
                log.info("入库单没有入库记录: 入库单ID={}", receiptOrderId);
                return new ArrayList<>();
            }
            
            // 提取明细ID列表（去重）
            List<Long> detailIds = records.stream()
                .map(ReceiptRecordDO::getReceiptOrderDetailId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
            
            log.info("获取已有入库记录的明细ID列表: 入库单ID={}, 明细ID数量={}, 明细IDs={}", 
                    receiptOrderId, detailIds.size(), detailIds);
            
            return detailIds;
        } catch (Exception e) {
            log.error("获取已有入库记录的明细ID列表异常: 入库单ID={}, 错误={}", receiptOrderId, e.getMessage(), e);
            return new ArrayList<>();
        }
    }

}