package cn.smart.wms.module.wms.service.receiptorder;

import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.module.wms.controller.admin.receiptorder.vo.ReceiptOrderPageReqVO;
import cn.smart.wms.module.wms.controller.admin.receiptorder.vo.ReceiptOrderSaveReqVO;
import cn.smart.wms.module.wms.dal.dataobject.receiptorder.ReceiptOrderDO;

import javax.validation.Valid;
import java.util.List;

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

    /**
     * 获得入库单列表
     *
     * @param pageReqVO 分页查询
     * @return 入库单列表
     */
    List<ReceiptOrderDO> getReceiptOrderList(ReceiptOrderPageReqVO pageReqVO);

    /**
     * 提交入库单审核
     *
     * @param id 入库单ID
     */
    void submitReceiptOrder(Long id);
    
    /**
     * 审核入库单
     *
     * @param id      入库单ID
     * @param approved 是否通过
     * @param remark   审核备注
     */
    void approveReceiptOrder(Long id, Boolean approved, String remark);
    
    /**
     * 执行入库操作
     *
     * @param detailId   入库单明细ID
     * @param locationId 库位ID
     * @param batchId    批次ID
     * @param count      入库数量
     * @param remark     备注
     * @return 入库记录ID
     */
    Long executeReceiptByDetail(Long detailId, Long locationId, Long batchId, Integer count, String remark);
    
    /**
     * 根据入库单号获取入库单ID
     *
     * @param receiptOrderNo 入库单号
     * @return 入库单ID
     */
    Long getIdByNo(String receiptOrderNo);
    
    /**
     * 更新入库单状态
     *
     * @param id 入库单ID
     */
    void updateReceiptOrderStatus(Long id);

    /**
     * 完成入库单
     *
     * @param id 入库单ID
     */
    void executeReceiptOrder(Long id);
    
    /**
     * 取消入库单
     *
     * @param id 入库单ID
     */
    void cancelReceiptOrder(Long id);

    /**
     * 创建入库单和明细（一次性保存）
     *
     * @param createReqVO 创建信息（包含入库单和明细）
     * @return 入库单ID
     */
    Long createReceiptOrderWithDetails(@Valid ReceiptOrderSaveReqVO createReqVO);
    
    /**
     * 更新入库单和明细（一次性保存）
     *
     * @param updateReqVO 更新信息（包含入库单和明细）
     */
    void updateReceiptOrderWithDetails(@Valid ReceiptOrderSaveReqVO updateReqVO);
    
    /**
     * 批量执行入库操作
     * 对入库单中的所有未入库或部分入库的明细执行批量入库
     *
     * @param orderId 入库单ID
     */
    void batchExecuteReceiptByOrderId(Long orderId);
    
    /**
     * 提交入库单并直接执行入库操作（一步到位）
     * 
     * @param reqVO 入库单和明细信息
     * @return 入库单ID
     */
    Long submitAndReceiptOrder(@Valid ReceiptOrderSaveReqVO reqVO);
}