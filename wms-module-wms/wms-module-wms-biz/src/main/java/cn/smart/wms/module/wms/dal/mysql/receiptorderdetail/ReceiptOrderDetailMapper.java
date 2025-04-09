package cn.smart.wms.module.wms.dal.mysql.receiptorderdetail;

import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.mybatis.core.mapper.BaseMapperX;
import cn.smart.wms.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.smart.wms.module.wms.controller.admin.receiptorderdetail.vo.ReceiptOrderDetailPageReqVO;
import cn.smart.wms.module.wms.dal.dataobject.receiptorderdetail.ReceiptOrderDetailDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 入库单明细 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ReceiptOrderDetailMapper extends BaseMapperX<ReceiptOrderDetailDO> {

    default PageResult<ReceiptOrderDetailDO> selectPage(ReceiptOrderDetailPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ReceiptOrderDetailDO>()
                .eqIfPresent(ReceiptOrderDetailDO::getReceiptOrderId, reqVO.getReceiptOrderId())
                .eqIfPresent(ReceiptOrderDetailDO::getItemId, reqVO.getItemId())
                .eqIfPresent(ReceiptOrderDetailDO::getPlanCount, reqVO.getPlanCount())
                .eqIfPresent(ReceiptOrderDetailDO::getRealCount, reqVO.getRealCount())
                .eqIfPresent(ReceiptOrderDetailDO::getLocationId, reqVO.getLocationId())
                .eqIfPresent(ReceiptOrderDetailDO::getBatchId, reqVO.getBatchId())
                .eqIfPresent(ReceiptOrderDetailDO::getPrice, reqVO.getPrice())
                .eqIfPresent(ReceiptOrderDetailDO::getQualityStatus, reqVO.getQualityStatus())
                .eqIfPresent(ReceiptOrderDetailDO::getStatus, reqVO.getStatus())
                .eqIfPresent(ReceiptOrderDetailDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(ReceiptOrderDetailDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ReceiptOrderDetailDO::getId));
    }

    default List<ReceiptOrderDetailDO> selectByReceiptOrderId(Long receiptOrderId) {
        return selectList(new LambdaQueryWrapperX<ReceiptOrderDetailDO>()
                .eq(ReceiptOrderDetailDO::getReceiptOrderId, receiptOrderId)
                .orderByAsc(ReceiptOrderDetailDO::getId));
    }
    
    default int deleteByReceiptOrderId(Long receiptOrderId) {
        return delete(new LambdaQueryWrapperX<ReceiptOrderDetailDO>()
                .eq(ReceiptOrderDetailDO::getReceiptOrderId, receiptOrderId));
    }

}