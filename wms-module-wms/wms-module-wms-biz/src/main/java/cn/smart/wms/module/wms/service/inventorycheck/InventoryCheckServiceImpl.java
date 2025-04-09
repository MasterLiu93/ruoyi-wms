package cn.smart.wms.module.wms.service.inventorycheck;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.smart.wms.module.wms.controller.admin.inventorycheck.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.inventorycheck.InventoryCheckDO;
import cn.smart.wms.module.wms.dal.dataobject.inventorycheckdetail.InventoryCheckDetailDO;
import cn.smart.wms.module.wms.dal.dataobject.inventory.InventoryDO;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.util.object.BeanUtils;
import cn.smart.wms.framework.common.exception.ErrorCode;
import cn.smart.wms.framework.mybatis.core.query.LambdaQueryWrapperX;

import cn.smart.wms.module.wms.dal.mysql.inventorycheck.InventoryCheckMapper;
import cn.smart.wms.module.wms.dal.mysql.inventorycheckdetail.InventoryCheckDetailMapper;
import cn.smart.wms.module.wms.enums.inventory.InventoryCheckStatusEnum;
import cn.smart.wms.module.wms.enums.inventory.InventoryCheckTypeEnum;
import cn.smart.wms.module.wms.service.inventory.InventoryService;
import cn.smart.wms.module.wms.service.inventorycheckdetail.InventoryCheckDetailService;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;

import static cn.smart.wms.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.smart.wms.module.wms.enums.ErrorCodeConstants.*;

