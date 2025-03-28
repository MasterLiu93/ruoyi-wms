package cn.smart.wms.module.wms.dal.mysql.receiptrecord;

import java.util.*;

import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.smart.wms.framework.mybatis.core.mapper.BaseMapperX;
import cn.smart.wms.module.wms.dal.dataobject.receiptrecord.ReceiptRecordDO;
import org.apache.ibatis.annotations.Mapper;
import cn.smart.wms.module.wms.controller.admin.receiptrecord.vo.*;

/**
 * 入库操作记录 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ReceiptRecordMapper extends BaseMapperX<ReceiptRecordDO> {

    default PageResult<ReceiptRecordDO> selectPage(ReceiptRecordPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ReceiptRecordDO>()
                .eqIfPresent(ReceiptRecordDO::getReceiptOrderId, reqVO.getReceiptOrderId())
                .eqIfPresent(ReceiptRecordDO::getReceiptOrderDetailId, reqVO.getReceiptOrderDetailId())
                .eqIfPresent(ReceiptRecordDO::getItemId, reqVO.getItemId())
                .eqIfPresent(ReceiptRecordDO::getLocationId, reqVO.getLocationId())
                .eqIfPresent(ReceiptRecordDO::getBatchId, reqVO.getBatchId())
                .eqIfPresent(ReceiptRecordDO::getCount, reqVO.getCount())
                .eqIfPresent(ReceiptRecordDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(ReceiptRecordDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ReceiptRecordDO::getId));
    }

}