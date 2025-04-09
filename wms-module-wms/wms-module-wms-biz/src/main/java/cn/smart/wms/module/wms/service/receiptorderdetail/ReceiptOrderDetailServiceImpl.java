package cn.smart.wms.module.wms.service.receiptorderdetail;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.util.object.BeanUtils;
import cn.smart.wms.module.wms.controller.admin.item.vo.ItemPageReqVO;
import cn.smart.wms.module.wms.controller.admin.receiptorderdetail.vo.ReceiptOrderDetailImportVO;
import cn.smart.wms.module.wms.controller.admin.receiptorderdetail.vo.ReceiptOrderDetailPageReqVO;
import cn.smart.wms.module.wms.controller.admin.receiptorderdetail.vo.ReceiptOrderDetailSaveReqVO;
import cn.smart.wms.module.wms.dal.dataobject.area.AreaDO;
import cn.smart.wms.module.wms.dal.dataobject.item.ItemDO;
import cn.smart.wms.module.wms.dal.dataobject.location.LocationDO;
import cn.smart.wms.module.wms.dal.dataobject.rack.RackDO;
import cn.smart.wms.module.wms.dal.dataobject.receiptorder.ReceiptOrderDO;
import cn.smart.wms.module.wms.dal.dataobject.receiptorderdetail.ReceiptOrderDetailDO;
import cn.smart.wms.module.wms.dal.dataobject.warehouse.WarehouseDO;
import cn.smart.wms.module.wms.dal.mysql.receiptorder.ReceiptOrderMapper;
import cn.smart.wms.module.wms.dal.mysql.receiptorderdetail.ReceiptOrderDetailMapper;
import cn.smart.wms.module.wms.service.area.WmsAreaService;
import cn.smart.wms.module.wms.service.item.ItemService;
import cn.smart.wms.module.wms.service.location.LocationService;
import cn.smart.wms.module.wms.service.rack.RackService;
import cn.smart.wms.module.wms.service.receiptorder.ReceiptOrderService;
import cn.smart.wms.module.wms.service.receiptorderdetail.impl.ExcelDropdownHandler;
import cn.smart.wms.module.wms.service.warehouse.WarehouseService;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.converters.longconverter.LongStringConverter;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static cn.smart.wms.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.smart.wms.module.wms.enums.ErrorCodeConstants.*;

