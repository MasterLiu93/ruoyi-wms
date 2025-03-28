package cn.smart.wms.module.wms.dal.mysql.rack;

import java.util.*;

import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.smart.wms.framework.mybatis.core.mapper.BaseMapperX;
import cn.smart.wms.module.wms.dal.dataobject.rack.RackDO;
import org.apache.ibatis.annotations.Mapper;
import cn.smart.wms.module.wms.controller.admin.rack.vo.*;

/**
 * 货架 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface RackMapper extends BaseMapperX<RackDO> {

    default PageResult<RackDO> selectPage(RackPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<RackDO>()
                .eqIfPresent(RackDO::getRackCode, reqVO.getRackCode())
                .likeIfPresent(RackDO::getRackName, reqVO.getRackName())
                .eqIfPresent(RackDO::getAreaId, reqVO.getAreaId())
                .eqIfPresent(RackDO::getRackType, reqVO.getRackType())
                .eqIfPresent(RackDO::getStatus, reqVO.getStatus())
                .eqIfPresent(RackDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(RackDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(RackDO::getId));
    }

}