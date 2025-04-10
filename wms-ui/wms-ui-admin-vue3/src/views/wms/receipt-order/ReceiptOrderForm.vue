-*<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible" width="80%">
    <!-- 基础信息部分 -->
    <div class="receipt-order-container">
      <div class="section-header">
        <h3>基础信息</h3>
      </div>
      
        <el-form
          ref="formRef"
          :model="formData"
          :rules="formRules"
          label-width="120px"
          v-loading="formLoading"
        >
          <el-row :gutter="16">
          <el-col :span="8">
              <el-form-item label="入库类型" prop="receiptType">
                <el-select v-model="formData.receiptType" placeholder="请选择入库类型" style="width: 100%">
                  <el-option
                    v-for="dict in getIntDictOptions(DICT_TYPE.WMS_RECEIPT_TYPE)"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          <el-col :span="8">
              <el-form-item label="供应商" prop="supplierId">
                <el-select v-model="formData.supplierId" placeholder="请选择供应商" clearable filterable style="width: 100%">
                  <el-option
                    v-for="item in supplierOptions"
                    :key="item.id"
                    :label="`${item.supplierName || ''} (${item.supplierCode || ''})`"
                    :value="item.id"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          <el-col :span="8">
              <el-form-item label="仓库" prop="warehouseId">
                <el-select 
                  v-model="formData.warehouseId" 
                  placeholder="请选择仓库" 
                  clearable 
                  filterable 
                  style="width: 100%"
                  @change="handleWarehouseChange"
                >
                  <el-option
                    v-for="item in warehouseOptions"
                    :key="item.id"
                    :label="item.warehouseName || `仓库${item.id}`"
                    :value="Number(item.id)"
                  />
                </el-select>
              </el-form-item>
            </el-col>
        </el-row>
        
        <el-row :gutter="16">
          <el-col :span="8">
              <el-form-item label="单据状态" prop="orderStatus">
                <el-select v-model="formData.orderStatus" placeholder="请选择单据状态" style="width: 100%">
                  <el-option
                    v-for="dict in getIntDictOptions(DICT_TYPE.WMS_RECEIPT_ORDER_STATUS)"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          <el-col :span="8">
              <el-form-item label="入库状态" prop="receiptStatus">
                <el-select v-model="formData.receiptStatus" placeholder="请选择入库状态" style="width: 100%">
                  <el-option
                    v-for="dict in getIntDictOptions(DICT_TYPE.WMS_RECEIPT_STATUS)"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          <el-col :span="8">
              <el-form-item label="预计到货时间" prop="expectTime">
                <el-date-picker
                  v-model="formData.expectTime"
                  type="datetime"
                  value-format="x"
                  placeholder="选择预计到货时间"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-row :gutter="16">
          <el-col :span="8">
              <el-form-item label="实际到货时间" prop="arrivalTime">
                <el-date-picker
                  v-model="formData.arrivalTime"
                  type="datetime"
                  value-format="x"
                  placeholder="选择实际到货时间"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
          <el-col :span="8">
              <el-form-item label="完成时间" prop="completionTime">
                <el-date-picker
                  v-model="formData.completionTime"
                  type="datetime"
                  value-format="x"
                  placeholder="入库完成时间"
                  style="width: 100%"
                  :disabled="true"
                />
              </el-form-item>
            </el-col>
          <el-col :span="8">
              <el-form-item label="备注" prop="remark">
                <el-input v-model="formData.remark" type="textarea" placeholder="请输入备注" />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      
      <!-- 审核信息部分 -->
      <el-divider v-if="auditInfo" content-position="left">审核信息</el-divider>
      <el-row v-if="auditInfo" :gutter="16">
        <el-col :span="8">
          <el-form-item label="审核人">
            {{ auditInfo.auditor }}
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="审核时间">
            {{ formatDate(auditInfo.auditTime) }}
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="审核状态">
            <el-tag :type="auditInfo.status === 2 ? 'success' : 'danger'">
              {{ auditInfo.status === 2 ? '已通过' : '已拒绝' }}
            </el-tag>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row v-if="auditInfo" :gutter="16">
        <el-col :span="24">
          <el-form-item label="审核备注">
            {{ auditInfo.auditRemark || '无' }}
          </el-form-item>
        </el-col>
      </el-row>
      
      <!-- 明细信息部分 -->
      <div class="section-header">
        <h3>明细信息</h3>
        <div class="action-buttons">
          <el-button type="primary" @click="addNewRow" :disabled="!formData.warehouseId">
            <Icon icon="ep:plus" class="mr-5px" /> 添加物料
          </el-button>
          <el-button type="warning" @click="handleExportTemplate">
            <Icon icon="ep:download" class="mr-5px" /> 下载模板
          </el-button>
          <el-button type="success" @click="handleImportDetails" :disabled="!formData.warehouseId">
            <Icon icon="ep:upload" class="mr-5px" /> 导入明细
          </el-button>
        </div>
        </div>
        
        <el-table v-loading="detailLoading" :data="detailList" :stripe="true" :show-overflow-tooltip="true">
          <el-table-column label="序号" type="index" width="60" align="center" />
          <el-table-column label="物料名称" align="center" min-width="150">
            <template #default="scope">
            <template v-if="scope.row.isEdit">
              <el-select v-model="scope.row.itemId" placeholder="选择物料" filterable style="width: 100%">
                <el-option
                  v-for="item in itemOptions"
                  :key="item.id"
                  :label="item.itemName"
                  :value="item.id"
                />
              </el-select>
            </template>
            <span v-else>{{ getItemName(scope.row.itemId) }}</span>
            </template>
          </el-table-column>
        <el-table-column label="计划数量" align="center" min-width="100">
            <template #default="scope">
            <template v-if="scope.row.isEdit">
              <el-input-number v-model="scope.row.planCount" :min="1" size="small" controls-position="right" style="width: 100%" />
            </template>
            <span v-else>{{ scope.row.planCount }}</span>
            </template>
          </el-table-column>
        <el-table-column label="实际入库数量" align="center" min-width="120">
          <template #default="scope">
            <template v-if="scope.row.isEdit">
              <el-input-number v-model="scope.row.realCount" :min="0" size="small" controls-position="right" style="width: 100%" />
            </template>
            <span v-else>{{ scope.row.realCount }}</span>
          </template>
        </el-table-column>
          <el-table-column label="货区" align="center" min-width="120">
            <template #default="scope">
            <template v-if="scope.row.isEdit">
              <el-select v-model="scope.row.areaId" placeholder="选择货区" filterable @change="handleAreaChange(scope.row)" style="width: 100%">
                <el-option
                  v-for="area in areaOptions"
                  :key="area.id"
                  :label="area.areaName"
                  :value="area.id"
                />
              </el-select>
            </template>
            <span v-else>{{ getAreaName(scope.row.areaId) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="货架" align="center" min-width="120">
            <template #default="scope">
            <template v-if="scope.row.isEdit">
              <el-select v-model="scope.row.rackId" placeholder="选择货架" filterable @change="handleRackChange(scope.row)" style="width: 100%">
                <el-option
                  v-for="rack in getRackOptionsByArea(scope.row.areaId)"
                  :key="rack.id"
                  :label="rack.rackName"
                  :value="rack.id"
                />
              </el-select>
            </template>
            <span v-else>{{ getRackName(scope.row.rackId) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="入库库位" align="center" min-width="120">
            <template #default="scope">
            <template v-if="scope.row.isEdit">
              <el-select v-model="scope.row.locationId" placeholder="选择库位" filterable style="width: 100%">
                <el-option
                  v-for="location in getRackLocationOptions(scope.row.rackId)"
                  :key="location.id"
                  :label="location.locationName"
                  :value="location.id"
                />
              </el-select>
            </template>
            <span v-else>{{ getLocationName(scope.row.locationId) }}</span>
            </template>
          </el-table-column>
        <el-table-column label="入库单价" align="center" min-width="100">
            <template #default="scope">
            <template v-if="scope.row.isEdit">
              <el-input-number v-model="scope.row.price" :min="0" :precision="2" size="small" controls-position="right" style="width: 100%" />
            </template>
            <span v-else>{{ scope.row.price }}</span>
            </template>
          </el-table-column>
        <el-table-column label="质检状态" align="center" min-width="100">
            <template #default="scope">
            <template v-if="scope.row.isEdit">
              <el-select v-model="scope.row.qualityStatus" placeholder="选择质检状态" style="width: 100%">
                <el-option
                  v-for="dict in getIntDictOptions(DICT_TYPE.COMMON_STATUS)"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </template>
            <dict-tag v-else :type="DICT_TYPE.COMMON_STATUS" :value="scope.row.qualityStatus" />
            </template>
          </el-table-column>
        <el-table-column label="状态" align="center" min-width="100">
            <template #default="scope">
            <template v-if="scope.row.isEdit">
              <el-select v-model="scope.row.status" placeholder="选择状态" style="width: 100%">
                <el-option
                  v-for="dict in getIntDictOptions(DICT_TYPE.WMS_RECEIPT_STATUS)"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </template>
            <dict-tag v-else :type="DICT_TYPE.WMS_RECEIPT_STATUS" :value="scope.row.status" />
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" min-width="180">
          <template #default="scope">
            <template v-if="scope.row.isEdit">
              <el-button link type="primary" @click="saveRow(scope.row)">
                保存
              </el-button>
              <el-button link type="danger" @click="cancelEdit(scope.$index)">
                取消
              </el-button>
            </template>
            <template v-else>
              <el-button link type="primary" @click="editRow(scope.$index, scope.row)">
                编辑
              </el-button>
              <el-button link type="danger" @click="handleDeleteDetail(scope.row.id)" v-if="scope.row.id">
                删除
              </el-button>
            </template>
            </template>
          </el-table-column>
        </el-table>
    </div>
    
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">保存草稿</el-button>
      <el-button @click="submitOrder" type="success" :disabled="formLoading">提交入库</el-button>
      <el-button @click="dialogVisible = false">关闭</el-button>
    </template>
  </Dialog>
  
  <!-- 导入对话框 -->
  <el-dialog v-model="importDialogVisible" title="导入明细" width="500px">
    <div class="upload-container">
      <el-upload
        ref="uploadRef"
        action="#"
        :auto-upload="false"
        :limit="1"
        :on-change="handleFileChange"
        :on-exceed="handleExceed"
        accept=".xls,.xlsx,.csv"
        drag
        class="upload-demo"
      >
        <el-icon class="el-icon--upload"><UploadFilled /></el-icon>
        <div class="el-upload__text">
          将文件拖到此处，或 <em>点击上传</em>
        </div>
        <template #tip>
          <div class="el-upload__tip">
            支持上传 .xls/.xlsx/.csv 文件，且文件大小不超过 10MB
            <el-button link type="primary" @click="handleExportTemplate">下载模板</el-button>
          </div>
        </template>
      </el-upload>

      <div v-if="fileList.length > 0" class="selected-file">
        <div class="file-info">
          <span class="file-name">已选择: {{ fileList[0]?.name }}</span>
          <span class="file-size">{{ formatFileSize(fileList[0]?.size) }}</span>
        </div>
      </div>
    </div>
    <template #footer>
      <el-button @click="importDialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="submitFileUpload" :loading="importLoading">上 传</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ReceiptOrderApi, ReceiptOrderVO } from '@/api/wms/receiptorder'
import { WarehouseApi } from '@/api/wms/warehouse'
import { AreaApi } from '@/api/wms/area'
import { RackApi } from '@/api/wms/rack'
import { LocationApi } from '@/api/wms/location'
import { ItemApi } from '@/api/wms/item'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import DictTag from '@/components/DictTag/src/DictTag.vue'
import download from '@/utils/download'
import request from '@/config/axios'
import { SupplierApi } from '@/api/wms/supplier'
import { OrderAuditApi } from '@/api/wms/audit'
import { OrderTypeEnum } from '@/enums/orderTypeEnum'

import { UploadFilled } from '@element-plus/icons-vue'
import { ElMessageBox } from 'element-plus'

/** 入库单 表单 */
defineOptions({ name: 'ReceiptOrderForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const title = ref('') // 添加title变量
const loading = ref(false) // 添加loading变量

// 表单数据
const formData = ref<Partial<ReceiptOrderVO>>({
  id: undefined,
  receiptOrderNo: '', // 默认为空字符串而不是undefined
  receiptType: 0, // 默认采购入库
  supplierId: undefined,
  warehouseId: undefined,
  orderStatus: 0, // 默认草稿
  receiptStatus: 0, // 默认待入库
  expectTime: undefined,
  arrivalTime: undefined,
  totalCount: 0,
  totalAmount: 0,
  remark: undefined,
  details: [], // 明细列表
})

// 表单校验规则
const formRules = reactive({
  receiptType: [{ required: true, message: '入库类型不能为空', trigger: 'change' }],
  warehouseId: [{ required: true, message: '仓库不能为空', trigger: 'change' }],
  orderStatus: [{ required: true, message: '单据状态不能为空', trigger: 'change' }],
  receiptStatus: [{ required: true, message: '入库状态不能为空', trigger: 'change' }],
})

const formRef = ref() // 表单 Ref
const uploadRef = ref() // 上传组件 Ref
const importDialogVisible = ref(false) // 导入对话框显示状态
const importLoading = ref(false) // 上传加载状态
const fileList = ref<File[]>([]) // 待上传的文件列表

// 选项数据
const warehouseOptions = ref<any[]>([]) // 仓库下拉选项
const supplierOptions = ref<any[]>([]) // 供应商下拉选项
const itemOptions = ref<any[]>([]) // 物料下拉选项
const itemMap = ref<Record<number, any>>({}) // 物料映射

// 明细列表数据
const detailList = ref<any[]>([]) // 明细列表（使用any类型替代ReceiptOrderDetailVO，以支持isEdit属性）
const detailLoading = ref(false) // 明细列表加载状态
const exportLoading = ref(false) // 导出加载状态

// 添加获取区域选项的方法
const areaMap = ref<Record<number, any>>({}) // 货区映射
const rackMap = ref<Record<number, any>>({}) // 货架映射

// 记录原始数据，用于取消编辑
const originalEditingRow = ref<any>(null)
// 添加区域选项数组
const areaOptions = ref<any[]>([])
// 添加货架选项映射
const rackOptions = ref<any[]>([])
// 添加库位选项映射
const locationOptions = ref<any[]>([])
// 库位映射
const locationMap = ref<Record<number, any>>({}) // 改为Record类型而不是数组

// 审核信息
const auditInfo = ref(null)

/** 获取仓库选项 */
const getWarehouseOptions = async () => {
  try {
    // 使用分页接口获取所有仓库数据
    const result = await WarehouseApi.getWarehousePage({
      pageNo: 1,
      pageSize: 100 // 获取足够多的数据
    })
    
    if (result && result.list && Array.isArray(result.list)) {
      // 直接使用返回的仓库数据，不做字段映射
      warehouseOptions.value = result.list
    }
  } catch (error) {
    console.error('获取仓库列表失败:', error)
    warehouseOptions.value = []
  }
}

/** 获取供应商选项 */
const getSupplierOptions = async () => {
  try {
    // 使用分页接口获取供应商数据
    const result = await SupplierApi.getSupplierPage({
      pageNo: 1,
      pageSize: 100 // 获取足够多的数据
    })
    
    if (result && result.list && Array.isArray(result.list)) {
      // 直接使用返回的供应商数据，不做字段映射
      supplierOptions.value = result.list
    }
  } catch (error) {
    console.error('获取供应商列表失败:', error)
    supplierOptions.value = []
  }
}

/** 获取物料选项 */
const getItemOptions = async () => {
  try {
    // 这里假设有物料API
    // const result = await ItemApi.getItemSimpleList()
    // itemOptions.value = result
    
    // 由于未提供物料的简单列表API，这里使用分页查询
    const result = await ItemApi.getItemPage({
      pageNo: 1,
      pageSize: 100 // 获取足够多的数据
    })
    
    if (result && result.list && Array.isArray(result.list)) {
      itemOptions.value = result.list
      
      // 构建物料ID到物料对象的映射
      itemMap.value = {}
      result.list.forEach(item => {
        itemMap.value[item.id] = item
      })
    }
  } catch (error) {
    console.error('获取物料列表失败:', error)
    itemOptions.value = []
    itemMap.value = {}
  }
}

/** 获取库位选项 */
const getLocationOptions = async (warehouseId?: number) => {
  if (!warehouseId) {
    // 不再操作locationOptions变量
    locationMap.value = {}
    return
  }
  
  try {
    // 使用库位API按仓库ID查询
    const result = await LocationApi.getLocationPage({
      pageNo: 1,
      pageSize: 100, // 获取足够多的数据
      warehouseId: warehouseId // 添加仓库ID作为过滤条件
    })
    
    if (result && result.list && Array.isArray(result.list)) {
      // 构建库位ID到库位对象的映射
      locationMap.value = {}
      result.list.forEach(item => {
        locationMap.value[item.id] = item
      })
    }
  } catch (error) {
    console.error('获取库位列表失败:', error)
    locationMap.value = {}
  }
}

/** 处理仓库变更 */
const handleWarehouseChange = async (warehouseId?: number) => {
  if (!warehouseId) {
    return
  }
  
  try {
    console.log(`仓库变更为: ${warehouseId}, 加载相关基础数据`);
    // 一次性加载该仓库下的所有区域、货架和库位
    // 1. 加载区域
    await getAllAreaOptions();
    
    // 2. 加载所有货架数据
    try {
      const result = await RackApi.getRackPage({
        pageNo: 1,
        pageSize: 100, // 获取尽可能多的数据
        warehouseId: warehouseId
      });
      
      if (result && result.list && Array.isArray(result.list)) {
        // 清空并重建货架选项数组
        rackOptions.value = result.list;
        
        // 构建货架ID到货架对象的映射
        rackMap.value = {};
        result.list.forEach(item => {
          rackMap.value[item.id] = item;
        });
        
        console.log(`成功加载 ${result.list.length} 个货架数据`);
      }
    } catch (error) {
      console.error('加载货架数据失败:', error);
    }
    
    // 3. 加载所有库位数据
    try {
      const result = await LocationApi.getLocationPage({
        pageNo: 1,
        pageSize: 100, // 获取尽可能多的数据
        warehouseId: warehouseId
      });
      
      if (result && result.list && Array.isArray(result.list)) {
        // 清空并重建库位选项数组
        locationOptions.value = result.list;
        
        // 构建库位ID到库位对象的映射
        locationMap.value = {};
        result.list.forEach(item => {
          locationMap.value[item.id] = item;
        });
        
        console.log(`成功加载 ${result.list.length} 个库位数据`);
      }
    } catch (error) {
      console.error('加载库位数据失败:', error);
    }
  } catch (error) {
    console.error('仓库变更处理失败:', error);
  }
}

/** 获取物料名称 */
const getItemName = (itemId?: number) => {
  if (!itemId) return '--'
  
  // 首先检查行本身是否包含itemName
  const detailRow = detailList.value.find(row => row.itemId === itemId)
  if (detailRow && detailRow.itemName) {
    return detailRow.itemName
  }
  
  // 如果行没有名称，从映射中获取
  if (itemMap.value[itemId]) {
  return itemMap.value[itemId].itemName
  }
  
  // 如果还是找不到，返回ID
  return `物料(${itemId})`
}

/** 获取库位名称 */
const getLocationName = (locationId?: number) => {
  if (!locationId) return '--';
  // 先检查行本身是否有locationName属性
  const detailRow = detailList.value.find(row => row.locationId === locationId);
  if (detailRow && detailRow.locationName) {
    return detailRow.locationName;
  }
  // 否则从映射中获取
  if (locationMap.value[locationId]) {
    return locationMap.value[locationId].locationName;
  }
  return `库位(${locationId})`; // 至少显示ID，避免显示'--'
}

/** 删除明细 */
const handleDeleteDetail = async (id: number) => {
  try {
    await message.confirm('是否确认删除该明细？')
    // 从本地列表中删除，不再调用API单独删除
    const index = detailList.value.findIndex(item => item.id === id)
    if (index !== -1) {
      detailList.value.splice(index, 1)
      // 更新主表中的明细列表
      formData.value.details = [...detailList.value]
      // 更新总计
      updateTotals()
    message.success('删除成功')
    }
  } catch (error) {
    console.error('删除明细失败:', error)
  }
}

/** 导入明细 */
const handleImportDetails = () => {
  if (!formData.value.warehouseId) {
    message.error('请先选择仓库，再导入明细')
    return
  }
  
  // 清空文件列表
  fileList.value = []
  // 确保每次打开对话框时重置上传组件
  importDialogVisible.value = true
  // 下一个事件循环中确保上传组件被重置
  setTimeout(() => {
    if (uploadRef.value) {
      uploadRef.value.clearFiles()
    }
  }, 0)
}

/** 导出模板 */
const handleExportTemplate = async () => {
  // 检查是否已选择仓库
  if (!formData.value.warehouseId) {
    message.warning('请先选择仓库，以便生成包含该仓库货区和库位的Excel模板')
    return
  }
  
  const warehouseId = formData.value.warehouseId
  
  exportLoading.value = true
  message.info('正在下载Excel模板，请稍候...')
  
  try {
    console.log(`尝试为仓库ID ${warehouseId} 导出Excel下拉模板`)
    
    // 直接使用request.download发送请求
    const data = await request.download({
      url: `/wms/receipt-order-detail/open/export-excel-template`,
      params: { warehouseId }
    })
    
    // 使用download.excel工具下载Excel文件
    download.excel(data, '入库单明细导入模板.xlsx')
    
    message.success('Excel模板下载成功')
  } catch (error) {
    console.error('导出模板失败:', error)
    message.error('导出模板失败，请稍后重试')
  } finally {
    exportLoading.value = false
  }
}

/** 获取区域选项 */
const getAllAreaOptions = async () => {
  try {
    // 如果已经有数据，则不再加载
    if (areaOptions.value && areaOptions.value.length > 0) {
      return;
    }
    
    if (!formData.value.warehouseId) {
      console.warn('未指定仓库ID，无法加载区域信息');
      return;
    }
    
    // 查询所有区域，使用分页加载
    let pageNo = 1;
    const pageSize = 100; // 调整为最大允许的100
    let allAreas: any[] = [];
    let hasMore = true;
    
    while (hasMore) {
      const result = await AreaApi.getAreaPage({
        pageNo: pageNo,
        pageSize: pageSize,
        warehouseId: formData.value.warehouseId
      });
    
    if (result && result.list && Array.isArray(result.list)) {
        allAreas.push(...result.list);
        
        // 检查是否还有更多数据
        hasMore = result.list.length === pageSize;
        pageNo++;
      } else {
        hasMore = false;
      }
    }
    
    // 设置区域选项
    areaOptions.value = allAreas;
    
    // 更新货区映射
    allAreas.forEach((item: any) => {
      areaMap.value[item.id] = item;
    });
  } catch (error) {
    console.error('获取所有区域选项失败:', error);
  }
}

/** 根据区域获取货架选项 - 改为从已加载的数据中筛选 */
const getRackOptionsByArea = (areaId?: number) => {
  if (!areaId) return [];
  
  // 从已加载的货架数据中筛选出指定区域的货架
  const filteredRacks = rackOptions.value.filter(rack => rack.areaId === areaId);
  return filteredRacks;
}

/** 根据货架获取库位选项 - 改为从已加载的数据中筛选 */
const getRackLocationOptions = (rackId?: number) => {
  if (!rackId) return [];
  
  // 从已加载的库位数据中筛选出指定货架的库位
  const filteredLocations = locationOptions.value.filter(location => location.rackId === rackId);
  return filteredLocations;
}

/** 处理区域变更 - 简化为不再进行API调用 */
const handleAreaChange = (row: any) => {
  // 清除货架和库位选择
  row.rackId = undefined;
  row.locationId = undefined;
}

/** 处理货架变更 - 简化为不再进行API调用 */
const handleRackChange = (row: any) => {
  // 清除库位选择
  row.locationId = undefined;
}

/** 更新总计 */
const updateTotals = () => {
  let totalCount = 0
  let totalAmount = 0
  
  detailList.value.forEach(item => {
    if (!item.isEdit) {  // 只计算非编辑状态的行
      totalCount += item.planCount || 0
      totalAmount += (item.price || 0) * (item.planCount || 0)
    }
  })
  
  // 更新主表数据
  formData.value.totalCount = totalCount
  formData.value.totalAmount = totalAmount
}

// 导入对话框关闭时清理文件列表和上传组件
watch(importDialogVisible, (newVal) => {
  if (!newVal) { // 当对话框关闭时
    fileList.value = []
    // 确保上传组件被清理
    if (uploadRef.value) {
      uploadRef.value.clearFiles()
    }
  }
})

/** 打开弹窗 */
const emit = defineEmits(['success'])

/** 加载入库单数据 */
const loadReceiptOrderData = async (id: number) => {
  try {
    // 加载入库单详情
    const data = await ReceiptOrderApi.getReceiptOrder(id)
    formData.value = data
    
    // 设置明细列表
    if (data.details && Array.isArray(data.details)) {
      detailList.value = data.details.map(detail => ({
        ...detail,
        edited: false, // 添加编辑状态标志
        isEdit: false  // 添加行编辑标志
      }))
      
      // 收集所有物料ID
      const itemIds = new Set(detailList.value.map(item => item.itemId).filter(Boolean))
      
      // 如果有物料ID，加载物料数据
      if (itemIds.size > 0) {
        await loadItemData(Array.from(itemIds))
      }
    }
    
    // 加载相关区域、货架和库位数据
    await loadAreaAndLocationData()
    
    // 获取审核信息
    if (data.orderStatus > 1) { // 如果已审核
      fetchAuditInfo(id)
    }
  } catch (error) {
    console.error('加载入库单数据失败:', error)
    message.error('加载入库单数据失败')
  }
}

/** 加载物料数据 */
const loadItemData = async (itemIds: number[]) => {
  try {
    if (itemIds.length === 0) return
    
    // 使用物料分页查询API，同时传递多个itemId
    const result = await ItemApi.getItemPage({
            pageNo: 1,
      pageSize: 100
    })
          
          if (result && result.list && Array.isArray(result.list)) {
      // 更新物料选项
      itemOptions.value = result.list
      
      // 重建物料映射
      result.list.forEach(item => {
        itemMap.value[item.id] = item
      })
      
      console.log(`成功加载 ${result.list.length} 个物料数据`)
          }
        } catch (error) {
    console.error('加载物料数据失败:', error)
  }
}

/**
 * 打开弹窗
 * @param type 操作类型，create: 创建，update: 修改，view: 查看
 * @param id 入库单ID
 */
const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  formType.value = type
  formLoading.value = true
  detailLoading.value = true
  
  try {
    // 重置表单数据
  resetForm()
  
    // 根据不同类型设置不同的标题
    if (type === 'create') {
      dialogTitle.value = '新增入库单'
      
      // 设置默认值
      setFormDefaultValues()
    } else if (type === 'edit') {
      dialogTitle.value = '编辑入库单'
      
      // 加载数据
    if (id) {
        console.log(`加载入库单数据，ID: ${id}`)
        const data = await ReceiptOrderApi.getReceiptOrder(id)
        console.log('获取到的入库单数据:', data)
        
        // 处理日期字段，确保它们是数字时间戳或者null
        if (data.expectTime) {
          data.expectTime = new Date(data.expectTime).getTime()
        }
        
        if (data.arrivalTime) {
          data.arrivalTime = new Date(data.arrivalTime).getTime()
        }
        
        // 将数据赋值给表单
        formData.value = { ...data }
        
        // 加载明细数据
        if (data.details && Array.isArray(data.details)) {
          console.log(`加载 ${data.details.length} 条明细数据`)
          // 确保每条明细都有isEdit=false标记
          detailList.value = data.details.map(item => ({
              ...item,
            isEdit: false
          }))
          
          // 如果有物料数据，加载物料信息
          const itemIds = new Set(detailList.value.map(item => item.itemId).filter(Boolean))
          if (itemIds.size > 0) {
            await loadItemData(Array.from(itemIds))
          }
          
          // 加载货区、货架和库位数据
          await loadAreaAndLocationData()
        }
      }
    } else if (type === 'view') {
      dialogTitle.value = '查看入库单'
      
      // 加载数据，与编辑模式类似
      if (id) {
        const data = await ReceiptOrderApi.getReceiptOrder(id)
        
        // 处理日期字段
        if (data.expectTime) {
          data.expectTime = new Date(data.expectTime).getTime()
        }
        
        if (data.arrivalTime) {
          data.arrivalTime = new Date(data.arrivalTime).getTime()
        }
        
        // 将数据赋值给表单
        formData.value = { ...data }
        
        // 加载明细数据
        if (data.details && Array.isArray(data.details)) {
          // 确保每条明细都有isEdit=false标记
          detailList.value = data.details.map(item => ({
            ...item,
            isEdit: false
          }))
          
          // 如果有物料数据，加载物料信息
          const itemIds = new Set(detailList.value.map(item => item.itemId).filter(Boolean))
          if (itemIds.size > 0) {
            await loadItemData(Array.from(itemIds))
          }
          
          // 加载货区、货架和库位数据
          await loadAreaAndLocationData()
        }
      }
    }
    
    // 加载仓库选项
    await getWarehouseOptions()
    
    // 加载供应商选项
    await getSupplierOptions()
    
    console.log('表单加载完成，明细条数:', detailList.value.length)
  } catch (error) {
    console.error('打开表单失败:', error)
    message.error('加载数据失败，请稍后重试')
  } finally {
    formLoading.value = false
    detailLoading.value = false
  }
}

// 暴露open方法给父组件调用
defineExpose({
  open
})

/** 处理文件选择变化 */
const handleFileChange = (file: any) => {
  // 每次选择文件时，先清空现有文件列表
  fileList.value = []
  if (file && file.raw) {
  fileList.value = [file.raw]
  }
}

/** 处理文件超出限制 */
const handleExceed = (): void => {
  message.error('最多只能上传1个文件')
}

/** 格式化文件大小 */
const formatFileSize = (size?: number): string => {
  if (!size) return '0 B'
  
  const units = ['B', 'KB', 'MB', 'GB']
  let i = 0
  while (size >= 1024 && i < units.length - 1) {
    size /= 1024
    i++
  }
  
  return `${size.toFixed(2)} ${units[i]}`
}

/** 重置表单 */
const resetForm = () => {
  formData.value = {
    id: undefined,
    receiptOrderNo: '', // 提供默认空字符串而不是undefined
    receiptType: 0, // 默认采购入库
    supplierId: undefined,
    warehouseId: undefined,
    orderStatus: 0, // 默认草稿
    receiptStatus: 0, // 默认待入库
    expectTime: undefined,
    arrivalTime: undefined,
    totalCount: 0,
    totalAmount: 0,
    remark: undefined,
    details: [], // 重置明细列表
  }
  
  // 重置明细列表
  detailList.value = []
}

/** 设置表单默认值 */
const setFormDefaultValues = () => {
  formData.value = {
    ...formData.value,
    receiptType: 0, // 默认采购入库
    orderStatus: 0, // 默认草稿
    receiptStatus: 0, // 默认待入库
    totalCount: 0,
    totalAmount: 0,
    details: [], // 确保details字段有默认值
  }
}

/** 加载区域、货架和库位数据 */
const loadAreaAndLocationData = async () => {
  if (!formData.value.warehouseId) return
  
  try {
    // 1. 加载区域数据
    await getAllAreaOptions()
    
    // 2. 获取明细中的所有区域ID和货架ID
    const areaIds = new Set(detailList.value.map(item => item.areaId).filter(Boolean))
    const rackIds = new Set(detailList.value.map(item => item.rackId).filter(Boolean))
    
    // 3. 加载所有涉及的货架数据
    if (rackIds.size > 0) {
      // 不支持ids参数，改为按仓库ID查询所有货架
        const result = await RackApi.getRackPage({
          pageNo: 1,
          pageSize: 100,
        warehouseId: formData.value.warehouseId
      })
        
        if (result && result.list && Array.isArray(result.list)) {
        // 筛选出我们需要的货架ID
        const filteredRacks = result.list.filter(rack => 
          rackIds.has(rack.id) && !rackOptions.value.some(r => r.id === rack.id)
        )
        
        if (filteredRacks.length > 0) {
          rackOptions.value = [...rackOptions.value, ...filteredRacks]
          
          // 更新货架映射
          filteredRacks.forEach(rack => {
            rackMap.value[rack.id] = rack
          })
        }
      }
    }
    
    // 4. 加载所有涉及的库位数据
    const locationIds = new Set(detailList.value.map(item => item.locationId).filter(Boolean))
    if (locationIds.size > 0) {
      // 不支持ids参数，改为按仓库ID查询所有库位
        const result = await LocationApi.getLocationPage({
          pageNo: 1,
          pageSize: 100,
        warehouseId: formData.value.warehouseId
      })
        
        if (result && result.list && Array.isArray(result.list)) {
        // 筛选出我们需要的库位ID
        const filteredLocations = result.list.filter(location => 
          locationIds.has(location.id) && !locationOptions.value.some(l => l.id === location.id)
        )
        
        if (filteredLocations.length > 0) {
          locationOptions.value = [...locationOptions.value, ...filteredLocations]
          
          // 更新库位映射
          filteredLocations.forEach(location => {
            locationMap.value[location.id] = location
          })
        }
      }
    }
    
    // 5. 确保明细数据中包含名称字段
    detailList.value.forEach(item => {
      if (item.areaId && areaMap.value[item.areaId]) {
        item.areaName = areaMap.value[item.areaId].areaName
      }
      
      if (item.rackId && rackMap.value[item.rackId]) {
        item.rackName = rackMap.value[item.rackId].rackName
      }
      
      if (item.locationId && locationMap.value[item.locationId]) {
        item.locationName = locationMap.value[item.locationId].locationName
      }
    })
  } catch (error) {
    console.error('加载区域和库位数据失败:', error)
  }
}

/** 获取区域名称 */
const getAreaName = (areaId?: number) => {
  if (!areaId) return '--'
  // 先检查行本身是否有areaName属性
  const detailRow = detailList.value.find(row => row.areaId === areaId)
  if (detailRow && detailRow.areaName) {
    return detailRow.areaName
  }
  // 否则从映射中获取
  if (areaMap.value[areaId]) {
    return areaMap.value[areaId].areaName
  }
  return `区域(${areaId})` // 至少显示ID，避免显示'--'
}

/** 获取货架名称 */
const getRackName = (rackId?: number) => {
  if (!rackId) return '--'
  
  // 先检查行本身是否有rackName属性
  const detailRow = detailList.value.find(row => row.rackId === rackId)
  if (detailRow && detailRow.rackName) {
    return detailRow.rackName
  }
  
  // 从映射中获取
  if (rackMap.value[rackId]) {
    return rackMap.value[rackId].rackName
  }
  
  // 如果都找不到，直接返回带ID的文本
  return `货架(${rackId})`
}

/** 添加新行 */
const addNewRow = async () => {
  if (!formData.value.warehouseId) {
    message.error('请先选择仓库，再添加明细')
    return
  }
  
  // 加载物料列表
  if (itemOptions.value.length === 0) {
    await getItemOptions()
  }
  
  // 加载区域列表
  if (areaOptions.value.length === 0) {
    await getAllAreaOptions()
  }
  
  // 创建一个临时的负数ID，确保与数据库中的ID不冲突
  const tempId = -Math.floor(Date.now() + Math.random() * 1000)
  
  // 创建一个临时的receiptOrderId，前端用于标识该行属于当前表单
  const tempReceiptOrderId = formData.value.id || -Math.floor(Math.random() * 1000000)
  
  const newRow = {
    id: tempId, // 使用负数作为临时ID
    receiptOrderId: tempReceiptOrderId,
    itemId: undefined,
    itemCode: '',
    itemName: '',
    planCount: 1,
    realCount: 0,
    areaId: undefined,
    rackId: undefined,
    locationId: undefined,
    price: 0,
    qualityStatus: 0, // 默认未质检
    status: 0, // 默认待入库
    isEdit: true  // 标记为编辑状态
  }
  
  // 将新行添加到明细列表的底部
  detailList.value.push(newRow)
}

/** 编辑行 */
const editRow = async (index: number, row: any) => {
  // 检查是否有其他正在编辑的行
  if (detailList.value.some(item => item.isEdit)) {
    message.warning('请先完成当前编辑行的操作')
    return
  }
  
  // 保存原始数据，以便取消编辑时恢复
  originalEditingRow.value = JSON.parse(JSON.stringify(row))
  
  // 将行标记为编辑状态
  detailList.value[index].isEdit = true
}

/** 保存行 */
const saveRow = async (row: any) => {
  // 基本验证
  if (!row.itemId || !row.planCount || row.planCount <= 0) {
    message.error('请完整填写物料和计划数量')
    return
  }
  
  if (!row.areaId || !row.rackId || !row.locationId) {
    message.error('请选择入库位置（货区/货架/库位）')
    return
  }
  
  // 确保行数据中包含名称字段
  // 设置物料名称和代码
  if (row.itemId && itemMap.value[row.itemId]) {
    row.itemName = itemMap.value[row.itemId].itemName
    row.itemCode = itemMap.value[row.itemId].itemCode
  }
  
  // 设置区域、货架和库位名称
  if (row.areaId && areaMap.value[row.areaId]) {
    row.areaName = areaMap.value[row.areaId].areaName
  }
  
  if (row.rackId && rackMap.value[row.rackId]) {
    row.rackName = rackMap.value[row.rackId].rackName
  }
  
  if (row.locationId && locationMap.value[row.locationId]) {
    row.locationName = locationMap.value[row.locationId].locationName
  }
  
  // 清除编辑状态
  row.isEdit = false
  
  // 更新主表中的明细列表数据
  formData.value.details = [...detailList.value]
  
  // 重新计算总数和总金额
  updateTotals()
  
  message.success('行数据已更新，请记得点击"保存草稿"保存所有变更')
}

/** 取消编辑 */
const cancelEdit = (index: number) => {
  if (originalEditingRow.value) {
    // 无论是否有ID，都恢复原始数据
    detailList.value[index] = { ...originalEditingRow.value, isEdit: false }
  } else {
    // 如果originalEditingRow为null（不应该发生），保留行但退出编辑状态
    detailList.value[index].isEdit = false
  }
  // 清除原始数据
  originalEditingRow.value = null
}

/** 提交表单 - 保存草稿 */
const submitForm = async () => {
  // 校验表单
  await formRef.value.validate()
  
  // 校验明细表中的编辑状态行
  const hasEditingRows = detailList.value.some(row => row.isEdit)
  if (hasEditingRows) {
    message.warning('请先保存或取消正在编辑的明细行')
    return
  }
  
  // 设置加载状态
  formLoading.value = true
  
  try {
    // 处理明细行数据，移除isEdit标记
    const processedDetails = detailList.value.map(item => {
      // 移除isEdit标记
      const { isEdit, ...detail } = item
      
      // 如果是新行(无ID或临时ID<0)，设置为undefined让后端生成新ID
      if (detail.id === undefined || (detail.id && detail.id < 0)) {
        detail.id = undefined
      }
      
      // 确保每行都有receiptOrderId
      if (formData.value.id && !detail.receiptOrderId) {
        detail.receiptOrderId = formData.value.id
      }
      
      return detail
    })
    
    // 将处理后的明细赋值给表单数据
    formData.value.details = processedDetails
    
    // 确保入库单号有值
    if (formType.value === 'create' && !formData.value.receiptOrderNo) {
      formData.value.receiptOrderNo = `RO${new Date().getTime()}`
    }
    
    // 设置单据状态为草稿
    formData.value.orderStatus = 0 // 草稿状态
    
    // 准备提交数据，确保所有必填字段都有值
    const submitData = {
      ...formData.value,
      receiptOrderNo: formData.value.receiptOrderNo
    } as ReceiptOrderVO
    
    let resultId
    if (formType.value === 'create') {
      // 创建入库单和明细，使用统一接口
      resultId = await ReceiptOrderApi.createReceiptOrderWithDetails(submitData)
      message.success('创建成功')
      } else {
      // 更新入库单和明细，使用统一接口
      await ReceiptOrderApi.updateReceiptOrderWithDetails(submitData)
      resultId = formData.value.id
      message.success('更新成功')
    }
    
    // 如果需要可以更新表单的ID
    if (formType.value === 'create') {
      formData.value.id = resultId
    }
    
    dialogVisible.value = false
    // 触发成功回调
    emit('success')
  } catch (error: any) {
    console.error('提交表单失败:', error)
    message.error('提交失败，请重试：' + (error.message || '未知错误'))
  } finally {
    formLoading.value = false
  }
}

/** 提交入库单 */
const submitOrder = async () => {
  // 确保表单数据完整
  if (!formData.value.id) {
    message.error('请先保存入库单，再提交')
    return
  }
  
  // 确认对话框
  try {
    await ElMessageBox.confirm(
      '确认要提交该入库单吗？提交后将进入审核流程，在审核通过前无法修改。',
      '提交确认',
      {
        confirmButtonText: '确定提交',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // 显示加载中
    const loading = ElLoading.service({
      lock: true,
      text: '正在提交...',
      background: 'rgba(0, 0, 0, 0.7)',
    })
    
    try {
      // 使用API提交入库单
      await ReceiptOrderApi.submitReceiptOrder(formData.value.id)
      
      // 显示成功消息
      message.success('入库单提交成功，等待审核')
      
      // 触发成功事件
      emit('success')
      
      // 关闭对话框
      dialogVisible.value = false
    } finally {
      loading.close()
    }
  } catch (error: any) {
    console.error('提交入库单失败:', error)
    if (error.message) {
      message.error(error.message)
    } else {
      message.error('提交入库单失败')
    }
  }
}

// 上传文件处理
/** 导入Excel */
const submitFileUpload = async () => {
  if (fileList.value.length === 0) {
    message.error('请选择要上传的文件')
    return
  }
  
  if (!formData.value.warehouseId) {
    message.error('请先选择仓库，再导入明细')
    return
  }
  
  importLoading.value = true
  
  try {
    const file = fileList.value[0]
    
    // 创建FormData对象
    const formDataObj = new FormData()
    formDataObj.append('file', file)
    
    // 如果已经有ID，则附加ID，否则传递-1表示新建
    formDataObj.append('receiptOrderId', formData.value.id ? formData.value.id.toString() : '-1')
    // 添加仓库ID，确保后端可以正确处理
    formDataObj.append('warehouseId', formData.value.warehouseId.toString())
    
    // 调用导入API
    const response = await request.post({
      url: '/wms/receipt-order-detail/import-excel',
      data: formDataObj,
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
    
    // 处理不同的返回格式
    let importedData: any[] = []
    
    if (Array.isArray(response)) {
      importedData = response
    } else if (response && response.code === 0 && response.data && Array.isArray(response.data)) {
      importedData = response.data
    } else {
      throw new Error('导入失败，返回数据格式不正确')
    }
    
    if (importedData.length === 0) {
      message.warning('未获取到有效数据，请检查文件内容')
      return
    }
    
    // 添加导入数据到明细表
    const newRows = importedData.map(item => ({
      ...item,
      isEdit: false,
      receiptOrderId: formData.value.id || -1
    }))
    
    // 将新行添加到明细列表
    detailList.value = [...detailList.value, ...newRows]
    
    // 收集导入数据中的物料ID
    const itemIds = new Set(newRows.map(item => item.itemId).filter(Boolean))
    
    // 如果有物料ID，加载物料数据
    if (itemIds.size > 0) {
      await loadItemData(Array.from(itemIds))
      
      // 更新物料名称等信息
      detailList.value.forEach(row => {
        if (row.itemId && itemMap.value[row.itemId]) {
          row.itemName = itemMap.value[row.itemId]?.itemName || `物料(${row.itemId})`
          row.itemCode = itemMap.value[row.itemId]?.itemCode || ''
        }
      })
    }
    
    // 加载相关数据
    await loadAreaAndLocationData()
    
    // 更新总计
    updateTotals()
    
    message.success(`成功导入 ${importedData.length} 条数据`)
    importDialogVisible.value = false
  } catch (error: any) {
    console.error('导入失败:', error)
    message.error('导入失败: ' + (error.message || '未知错误'))
  } finally {
    importLoading.value = false
    fileList.value = []
    if (uploadRef.value) {
      uploadRef.value.clearFiles()
    }
  }
}

/** 获取审核信息 */
const fetchAuditInfo = async (id) => {
  try {
    const res = await OrderAuditApi.getLatestOrderAudit(id, OrderTypeEnum.RECEIPT)
    auditInfo.value = res.data
  } catch (error) {
    console.error('获取审核信息失败', error)
  }
}

/** 格式化日期 */
const formatDate = (timestamp?: number): string => {
  if (!timestamp) return '--'
  
  const date = new Date(timestamp)
  return date.toLocaleString()
}
</script>

<style scoped>
.receipt-order-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  border-bottom: 1px solid #ebeef5;
  padding-bottom: 10px;
}

.section-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 500;
  color: #303133;
}

.action-buttons {
  display: flex;
  gap: 10px;
}

.mb-10px {
  margin-bottom: 10px;
}

.upload-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 10px;
}

.upload-demo {
  width: 100%;
  margin-bottom: 15px;
}

.el-upload--text {
  width: 100%;
}

.el-upload-dragger {
  width: 100%;
  height: 180px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.el-icon--upload {
  font-size: 48px;
  color: #409EFF;
  margin-bottom: 10px;
}

.el-upload__text {
  font-size: 14px;
  color: #606266;
  margin-bottom: 15px;
}

.el-upload__text em {
  color: #409EFF;
  font-style: normal;
  cursor: pointer;
}

.el-upload__tip {
  width: 100%;
  text-align: center;
  font-size: 12px;
  color: #909399;
  margin-top: 10px;
}

.selected-file {
  width: 100%;
  margin-top: 15px;
  padding: 10px;
  border: 1px solid #e6e6e6;
  border-radius: 4px;
  background-color: #f9f9f9;
}

.file-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.file-name {
  font-size: 14px;
  color: #303133;
  font-weight: 500;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 75%;
}

.file-size {
  font-size: 12px;
  color: #909399;
}
</style>
