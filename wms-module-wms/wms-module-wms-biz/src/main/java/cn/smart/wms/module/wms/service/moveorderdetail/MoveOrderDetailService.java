package cn.smart.wms.module.wms.service.moveorderdetail;

import java.util.*;
import javax.validation.*;
import cn.smart.wms.module.wms.controller.admin.moveorderdetail.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.moveorderdetail.MoveOrderDetailDO;
import cn.smart.wms.framework.common.pojo.PageResult;

/**
 * 移库单明细 Service 接口
 *
 * @author 芋道源码
 */
public interface MoveOrderDetailService {

    /**
     * 创建移库单明细
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createMoveOrderDetail(@Valid MoveOrderDetailSaveReqVO createReqVO);

    /**
     * 更新移库单明细
     *
     * @param updateReqVO 更新信息
     */
    void updateMoveOrderDetail(@Valid MoveOrderDetailSaveReqVO updateReqVO);

    /**
     * 删除移库单明细
     *
     * @param id 编号
     */
    void deleteMoveOrderDetail(Long id);

    /**
     * 获得移库单明细
     *
     * @param id 编号
     * @return 移库单明细
     */
    MoveOrderDetailDO getMoveOrderDetail(Long id);

    /**
     * 获得移库单明细分页
     *
     * @param pageReqVO 分页查询
     * @return 移库单明细分页
     */
    PageResult<MoveOrderDetailDO> getMoveOrderDetailPage(MoveOrderDetailPageReqVO pageReqVO);
    
    /**
     * 根据移库单ID获取移库单明细列表
     *
     * @param moveOrderId 移库单ID
     * @return 移库单明细列表
     */
    List<MoveOrderDetailDO> getMoveOrderDetailListByMoveOrderId(Long moveOrderId);
    
    /**
     * 批量创建移库单明细
     *
     * @param details 明细列表
     * @return 创建的明细ID列表
     */
    List<Long> batchCreateMoveOrderDetail(List<MoveOrderDetailSaveReqVO> details);
    
    /**
     * 更新移库单明细状态
     *
     * @param id 明细ID
     * @param realCount 实际移库数量
     * @param status 状态
     */
    void updateMoveOrderDetailStatus(Long id, Integer realCount, Integer status);
}