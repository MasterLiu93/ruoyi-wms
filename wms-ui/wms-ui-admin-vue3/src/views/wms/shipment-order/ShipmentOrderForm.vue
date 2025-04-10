<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible" width="80%">
    <!-- 基础信息部分 -->
    <div class="shipment-order-container">
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
            <el-form-item label="出库单号" prop="shipmentOrderNo">
              <el-input 
                v-model="formData.shipmentOrderNo" 
                placeholder="创建时后台自动生成"
                disabled
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="出库类型" prop="shipmentType">
              <el-select 
                v-model="formData.shipmentType" 
                placeholder="请选择出库类型" 
                style="width: 100%"
                :disabled="isView"
              >
                <el-option
                  v-for="dict in getIntDictOptions(DICT_TYPE.WMS_SHIPMENT_TYPE)"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="预计出库时间" prop="expectTime">
              <el-date-picker
                v-model="formData.expectTime"
                type="datetime"
                value-format="x"
                placeholder="选择预计出库时间"
                style="width: 100%"
                :disabled="isView"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="16">
          <el-col :span="8">
            <el-form-item label="客户" prop="customerId">
              <el-select 
                v-model="formData.customerId" 
                filterable 
                placeholder="请选择客户"
                clearable
                style="width: 100%"
                :disabled="isView"
              >
                <el-option
                  v-for="item in customerOptions"
                  :key="item.id"
                  :label="`${item.customerName || ''} (${item.customerCode || ''})`"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="仓库" prop="warehouseId">
              <el-select 
                v-model="formData.warehouseId" 
                filterable 
                placeholder="请选择仓库"
                clearable
                style="width: 100%"
                :disabled="isView || formType === 'edit'"
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
          <el-col :span="8">
            <el-form-item label="备注" prop="remark">
              <el-input 
                v-model="formData.remark" 
                type="textarea"
                placeholder="请输入备注信息" 
                :disabled="isView"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <!-- 明细信息部分 -->
      <div class="section-header" style="margin-top: 20px;">
        <h3>明细信息</h3>
        <div class="operation-group">
          <el-button 
            type="primary" 
            @click="openInventorySelectionDialog"
            :disabled="!formData.warehouseId"
            plain
          >
            <el-icon class="mr-5"><Plus /></el-icon>从库存添加
          </el-button>
          <el-button 
            v-if="formData.details && formData.details.length > 0" 
            type="danger" 
            @click="handleClearDetails"
            plain
          >
            <el-icon class="mr-5"><Delete /></el-icon>清空明细
          </el-button>
        </div>
      </div>

      <!-- 明细表格 -->
      <el-table 
        :data="formData.details || []" 
        stripe 
        border 
        style="width: 100%"
        v-loading="detailLoading"
      >
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column label="商品名称" prop="itemName" min-width="150" show-overflow-tooltip />
        <el-table-column label="规格" prop="spec" min-width="120" show-overflow-tooltip />
        <el-table-column label="单位" prop="unit" width="80" align="center" />
        <el-table-column label="物料类型" prop="itemType" width="100" align="center">
          <template #default="scope">
            <dict-tag :type="DICT_TYPE.WMS_ITEM_TYPE" :value="scope.row.itemType" />
          </template>
        </el-table-column>
        <el-table-column label="库存数量" prop="stockCount" width="100" align="right" />
        <el-table-column label="计划数量" prop="planCount" width="150" align="right">
          <template #default="scope">
            <el-input-number 
              v-model="scope.row.planCount" 
              :min="0" 
              :max="scope.row.stockCount || 0" 
              :precision="2" 
              :step="1" 
              :disabled="isView"
              @change="calculateTotals"
              controls-position="right"
              style="width: 100%"
            />
          </template>
        </el-table-column>
        <el-table-column label="实际数量" prop="realCount" width="150" align="right">
          <template #default="scope">
            <el-input-number 
              v-if="!isView"
              v-model="scope.row.realCount" 
              :min="0" 
              :max="scope.row.planCount"
              :precision="2" 
              :step="1"
              controls-position="right"
              style="width: 100%"
              @change="calculateTotals"
            />
            <span v-else>{{ scope.row.realCount || 0 }}</span>
            <div v-if="!isView" class="text-xs text-gray-400">不能超过计划数量</div>
          </template>
        </el-table-column>
        <el-table-column label="状态" prop="status" width="100" align="center">
          <template #default="scope">
            <dict-tag :type="DICT_TYPE.WMS_SHIPMENT_STATUS" :value="scope.row.status || 0" />
          </template>
        </el-table-column>
        <el-table-column label="备注" prop="remark" min-width="150" show-overflow-tooltip>
          <template #default="scope">
            <el-input 
              v-model="scope.row.remark" 
              placeholder="请输入备注" 
              :disabled="isView"
              @blur="calculateTotals"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right" align="center">
          <template #default="scope">
            <el-button 
              v-if="!isView && scope.row.status !== 2" 
              type="primary" 
              link 
              @click="handleExecuteShipment(scope.row, scope.$index)"
            >
              出库
            </el-button>
            <el-button v-if="!isView" type="danger" link @click="handleDeleteDetail(scope.$index)">
              <el-icon><Delete /></el-icon>
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    
    <!-- 底部按钮 -->
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogVisible = false">关 闭</el-button>
        <el-button v-if="!isView" type="primary" @click="handleSaveDraft" :loading="formLoading">保存草稿</el-button>
        <el-button v-if="!isView" type="success" @click="handleSubmitShipment" :loading="formLoading">提交出库</el-button>
        <el-button 
          v-if="!isView && formData.orderStatus === 2 && formData.shipmentStatus !== 2" 
          type="warning" 
          @click="handleCompleteShipment" 
          :loading="formLoading"
        >
          完成出库
        </el-button>
      </div>
    </template>
    
    <!-- 库存选择对话框 -->
    <el-dialog
      v-model="inventorySelectionVisible"
      title="选择库存商品"
      width="1000px"
      append-to-body
      destroy-on-close
    >
      <div class="search-bar">
        <el-form :inline="true" class="search-form">
          <el-form-item label="商品名称">
            <el-input v-model="inventorySearch.itemName" placeholder="请输入商品名称或编码" clearable />
          </el-form-item>
          <el-form-item label="物料类型">
            <el-select v-model="inventorySearch.itemType" placeholder="请选择物料类型" clearable>
              <el-option
                v-for="dict in getIntDictOptions(DICT_TYPE.WMS_ITEM_TYPE)"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="loadInventoryList">查询</el-button>
            <el-button @click="resetInventorySearch">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      
      <el-table
        :data="filteredInventoryList"
        border
        style="width: 100%"
        height="450px"
        v-loading="inventoryLoading"
        @selection-change="handleInventorySelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column prop="itemCode" label="商品编码" min-width="120" show-overflow-tooltip />
        <el-table-column prop="itemName" label="商品名称" min-width="150" show-overflow-tooltip />
        <el-table-column prop="spec" label="规格" min-width="120" show-overflow-tooltip />
        <el-table-column prop="unit" label="单位" width="80" align="center" />
        <el-table-column prop="itemType" label="物料类型" width="100" align="center">
          <template #default="scope">
            <dict-tag :type="DICT_TYPE.WMS_ITEM_TYPE" :value="scope.row.itemType" />
          </template>
        </el-table-column>
        <el-table-column prop="availableCount" label="可用库存" width="100" align="right" />
        <el-table-column prop="price" label="参考价格" width="100" align="right">
          <template #default="scope">
            {{ scope.row.price ? `￥${scope.row.price}` : '-' }}
          </template>
        </el-table-column>
      </el-table>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="inventorySelectionVisible = false">取 消</el-button>
          <el-button type="primary" @click="handleAddSelectedInventory">确认添加</el-button>
        </div>
      </template>
    </el-dialog>
  </Dialog>
