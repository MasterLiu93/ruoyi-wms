package cn.smart.wms.module.wms.service.area;

import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.module.wms.controller.admin.area.vo.AreaPageReqVO;
import cn.smart.wms.module.wms.controller.admin.area.vo.AreaSaveReqVO;
import cn.smart.wms.module.wms.dal.dataobject.area.AreaDO;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * 货区 Service 接口
 *
 * @author 芋道源码
 */
public interface WmsAreaService {

    /**
     * 创建货区
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createArea(@Valid AreaSaveReqVO createReqVO);

    /**
     * 更新货区
     *
     * @param updateReqVO 更新信息
     */
    void updateArea(@Valid AreaSaveReqVO updateReqVO);

    /**
     * 删除货区
     *
     * @param id 编号
     */
    void deleteArea(Long id);

    /**
     * 获得货区
     *
     * @param id 编号
     * @return 货区
     */
    AreaDO getArea(Long id);

    /**
     * 获得货区分页
     *
     * @param pageReqVO 分页查询
     * @return 货区分页
     */
    PageResult<AreaDO> getAreaPage(AreaPageReqVO pageReqVO);
    
    /**
     * 获取货区列表
     *
     * @param ids 货区ID集合
     * @return 货区列表
     */
    List<AreaDO> getAreaList(Set<Long> ids);
    
    /**
     * 获取货区列表
     *
     * @param warehouseId 仓库ID
     * @return 货区列表
     */
    List<AreaDO> getAreaListByWarehouseId(Long warehouseId);

}