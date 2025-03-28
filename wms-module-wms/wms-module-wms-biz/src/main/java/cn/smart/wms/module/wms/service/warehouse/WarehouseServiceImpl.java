package cn.smart.wms.module.wms.service.warehouse;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.smart.wms.module.wms.controller.admin.warehouse.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.warehouse.WarehouseDO;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.pojo.PageParam;
import cn.smart.wms.framework.common.util.object.BeanUtils;

import cn.smart.wms.module.wms.dal.mysql.warehouse.WarehouseMapper;

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

    @Override
    public Long createWarehouse(WarehouseSaveReqVO createReqVO) {
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
        // 更新
        WarehouseDO updateObj = BeanUtils.toBean(updateReqVO, WarehouseDO.class);
        warehouseMapper.updateById(updateObj);
    }

    @Override
    public void deleteWarehouse(Long id) {
        // 校验存在
        validateWarehouseExists(id);
        // 删除
        warehouseMapper.deleteById(id);
    }

    private void validateWarehouseExists(Long id) {
        if (warehouseMapper.selectById(id) == null) {
            throw exception(WAREHOUSE_NOT_EXISTS);
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

}