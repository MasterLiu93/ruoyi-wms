package cn.smart.wms.module.wms.service.receiptrecord;

import java.util.*;
import javax.validation.*;
import cn.smart.wms.module.wms.controller.admin.receiptrecord.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.receiptrecord.ReceiptRecordDO;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.pojo.PageParam;

/**
 * 入库操作记录 Service 接口
 *
 * @author 芋道源码
 */
public interface ReceiptRecordService {

    /**
     * 创建入库操作记录
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createReceiptRecord(@Valid ReceiptRecordSaveReqVO createReqVO);

    /**
     * 更新入库操作记录
     *
     * @param updateReqVO 更新信息
     */
    void updateReceiptRecord(@Valid ReceiptRecordSaveReqVO updateReqVO);

    /**
     * 删除入库操作记录
     *
     * @param id 编号
     */
    void deleteReceiptRecord(Long id);

    /**
     * 获得入库操作记录
     *
     * @param id 编号
     * @return 入库操作记录
     */
    ReceiptRecordDO getReceiptRecord(Long id);

    /**
     * 获得入库操作记录分页
     *
     * @param pageReqVO 分页查询
     * @return 入库操作记录分页
     */
    PageResult<ReceiptRecordDO> getReceiptRecordPage(ReceiptRecordPageReqVO pageReqVO);

}