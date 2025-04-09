package cn.smart.wms.module.wms.service.rack;

import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.module.wms.controller.admin.rack.vo.RackPageReqVO;
import cn.smart.wms.module.wms.controller.admin.rack.vo.RackSaveReqVO;
import cn.smart.wms.module.wms.dal.dataobject.rack.RackDO;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * 货架 Service 接口
 *
 * @author 芋道源码
 */
public interface RackService {

    /**
     * 创建货架
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createRack(@Valid RackSaveReqVO createReqVO);

    /**
     * 更新货架
     *
     * @param updateReqVO 更新信息
     */
    void updateRack(@Valid RackSaveReqVO updateReqVO);

    /**
     * 删除货架
     *
     * @param id 编号
     */
    void deleteRack(Long id);

    /**
     * 获得货架
     *
     * @param id 编号
     * @return 货架
     */
    RackDO getRack(Long id);

    /**
     * 获得货架分页
     *
     * @param pageReqVO 分页查询
     * @return 货架分页
     */
    PageResult<RackDO> getRackPage(RackPageReqVO pageReqVO);

    /**
     * 根据货区ID获取货架列表
     *
     * @param areaId 货区ID
     * @return 货架列表
     */
    List<RackDO> getRackListByAreaId(Long areaId);

    /**
     * 获取货架列表
     *
     * @param ids 货架ID集合
     * @return 货架列表
     */
    List<RackDO> getRackList(Set<Long> ids);

}