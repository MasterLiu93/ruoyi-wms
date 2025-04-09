package cn.smart.wms.module.wms.dal.mysql.inventorymovement;

import java.util.*;

import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.mybatis.core.mapper.BaseMapperX;
import cn.smart.wms.module.wms.dal.dataobject.inventorymovement.InventoryMovementDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import cn.smart.wms.module.wms.controller.admin.inventorymovement.vo.*;

/**
 * 库存移动记录 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface InventoryMovementMapper extends BaseMapperX<InventoryMovementDO> {

    InventoryMovementDO selectByIdWithDetails(@Param("id") Long id);

    List<InventoryMovementDO> selectPageWithDetails(@Param("reqVO") InventoryMovementPageReqVO reqVO, 
                                                   @Param("offset") Integer offset);

    Long selectPageCount(@Param("reqVO") InventoryMovementPageReqVO reqVO);

    default PageResult<InventoryMovementDO> selectPage(InventoryMovementPageReqVO reqVO) {
        // 计算分页参数
        Integer offset = (reqVO.getPageNo() - 1) * reqVO.getPageSize();
        
        // 查询总数
        Long total = selectPageCount(reqVO);
        if (total == 0) {
            return new PageResult<>(Collections.emptyList(), 0L);
        }
        
        // 查询分页数据
        List<InventoryMovementDO> list = selectPageWithDetails(reqVO, offset);
        return new PageResult<>(list, total);
    }

}