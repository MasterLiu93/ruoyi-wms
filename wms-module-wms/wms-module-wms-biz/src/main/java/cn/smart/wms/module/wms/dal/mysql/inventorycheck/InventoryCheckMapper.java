package cn.smart.wms.module.wms.dal.mysql.inventorycheck;

import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.mybatis.core.mapper.BaseMapperX;
import cn.smart.wms.module.wms.controller.admin.inventorycheck.vo.InventoryCheckPageReqVO;
import cn.smart.wms.module.wms.dal.dataobject.inventorycheck.InventoryCheckDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collections;
import java.util.List;

/**
 * 库存盘点单 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface InventoryCheckMapper extends BaseMapperX<InventoryCheckDO> {

    InventoryCheckDO selectByIdWithDetails(@Param("id") Long id);

    List<InventoryCheckDO> selectPageWithDetails(@Param("reqVO") InventoryCheckPageReqVO reqVO, 
                                                @Param("offset") Integer offset);

    Long selectPageCount(@Param("reqVO") InventoryCheckPageReqVO reqVO);

    default PageResult<InventoryCheckDO> selectPage(InventoryCheckPageReqVO reqVO) {
        // 计算分页参数
        Integer offset = (reqVO.getPageNo() - 1) * reqVO.getPageSize();
        
        // 查询总数
        Long total = selectPageCount(reqVO);
        if (total == 0) {
            return new PageResult<>(Collections.emptyList(), 0L);
        }
        
        // 查询分页数据
        List<InventoryCheckDO> list = selectPageWithDetails(reqVO, offset);
        return new PageResult<>(list, total);
    }

}