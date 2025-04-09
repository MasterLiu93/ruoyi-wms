package cn.smart.wms.module.wms.service.warehouse;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Collectors;
import cn.smart.wms.module.wms.controller.admin.warehouse.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.warehouse.WarehouseDO;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.util.object.BeanUtils;

import cn.smart.wms.module.wms.dal.mysql.warehouse.WarehouseMapper;
import cn.smart.wms.module.wms.dal.mysql.area.WmsAreaMapper;

import static cn.smart.wms.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.smart.wms.module.wms.enums.ErrorCodeConstants.*;

/**
 * 仓库 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class WarehouseServiceImpl implements WarehouseService {

    @Resource
    private WarehouseMapper warehouseMapper;
    
    @Resource
    private WmsAreaMapper areaMapper;

    @Override
    public Long createWarehouse(WarehouseSaveReqVO createReqVO) {
        // 校验仓库编码是否唯一
        validateWarehouseCodeUnique(null, createReqVO.getWarehouseCode());
        
        // 插入
        WarehouseDO warehouse = BeanUtils.toBean(createReqVO, WarehouseDO.class);
        warehouseMapper.insert(warehouse);
        // 返回
        return warehouse.getId();
    }

    @Override
    public void updateWarehouse(WarehouseSaveReqVO updateReqVO) {
        // 校验存在
        validateWarehouseExists(updateReqVO.getId());
        // 校验仓库编码是否唯一
        validateWarehouseCodeUnique(updateReqVO.getId(), updateReqVO.getWarehouseCode());
        
        // 更新
        WarehouseDO updateObj = BeanUtils.toBean(updateReqVO, WarehouseDO.class);
        warehouseMapper.updateById(updateObj);
    }

    @Override
    public void deleteWarehouse(Long id) {
        // 校验存在
        validateWarehouseExists(id);
        // 校验是否有关联的货区
        validateWarehouseHasArea(id);
        
        // 删除
        warehouseMapper.deleteById(id);
    }

    private void validateWarehouseExists(Long id) {
        if (warehouseMapper.selectById(id) == null) {
            throw exception(WAREHOUSE_NOT_EXISTS);
        }
    }
    
    /**
     * 校验仓库编码是否唯一
     *
     * @param id 仓库ID
     * @param warehouseCode 仓库编码
     */
    private void validateWarehouseCodeUnique(Long id, String warehouseCode) {
        WarehouseDO warehouse = warehouseMapper.selectByWarehouseCode(warehouseCode);
        if (warehouse == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的仓库
        if (id == null) {
            throw exception(WAREHOUSE_CODE_EXISTS);
        }
        if (!warehouse.getId().equals(id)) {
            throw exception(WAREHOUSE_CODE_EXISTS);
        }
    }
    
    /**
     * 校验仓库下是否有货区
     *
     * @param id 仓库ID
     */
    private void validateWarehouseHasArea(Long id) {
        if (areaMapper.selectCountByWarehouseId(id) > 0) {
            throw exception(WAREHOUSE_HAS_AREA);
        }
    }

    @Override
    public WarehouseDO getWarehouse(Long id) {
        return warehouseMapper.selectById(id);
    }

    @Override
    public PageResult<WarehouseDO> getWarehousePage(WarehousePageReqVO pageReqVO) {
        return warehouseMapper.selectPage(pageReqVO);
    }

    @Override
    public List<WarehouseDO> getWarehouseList() {
        return warehouseMapper.selectList();
    }

    @Override
    public Map<Long, WarehouseDO> getWarehouseMap(Set<Long> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyMap();
        }
        List<WarehouseDO> list = warehouseMapper.selectBatchIds(ids);
        return list.stream().collect(Collectors.toMap(WarehouseDO::getId, warehouse -> warehouse));
    }

}