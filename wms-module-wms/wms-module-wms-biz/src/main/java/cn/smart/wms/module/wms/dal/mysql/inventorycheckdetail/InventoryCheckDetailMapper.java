package cn.smart.wms.module.wms.dal.mysql.inventorycheckdetail;

import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.mybatis.core.mapper.BaseMapperX;
import cn.smart.wms.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.smart.wms.module.wms.controller.admin.inventorycheckdetail.vo.InventoryCheckDetailPageReqVO;
import cn.smart.wms.module.wms.dal.dataobject.inventorycheckdetail.InventoryCheckDetailDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 库存盘点明细 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface InventoryCheckDetailMapper extends BaseMapperX<InventoryCheckDetailDO> {

    default PageResult<InventoryCheckDetailDO> selectPage(InventoryCheckDetailPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<InventoryCheckDetailDO>()
                .eqIfPresent(InventoryCheckDetailDO::getCheckId, reqVO.getCheckId())
                .eqIfPresent(InventoryCheckDetailDO::getItemId, reqVO.getItemId())
                .eqIfPresent(InventoryCheckDetailDO::getLocationId, reqVO.getLocationId())
                .eqIfPresent(InventoryCheckDetailDO::getBookCount, reqVO.getBookCount())
                .eqIfPresent(InventoryCheckDetailDO::getCheckCount, reqVO.getCheckCount())
                .eqIfPresent(InventoryCheckDetailDO::getDifferenceCount, reqVO.getDifferenceCount())
                .eqIfPresent(InventoryCheckDetailDO::getCheckStatus, reqVO.getCheckStatus())
                .eqIfPresent(InventoryCheckDetailDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(InventoryCheckDetailDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(InventoryCheckDetailDO::getId));
    }

}