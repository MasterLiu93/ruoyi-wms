package cn.smart.wms.module.wms.service.receiptorder;

import java.util.*;
import javax.validation.*;
import cn.smart.wms.module.wms.controller.admin.receiptorder.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.receiptorder.ReceiptOrderDO;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.pojo.PageParam;

/**
 * 入库单 Service 接口
 *
 * @author 芋道源码
 */
public interface ReceiptOrderService {

    /**
     * 创建入库单
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createReceiptOrder(@Valid ReceiptOrderSaveReqVO createReqVO);

    /**
     * 更新入库单
     *
     * @param updateReqVO 更新信息
     */
    void updateReceiptOrder(@Valid ReceiptOrderSaveReqVO updateReqVO);

    /**
     * 删除入库单
     *
     * @param id 编号
     */
    void deleteReceiptOrder(Long id);

    /**
     * 获得入库单
     *
     * @param id 编号
     * @return 入库单
     */
    ReceiptOrderDO getReceiptOrder(Long id);

    /**
     * 获得入库单分页
     *
     * @param pageReqVO 分页查询
     * @return 入库单分页
     */
    PageResult<ReceiptOrderDO> getReceiptOrderPage(ReceiptOrderPageReqVO pageReqVO);

}