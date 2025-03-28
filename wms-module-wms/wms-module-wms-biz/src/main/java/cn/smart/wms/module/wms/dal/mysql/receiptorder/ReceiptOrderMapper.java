package cn.smart.wms.module.wms.dal.mysql.receiptorder;

import java.util.*;

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
                .eqIfPresent(ReceiptOrderDO::getReceiptOrderNo, reqVO.getReceiptOrderNo())
                .eqIfPresent(ReceiptOrderDO::getReceiptType, reqVO.getReceiptType())
                .eqIfPresent(ReceiptOrderDO::getSupplierId, reqVO.getSupplierId())
                .eqIfPresent(ReceiptOrderDO::getWarehouseId, reqVO.getWarehouseId())
                .eqIfPresent(ReceiptOrderDO::getOrderStatus, reqVO.getOrderStatus())
                .eqIfPresent(ReceiptOrderDO::getReceiptStatus, reqVO.getReceiptStatus())
                .betweenIfPresent(ReceiptOrderDO::getExpectTime, reqVO.getExpectTime())
                .betweenIfPresent(ReceiptOrderDO::getArrivalTime, reqVO.getArrivalTime())
                .eqIfPresent(ReceiptOrderDO::getTotalCount, reqVO.getTotalCount())
                .eqIfPresent(ReceiptOrderDO::getTotalAmount, reqVO.getTotalAmount())
                .eqIfPresent(ReceiptOrderDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(ReceiptOrderDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ReceiptOrderDO::getId));
    }

}