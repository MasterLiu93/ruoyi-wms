package cn.smart.wms.module.wms.service.batchinventory;

import java.util.*;
import javax.validation.*;
import cn.smart.wms.module.wms.controller.admin.batchinventory.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.batchinventory.BatchInventoryDO;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.pojo.PageParam;

/**
 * 批次库存 Service 接口
 *
 * @author 芋道源码
 */
public interface BatchInventoryService {

    /**
     * 创建批次库存
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createBatchInventory(@Valid BatchInventorySaveReqVO createReqVO);

    /**
     * 更新批次库存
     *
     * @param updateReqVO 更新信息
     */
    void updateBatchInventory(@Valid BatchInventorySaveReqVO updateReqVO);

    /**
     * 删除批次库存
     *
     * @param id 编号
     */
    void deleteBatchInventory(Long id);

    /**
     * 获得批次库存
     *
     * @param id 编号
     * @return 批次库存
     */
    BatchInventoryDO getBatchInventory(Long id);

    /**
     * 获得批次库存分页
     *
     * @param pageReqVO 分页查询
     * @return 批次库存分页
     */
    PageResult<BatchInventoryDO> getBatchInventoryPage(BatchInventoryPageReqVO pageReqVO);

}