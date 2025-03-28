package cn.smart.wms.module.wms.dal.mysql.area;

import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.smart.wms.framework.mybatis.core.mapper.BaseMapperX;
import cn.smart.wms.module.wms.dal.dataobject.area.AreaDO;
import org.apache.ibatis.annotations.Mapper;
import cn.smart.wms.module.wms.controller.admin.area.vo.*;

/**
 * 货区 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface WmsAreaMapper extends BaseMapperX<AreaDO> {

    default PageResult<AreaDO> selectPage(AreaPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AreaDO>()
                .eqIfPresent(AreaDO::getAreaCode, reqVO.getAreaCode())
                .likeIfPresent(AreaDO::getAreaName, reqVO.getAreaName())
                .eqIfPresent(AreaDO::getWarehouseId, reqVO.getWarehouseId())
                .eqIfPresent(AreaDO::getAreaType, reqVO.getAreaType())
                .eqIfPresent(AreaDO::getStatus, reqVO.getStatus())
                .eqIfPresent(AreaDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(AreaDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(AreaDO::getId));
    }

}