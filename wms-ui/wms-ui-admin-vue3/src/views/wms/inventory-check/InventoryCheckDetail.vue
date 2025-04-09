<template>
  <ContentWrap>
    <el-card shadow="never" class="mb-4">
      <template #header>
        <div class="flex justify-between items-center">
          <h3 class="text-xl font-bold">基本信息</h3>
          <InventoryCheckActions 
            v-if="checkDetail" 
            :id="id" 
            :checkStatus="checkDetail.checkStatus" 
            @success="getCheckDetail"
          />
        </div>
      </template>
      <el-descriptions v-loading="loading" :column="3" border>
        <el-descriptions-item label="盘点单号">{{ checkDetail?.checkNo || '-' }}</el-descriptions-item>
        <el-descriptions-item label="仓库">{{ getWarehouseName(checkDetail?.warehouseId) }}</el-descriptions-item>
        <el-descriptions-item label="盘点类型">
          <dict-tag :type="DICT_TYPE.WMS_CHECK_TYPE" :value="checkDetail?.checkType" />
        </el-descriptions-item>
        <el-descriptions-item label="盘点状态">
          <dict-tag :type="DICT_TYPE.WMS_CHECK_STATUS" :value="checkDetail?.checkStatus" />
        </el-descriptions-item>
        <el-descriptions-item label="盘点进度">
          <el-progress 
            :percentage="checkDetail?.totalCount ? Math.round(checkDetail?.checkedCount / checkDetail?.totalCount * 100) : 0"
            :status="checkDetail?.checkStatus === 2 ? 'success' : ''"
          />
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ formatDate(checkDetail?.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="盘点总数">{{ checkDetail?.totalCount || 0 }}</el-descriptions-item>
        <el-descriptions-item label="已盘点数">{{ checkDetail?.checkedCount || 0 }}</el-descriptions-item>
        <el-descriptions-item label="差异数">
          <el-tag :type="checkDetail?.differenceCount > 0 ? 'warning' : 'success'">
            {{ checkDetail?.differenceCount || 0 }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item :span="3" label="备注">{{ checkDetail?.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-card>
    
    <el-card shadow="never">
      <template #header>
        <div class="flex justify-between items-center">
          <h3 class="text-xl font-bold">盘点明细</h3>
          <div>
            <el-button v-if="checkDetail?.checkStatus === 1" type="primary" @click="openBatchSubmitDialog">
              批量提交
            </el-button>
            <el-button
              type="success"
              plain
              @click="handleExport"
              :loading="exportLoading"
              v-hasPermi="['wms:inventory-check-detail:export']"
            >
              <Icon icon="ep:download" class="mr-5px" /> 导出
            </el-button>
          </div>
        </div>
      </template>
      
      <!-- 明细列表 -->
      <el-table 
        v-loading="detailLoading" 
        :data="checkDetails" 
        :stripe="true" 
        :border="true"
        :highlight-current-row="true"
      >
        <el-table-column label="物料信息" min-width="200" align="center">
          <template #default="{ row }">
            <div>{{ row.itemName }}</div>
            <div class="text-gray-400 text-sm">{{ row.itemCode }}</div>
          </template>
        </el-table-column>
        <el-table-column label="库位信息" min-width="180" align="center">
          <template #default="{ row }">
            <div>{{ row.locationName }}</div>
            <div class="text-gray-400 text-sm">{{ row.locationCode }}</div>
          </template>
        </el-table-column>
        <el-table-column label="盘点数量" min-width="180" align="center">
          <template #default="{ row }">
            <div>
              <span v-if="row.checkStatus === 1">{{ row.checkCount }}</span>
              <el-input-number 
                v-else-if="checkDetail?.checkStatus === 1"
                v-model="row.inputCheckCount" 
                :min="0"
                size="small" 
                class="!w-32"
              />
              <span v-else>{{ row.checkCount }}</span>
            </div>
            <div class="text-gray-400 text-sm">账面: {{ row.bookCount }}</div>
          </template>
        </el-table-column>
        <el-table-column label="差异数量" min-width="120" align="center">
          <template #default="{ row }">
            <el-tag 
              v-if="row.checkStatus === 1" 
              :type="row.differenceCount > 0 ? 'warning' : row.differenceCount < 0 ? 'danger' : 'success'"
            >
              {{ row.differenceCount }}
            </el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="盘点状态" min-width="100" align="center">
          <template #default="{ row }">
            <dict-tag :type="DICT_TYPE.WMS_CHECK_STATUS" :value="row.checkStatus" />
          </template>
        </el-table-column>
        <el-table-column label="备注" prop="remark" min-width="120" show-overflow-tooltip />
        <el-table-column label="操作" align="center" width="180" v-if="checkDetail?.checkStatus === 1">
          <template #default="{ row }">
            <el-button
              v-if="row.checkStatus === 0"
              link
              type="primary"
              @click="handleSubmitResult(row)"
            >
              提交盘点
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <Pagination
        class="mt-4"
        :total="total"
        v-model:page="queryParams.pageNo"
        v-model:limit="queryParams.pageSize"
        @pagination="getDetailList"
      />
    </el-card>
    
    <!-- 批量提交对话框 -->
    <el-dialog v-model="batchSubmitDialogVisible" title="批量提交盘点结果" width="700px">
      <el-table 
        ref="batchSubmitTableRef"
        :data="batchSubmitDetails"
        :stripe="true"
        :border="true"
        height="400px"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="50" />
        <el-table-column label="物料编码" prop="itemCode" align="center" />
        <el-table-column label="物料名称" prop="itemName" align="center" />
        <el-table-column label="库位编码" prop="locationCode" align="center" />
        <el-table-column label="账面数量" prop="bookCount" align="center" />
        <el-table-column label="盘点数量" align="center" width="150">
          <template #default="{ row }">
            <el-input 
              v-model="row.checkCount" 
              type="number" 
              :min="0"
              size="small"
              class="!w-24"
            />
          </template>
        </el-table-column>
        <el-table-column label="备注" align="center" width="150">
          <template #default="{ row }">
            <el-input 
              v-model="row.remark" 
              size="small"
              placeholder="备注"
            />
          </template>
        </el-table-column>
      </el-table>
      
      <template #footer>
        <el-button @click="batchSubmitDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleBatchSubmit" :loading="batchSubmitLoading">
          提交
        </el-button>
      </template>
    </el-dialog>
  </ContentWrap>
</template>

<script setup lang="ts">
import { InventoryCheckApi, InventoryCheckDetailSubmitVO, InventoryCheckBatchResultVO } from '@/api/wms/inventorycheck'
import { InventoryCheckDetailApi } from '@/api/wms/inventorycheckdetail'
import { WarehouseApi } from '@/api/wms/warehouse'
import InventoryCheckActions from './InventoryCheckActions.vue'
import { DICT_TYPE } from '@/utils/dict'
import { formatDate } from '@/utils/formatTime'
import download from '@/utils/download'

defineOptions({ name: 'InventoryCheckDetail' })

const route = useRoute()
const message = useMessage()
const id = computed(() => Number(route.query.id || 0))

// 盘点单详情
const loading = ref(false)
const checkDetail = ref()

// 盘点进度
const checkProgress = ref<any>(null)

// 仓库名称
const warehouseName = ref('-')

// 仓库选项
const warehouseOptions = ref<any[]>([])

// 盘点单详情列表
const detailLoading = ref(false)
const checkDetails = ref<any[]>([])
const total = ref(0)
const queryParams = reactive({
  checkId: id,
  pageNo: 1,
  pageSize: 10
})

// 批量提交相关
const batchSubmitDialogVisible = ref(false)
const batchSubmitDetails = ref<any[]>([])
const selectedDetails = ref<any[]>([])
const batchSubmitLoading = ref(false)
const batchSubmitTableRef = ref()

// 导出按钮操作
const exportLoading = ref(false)

// 查询盘点单详情
const getCheckDetail = async () => {
  if (!id.value) return
  
  loading.value = true
  try {
    const data = await InventoryCheckApi.getInventoryCheck(id.value)
    checkDetail.value = data
    
    // 获取仓库名称
    if (data.warehouseId) {
      try {
        const warehouse = await WarehouseApi.getWarehouse(data.warehouseId)
        warehouseName.value = warehouse?.name || '-'
      } catch (error) {
        console.error('获取仓库详情失败:', error)
      }
    }
    
    // 获取盘点进度
    getCheckProgress()
  } catch (error) {
    console.error('获取盘点单详情失败:', error)
  } finally {
    loading.value = false
  }
}

// 获取盘点进度
const getCheckProgress = async () => {
  if (!id.value) return
  
  try {
    const progress = await InventoryCheckApi.getCheckProgress(id.value)
    checkProgress.value = progress
  } catch (error) {
    console.error('获取盘点进度失败:', error)
  }
}

// 盘点类型文字
const checkTypeText = computed(() => {
  if (!checkDetail.value) return '-'
  return checkDetail.value.checkType === 0 ? '全部盘点' : '部分盘点'
})

// 盘点状态文字
const checkStatusText = computed(() => {
  if (!checkDetail.value) return '-'
  switch (checkDetail.value.checkStatus) {
    case 0: return '新建'
    case 1: return '盘点中'
    case 2: return '已完成'
    default: return '未知'
  }
})

// 获取状态标签类型
const getStatusTagType = (status: any) => {
  switch (status) {
    case 0: return 'info'
    case 1: return 'warning'
    case 2: return 'success'
    default: return 'info'
  }
}

// 获取进度状态
const getProgressStatus = () => {
  if (!checkProgress.value) return ''
  const { progress } = checkProgress.value
  return progress >= 100 ? 'success' : ''
}

// 查询盘点单明细列表
const getDetailList = async () => {
  if (!id.value) return
  
  detailLoading.value = true
  try {
    const data = await InventoryCheckDetailApi.getInventoryCheckDetailPage({
      ...queryParams,
      checkId: id.value
    })
    checkDetails.value = data.list.map(item => ({
      ...item,
      inputCheckCount: item.checkCount || item.bookCount
    }))
    total.value = data.total
  } catch (error) {
    console.error('获取盘点明细列表失败:', error)
  } finally {
    detailLoading.value = false
  }
}

// 提交单个盘点结果
const handleSubmitResult = async (row: any) => {
  if (!row.inputCheckCount) {
    message.warning('请输入盘点数量')
    return
  }
  
  try {
    await InventoryCheckApi.submitCheckResult({
      detailId: row.id,
      checkCount: row.inputCheckCount,
      remark: row.remark
    })
    message.success('提交成功')
    await getDetailList()
  } catch {
    // 错误处理由全局处理器处理
  }
}

// 打开批量提交对话框
const openBatchSubmitDialog = async () => {
  try {
    detailLoading.value = true
    // 获取所有未盘点的明细
    const res = await InventoryCheckDetailApi.getInventoryCheckDetailPage({
      checkId: id.value,
      checkStatus: 0,
      pageSize: 1000
    })
    
    batchSubmitDetails.value = res.list.map(item => ({
      ...item,
      checkCount: 0,
      remark: ''
    }))
    
    batchSubmitDialogVisible.value = true
  } catch (error) {
    console.error('获取未盘点明细失败:', error)
  } finally {
    detailLoading.value = false
  }
}

// 表格选择变化
const handleSelectionChange = (selection: any[]) => {
  selectedDetails.value = selection
}

// 批量提交盘点结果
const handleBatchSubmit = async () => {
  if (selectedDetails.value.length === 0) {
    message.warning('请选择要提交的盘点明细')
    return
  }
  
  const checkDetails: InventoryCheckDetailSubmitVO[] = selectedDetails.value.map(item => ({
    detailId: item.id,
    checkCount: Number(item.checkCount),
    remark: item.remark
  }))
  
  try {
    batchSubmitLoading.value = true
    const data: InventoryCheckBatchResultVO = {
      checkId: id.value,
      checkDetails
    }
    await InventoryCheckApi.batchSubmitCheckResult(data)
    message.success('批量提交成功')
    batchSubmitDialogVisible.value = false
    getDetailList()
    getCheckDetail()
  } catch (error) {
    console.error('批量提交盘点结果失败:', error)
  } finally {
    batchSubmitLoading.value = false
  }
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await InventoryCheckDetailApi.exportInventoryCheckDetail({
      ...queryParams,
      pageNo: 1,
      pageSize: Number.MAX_VALUE
    })
    download.excel(data, '库存盘点明细.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

/** 获取仓库名称 */
const getWarehouseName = (warehouseId?: number) => {
  if (!warehouseId) return '-'
  const warehouse = warehouseOptions.value.find(item => item.id === warehouseId)
  return warehouse ? warehouse.warehouseName || `仓库${warehouse.id}` : '-'
}

/** 加载基础数据 */
const loadBaseData = async () => {
  try {
    // 加载仓库列表
    const res = await WarehouseApi.getSimpleWarehouseList()
    warehouseOptions.value = res || []
  } catch (error) {
    console.error('加载基础数据失败:', error)
  }
}

// 页面加载时获取数据
onMounted(() => {
  loadBaseData()
  getCheckDetail()
  getDetailList()
})

// 监听路由参数变化
watch(() => id.value, () => {
  getCheckDetail()
  getDetailList()
})
</script> 