package cn.smart.wms.module.wms.dal.mysql.receiptorder;

import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.smart.wms.framework.mybatis.core.mapper.BaseMapperX;
import cn.smart.wms.module.wms.dal.dataobject.receiptorder.ReceiptOrderDO;
import org.apache.ibatis.annotations.Mapper;
import cn.smart.wms.module.wms.controller.admin.receiptorder.vo.*;

/**
 * 入库单 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ReceiptOrderMapper extends BaseMapperX<ReceiptOrderDO> {

    default PageResult<ReceiptOrderDO> selectPage(ReceiptOrderPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ReceiptOrderDO>()
                .likeIfPresent(ReceiptOrderDO::getReceiptOrderNo, reqVO.getReceiptOrderNo())
                .eqIfPresent(ReceiptOrderDO::getReceiptType, reqVO.getReceiptType())
                .eqIfPresent(ReceiptOrderDO::getSupplierId, reqVO.getSupplierId())
                .eqIfPresent(ReceiptOrderDO::getWarehouseId, reqVO.getWarehouseId())
                .eqIfPresent(ReceiptOrderDO::getOrderStatus, reqVO.getOrderStatus())
                .eqIfPresent(ReceiptOrderDO::getReceiptStatus, reqVO.getReceiptStatus())
                .betweenIfPresent(ReceiptOrderDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ReceiptOrderDO::getId));
    }
    
    default ReceiptOrderDO selectByReceiptOrderNo(String receiptOrderNo) {
        return selectOne(ReceiptOrderDO::getReceiptOrderNo, receiptOrderNo);
    }

}