/**
 * 入库单明细 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class ReceiptOrderDetailServiceImpl implements ReceiptOrderDetailService {

    @Resource
    private ReceiptOrderDetailMapper receiptOrderDetailMapper;
    
    @Resource
    private ItemService itemService;
    
    @Resource
    private WmsAreaService areaService;
    
    @Resource
    private RackService rackService;
    
    @Resource
    private LocationService locationService;
    
    // 移除直接引用ReceiptOrderService，改用ApplicationContext获取
    @Autowired
    private ApplicationContext applicationContext;
    
    @Resource
    private ReceiptOrderMapper receiptOrderMapper;
    
    @Resource
    private WarehouseService warehouseService;

    @Override
    public Long createReceiptOrderDetail(ReceiptOrderDetailSaveReqVO createReqVO) {
        // 记录入库单明细请求VO
        System.out.println("创建入库单明细请求VO: " + createReqVO);
        
        // 插入
        ReceiptOrderDetailDO receiptOrderDetail = BeanUtils.toBean(createReqVO, ReceiptOrderDetailDO.class);
        
        // 确保关键字段不为null
        if (receiptOrderDetail.getAreaId() == null) {
            System.out.println("警告: areaId为null，可能是前端未传递或字段名不匹配");
        }
        
        if (receiptOrderDetail.getRackId() == null) {
            System.out.println("警告: rackId为null，可能是前端未传递或字段名不匹配");
        }
        
        // 记录最终转换后的DO对象
        System.out.println("转换后的DO对象: " + receiptOrderDetail);
        
        receiptOrderDetailMapper.insert(receiptOrderDetail);
        // 返回
        return receiptOrderDetail.getId();
    }

    @Override
    public void updateReceiptOrderDetail(ReceiptOrderDetailSaveReqVO updateReqVO) {
        // 记录更新入库单明细请求VO
        System.out.println("更新入库单明细请求VO: " + updateReqVO);
        
        // 校验存在
        validateReceiptOrderDetailExists(updateReqVO.getId());
        
        // 更新
        ReceiptOrderDetailDO updateObj = BeanUtils.toBean(updateReqVO, ReceiptOrderDetailDO.class);
        
        // 确保关键字段不为null
        if (updateObj.getAreaId() == null) {
            System.out.println("警告: 更新时areaId为null，可能是前端未传递或字段名不匹配");
        }
        
        if (updateObj.getRackId() == null) {
            System.out.println("警告: 更新时rackId为null，可能是前端未传递或字段名不匹配");
        }
        
        // 记录最终转换后的DO对象
        System.out.println("更新时转换后的DO对象: " + updateObj);
        
        receiptOrderDetailMapper.updateById(updateObj);
    }

    @Override
    public void deleteReceiptOrderDetail(Long id) {
        // 校验存在
        validateReceiptOrderDetailExists(id);
        // 删除
        receiptOrderDetailMapper.deleteById(id);
    }

    private void validateReceiptOrderDetailExists(Long id) {
        if (receiptOrderDetailMapper.selectById(id) == null) {
            throw exception(RECEIPT_ORDER_DETAIL_NOT_EXISTS);
        }
    }

    @Override
    public ReceiptOrderDetailDO getReceiptOrderDetail(Long id) {
        ReceiptOrderDetailDO detail = receiptOrderDetailMapper.selectById(id);
        if (detail != null) {
            // 填充物料信息
            setItemInfo(detail);
            
            // 填充货区、货架、库位信息
            setLocationInfo(detail);
            
            // 填充仓库信息
            setWarehouseInfo(detail);
        }
        return detail;
    }

    @Override
    public PageResult<ReceiptOrderDetailDO> getReceiptOrderDetailPage(ReceiptOrderDetailPageReqVO pageReqVO) {
        PageResult<ReceiptOrderDetailDO> pageResult = receiptOrderDetailMapper.selectPage(pageReqVO);
        
        // 获取关联数据
        if (!pageResult.getList().isEmpty()) {
            // 收集相关ID
            Set<Long> itemIds = pageResult.getList().stream()
                    .map(ReceiptOrderDetailDO::getItemId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            
            Set<Long> areaIds = pageResult.getList().stream()
                    .map(ReceiptOrderDetailDO::getAreaId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            
            Set<Long> rackIds = pageResult.getList().stream()
                    .map(ReceiptOrderDetailDO::getRackId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            
            Set<Long> locationIds = pageResult.getList().stream()
                    .map(ReceiptOrderDetailDO::getLocationId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            
            Set<Long> orderIds = pageResult.getList().stream()
                    .map(ReceiptOrderDetailDO::getReceiptOrderId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            
            // 批量查询关联数据
            Map<Long, ItemDO> itemMap = new HashMap<>();
            if (!itemIds.isEmpty()) {
                List<ItemDO> items = itemService.getItemList(itemIds);
                itemMap = items.stream().collect(Collectors.toMap(ItemDO::getId, item -> item));
            }
            
            Map<Long, AreaDO> areaMap = new HashMap<>();
            if (!areaIds.isEmpty()) {
                List<AreaDO> areas = areaService.getAreaList(areaIds);
                areaMap = areas.stream().collect(Collectors.toMap(AreaDO::getId, area -> area));
            }
            
            Map<Long, RackDO> rackMap = new HashMap<>();
            if (!rackIds.isEmpty()) {
                List<RackDO> racks = rackService.getRackList(rackIds);
                rackMap = racks.stream().collect(Collectors.toMap(RackDO::getId, rack -> rack));
            }
            
            Map<Long, LocationDO> locationMap = new HashMap<>();
            if (!locationIds.isEmpty()) {
                List<LocationDO> locations = locationService.getLocationListByIds(locationIds);
                locationMap = locations.stream().collect(Collectors.toMap(LocationDO::getId, location -> location));
            }
            
            Map<Long, ReceiptOrderDO> orderMap = new HashMap<>();
            Set<Long> warehouseIds = new HashSet<>();
            if (!orderIds.isEmpty()) {
                // 不直接使用ReceiptOrderService，避免循环依赖
                List<ReceiptOrderDO> orders = receiptOrderMapper.selectBatchIds(orderIds);
                for (ReceiptOrderDO order : orders) {
                    orderMap.put(order.getId(), order);
                    if (order.getWarehouseId() != null) {
                        warehouseIds.add(order.getWarehouseId());
                    }
                }
            }
            
            Map<Long, WarehouseDO> warehouseMap = new HashMap<>();
            if (!warehouseIds.isEmpty()) {
                warehouseMap = warehouseService.getWarehouseMap(warehouseIds);
            }
            
            // 填充数据
            for (ReceiptOrderDetailDO detail : pageResult.getList()) {
                // 物料信息
                if (detail.getItemId() != null && itemMap.containsKey(detail.getItemId())) {
                    ItemDO item = itemMap.get(detail.getItemId());
                    detail.setItemCode(item.getItemCode());
                    detail.setItemName(item.getItemName());
                    detail.setSpec(item.getSpec());
                    detail.setUnit(item.getUnit());
                }
                
                // 货区信息
                if (detail.getAreaId() != null && areaMap.containsKey(detail.getAreaId())) {
                    AreaDO area = areaMap.get(detail.getAreaId());
                    detail.setAreaName(area.getAreaName());
                }
                
                // 货架信息
                if (detail.getRackId() != null && rackMap.containsKey(detail.getRackId())) {
                    RackDO rack = rackMap.get(detail.getRackId());
                    detail.setRackName(rack.getRackName());
                }
                
                // 库位信息
                if (detail.getLocationId() != null && locationMap.containsKey(detail.getLocationId())) {
                    LocationDO location = locationMap.get(detail.getLocationId());
                    detail.setLocationName(location.getLocationName());
                }
                
                // 仓库信息
                if (detail.getReceiptOrderId() != null && orderMap.containsKey(detail.getReceiptOrderId())) {
                    ReceiptOrderDO order = orderMap.get(detail.getReceiptOrderId());
                    Long warehouseId = order.getWarehouseId();
                    detail.setWarehouseId(warehouseId);
                    
                    if (warehouseId != null && warehouseMap.containsKey(warehouseId)) {
                        WarehouseDO warehouse = warehouseMap.get(warehouseId);
                        detail.setWarehouseName(warehouse.getWarehouseName());
                    }
                }
            }
        }
        
        return pageResult;
    }

    @Override
    public List<ReceiptOrderDetailDO> getReceiptOrderDetailListByOrderId(Long receiptOrderId) {
        List<ReceiptOrderDetailDO> details = receiptOrderDetailMapper.selectList(
            new LambdaQueryWrapper<ReceiptOrderDetailDO>()
                .eq(ReceiptOrderDetailDO::getReceiptOrderId, receiptOrderId)
                .orderByAsc(ReceiptOrderDetailDO::getId)
        );
        
        // 填充关联数据
        if (!details.isEmpty()) {
            for (ReceiptOrderDetailDO detail : details) {
                // 填充物料信息
                setItemInfo(detail);
                
                // 填充货区、货架、库位信息
                setLocationInfo(detail);
                
                // 填充仓库信息
                setWarehouseInfo(detail);
            }
        }
        
        return details;
    }
    
    /**
     * 获取ReceiptOrderService，避免循环依赖
     */
    private ReceiptOrderService getReceiptOrderService() {
        return applicationContext.getBean(ReceiptOrderService.class);
    }
    
    /**
     * 填充物料信息
     *
     * @param detail 入库单明细
     */
    private void setItemInfo(ReceiptOrderDetailDO detail) {
        if (detail.getItemId() != null) {
            ItemDO item = itemService.getItem(detail.getItemId());
            if (item != null) {
                detail.setItemCode(item.getItemCode());
                detail.setItemName(item.getItemName());
                detail.setSpec(item.getSpec());
                detail.setUnit(item.getUnit());
            }
        }
    }
    
    /**
     * 填充货区、货架、库位信息
     *
     * @param detail 入库单明细
     */
    private void setLocationInfo(ReceiptOrderDetailDO detail) {
        // 填充货区信息
        if (detail.getAreaId() != null) {
            AreaDO area = areaService.getArea(detail.getAreaId());
            if (area != null) {
                detail.setAreaName(area.getAreaName());
            }
        }
        
        // 填充货架信息
        if (detail.getRackId() != null) {
            RackDO rack = rackService.getRack(detail.getRackId());
            if (rack != null) {
                detail.setRackName(rack.getRackName());
            }
        }
        
        // 填充库位信息
        if (detail.getLocationId() != null) {
            LocationDO location = locationService.getLocation(detail.getLocationId());
            if (location != null) {
                detail.setLocationName(location.getLocationName());
            }
        }
    }
    
    /**
     * 填充仓库信息
     *
     * @param detail 入库单明细
     */
    private void setWarehouseInfo(ReceiptOrderDetailDO detail) {
        if (detail.getReceiptOrderId() != null) {
            // 使用mapper直接查询，避免循环依赖
            ReceiptOrderDO order = receiptOrderMapper.selectById(detail.getReceiptOrderId());
            if (order != null && order.getWarehouseId() != null) {
                detail.setWarehouseId(order.getWarehouseId());
                
                WarehouseDO warehouse = warehouseService.getWarehouse(order.getWarehouseId());
                if (warehouse != null) {
                    detail.setWarehouseName(warehouse.getWarehouseName());
                }
            }
        }
    }

    @Override
    public void exportEnhancedTemplate(Long warehouseId, HttpServletResponse response) throws IOException {
        System.out.println("开始导出增强版Excel模板，仓库ID: " + warehouseId);
        
        try {
            // 设置CORS相关响应头
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            
            // 设置正确的Content-Type，确保是纯粹的二进制格式，不含charset
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            
            // 设置下载文件名
            String fileName = "入库单明细导入模板_" + System.currentTimeMillis() + ".xlsx";
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name()).replaceAll("\\+", "%20");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");
            
            // 设置其他通用响应头
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "0");
            
            // 打印当前响应头，用于调试
            System.out.println("设置响应头: Content-Type=" + response.getContentType());
            System.out.println("设置响应头: Content-Disposition=" + response.getHeader("Content-Disposition"));
            
            // 获取数据
            List<ItemDO> items = getItems();
        List<AreaDO> areas = new ArrayList<>();
        List<RackDO> racks = new ArrayList<>();
        List<LocationDO> locations = new ArrayList<>();
        
        // 如果指定了仓库ID，则只获取该仓库下的货区、货架和库位
        if (warehouseId != null) {
            areas = areaService.getAreaListByWarehouseId(warehouseId);
                System.out.println("获取到货区列表，数量: " + areas.size());
            
            // 获取该仓库下所有货区的货架
            if (!areas.isEmpty()) {
                List<Long> areaIds = areas.stream().map(AreaDO::getId).collect(Collectors.toList());
                // 获取指定货区下的货架列表
                for (Long areaId : areaIds) {
                        List<RackDO> areaRacks = rackService.getRackListByAreaId(areaId);
                        racks.addAll(areaRacks);
                }
                    System.out.println("获取到货架列表，数量: " + racks.size());
                
                // 获取该仓库下所有货架的库位
                if (!racks.isEmpty()) {
                    List<Long> rackIds = racks.stream().map(RackDO::getId).collect(Collectors.toList());
                    // 获取指定货架下的库位列表
                    for (Long rackId : rackIds) {
                            List<LocationDO> rackLocations = locationService.getLocationList(rackId);
                            locations.addAll(rackLocations);
                    }
                        System.out.println("获取到库位列表，数量: " + locations.size());
                }
            }
        } else {
            // 如果没有指定仓库，则获取所有的货区、货架和库位
            areas = areaService.getAreaList(null);
            racks = rackService.getRackList(null);
                System.out.println("未指定仓库，获取全部货区和货架");
            // 由于无法一次性获取所有库位，这里不设置库位下拉选项
            locations = new ArrayList<>();
        }
        
            // 提取带ID的名称列表，用于生成下拉选项
            List<String> itemNamesWithId = items.stream()
                .map(item -> String.format("%s(%d)", item.getItemName(), item.getId()))
                .collect(Collectors.toList());
                
            List<String> areaNamesWithId = areas.stream()
                .map(area -> String.format("%s(%d)", area.getAreaName(), area.getId()))
                .collect(Collectors.toList());
                
            List<String> rackNamesWithId = racks.stream()
                .map(rack -> String.format("%s(%d)", rack.getRackName(), rack.getId()))
                .collect(Collectors.toList());
                
            List<String> locationNamesWithId = locations.stream()
                .map(location -> String.format("%s(%d)", location.getLocationName(), location.getId()))
                .collect(Collectors.toList());
            
            System.out.println("下拉选项数据统计:");
            System.out.println("- 物料名称(ID): " + itemNamesWithId.size());
            System.out.println("- 货区名称(ID): " + areaNamesWithId.size());
            System.out.println("- 货架名称(ID): " + rackNamesWithId.size());
            System.out.println("- 库位名称(ID): " + locationNamesWithId.size());
            
            // 打印ReceiptOrderDetailImportVO类的字段与Excel列的映射关系
            System.out.println("注意: ReceiptOrderDetailImportVO与Excel列映射:");
            System.out.println("- @ExcelProperty(\"序号\") -> index");
            System.out.println("- @ExcelProperty(\"物料名称(格式: 物料名称(物料ID))\") -> itemName");
            System.out.println("- @ExcelProperty(\"计划数量\") -> planCount");
            System.out.println("- @ExcelProperty(\"实际入库数量\") -> realCount");
            System.out.println("- @ExcelProperty(\"货区\") -> areaName");
            System.out.println("- @ExcelProperty(\"货架\") -> rackName");
            System.out.println("- @ExcelProperty(\"入库库位\") -> locationName");
            System.out.println("- @ExcelProperty(\"入库单价\") -> price");
            System.out.println("- @ExcelProperty(\"质检状态\") -> qualityStatus");
            System.out.println("- @ExcelProperty(\"状态\") -> status");
            System.out.println("- @ExcelProperty(\"规格\") -> spec");
            System.out.println("- @ExcelProperty(\"单位\") -> unit");
            System.out.println("- @ExcelProperty(\"备注\") -> remark");
            
            // 创建一个示例数据，用于展示模板填写方式
            List<ReceiptOrderDetailImportVO> list = new ArrayList<>();
            if (!items.isEmpty() && !areas.isEmpty() && !racks.isEmpty() && !locations.isEmpty()) {
                ReceiptOrderDetailImportVO example = new ReceiptOrderDetailImportVO();
                example.setIndex(1);
                
                ItemDO sampleItem = items.get(0);
                example.setItemName(String.format("%s(%d)", sampleItem.getItemName(), sampleItem.getId()));
                // 移除物料编码
                // example.setItemCode(sampleItem.getItemCode());
                example.setSpec(sampleItem.getSpec());
                example.setUnit(sampleItem.getUnit());
                
                example.setPlanCount(100);
                example.setRealCount(90);
                
                AreaDO sampleArea = areas.get(0);
                example.setAreaName(String.format("%s(%d)", sampleArea.getAreaName(), sampleArea.getId()));
                
                RackDO sampleRack = racks.get(0);
                example.setRackName(String.format("%s(%d)", sampleRack.getRackName(), sampleRack.getId()));
                
                LocationDO sampleLocation = locations.get(0);
                example.setLocationName(String.format("%s(%d)", sampleLocation.getLocationName(), sampleLocation.getId()));
                
                example.setPrice(new BigDecimal("10.50"));
                example.setQualityStatus(1); // 1表示合格
                example.setStatus(1); // 1表示待入库
                example.setRemark("这是一个示例行，导入时请删除或替换为实际数据");
                
                list.add(example);
            }
            
            // 创建下拉选项Map (索引对应VO中的字段顺序)
            Map<Integer, List<String>> selectMap = new HashMap<>();
            selectMap.put(1, itemNamesWithId); // 物料名称列，索引从0开始，对应第2列
            // 移除物料编码列的下拉选项
            // 调整后续列的索引
            selectMap.put(4, areaNamesWithId); // 货区列，对应第5列（因为移除了物料编码列）
            selectMap.put(5, rackNamesWithId); // 货架列，对应第6列
            selectMap.put(6, locationNamesWithId); // 库位列，对应第7列
            
            // 添加质检状态和入库状态的下拉选项
            List<String> qualityStatusOptions = Arrays.asList("0-不合格", "1-合格");
            List<String> statusOptions = Arrays.asList("0-未入库", "1-待入库", "2-已入库", "3-已取消");
            selectMap.put(8, qualityStatusOptions); // 索引减一
            selectMap.put(9, statusOptions); // 索引减一
            
            System.out.println("创建ExcelDropdownHandler实例");
            // 使用ExcelDropdownHandler处理下拉选项
            ExcelDropdownHandler dropdownHandler = new ExcelDropdownHandler(selectMap, "数据字典");
            
            System.out.println("准备写入Excel数据...");
            
            // 使用最简单的EasyExcel API，减少可能的问题
            try (com.alibaba.excel.ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).build()) {
                // 创建一个工作表
                com.alibaba.excel.write.metadata.WriteSheet writeSheet = EasyExcel.writerSheet("数据")
                        .head(ReceiptOrderDetailImportVO.class)
                        .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                        .registerWriteHandler(dropdownHandler)
                        .registerConverter(new LongStringConverter())
                        .build();
                
                // 写入数据
                excelWriter.write(list, writeSheet);
                // 因为使用try-with-resources，这里不需要手动finish()
                
                // 确保输出流被刷新
                response.getOutputStream().flush();
            }
            
            System.out.println("Excel导出完成");
        } catch (Exception e) {
            System.err.println("导出Excel模板时发生错误: " + e.getMessage());
            e.printStackTrace();
            
            // 如果已经开始写入响应，则无法再修改状态码，直接抛出异常
            if (response.isCommitted()) {
                throw new IOException("导出Excel模板失败: " + e.getMessage(), e);
            }
            
            // 如果还没有写入响应，则返回错误信息
            response.reset();
            response.setContentType("text/plain;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("导出Excel模板失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取物料列表，如果为空则创建一些示例数据
     */
    private List<ItemDO> getItems() {
        List<ItemDO> items = itemService.getItemList(null);
        System.out.println("获取到物料列表，数量: " + items.size());
        
        // 如果物料列表为空，尝试使用分页方式获取
        if (items.isEmpty()) {
            System.out.println("物料列表为空，尝试使用分页方式获取...");
            try {
                // 使用分页接口获取所有物料
                ItemPageReqVO pageReqVO = new ItemPageReqVO();
                pageReqVO.setPageNo(1);
                pageReqVO.setPageSize(1000); // 获取足够多的数据
                PageResult<ItemDO> itemPage = itemService.getItemPage(pageReqVO);
                if (itemPage != null && itemPage.getList() != null) {
                    items = itemPage.getList();
                    System.out.println("分页方式获取物料成功，数量: " + items.size());
                }
            } catch (Exception e) {
                System.out.println("分页方式获取物料失败: " + e.getMessage());
            }
        }
        
        // 如果仍然没有物料数据，创建一些样例数据
        if (items.isEmpty()) {
            System.out.println("仍未获取到物料数据，创建样例数据用于展示...");
            items = new ArrayList<>();
            // 添加一些示例物料，便于用户理解Excel模板格式
            ItemDO sampleItem1 = new ItemDO();
            sampleItem1.setId(1L);
            sampleItem1.setItemCode("SAMPLE001");
            sampleItem1.setItemName("示例物料1");
            sampleItem1.setSpec("规格示例");
            sampleItem1.setUnit("个");
            
            ItemDO sampleItem2 = new ItemDO();
            sampleItem2.setId(2L);
            sampleItem2.setItemCode("SAMPLE002");
            sampleItem2.setItemName("示例物料2");
            sampleItem2.setSpec("规格示例2");
            sampleItem2.setUnit("箱");
            
            items.add(sampleItem1);
            items.add(sampleItem2);
            System.out.println("创建了 " + items.size() + " 个样例物料数据");
        }
        
        return items;
    }

    @Override
    public List<ReceiptOrderDetailDO> importReceiptOrderDetails(List<ReceiptOrderDetailImportVO> importVOs, boolean save) {
        if (CollUtil.isEmpty(importVOs)) {
            return Collections.emptyList();
        }
        
        System.out.println("开始导入数据，记录数: " + importVOs.size());
        
        // 转换Excel数据为DO对象，直接从名称中提取ID
        List<ReceiptOrderDetailDO> detailList = new ArrayList<>(importVOs.size());
        
        for (int i = 0; i < importVOs.size(); i++) {
            ReceiptOrderDetailImportVO importVO = importVOs.get(i);
            ReceiptOrderDetailDO detail = new ReceiptOrderDetailDO();
            
            // 解析物料名称(ID)格式
            String itemName = importVO.getItemName();
            if (StrUtil.isNotEmpty(itemName) && itemName.contains("(") && itemName.endsWith(")")) {
                try {
                    // 提取ID部分
                    String idStr = itemName.substring(itemName.lastIndexOf("(") + 1, itemName.length() - 1);
                    Long itemId = Long.parseLong(idStr);
                    detail.setItemId(itemId);
                    
                    // 提取纯名称部分
                    String pureName = itemName.substring(0, itemName.lastIndexOf("(")).trim();
                    detail.setItemName(pureName);
                    
                    System.out.println("第 " + (i+1) + " 行, 物料名称: " + pureName + ", ID: " + itemId);
                } catch (Exception e) {
                    System.out.println("第 " + (i+1) + " 行, 物料解析失败: " + e.getMessage());
                    detail.setItemName(itemName);
                }
            } else {
                detail.setItemName(itemName);
            }
            
            // 设置计划数量
            detail.setPlanCount(importVO.getPlanCount() != null ? importVO.getPlanCount() : 0);
            
            // 设置实际入库数量
            detail.setRealCount(importVO.getRealCount() != null ? importVO.getRealCount() : 0);
            
            // 解析货区名称(ID)格式
            String areaName = importVO.getAreaName();
            if (StrUtil.isNotEmpty(areaName) && areaName.contains("(") && areaName.endsWith(")")) {
                try {
                    // 提取ID部分
                    String idStr = areaName.substring(areaName.lastIndexOf("(") + 1, areaName.length() - 1);
                    Long areaId = Long.parseLong(idStr);
                    detail.setAreaId(areaId);
                    
                    // 提取纯名称部分
                    String pureName = areaName.substring(0, areaName.lastIndexOf("(")).trim();
                    detail.setAreaName(pureName);
                    
                    System.out.println("第 " + (i+1) + " 行, 货区名称: " + pureName + ", ID: " + areaId);
                } catch (Exception e) {
                    System.out.println("第 " + (i+1) + " 行, 货区解析失败: " + e.getMessage());
                    detail.setAreaName(areaName);
                }
            } else {
                detail.setAreaName(areaName);
            }
            
            // 解析货架名称(ID)格式
            String rackName = importVO.getRackName();
            if (StrUtil.isNotEmpty(rackName) && rackName.contains("(") && rackName.endsWith(")")) {
                try {
                    // 提取ID部分
                    String idStr = rackName.substring(rackName.lastIndexOf("(") + 1, rackName.length() - 1);
                    Long rackId = Long.parseLong(idStr);
                    detail.setRackId(rackId);
                    
                    // 提取纯名称部分
                    String pureName = rackName.substring(0, rackName.lastIndexOf("(")).trim();
                    detail.setRackName(pureName);
                    
                    System.out.println("第 " + (i+1) + " 行, 货架名称: " + pureName + ", ID: " + rackId);
                } catch (Exception e) {
                    System.out.println("第 " + (i+1) + " 行, 货架解析失败: " + e.getMessage());
                    detail.setRackName(rackName);
                }
            } else {
                detail.setRackName(rackName);
            }
            
            // 解析库位名称(ID)格式
            String locationName = importVO.getLocationName();
            if (StrUtil.isNotEmpty(locationName) && locationName.contains("(") && locationName.endsWith(")")) {
                try {
                    // 提取ID部分
                    String idStr = locationName.substring(locationName.lastIndexOf("(") + 1, locationName.length() - 1);
                    Long locationId = Long.parseLong(idStr);
                    detail.setLocationId(locationId);
                    
                    // 提取纯名称部分
                    String pureName = locationName.substring(0, locationName.lastIndexOf("(")).trim();
                    detail.setLocationName(pureName);
                    
                    System.out.println("第 " + (i+1) + " 行, 库位名称: " + pureName + ", ID: " + locationId);
                } catch (Exception e) {
                    System.out.println("第 " + (i+1) + " 行, 库位解析失败: " + e.getMessage());
                    detail.setLocationName(locationName);
                }
            } else {
                detail.setLocationName(locationName);
            }
            
            // 设置单价
            detail.setPrice(importVO.getPrice());
            
            // 设置质检状态
            detail.setQualityStatus(importVO.getQualityStatus() != null ? importVO.getQualityStatus() : 1); // 默认合格
            
            // 设置状态
            detail.setStatus(importVO.getStatus() != null ? importVO.getStatus() : 0); // 默认未入库
            
            // 设置规格和单位
            detail.setSpec(importVO.getSpec());
            detail.setUnit(importVO.getUnit());
            
            // 设置备注
            detail.setRemark(importVO.getRemark());
            
            // 添加到列表
            detailList.add(detail);
        }
        
        // 如果需要保存，则批量插入
        if (save && !detailList.isEmpty()) {
            for (ReceiptOrderDetailDO detail : detailList) {
                receiptOrderDetailMapper.insert(detail);
            }
        }
        
        return detailList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveReceiptOrderDetails(Long receiptOrderId, List<ReceiptOrderDetailDO> details) {
        if (CollUtil.isEmpty(details)) {
            return;
        }
        
        // 校验入库单是否存在
        ReceiptOrderDO receiptOrder = receiptOrderMapper.selectById(receiptOrderId);
        if (receiptOrder == null) {
            throw exception(RECEIPT_ORDER_NOT_EXISTS);
        }
        
        // 只有草稿或审核中的入库单可以导入明细
        if (receiptOrder.getOrderStatus() > 1) {
            throw exception(RECEIPT_ORDER_STATUS_ERROR);
        }
        
        // 批量插入数据
        for (ReceiptOrderDetailDO detail : details) {
            // 确保使用正确的入库单ID
            detail.setReceiptOrderId(receiptOrderId);
            receiptOrderDetailMapper.insert(detail);
        }
        
        // 更新入库单的总数量和总金额
        updateReceiptOrderTotal(receiptOrderId);
    }
    
    /**
     * 更新入库单的总数量和总金额
     */
    @Override
    public void updateReceiptOrderTotal(Long receiptOrderId) {
        // 查询所有明细
        List<ReceiptOrderDetailDO> details = receiptOrderDetailMapper.selectList(
            new LambdaQueryWrapper<ReceiptOrderDetailDO>()
                .eq(ReceiptOrderDetailDO::getReceiptOrderId, receiptOrderId)
        );
        
        if (!details.isEmpty()) {
            int totalPlanCount = 0;
            int totalRealCount = 0;
            BigDecimal totalAmount = BigDecimal.ZERO;
            
            for (ReceiptOrderDetailDO detail : details) {
                // 计算总计划数量
                int planCount = detail.getPlanCount() != null ? detail.getPlanCount() : 0;
                totalPlanCount += planCount;
                
                // 计算总实际入库数量（用于日志，但不影响更新）
                int realCount = detail.getRealCount() != null ? detail.getRealCount() : 0;
                totalRealCount += realCount;
                
                // 计算总金额
                if (detail.getPlanCount() != null && detail.getPrice() != null) {
                    BigDecimal itemAmount = detail.getPrice().multiply(new BigDecimal(detail.getPlanCount()));
                    totalAmount = totalAmount.add(itemAmount);
                }
            }
            
            // 更新入库单的总计划数量和总金额，但不修改明细和入库状态
            ReceiptOrderDO updateObj = new ReceiptOrderDO();
            updateObj.setId(receiptOrderId);
            updateObj.setTotalCount(totalPlanCount);
            updateObj.setTotalAmount(totalAmount);
            
            receiptOrderMapper.updateById(updateObj);
            
            log.info("更新入库单总量: 入库单ID={}, 总计划数量={}, 总实际入库数量={}, 总金额={}",
                    receiptOrderId, totalPlanCount, totalRealCount, totalAmount);
        }
    }

    /**
     * 导入入库单明细数据并关联到指定入库单
     *
     * @param receiptOrderId 入库单ID
     * @param importVOs 导入的Excel数据
     * @return 关联后的明细DO列表
     */
        @Override
    public List<ReceiptOrderDetailDO> importReceiptOrderDetails(Long receiptOrderId, List<ReceiptOrderDetailImportVO> importVOs) {
        if (CollUtil.isEmpty(importVOs)) {
            return Collections.emptyList();
        }
        
        // 校验入库单是否存在
        ReceiptOrderDO receiptOrder = receiptOrderMapper.selectById(receiptOrderId);
        if (receiptOrder == null) {
            throw exception(RECEIPT_ORDER_NOT_EXISTS);
        }
        
        // 只有草稿或审核中的入库单可以导入明细
        if (receiptOrder.getOrderStatus() > 1) {
            throw exception(RECEIPT_ORDER_STATUS_ERROR);
        }
        
        // 解析导入数据
        List<ReceiptOrderDetailDO> detailList = importReceiptOrderDetails(importVOs, false);
        
        // 关联入库单ID并设置仓库信息
        for (ReceiptOrderDetailDO detail : detailList) {
            detail.setReceiptOrderId(receiptOrderId);
            detail.setWarehouseId(receiptOrder.getWarehouseId());
            
            WarehouseDO warehouse = warehouseService.getWarehouse(receiptOrder.getWarehouseId());
            if (warehouse != null) {
                detail.setWarehouseName(warehouse.getWarehouseName());
            }
        }
        
        return detailList;
    }

    @Override
    public List<ReceiptOrderDetailDO> importReceiptOrderDetails(InputStream inputStream, boolean save) throws IOException {
        try {
            // 使用Map方式读取Excel
            List<Map<Integer, String>> dataList = EasyExcel.read(inputStream)
                .sheet()
                .doReadSync();
            
            System.out.println("读取到Excel数据行数：" + (dataList != null ? dataList.size() : 0));
            
            if (dataList == null || dataList.isEmpty()) {
                System.out.println("Excel文件为空或无有效数据");
                return Collections.emptyList();
            }
            
            // 打印所有行数据
            for (int i = 0; i < dataList.size(); i++) {
                System.out.println("行 " + i + " 原始数据: " + dataList.get(i));
            }
            
            // 第一行通常是表头，第二行开始是数据
            if (dataList.size() > 1) {
                dataList.remove(0); // 移除表头行
                
                // 将Map数据转换为VO对象
                List<ReceiptOrderDetailImportVO> importVOs = new ArrayList<>();
                
                for (int i = 0; i < dataList.size(); i++) {
                    Map<Integer, String> rowData = dataList.get(i);
                    ReceiptOrderDetailImportVO vo = new ReceiptOrderDetailImportVO();
                    
                    // 手动设置字段值，索引对应Excel的列
                    if (rowData.get(0) != null) {
                        try {
                            vo.setIndex(Integer.parseInt(rowData.get(0)));
                        } catch (NumberFormatException e) {
                            // 忽略格式错误
                        }
                    }
                    
                    vo.setItemName(rowData.get(1)); // 物料名称(格式：物料名称(物料ID))
                    
                    if (rowData.get(2) != null) {
                        try {
                            vo.setPlanCount(Integer.parseInt(rowData.get(2)));
                        } catch (NumberFormatException e) {
                            // 忽略格式错误
                        }
                    }
                    
                    if (rowData.get(3) != null) {
                        try {
                            vo.setRealCount(Integer.parseInt(rowData.get(3)));
                        } catch (NumberFormatException e) {
                            // 忽略格式错误
                        }
                    }
                    
                    vo.setAreaName(rowData.get(4)); // 货区
                    vo.setRackName(rowData.get(5)); // 货架
                    vo.setLocationName(rowData.get(6)); // 入库库位
                    
                    if (rowData.get(7) != null) {
                        try {
                            vo.setPrice(new BigDecimal(rowData.get(7)));
                        } catch (NumberFormatException e) {
                            // 忽略格式错误
                        }
                    }
                    
                    if (rowData.get(8) != null) {
                        try {
                            vo.setQualityStatus(Integer.parseInt(rowData.get(8)));
                        } catch (NumberFormatException e) {
                            // 忽略格式错误
                        }
                    }
                    
                    if (rowData.get(9) != null) {
                        try {
                            vo.setStatus(Integer.parseInt(rowData.get(9)));
                        } catch (NumberFormatException e) {
                            // 忽略格式错误
                        }
                    }
                    
                    vo.setSpec(rowData.get(10)); // 规格
                    vo.setUnit(rowData.get(11)); // 单位
                    vo.setRemark(rowData.get(12)); // 备注
                    
                    System.out.println("行 " + (i+1) + " 转换后数据: " + vo);
                    importVOs.add(vo);
                }
                
                // 使用标准方法处理数据
                return importReceiptOrderDetails(importVOs, save);
            } else {
                System.out.println("Excel数据不足，至少需要表头行和数据行");
                return Collections.emptyList();
            }
            
        } catch (Exception e) {
            System.err.println("导入Excel时发生错误: " + e.getMessage());
            e.printStackTrace();
            throw new IOException("导入Excel失败: " + e.getMessage(), e);
        }
    }
}