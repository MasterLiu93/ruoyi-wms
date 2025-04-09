package cn.smart.wms.module.wms.service.location;

import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.module.wms.controller.admin.location.vo.LocationPageReqVO;
import cn.smart.wms.module.wms.controller.admin.location.vo.LocationSaveReqVO;
import cn.smart.wms.module.wms.dal.dataobject.location.LocationDO;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

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

    /**
     * 获取库位列表
     *
     * @param rackId 货架ID，如果为空则获取所有库位
     * @return 库位列表
     */
    List<LocationDO> getLocationList(Long rackId);
    
    /**
     * 批量获取库位信息
     *
     * @param ids 库位ID集合
     * @return 库位列表
     */
    List<LocationDO> getLocationListByIds(Set<Long> ids);

}