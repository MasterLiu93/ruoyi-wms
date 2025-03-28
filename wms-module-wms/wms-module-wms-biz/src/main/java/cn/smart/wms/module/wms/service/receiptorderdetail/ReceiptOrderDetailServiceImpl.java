package cn.smart.wms.module.wms.service.receiptorderdetail;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.smart.wms.module.wms.controller.admin.receiptorderdetail.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.receiptorderdetail.ReceiptOrderDetailDO;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.pojo.PageParam;
import cn.smart.wms.framework.common.util.object.BeanUtils;

import cn.smart.wms.module.wms.dal.mysql.receiptorderdetail.ReceiptOrderDetailMapper;

import static cn.smart.wms.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.smart.wms.module.wms.enums.ErrorCodeConstants.*;

/**
 * 入库单明细 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ReceiptOrderDetailServiceImpl implements ReceiptOrderDetailService {

    @Resource
    private ReceiptOrderDetailMapper receiptOrderDetailMapper;

    @Override
    public Long createReceiptOrderDetail(ReceiptOrderDetailSaveReqVO createReqVO) {
        // 插入
        ReceiptOrderDetailDO receiptOrderDetail = BeanUtils.toBean(createReqVO, ReceiptOrderDetailDO.class);
        receiptOrderDetailMapper.insert(receiptOrderDetail);
        // 返回
        return receiptOrderDetail.getId();
    }

    @Override
    public void updateReceiptOrderDetail(ReceiptOrderDetailSaveReqVO updateReqVO) {
        // 校验存在
        validateReceiptOrderDetailExists(updateReqVO.getId());
        // 更新
        ReceiptOrderDetailDO updateObj = BeanUtils.toBean(updateReqVO, ReceiptOrderDetailDO.class);
        receiptOrderDetailMapper.updateById(updateObj);
    }

    @Override
    public void deleteReceiptOrderDetail(Long id) {
        // 校验存在
        validateReceiptOrderDetailExists(id);
        // 删除
        receiptOrderDetailMapper.deleteById(id);
    }

    private void validateReceiptOrderDetailExists(Long id) {
        if (receiptOrderDetailMapper.selectById(id) == null) {
            throw exception(RECEIPT_ORDER_DETAIL_NOT_EXISTS);
        }
    }

    @Override
    public ReceiptOrderDetailDO getReceiptOrderDetail(Long id) {
        return receiptOrderDetailMapper.selectById(id);
    }

    @Override
    public PageResult<ReceiptOrderDetailDO> getReceiptOrderDetailPage(ReceiptOrderDetailPageReqVO pageReqVO) {
        return receiptOrderDetailMapper.selectPage(pageReqVO);
    }

}