/**
 * 库存盘点单 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class InventoryCheckServiceImpl implements InventoryCheckService {

    @Resource
    private InventoryCheckMapper inventoryCheckMapper;
    
    @Resource
    private InventoryCheckDetailMapper inventoryCheckDetailMapper;
    
    @Resource
    private InventoryCheckDetailService inventoryCheckDetailService;
    
    @Resource
    private InventoryService inventoryService;

    @Override
    public Long createInventoryCheck(InventoryCheckSaveReqVO createReqVO) {
        // 插入
        InventoryCheckDO inventoryCheck = BeanUtils.toBean(createReqVO, InventoryCheckDO.class);
        // 设置初始状态
        if (inventoryCheck.getCheckStatus() == null) {
            inventoryCheck.setCheckStatus(InventoryCheckStatusEnum.NEW.getStatus());
        }
        // 初始化盘点数据
        inventoryCheck.setTotalCount(0);
        inventoryCheck.setCheckedCount(0);
        inventoryCheck.setDifferenceCount(0);
        
        inventoryCheckMapper.insert(inventoryCheck);
        // 返回
        return inventoryCheck.getId();
    }

    @Override
    public void updateInventoryCheck(InventoryCheckSaveReqVO updateReqVO) {
        // 校验存在
        InventoryCheckDO inventoryCheck = validateInventoryCheckExists(updateReqVO.getId());
        // 校验状态，只有新建状态的盘点单可以修改
        if (!InventoryCheckStatusEnum.NEW.getStatus().equals(inventoryCheck.getCheckStatus())) {
            throw exception(new ErrorCode(1002004102, "非新建状态的盘点单不能修改"));
        }
        // 更新
        InventoryCheckDO updateObj = BeanUtils.toBean(updateReqVO, InventoryCheckDO.class);
        inventoryCheckMapper.updateById(updateObj);
    }

    @Override
    public void deleteInventoryCheck(Long id) {
        // 校验存在
        InventoryCheckDO inventoryCheck = validateInventoryCheckExists(id);
        // 校验状态，只有新建状态的盘点单可以删除
        if (!InventoryCheckStatusEnum.NEW.getStatus().equals(inventoryCheck.getCheckStatus())) {
            throw exception(new ErrorCode(1002004102, "非新建状态的盘点单不能删除"));
        }
        // 删除明细
        inventoryCheckDetailMapper.delete(InventoryCheckDetailDO::getCheckId, id);
        // 删除盘点单
        inventoryCheckMapper.deleteById(id);
    }

    private InventoryCheckDO validateInventoryCheckExists(Long id) {
        InventoryCheckDO inventoryCheck = inventoryCheckMapper.selectById(id);
        if (inventoryCheck == null) {
            throw exception(INVENTORY_CHECK_NOT_EXISTS);
        }
        return inventoryCheck;
    }

    @Override
    public InventoryCheckDO getInventoryCheck(Long id) {
        return inventoryCheckMapper.selectById(id);
    }

    @Override
    public PageResult<InventoryCheckDO> getInventoryCheckPage(InventoryCheckPageReqVO pageReqVO) {
        return inventoryCheckMapper.selectPage(pageReqVO);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long generateCheckPlan(Long warehouseId, Integer checkType, 
                                List<Long> locationIds, List<Long> itemIds, String remark) {
        // 校验盘点类型
        if (!InventoryCheckTypeEnum.FULL.getType().equals(checkType) && 
            !InventoryCheckTypeEnum.PARTIAL.getType().equals(checkType)) {
            throw exception(new ErrorCode(1002004104, "盘点类型无效"));
        }
        
        // 部分盘点时校验库位和物料参数
        if (InventoryCheckTypeEnum.PARTIAL.getType().equals(checkType) && 
            (locationIds == null || locationIds.isEmpty()) && 
            (itemIds == null || itemIds.isEmpty())) {
            throw exception(new ErrorCode(1002004105, "部分盘点必须指定库位或物料"));
        }
        
        // 创建盘点单
        InventoryCheckDO inventoryCheck = new InventoryCheckDO();
        // 生成盘点单号
        inventoryCheck.setCheckNo("IC" + IdUtil.getSnowflakeNextIdStr());
        inventoryCheck.setWarehouseId(warehouseId);
        inventoryCheck.setCheckType(checkType);
        inventoryCheck.setCheckStatus(InventoryCheckStatusEnum.NEW.getStatus());
        inventoryCheck.setRemark(remark);
        inventoryCheck.setTotalCount(0);
        inventoryCheck.setCheckedCount(0);
        inventoryCheck.setDifferenceCount(0);
        
        inventoryCheckMapper.insert(inventoryCheck);
        // 返回
        Long checkId = inventoryCheck.getId();
        
        // 查询需要盘点的库存数据
        List<InventoryDO> inventories = new ArrayList<>();
        
        if (InventoryCheckTypeEnum.FULL.getType().equals(checkType)) {
            // 全部盘点，查询该仓库所有有效库存
            inventories = inventoryService.getInventoryListByWarehouse(warehouseId);
        } else {
            // 部分盘点，根据条件查询库存
            if (locationIds != null && !locationIds.isEmpty() && itemIds != null && !itemIds.isEmpty()) {
                // 同时指定了库位和物料
                inventories = inventoryService.getInventoryListByWarehouseAndLocationIdsAndItemIds(
                    warehouseId, locationIds, itemIds);
            } else if (locationIds != null && !locationIds.isEmpty()) {
                // 只指定了库位
                inventories = inventoryService.getInventoryListByWarehouseAndLocationIds(
                    warehouseId, locationIds);
            } else {
                // 只指定了物料
                inventories = inventoryService.getInventoryListByWarehouseAndItemIds(
                    warehouseId, itemIds);
            }
        }
        
        // 生成盘点明细
        List<InventoryCheckDetailDO> details = new ArrayList<>();
        for (InventoryDO inventory : inventories) {
            // 只盘点有实际库存的记录
            if (inventory.getAvailableCount() <= 0 && inventory.getLockedCount() <= 0) {
                continue;
            }
            
            InventoryCheckDetailDO detail = new InventoryCheckDetailDO();
            detail.setCheckId(checkId);
            detail.setItemId(inventory.getItemId());
            detail.setLocationId(inventory.getLocationId());
            // 账面数量为可用库存 + 锁定库存
            detail.setBookCount(inventory.getAvailableCount() + inventory.getLockedCount());
            detail.setCheckCount(0); // 初始化盘点数量为0
            detail.setDifferenceCount(0); // 初始化差异数量为0
            detail.setCheckStatus(0); // 0-未盘点
            
            details.add(detail);
        }
        
        if (!details.isEmpty()) {
            // 批量插入盘点明细
            inventoryCheckDetailService.batchInsertInventoryCheckDetail(details);
            
            // 更新盘点单的盘点总数
            InventoryCheckDO updateObj = new InventoryCheckDO();
            updateObj.setId(checkId);
            updateObj.setTotalCount(details.size());
            inventoryCheckMapper.updateById(updateObj);
        }
        
        return checkId;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void startCheck(Long id) {
        // 校验存在
        InventoryCheckDO inventoryCheck = validateInventoryCheckExists(id);
        // 校验状态，只有新建状态的盘点单可以开始盘点
        if (!InventoryCheckStatusEnum.NEW.getStatus().equals(inventoryCheck.getCheckStatus())) {
            throw exception(new ErrorCode(1002004102, "非新建状态的盘点单不能开始盘点"));
        }
        
        // 校验是否已生成盘点明细
        Long detailCount = inventoryCheckDetailMapper.selectCount(
            InventoryCheckDetailDO::getCheckId, id);
        if (detailCount == 0) {
            throw exception(new ErrorCode(1002004106, "盘点单没有生成盘点明细，无法开始盘点"));
        }
        
        // 更新盘点单状态为盘点中
        InventoryCheckDO updateObj = new InventoryCheckDO();
        updateObj.setId(id);
        updateObj.setCheckStatus(InventoryCheckStatusEnum.CHECKING.getStatus());
        inventoryCheckMapper.updateById(updateObj);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public InventoryCheckDetailDO submitCheckResult(Long detailId, Integer checkCount, String remark) {
        // 校验盘点明细存在
        InventoryCheckDetailDO detail = inventoryCheckDetailMapper.selectById(detailId);
        if (detail == null) {
            throw exception(INVENTORY_CHECK_DETAIL_NOT_EXISTS);
        }
        
        // 校验盘点单状态
        InventoryCheckDO inventoryCheck = validateInventoryCheckExists(detail.getCheckId());
        if (!InventoryCheckStatusEnum.CHECKING.getStatus().equals(inventoryCheck.getCheckStatus())) {
            throw exception(new ErrorCode(1002004102, "盘点单不是盘点中状态，无法提交盘点结果"));
        }
        
        // 更新盘点明细
        InventoryCheckDetailDO updateDetail = new InventoryCheckDetailDO();
        updateDetail.setId(detailId);
        updateDetail.setCheckCount(checkCount);
        // 计算差异
        int differenceCount = checkCount - detail.getBookCount();
        updateDetail.setDifferenceCount(differenceCount);
        updateDetail.setCheckStatus(1); // 1-已盘点
        updateDetail.setRemark(remark);
        
        inventoryCheckDetailMapper.updateById(updateDetail);
        
        // 更新盘点单的已盘点数
        updateCheckProgress(detail.getCheckId());
        
        return inventoryCheckDetailMapper.selectById(detailId);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Long> batchSubmitCheckResult(Long checkId, List<InventoryCheckDetailSubmitVO> checkDetails) {
        // 校验盘点单存在及状态
        InventoryCheckDO inventoryCheck = validateInventoryCheckExists(checkId);
        if (!InventoryCheckStatusEnum.CHECKING.getStatus().equals(inventoryCheck.getCheckStatus())) {
            throw exception(new ErrorCode(1002004102, "盘点单不是盘点中状态，无法提交盘点结果"));
        }
        
        List<Long> submittedIds = new ArrayList<>();
        for (InventoryCheckDetailSubmitVO submitVO : checkDetails) {
            Long detailId = submitVO.getDetailId();
            
            // 校验盘点明细存在且属于当前盘点单
            InventoryCheckDetailDO detail = inventoryCheckDetailMapper.selectById(detailId);
            if (detail == null || !detail.getCheckId().equals(checkId)) {
                continue;  // 跳过无效明细
            }
            
            // 更新盘点明细
            InventoryCheckDetailDO updateDetail = new InventoryCheckDetailDO();
            updateDetail.setId(detailId);
            updateDetail.setCheckCount(submitVO.getCheckCount());
            // 计算差异
            int differenceCount = submitVO.getCheckCount() - detail.getBookCount();
            updateDetail.setDifferenceCount(differenceCount);
            updateDetail.setCheckStatus(1); // 1-已盘点
            updateDetail.setRemark(submitVO.getRemark());
            
            inventoryCheckDetailMapper.updateById(updateDetail);
            submittedIds.add(detailId);
        }
        
        // 更新盘点单的已盘点数
        if (!submittedIds.isEmpty()) {
            updateCheckProgress(checkId);
        }
        
        return submittedIds;
    }
    
    /**
     * 更新盘点单进度
     */
    private void updateCheckProgress(Long checkId) {
        // 查询已盘点明细数
        Long checkedCount = inventoryCheckDetailMapper.selectCount(
            new LambdaQueryWrapperX<InventoryCheckDetailDO>()
                .eq(InventoryCheckDetailDO::getCheckId, checkId)
                .eq(InventoryCheckDetailDO::getCheckStatus, 1)); // 1-已盘点
        
        // 查询有差异的明细数
        Long diffCount = inventoryCheckDetailMapper.selectCount(
            new LambdaQueryWrapperX<InventoryCheckDetailDO>()
                .eq(InventoryCheckDetailDO::getCheckId, checkId)
                .ne(InventoryCheckDetailDO::getDifferenceCount, 0));
        
        // 更新盘点单
        InventoryCheckDO updateObj = new InventoryCheckDO();
        updateObj.setId(checkId);
        updateObj.setCheckedCount(Math.toIntExact(checkedCount));
        updateObj.setDifferenceCount(Math.toIntExact(diffCount));
        inventoryCheckMapper.updateById(updateObj);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completeCheck(Long id, Boolean autoAdjust) {
        // 校验存在
        InventoryCheckDO inventoryCheck = validateInventoryCheckExists(id);
        // 校验状态，只有盘点中状态的盘点单可以完成盘点
        if (!InventoryCheckStatusEnum.CHECKING.getStatus().equals(inventoryCheck.getCheckStatus())) {
            throw exception(new ErrorCode(1002004102, "非盘点中状态的盘点单不能完成盘点"));
        }
        
        // 校验是否所有明细都已盘点
        Long uncheckedCount = inventoryCheckDetailMapper.selectCount(
            new LambdaQueryWrapperX<InventoryCheckDetailDO>()
                .eq(InventoryCheckDetailDO::getCheckId, id)
                .eq(InventoryCheckDetailDO::getCheckStatus, 0)); // 0-未盘点
        
        if (uncheckedCount > 0) {
            throw exception(new ErrorCode(1002004107, "还有未盘点的明细，无法完成盘点"));
        }
        
        // 更新盘点单状态为已完成
        InventoryCheckDO updateObj = new InventoryCheckDO();
        updateObj.setId(id);
        updateObj.setCheckStatus(InventoryCheckStatusEnum.COMPLETED.getStatus());
        inventoryCheckMapper.updateById(updateObj);
        
        // 如果选择自动调整库存差异
        if (Boolean.TRUE.equals(autoAdjust)) {
            adjustInventory(id, "盘点完成自动调整");
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void adjustInventory(Long id, String remark) {
        // 校验存在
        InventoryCheckDO inventoryCheck = validateInventoryCheckExists(id);
        // 校验状态，只有已完成状态的盘点单可以调整库存
        if (!InventoryCheckStatusEnum.COMPLETED.getStatus().equals(inventoryCheck.getCheckStatus())) {
            throw exception(new ErrorCode(1002004102, "非已完成状态的盘点单不能调整库存"));
        }
        
        // 查询有差异的盘点明细
        List<InventoryCheckDetailDO> differenceDetails = inventoryCheckDetailMapper.selectList(
            new LambdaQueryWrapperX<InventoryCheckDetailDO>()
                .eq(InventoryCheckDetailDO::getCheckId, id)
                .ne(InventoryCheckDetailDO::getDifferenceCount, 0));
        
        for (InventoryCheckDetailDO detail : differenceDetails) {
            // 根据差异调整库存
            if (detail.getDifferenceCount() > 0) {
                // 实际数量大于账面数量，增加库存
                inventoryService.increaseInventory(
                    inventoryCheck.getWarehouseId(),
                    detail.getItemId(),
                    detail.getLocationId(),
                    detail.getDifferenceCount(),
                    "CHECK",
                    id,
                    detail.getId(),
                    "盘点调整增加: " + (StrUtil.isNotEmpty(remark) ? remark : "")
                );
            } else if (detail.getDifferenceCount() < 0) {
                // 实际数量小于账面数量，减少库存
                inventoryService.decreaseInventory(
                    inventoryCheck.getWarehouseId(),
                    detail.getItemId(),
                    detail.getLocationId(),
                    -detail.getDifferenceCount(), // 转为正数
                    "CHECK",
                    id,
                    detail.getId(),
                    "盘点调整减少: " + (StrUtil.isNotEmpty(remark) ? remark : "")
                );
            }
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelCheck(Long id, String remark) {
        // 校验存在
        InventoryCheckDO inventoryCheck = validateInventoryCheckExists(id);
        // 校验状态，只有盘点中状态的盘点单可以取消
        if (!InventoryCheckStatusEnum.CHECKING.getStatus().equals(inventoryCheck.getCheckStatus())) {
            throw exception(new ErrorCode(1002004102, "非盘点中状态的盘点单不能取消"));
        }
        
        // 更新盘点单状态为新建
        InventoryCheckDO updateObj = new InventoryCheckDO();
        updateObj.setId(id);
        updateObj.setCheckStatus(InventoryCheckStatusEnum.NEW.getStatus());
        if (StrUtil.isNotEmpty(remark)) {
            updateObj.setRemark(remark);
        }
        inventoryCheckMapper.updateById(updateObj);
        
        // 重置所有明细的盘点状态
        InventoryCheckDetailDO updateDetail = new InventoryCheckDetailDO();
        updateDetail.setCheckStatus(0); // 0-未盘点
        updateDetail.setCheckCount(0);
        updateDetail.setDifferenceCount(0);
        
        inventoryCheckDetailMapper.update(updateDetail, 
            new LambdaQueryWrapperX<InventoryCheckDetailDO>()
                .eq(InventoryCheckDetailDO::getCheckId, id));
        
        // 更新盘点单的已盘点数为0
        updateObj = new InventoryCheckDO();
        updateObj.setId(id);
        updateObj.setCheckedCount(0);
        updateObj.setDifferenceCount(0);
        inventoryCheckMapper.updateById(updateObj);
    }
    
    @Override
    public Map<String, Object> getCheckProgress(Long id) {
        // 校验存在
        InventoryCheckDO inventoryCheck = validateInventoryCheckExists(id);
        
        Map<String, Object> result = new HashMap<>();
        result.put("totalCount", inventoryCheck.getTotalCount());
        result.put("checkedCount", inventoryCheck.getCheckedCount());
        result.put("differenceCount", inventoryCheck.getDifferenceCount());
        
        // 计算进度百分比
        int progress = 0;
        if (inventoryCheck.getTotalCount() > 0) {
            progress = (int) ((double) inventoryCheck.getCheckedCount() / inventoryCheck.getTotalCount() * 100);
        }
        result.put("progress", progress);
        
        // 获取状态描述
        InventoryCheckStatusEnum statusEnum = InventoryCheckStatusEnum.getByStatus(inventoryCheck.getCheckStatus());
        result.put("status", inventoryCheck.getCheckStatus());
        result.put("statusDesc", statusEnum != null ? statusEnum.getDescription() : "未知");
        
        return result;
    }
}