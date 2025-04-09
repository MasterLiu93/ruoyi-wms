package cn.smart.wms.module.wms.dal.mysql.inventory;

import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.mybatis.core.mapper.BaseMapperX;
import cn.smart.wms.module.wms.controller.admin.inventory.vo.InventoryPageReqVO;
import cn.smart.wms.module.wms.dal.dataobject.inventory.InventoryDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collections;
import java.util.List;

/**
 * 库存 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface InventoryMapper extends BaseMapperX<InventoryDO> {

    InventoryDO selectByIdWithDetails(@Param("id") Long id);

    List<InventoryDO> selectPageWithDetails(@Param("reqVO") InventoryPageReqVO reqVO, 
                                          @Param("offset") Integer offset);

    Long selectPageCount(@Param("reqVO") InventoryPageReqVO reqVO);

    default PageResult<InventoryDO> selectPage(InventoryPageReqVO reqVO) {
        // 计算分页参数
        Integer offset = (reqVO.getPageNo() - 1) * reqVO.getPageSize();
        
        // 查询总数
        Long total = selectPageCount(reqVO);
        if (total == 0) {
            return new PageResult<>(Collections.emptyList(), 0L);
        }
        // 查询分页数据
        List<InventoryDO> list = selectPageWithDetails(reqVO, offset);
        return new PageResult<>(list, total);
    }
    
    InventoryDO selectByWarehouseAndItemAndLocation(@Param("warehouseId") Long warehouseId, 
                                                  @Param("itemId") Long itemId, 
                                                  @Param("locationId") Long locationId);
    
    List<InventoryDO> selectListByItemId(@Param("itemId") Long itemId);
    
    List<InventoryDO> selectListByWarehouseId(@Param("warehouseId") Long warehouseId);
    
    List<InventoryDO> selectListByLocationId(@Param("locationId") Long locationId);
    
    List<InventoryDO> selectListByConditions(@Param("warehouseId") Long warehouseId,
                                           @Param("locationIds") List<Long> locationIds,
                                           @Param("itemIds") List<Long> itemIds);
}