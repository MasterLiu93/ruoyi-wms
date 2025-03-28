package cn.smart.wms.module.wms.service.area;

import javax.validation.*;
import cn.smart.wms.module.wms.controller.admin.area.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.area.AreaDO;
import cn.smart.wms.framework.common.pojo.PageResult;

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

}