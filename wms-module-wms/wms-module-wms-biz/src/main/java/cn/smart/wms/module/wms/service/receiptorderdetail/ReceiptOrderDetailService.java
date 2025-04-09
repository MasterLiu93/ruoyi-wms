package cn.smart.wms.module.wms.service.receiptorderdetail;

import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.module.wms.controller.admin.receiptorderdetail.vo.ReceiptOrderDetailImportVO;
import cn.smart.wms.module.wms.controller.admin.receiptorderdetail.vo.ReceiptOrderDetailPageReqVO;
import cn.smart.wms.module.wms.controller.admin.receiptorderdetail.vo.ReceiptOrderDetailSaveReqVO;
import cn.smart.wms.module.wms.dal.dataobject.receiptorderdetail.ReceiptOrderDetailDO;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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

    /**
     * 获得指定入库单下的入库单明细列表
     *
     * @param receiptOrderId 入库单ID
     * @return 入库单明细列表
     */
    List<ReceiptOrderDetailDO> getReceiptOrderDetailListByOrderId(Long receiptOrderId);

    /**
     * 导出增强版Excel模板（带数据筛选功能）
     *
     * @param warehouseId 仓库ID，可选
     * @param response HTTP响应对象
     * @throws IOException IO异常
     */
    void exportEnhancedTemplate(Long warehouseId, HttpServletResponse response) throws IOException;
    
    /**
     * 解析Excel数据但不关联到入库单
     *
     * @param importVOs 导入的数据列表
     * @param save 是否保存到数据库
     * @return 解析后的明细列表
     */
    List<ReceiptOrderDetailDO> importReceiptOrderDetails(List<ReceiptOrderDetailImportVO> importVOs, boolean save);

    /**
     * 导入入库单明细
     *
     * @param receiptOrderId 入库单ID
     * @param importVOs 导入的数据列表
     * @return 解析后的明细列表，用于前端展示
     */
    List<ReceiptOrderDetailDO> importReceiptOrderDetails(Long receiptOrderId, List<ReceiptOrderDetailImportVO> importVOs);
    
    /**
     * 解析Excel文件中的数据
     * 
     * @param inputStream Excel文件输入流
     * @param save 是否保存到数据库
     * @return 解析后的明细列表
     * @throws IOException IO异常
     */
    List<ReceiptOrderDetailDO> importReceiptOrderDetails(InputStream inputStream, boolean save) throws IOException;

    /**
     * 批量保存入库单明细
     *
     * @param receiptOrderId 入库单ID
     * @param details 明细列表
     */
    void saveReceiptOrderDetails(Long receiptOrderId, List<ReceiptOrderDetailDO> details);
    
    /**
     * 更新入库单的总数量和总金额
     *
     * @param receiptOrderId 入库单ID
     */
    void updateReceiptOrderTotal(Long receiptOrderId);
}