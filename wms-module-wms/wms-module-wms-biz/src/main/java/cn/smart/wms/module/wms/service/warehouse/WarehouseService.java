package cn.smart.wms.module.wms.service.warehouse;

import java.util.*;
import javax.validation.*;
import cn.smart.wms.module.wms.controller.admin.warehouse.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.warehouse.WarehouseDO;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.pojo.PageParam;

/**
 * 仓库 Service 接口
 *
 * @author 芋道源码
 */
public interface WarehouseService {

    /**
     * 创建仓库
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createWarehouse(@Valid WarehouseSaveReqVO createReqVO);

    /**
     * 更新仓库
     *
     * @param updateReqVO 更新信息
     */
    void updateWarehouse(@Valid WarehouseSaveReqVO updateReqVO);

    /**
     * 删除仓库
     *
     * @param id 编号
     */
    void deleteWarehouse(Long id);

    /**
     * 获得仓库
     *
     * @param id 编号
     * @return 仓库
     */
    WarehouseDO getWarehouse(Long id);

    /**
     * 获得仓库分页
     *
     * @param pageReqVO 分页查询
     * @return 仓库分页
     */
    PageResult<WarehouseDO> getWarehousePage(WarehousePageReqVO pageReqVO);

}