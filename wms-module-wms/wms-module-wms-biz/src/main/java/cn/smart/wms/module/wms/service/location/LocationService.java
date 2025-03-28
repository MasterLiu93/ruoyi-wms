package cn.smart.wms.module.wms.service.location;

import java.util.*;
import javax.validation.*;
import cn.smart.wms.module.wms.controller.admin.location.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.location.LocationDO;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.pojo.PageParam;

/**
 * 库位 Service 接口
 *
 * @author 芋道源码
 */
public interface LocationService {

    /**
     * 创建库位
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createLocation(@Valid LocationSaveReqVO createReqVO);

    /**
     * 更新库位
     *
     * @param updateReqVO 更新信息
     */
    void updateLocation(@Valid LocationSaveReqVO updateReqVO);

    /**
     * 删除库位
     *
     * @param id 编号
     */
    void deleteLocation(Long id);

    /**
     * 获得库位
     *
     * @param id 编号
     * @return 库位
     */
    LocationDO getLocation(Long id);

    /**
     * 获得库位分页
     *
     * @param pageReqVO 分页查询
     * @return 库位分页
     */
    PageResult<LocationDO> getLocationPage(LocationPageReqVO pageReqVO);

}