</template>

<script setup lang="ts">
import { computed, ref, reactive, onMounted, watch, nextTick } from 'vue'
import { ShipmentOrderApi, ShipmentOrderVO, ShipmentOrderDetailVO, ShipmentOperationReqVO } from '@/api/wms/shipmentorder'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { DictTag } from '@/components/DictTag'
import { Plus, Delete } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox, FormInstance } from 'element-plus'
import { useI18n } from '@/hooks/web/useI18n'
import { useMessage } from '@/hooks/web/useMessage'
import { CustomerApi } from '@/api/wms/customer'
import { WarehouseApi } from '@/api/wms/warehouse'
import { ItemStockApi } from '@/api/wms/itemstock'
import request from '@/config/axios'

/** 出库单 表单 */
defineOptions({ name: 'ShipmentOrderForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref<'create' | 'edit' | 'view'>('create') // 表单的类型：create - 新增；update - 修改；view - 查看
const isView = computed(() => formType.value === 'view')

// 客户选项
const customerOptions = ref<any[]>([])
// 仓库选项
const warehouseOptions = ref<any[]>([])

// 库存选择相关
const inventorySelectionVisible = ref(false)
const inventoryList = ref<any[]>([])
const selectedInventory = ref<any[]>([])
const inventoryLoading = ref(false)
const inventorySearch = reactive({
  itemName: '',
  itemType: 0
})

// 添加 detailLoading 变量
const detailLoading = ref(false)

// 创建一个自定义的ShipmentOrderVO实现，确保totalCount和totalAmount字段是number类型
interface FormDataType extends Omit<ShipmentOrderVO, 'totalCount' | 'totalAmount'> {
  totalCount: number;
  totalAmount: number;
  details: ShipmentOrderDetailVO[];
}

// 表单数据
const formData = ref<FormDataType>({
  id: 0,
  shipmentOrderNo: '',
  shipmentType: 0, 
  customerId: 0,
  warehouseId: 0,
  orderStatus: 0,
  shipmentStatus: 0,
  expectTime: 0,
  completeTime: 0,
  totalCount: 0,
  totalAmount: 0,
  remark: '',
  details: []
})

// 表单校验规则
const formRules = reactive({
  shipmentType: [{ required: true, message: '出库类型不能为空', trigger: 'change' }],
  customerId: [{ required: true, message: '客户不能为空', trigger: 'change' }],
  warehouseId: [{ required: true, message: '仓库不能为空', trigger: 'change' }],
  expectTime: [{ required: true, message: '预计出库时间不能为空', trigger: 'blur' }],
})

const formRef = ref<FormInstance>() // 表单 Ref

// 初始化客户和仓库数据
const loadBaseData = async () => {
  try {
    // 加载客户列表
    const customersRes = await CustomerApi.getCustomerPage({
      pageNo: 1,
      pageSize: 100
    })
    customerOptions.value = customersRes?.list || []
    
    // 加载仓库列表
    const warehousesRes = await WarehouseApi.getWarehousePage({
      pageNo: 1,
      pageSize: 100
    })
    warehouseOptions.value = warehousesRes?.list || []
  } catch (error) {
    console.error('加载基础数据失败', error)
    message.error('加载基础数据失败，请刷新页面重试')
  }
}

// 加载库存列表
const loadInventoryList = async () => {
  if (!formData.value.warehouseId) {
    message.warning('请先选择仓库')
    return
  }
  
  inventoryLoading.value = true
  try {
    const res = await ItemStockApi.getItemStockPage({
      pageNo: 1,
      pageSize: 100,
      warehouseId: formData.value.warehouseId,
      includeLocationInfo: true // 请求包含库位详细信息
    })
    inventoryList.value = res?.list || []
  } catch (error) {
    console.error('加载库存数据失败', error)
    message.error('加载库存数据失败，请刷新页面重试')
  } finally {
    inventoryLoading.value = false
  }
}

// 过滤后的库存列表
const filteredInventoryList = computed(() => {
  let result = inventoryList.value
  
  // 按物料名称过滤
  if (inventorySearch.itemName) {
    result = result.filter(item => 
      item.itemName?.includes(inventorySearch.itemName) || 
      item.itemCode?.includes(inventorySearch.itemName)
    )
  }
  
  // 按物料类型过滤 - 仅当itemType不为0时过滤
  if (inventorySearch.itemType !== 0) {
    result = result.filter(item => item.itemType === inventorySearch.itemType)
  }
  
  // 过滤掉已经添加的物料
  const existingItemIds = formData.value.details?.map(detail => detail.itemId) || []
  if (existingItemIds.length > 0) {
    result = result.filter(item => !existingItemIds.includes(item.itemId))
  }
  
  // 过滤掉库存为0的物料
  result = result.filter(item => item.availableCount > 0)
  
  return result
})

// 重置库存搜索条件
const resetInventorySearch = () => {
  inventorySearch.itemName = ''
  inventorySearch.itemType = 0
  loadInventoryList()
}

// 处理库存选择变化
const handleInventorySelectionChange = (selection: any[]) => {
  selectedInventory.value = selection
}

// 打开库存选择对话框
const openInventorySelectionDialog = () => {
  if (!formData.value.warehouseId) {
    message.warning('请先选择仓库')
    return
  }
  
  selectedInventory.value = []
  inventorySelectionVisible.value = true
  loadInventoryList()
}

// 处理仓库变更
const handleWarehouseChange = (warehouseId: number) => {
  if (formData.value.details && formData.value.details.length > 0) {
    ElMessageBox.confirm(
      '更换仓库将清空已添加的明细，是否继续？',
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
    ).then(() => {
      formData.value.details = []
      calculateTotals()
    }).catch(() => {
      // 恢复原来的仓库ID
      nextTick(() => {
        formData.value.warehouseId = warehouseId
      })
    })
  }
}

// 添加选中的库存项
const handleAddSelectedInventory = () => {
  if (selectedInventory.value.length === 0) {
    message.warning('请选择至少一项库存')
    return
  }
  
  // 将选中的库存项转换为出库单明细
  const newDetails: ShipmentOrderDetailVO[] = selectedInventory.value.map(item => ({
    id: 0,
    shipmentOrderId: formData.value.id || 0,
    warehouseId: formData.value.warehouseId, // 设置仓库ID
    areaId: item.areaId || null, // 设置货区ID
    rackId: item.rackId || null, // 设置货架ID
    itemId: item.itemId,
    itemCode: item.itemCode || '',
    itemName: item.itemName || '',
    itemType: item.itemType || 0,
    spec: item.spec || '',
    unit: item.unit || '',
    planCount: 0,
    realCount: 0,
    locationId: item.locationId || 0, // 设置库位ID
    batchId: 0,
    price: item.price || 0,
    status: 0,
    stockCount: item.availableCount || 0,
    remark: ''
  } as ShipmentOrderDetailVO))
  
  // 添加到表单明细中
  if (!formData.value.details) {
    formData.value.details = []
  }
  formData.value.details = [...formData.value.details, ...newDetails]
  
  // 关闭对话框
  inventorySelectionVisible.value = false
  
  // 计算总计
  calculateTotals()
  
  message.success(`已添加 ${newDetails.length} 项库存商品`)
}

// 清空明细
const handleClearDetails = () => {
  ElMessageBox.confirm(
    '确认要清空所有明细吗？',
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(() => {
    formData.value.details = []
    calculateTotals()
    message.success('已清空所有明细')
  }).catch(() => {})
}

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type as 'create' | 'edit' | 'view'
  resetForm()
  
  // 加载基础数据
  await loadBaseData()
  
  // 修改或查看时，设置数据
  if (id) {
    formLoading.value = true
    try {
      const result = await ShipmentOrderApi.getShipmentOrder(id)
      if (result) {
        formData.value = result
        // 确保details字段存在
        if (!formData.value.details) {
          formData.value.details = []
        }
      }
      // 计算总计
      calculateTotals()
    } catch (error) {
      console.error('获取出库单数据失败', error)
      message.error('获取出库单数据失败')
    } finally {
      formLoading.value = false
    }
  } else {
    // 新增时初始化details数组
    formData.value.details = []
  }
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 提交表单 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
const handleSaveDraft = async () => {
  if (!formRef.value) return
  
  // 表单校验
  await formRef.value.validate()
  
  // 校验明细
  if (!formData.value.details || formData.value.details.length === 0) {
    message.error('请至少添加一条出库单明细')
    return
  }
  
  // 校验明细数量
  const invalidDetail = formData.value.details.find(detail => !detail.planCount || detail.planCount <= 0)
  if (invalidDetail) {
    message.error('出库单明细的计划数量必须大于0')
    return
  }
  
  // 确认实际数量不超过计划数量
  const invalidRealCount = formData.value.details.find(detail => 
    detail.realCount !== undefined && 
    detail.realCount > detail.planCount
  )
  if (invalidRealCount) {
    message.error('出库单明细的实际数量不能超过计划数量')
    return
  }
  
  // 设置状态为草稿
  formData.value.orderStatus = 0 // 0-草稿
  
  try {
    formLoading.value = true
    // 计算总数量
    const totalCount = formData.value.details.reduce((sum, detail) => sum + (detail.planCount || 0), 0)
    const data = {
      ...formData.value,
      totalCount,
      totalAmount: 0 // 如果需要计算总金额，在这里添加计算逻辑
    }
    
    // 提交表单
    if (formType.value === 'create') {
      await ShipmentOrderApi.createShipmentOrder(data)
      message.success('保存成功')
    } else {
      await ShipmentOrderApi.updateShipmentOrder(data)
      message.success('修改成功')
    }
    // 关闭弹窗
    dialogVisible.value = false
    // 通知父组件刷新
    emit('success')
  } catch (error: any) {
    message.error('保存失败：' + (error.message || '未知错误'))
  } finally {
    formLoading.value = false
  }
}

// 提交出库
const handleSubmitShipment = async () => {
  if (!formRef.value) return
  
  // 表单校验
  await formRef.value.validate()
  
  // 校验明细
  if (!formData.value.details || formData.value.details.length === 0) {
    message.error('请至少添加一条出库单明细')
    return
  }
  
  // 校验明细数量
  const invalidDetail = formData.value.details.find(detail => !detail.planCount || detail.planCount <= 0)
  if (invalidDetail) {
    message.error('出库单明细的计划数量必须大于0')
    return
  }
  
  // 确认实际数量不超过计划数量
  const invalidRealCount = formData.value.details.find(detail => 
    detail.realCount !== undefined && 
    detail.realCount > detail.planCount
  )
  if (invalidRealCount) {
    message.error('出库单明细的实际数量不能超过计划数量')
    return
  }
  
  // 设置状态为待审核
  formData.value.orderStatus = 1 // 1-待审核
  
  try {
    formLoading.value = true
    // 计算总数量（计划数量）
    const totalCount = formData.value.details.reduce((sum, detail) => sum + (detail.planCount || 0), 0)
    const data = {
      ...formData.value,
      totalCount,
      totalAmount: 0 // 如果需要计算总金额，在这里添加计算逻辑
    }
    
    // 提交表单
    if (formType.value === 'create') {
      await ShipmentOrderApi.createShipmentOrder(data)
      message.success('提交成功')
    } else {
      await ShipmentOrderApi.updateShipmentOrder(data)
      message.success('提交成功')
    }
    // 关闭弹窗
    dialogVisible.value = false
    // 通知父组件刷新
    emit('success')
  } catch (error: any) {
    message.error('提交失败：' + (error.message || '未知错误'))
  } finally {
    formLoading.value = false
  }
}

/** 删除明细 */
const handleDeleteDetail = (index: number) => {
  if (formData.value.details) {
    formData.value.details.splice(index, 1)
    calculateTotals()
  }
}

/** 计算总计 */
const calculateTotals = () => {
  if (!formData.value.details || formData.value.details.length === 0) {
    formData.value.totalCount = 0
    return
  }
  
  // 计算计划总数量
  formData.value.totalCount = formData.value.details.reduce(
    (sum, detail) => sum + (detail.planCount || 0), 
    0
  )
  
  // 如果需要，也可以计算实际总数量和总金额
  // 目前仅作为前端显示，不影响提交的数据
  const totalRealCount = formData.value.details.reduce(
    (sum, detail) => sum + (detail.realCount || 0), 
    0
  )
  
  // 计算总金额
  formData.value.totalAmount = formData.value.details.reduce(
    (sum, detail) => sum + ((detail.realCount || detail.planCount || 0) * (detail.price || 0)), 
    0
  )
}

/** 格式化金额 */
const formatAmount = (amount: number) => {
  return amount.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,')
}

/** 重置表单 */
const resetForm = () => {
  formData.value = {
    id: 0,
    shipmentOrderNo: '',
    shipmentType: 0,
    customerId: 0,
    warehouseId: 0,
    orderStatus: 0,
    shipmentStatus: 0,
    expectTime: 0,
    completeTime: 0,
    totalCount: 0,
    totalAmount: 0,
    remark: '',
    details: []
  }
  formRef.value?.resetFields()
}

// 监听明细数据变化，重新计算总计
watch(() => formData.value.details, () => {
  calculateTotals()
}, { deep: true })

// 组件挂载时初始化
onMounted(() => {
  // 在组件初始化时不必加载基础数据，打开弹窗时会加载
})

// 执行出库操作
const handleExecuteShipment = async (detail: ShipmentOrderDetailVO, index: number) => {
  // 确保仓库ID存在
  if (!formData.value.warehouseId) {
    message.warning('请先选择仓库')
    return
  }
  
  // 计算可出库数量（计划数量减去已出库数量）
  const availableCount = detail.planCount - (detail.realCount || 0)
  if (availableCount <= 0) {
    message.warning('已达到计划出库数量，无法继续出库')
    return
  }
  
  // 弹出输入框，输入本次出库数量
  try {
    const { value: quantity } = await ElMessageBox.prompt(
      '请输入本次出库数量',
      '执行出库操作', 
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputType: 'number',
        inputValue: availableCount.toString(), // 转换为字符串类型
        inputValidator: (val) => {
          const num = Number(val)
          return (num > 0 && num <= availableCount) || '出库数量必须大于0且不能超过可出库数量'
        },
        inputPlaceholder: `可出库数量: ${availableCount}`
      }
    )
    
    if (!quantity) return
    
    // 执行出库操作
    const shipmentData: ShipmentOperationReqVO = {
      shipmentOrderId: formData.value.id || 0,
      detailId: detail.id || 0,
      quantity: Number(quantity),
      warehouseId: formData.value.warehouseId, // 传递仓库ID
      remark: detail.remark || ''
    };
    
    // 如果用户选择了库位，则添加相关信息
    if (detail.locationId) {
      shipmentData.locationId = detail.locationId;
      
      // 如果有库位信息，尝试获取货区和货架ID
      try {
        const locationInfo = await getLocationInfo(detail.locationId);
        if (locationInfo) {
          shipmentData.areaId = locationInfo.areaId;
          shipmentData.rackId = locationInfo.rackId;
        }
      } catch (error) {
        console.error('获取库位信息失败', error);
        // 继续执行出库操作，后端会处理
      }
    } else {
      // 没有选择库位时，后端将自动选择最早入库的库位
      console.log('未指定库位，后端将自动选择最早入库的库位');
    }
    
    formLoading.value = true
    try {
      await ShipmentOrderApi.executeShipment(shipmentData)
      
      // 更新本地数据
      detail.realCount = (detail.realCount || 0) + Number(quantity)
      
      // 更新明细状态
      if (detail.realCount >= detail.planCount) {
        detail.status = 2 // 已出库
      } else if (detail.realCount > 0) {
        detail.status = 1 // 部分出库
      }
      
      // 更新整个出库单状态
      updateOrderShipmentStatus()
      
      // 刷新数据
      calculateTotals()
      
      message.success('出库操作成功')
    } catch (error: any) {
      message.error('出库操作失败：' + (error.message || '未知错误'))
    } finally {
      formLoading.value = false
    }
  } catch (e) {
    // 用户取消操作
  }
}

// 根据库位ID获取库位完整信息
const getLocationInfo = async (locationId: number) => {
  if (!locationId) return null;
  
  try {
    // 调用库位服务获取完整信息
    const response = await request.get({ 
      url: `/wms/location/get?id=${locationId}` 
    });
    
    if (response && response.data) {
      return {
        warehouseId: response.data.warehouseId,
        areaId: response.data.areaId,
        rackId: response.data.rackId
      };
    }
    return null;
  } catch (error) {
    console.error('获取库位信息失败', error);
    return null;
  }
}

// 更新出库单出库状态
const updateOrderShipmentStatus = () => {
  if (!formData.value.details || formData.value.details.length === 0) {
    return
  }
  
  // 检查是否所有明细都已完成出库
  const allCompleted = formData.value.details.every(detail => detail.status === 2)
  if (allCompleted) {
    formData.value.shipmentStatus = 2 // 已完成
    return
  }
  
  // 检查是否有部分出库
  const hasPartial = formData.value.details.some(detail => detail.status === 1 || detail.status === 2)
  if (hasPartial) {
    formData.value.shipmentStatus = 1 // 部分出库
    return
  }
  
  // 默认为待出库
  formData.value.shipmentStatus = 0
}

// 完成出库
const handleCompleteShipment = async () => {
  if (!formData.value.id) {
    message.warning('出库单ID不能为空')
    return
  }
  
  try {
    await ElMessageBox.confirm(
      '确认标记此出库单为已完成状态吗？此操作将无法撤销。',
      '完成出库单',
      {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    formLoading.value = true
    try {
      await ShipmentOrderApi.completeShipmentOrder(formData.value.id)
      
      // 更新本地状态
      formData.value.shipmentStatus = 2 // 已完成
      formData.value.completeTime = Date.now() // 更新完成时间为当前时间
      
      // 更新所有明细状态为已出库
      if (formData.value.details) {
        formData.value.details.forEach(detail => {
          detail.status = 2 // 已出库
          // 如果实际数量为0，则设置为与计划数量相等
          if (!detail.realCount) {
            detail.realCount = detail.planCount
          }
        })
      }
      
      message.success('出库单已完成')
      dialogVisible.value = false
      emit('success')
    } catch (error: any) {
      message.error('完成出库单失败：' + (error.message || '未知错误'))
    } finally {
      formLoading.value = false
    }
  } catch (e) {
    // 用户取消操作
  }
}
</script>

<style scoped>
.shipment-order-container {
  padding: 0 20px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.section-header h3 {
  font-size: 16px;
  font-weight: bold;
  color: var(--el-text-color-primary);
  margin: 0;
}

.operation-group {
  display: flex;
  gap: 8px;
}

.summary-section {
  margin-top: 20px;
  padding: 16px;
  background: var(--el-fill-color-blank);
  border: 1px solid var(--el-border-color-light);
  border-radius: 4px;
}

.search-bar {
  margin-bottom: 16px;
}

.search-form {
  padding: 18px 20px;
  background-color: var(--el-fill-color-blank);
  border-radius: 4px;
  border: 1px solid var(--el-border-color-light);
}

:deep(.el-descriptions) {
  padding: 0;
}

:deep(.el-descriptions__cell) {
  padding: 12px 16px;
}

:deep(.el-descriptions__label) {
  width: 120px;
  justify-content: flex-end;
  margin-right: 16px;
  color: var(--el-text-color-regular);
  font-weight: normal;
  background-color: var(--el-fill-color-light) !important;
}

:deep(.el-descriptions__content) {
  font-weight: bold;
  color: var(--el-text-color-primary);
}

:deep(.el-table) {
  margin: 16px 0;
}

:deep(.el-form-item) {
  margin-bottom: 18px;
}

:deep(.el-form-item__label) {
  font-weight: normal;
  color: var(--el-text-color-regular);
}

:deep(.el-input-number) {
  width: 100%;
}

.w-full {
  width: 100%;
}

.mr-5 {
  margin-right: 5px;
}

.ml-2 {
  margin-left: 2px;
}

.text-xs {
  font-size: 12px;
}

.text-gray-400 {
  color: var(--el-text-color-secondary);
}

.dialog-footer {
  padding: 16px 20px;
  text-align: right;
}
</style>
