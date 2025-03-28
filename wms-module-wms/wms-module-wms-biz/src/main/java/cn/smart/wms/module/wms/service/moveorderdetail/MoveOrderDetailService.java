package cn.smart.wms.module.wms.service.moveorderdetail;

import java.util.*;
import javax.validation.*;
import cn.smart.wms.module.wms.controller.admin.moveorderdetail.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.moveorderdetail.MoveOrderDetailDO;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.pojo.PageParam;

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

}