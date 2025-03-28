package cn.smart.wms.module.wms.service.receiptorderdetail;

import java.util.*;
import javax.validation.*;
import cn.smart.wms.module.wms.controller.admin.receiptorderdetail.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.receiptorderdetail.ReceiptOrderDetailDO;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.pojo.PageParam;

/**
 * 入库单明细 Service 接口
 *
 * @author 芋道源码
 */
public interface ReceiptOrderDetailService {

    /**
     * 创建入库单明细
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createReceiptOrderDetail(@Valid ReceiptOrderDetailSaveReqVO createReqVO);

    /**
     * 更新入库单明细
     *
     * @param updateReqVO 更新信息
     */
    void updateReceiptOrderDetail(@Valid ReceiptOrderDetailSaveReqVO updateReqVO);

    /**
     * 删除入库单明细
     *
     * @param id 编号
     */
    void deleteReceiptOrderDetail(Long id);

    /**
     * 获得入库单明细
     *
     * @param id 编号
     * @return 入库单明细
     */
    ReceiptOrderDetailDO getReceiptOrderDetail(Long id);

    /**
     * 获得入库单明细分页
     *
     * @param pageReqVO 分页查询
     * @return 入库单明细分页
     */
    PageResult<ReceiptOrderDetailDO> getReceiptOrderDetailPage(ReceiptOrderDetailPageReqVO pageReqVO);

}