package cn.smart.wms.module.wms.service.batchinventory.impl;

import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.module.wms.controller.admin.batchinventory.vo.BatchInventoryPageReqVO;
import cn.smart.wms.module.wms.controller.admin.batchinventory.vo.BatchInventorySaveReqVO;
import cn.smart.wms.module.wms.dal.dataobject.batchinventory.BatchInventoryDO;
import cn.smart.wms.module.wms.dal.mysql.batchinventory.BatchInventoryMapper;
import cn.smart.wms.module.wms.service.batchinventory.BatchInventoryService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import cn.smart.wms.framework.common.util.object.BeanUtils;

import javax.annotation.Resource;

import static cn.smart.wms.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.smart.wms.module.wms.enums.ErrorCodeConstants.BATCH_INVENTORY_NOT_EXISTS;

/**
 * 批次库存 Service 实现类
 */
@Service
@Validated
public class BatchInventoryServiceImpl implements BatchInventoryService {

    @Resource
    private BatchInventoryMapper batchInventoryMapper;

    @Override
    public Long createBatchInventory(BatchInventorySaveReqVO createReqVO) {
        // 插入
        BatchInventoryDO batchInventory = BeanUtils.toBean(createReqVO, BatchInventoryDO.class);
        batchInventoryMapper.insert(batchInventory);
        // 返回
        return batchInventory.getId();
    }

    @Override
    public void updateBatchInventory(BatchInventorySaveReqVO updateReqVO) {
        // 校验存在
        validateBatchInventoryExists(updateReqVO.getId());
        // 更新
        BatchInventoryDO updateObj = BeanUtils.toBean(updateReqVO, BatchInventoryDO.class);
        batchInventoryMapper.updateById(updateObj);
    }

    @Override
    public void deleteBatchInventory(Long id) {
        // 校验存在
        validateBatchInventoryExists(id);
        // 删除
        batchInventoryMapper.deleteById(id);
    }

    private void validateBatchInventoryExists(Long id) {
        if (batchInventoryMapper.selectById(id) == null) {
            throw exception(BATCH_INVENTORY_NOT_EXISTS);
        }
    }

    @Override
    public BatchInventoryDO getBatchInventory(Long id) {
        return batchInventoryMapper.selectById(id);
    }

    @Override
    public PageResult<BatchInventoryDO> getBatchInventoryPage(BatchInventoryPageReqVO pageReqVO) {
        return batchInventoryMapper.selectPage(pageReqVO);
    }
} 