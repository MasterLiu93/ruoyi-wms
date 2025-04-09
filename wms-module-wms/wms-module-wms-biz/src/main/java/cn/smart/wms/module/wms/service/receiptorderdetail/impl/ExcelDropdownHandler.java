package cn.smart.wms.module.wms.service.receiptorderdetail.impl;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * Excel下拉框处理器
 */
public class ExcelDropdownHandler implements SheetWriteHandler {

    // 存储下拉框数据的Map，key为列索引，value为下拉选项列表
    private Map<Integer, List<String>> dropdownMap;
    
    // 创建隐藏Sheet的名称
    private String hiddenSheetName = "数据字典";

    /**
     * 构造函数
     * @param dropdownMap 下拉框数据Map
     */
    public ExcelDropdownHandler(Map<Integer, List<String>> dropdownMap) {
        this.dropdownMap = dropdownMap;
    }

    /**
     * 构造函数
     * @param dropdownMap 下拉框数据Map
     * @param hiddenSheetName 隐藏sheet名称
     */
    public ExcelDropdownHandler(Map<Integer, List<String>> dropdownMap, String hiddenSheetName) {
        this.dropdownMap = dropdownMap;
        this.hiddenSheetName = hiddenSheetName;
    }

    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        // 空实现，在sheet创建前不做任何操作
    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        // 如果没有下拉框数据，直接返回
        if (CollectionUtils.isEmpty(dropdownMap)) {
            System.out.println("没有下拉框数据，跳过创建下拉框");
            return;
        }
        
        System.out.println("开始处理Excel下拉框数据，共 " + dropdownMap.size() + " 列");
        
        // 获取Excel工作簿
        Workbook workbook = writeWorkbookHolder.getWorkbook();
        
        // 创建隐藏的数据源Sheet
        System.out.println("创建隐藏Sheet：" + hiddenSheetName);
        Sheet hiddenSheet = workbook.createSheet(hiddenSheetName);
        
        // 循环处理每列的下拉选项
        for (Map.Entry<Integer, List<String>> entry : dropdownMap.entrySet()) {
            Integer columnIndex = entry.getKey();
            List<String> valueList = entry.getValue();
            
            // 如果该列没有下拉选项，跳过
            if (CollectionUtils.isEmpty(valueList)) {
                System.out.println("列 " + columnIndex + " 没有下拉选项，跳过");
                continue;
            }
            
            System.out.println("处理列 " + columnIndex + " 的下拉选项，共 " + valueList.size() + " 项");
            
            // 判断下拉项数量，如果数量小于20个，直接使用数据有效性约束
            if (valueList.size() <= 20) {
                System.out.println("列 " + columnIndex + " 下拉选项少于20个，使用直接列表方式");
                setValidationData(writeSheetHolder.getSheet(), valueList, columnIndex);
            } else {
                // 数量大于20个时，使用引用数据的方式创建下拉
                System.out.println("列 " + columnIndex + " 下拉选项大于20个，使用引用数据方式");
                setReferenceData(workbook, hiddenSheet, writeSheetHolder.getSheet(), valueList, columnIndex);
            }
        }
        
        // 隐藏数据源Sheet
        System.out.println("隐藏数据源Sheet");
        workbook.setSheetHidden(workbook.getSheetIndex(hiddenSheet), true);
        System.out.println("Excel下拉框处理完成");
    }
    
    /**
     * 创建数据有效性约束 - 直接使用固定值列表
     * @param sheet 工作表
     * @param valueList 下拉值列表
     * @param columnIndex 列索引
     */
    private void setValidationData(Sheet sheet, List<String> valueList, int columnIndex) {
        // 创建数据有效性约束 - 有效范围为2~500行
        CellRangeAddressList addressList = new CellRangeAddressList(1, 500, columnIndex, columnIndex);
        
        // 将下拉项拼接成字符串
        String[] values = valueList.toArray(new String[0]);
        
        // 创建下拉框
        DataValidationHelper helper = sheet.getDataValidationHelper();
        DataValidationConstraint constraint = helper.createExplicitListConstraint(values);
        DataValidation dataValidation = helper.createValidation(constraint, addressList);
        
        // 处理Excel兼容性 - 允许空值
        dataValidation.setEmptyCellAllowed(true);
        if (dataValidation instanceof XSSFDataValidation) {
            dataValidation.setSuppressDropDownArrow(true);
            dataValidation.setShowErrorBox(true);
        } else {
            dataValidation.setSuppressDropDownArrow(false);
        }
        
        // 添加到sheet
        sheet.addValidationData(dataValidation);
    }
    
    /**
     * 创建引用数据的下拉选项
     * @param workbook 工作簿
     * @param hiddenSheet 隐藏的数据源sheet
     * @param visibleSheet 可见的工作表
     * @param valueList 下拉选项列表
     * @param columnIndex 列索引
     */
    private void setReferenceData(Workbook workbook, Sheet hiddenSheet, Sheet visibleSheet, List<String> valueList, int columnIndex) {
        // 在隐藏的Sheet中创建名称
        String hiddenNamePrefix = "option_";
        String hiddenName = hiddenNamePrefix + columnIndex;
        
        // 在隐藏表中填充下拉项数据
        for (int i = 0; i < valueList.size(); i++) {
            // 创建行并填充单元格数据
            Row row = hiddenSheet.createRow(i);
            Cell cell = row.createCell(0);
            cell.setCellValue(valueList.get(i));
        }
        
        // 创建名称（命名范围）
        Name namedRange = workbook.createName();
        namedRange.setNameName(hiddenName);
        // 设置引用区域 - 例如 数据字典!$A$1:$A$10
        namedRange.setRefersToFormula(hiddenSheetName + "!$A$1:$A$" + valueList.size());
        
        // 创建数据有效性约束 - 有效范围为2~500行
        CellRangeAddressList addressList = new CellRangeAddressList(1, 500, columnIndex, columnIndex);
        
        // 创建基于引用的下拉框
        DataValidationHelper helper = visibleSheet.getDataValidationHelper();
        // 使用 =option_X 的形式引用命名范围
        DataValidationConstraint constraint = helper.createFormulaListConstraint(hiddenName);
        DataValidation dataValidation = helper.createValidation(constraint, addressList);
        
        // 处理Excel兼容性 - 允许空值
        dataValidation.setEmptyCellAllowed(true);
        if (dataValidation instanceof XSSFDataValidation) {
            dataValidation.setSuppressDropDownArrow(true);
            dataValidation.setShowErrorBox(true);
        } else {
            dataValidation.setSuppressDropDownArrow(false);
        }
        
        // 添加到sheet
        visibleSheet.addValidationData(dataValidation);
    }
} 