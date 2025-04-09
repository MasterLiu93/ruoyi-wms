package cn.smart.wms.module.wms.service.inventorycheck;

import java.util.*;
import javax.validation.*;
import cn.smart.wms.module.wms.controller.admin.inventorycheck.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.inventorycheck.InventoryCheckDO;
import cn.smart.wms.module.wms.dal.dataobject.inventorycheckdetail.InventoryCheckDetailDO;
import cn.smart.wms.framework.common.pojo.PageResult;

/**
 * 库存盘点单 Service 接口
 *
 * @author 芋道源码
 */
public interface InventoryCheckService {

    /**
     * 创建库存盘点单
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createInventoryCheck(@Valid InventoryCheckSaveReqVO createReqVO);

    /**
     * 更新库存盘点单
     *
     * @param updateReqVO 更新信息
     */
    void updateInventoryCheck(@Valid InventoryCheckSaveReqVO updateReqVO);

    /**
     * 删除库存盘点单
     *
     * @param id 编号
     */
    void deleteInventoryCheck(Long id);

    /**
     * 获得库存盘点单
     *
     * @param id 编号
     * @return 库存盘点单
     */
    InventoryCheckDO getInventoryCheck(Long id);

    /**
     * 获得库存盘点单分页
     *
     * @param pageReqVO 分页查询
     * @return 库存盘点单分页
     */
    PageResult<InventoryCheckDO> getInventoryCheckPage(InventoryCheckPageReqVO pageReqVO);
    
    /**
     * 生成盘点计划
     * 
     * @param warehouseId 仓库ID
     * @param checkType 盘点类型(0-全部盘点 1-部分盘点)
     * @param locationIds 库位ID列表，当盘点类型为部分盘点时使用
     * @param itemIds 物料ID列表，当盘点类型为部分盘点时使用
     * @param remark 备注
     * @return 盘点单ID
     */
    Long generateCheckPlan(Long warehouseId, Integer checkType, 
                         List<Long> locationIds, List<Long> itemIds, String remark);

    /**
     * 开始盘点
     * 
     * @param id 盘点单ID
     */
    void startCheck(Long id);
    
    /**
     * 提交盘点结果
     * 
     * @param detailId 盘点明细ID
     * @param checkCount 盘点数量
     * @param remark 备注
     * @return 盘点明细对象
     */
    InventoryCheckDetailDO submitCheckResult(Long detailId, Integer checkCount, String remark);
    
    /**
     * 批量提交盘点结果
     * 
     * @param checkId 盘点单ID
     * @param checkDetails 盘点明细列表
     * @return 盘点明细ID列表
     */
    List<Long> batchSubmitCheckResult(Long checkId, List<InventoryCheckDetailSubmitVO> checkDetails);
    
    /**
     * 完成盘点
     * 
     * @param id 盘点单ID
     * @param autoAdjust 是否自动调整库存差异(true-是 false-否)
     */
    void completeCheck(Long id, Boolean autoAdjust);
    
    /**
     * 差异处理 - 调整库存
     * 
     * @param id 盘点单ID
     * @param remark 备注
     */
    void adjustInventory(Long id, String remark);
    
    /**
     * 取消盘点
     * 
     * @param id 盘点单ID
     * @param remark 备注
     */
    void cancelCheck(Long id, String remark);
    
    /**
     * 获取盘点进度
     * 
     * @param id 盘点单ID
     * @return 盘点进度信息，包含总数、已盘点数、差异数等
     */
    Map<String, Object> getCheckProgress(Long id);
}