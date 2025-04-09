<template>
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="68px"
    >
      <el-form-item label="盘点单号" prop="checkNo">
        <el-input
          v-model="queryParams.checkNo"
          placeholder="请输入盘点单号"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="仓库" prop="warehouseId">
        <el-select
          v-model="queryParams.warehouseId"
          placeholder="请选择仓库"
          clearable
          filterable
          class="!w-240px"
        >
          <el-option
            v-for="item in warehouseOptions"
            :key="item.id"
            :label="item.warehouseName || `仓库${item.id}`"
            :value="Number(item.id)"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="盘点类型" prop="checkType">
        <el-select v-model="queryParams.checkType" placeholder="请选择盘点类型" clearable class="!w-240px">
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.WMS_CHECK_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="盘点状态" prop="checkStatus">
        <el-select v-model="queryParams.checkStatus" placeholder="请选择盘点状态" clearable class="!w-240px">
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.WMS_CHECK_STATUS)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery"><Icon icon="ep:search" class="mr-5px" /> 搜索</el-button>
        <el-button @click="resetQuery"><Icon icon="ep:refresh" class="mr-5px" /> 重置</el-button>
        <el-button
          type="primary"
          plain
          @click="openForm('create')"
          v-hasPermi="['wms:inventory-check:create']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> 新增
        </el-button>
        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['wms:inventory-check:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> 导出
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column label="盘点单号" align="center" prop="checkNo" min-width="120px" />
      <el-table-column label="仓库" align="center" min-width="120px">
        <template #default="{ row }">
          {{ getWarehouseName(row.warehouseId) }}
        </template>
      </el-table-column>
      <el-table-column label="盘点类型" align="center" prop="checkType" min-width="100px">
        <template #default="{ row }">
          <dict-tag :type="DICT_TYPE.WMS_CHECK_TYPE" :value="row.checkType" />
        </template>
      </el-table-column>
      <el-table-column label="盘点状态" align="center" prop="checkStatus" min-width="100px">
        <template #default="{ row }">
          <dict-tag :type="DICT_TYPE.WMS_CHECK_STATUS" :value="row.checkStatus" />
        </template>
      </el-table-column>
      <el-table-column label="盘点进度" align="center" min-width="180px">
        <template #default="{ row }">
          <div>
            <el-progress 
              :percentage="row.totalCount ? Math.round(row.checkedCount / row.totalCount * 100) : 0"
              :status="row.checkStatus === 2 ? 'success' : ''"
            />
          </div>
          <div class="text-gray-400 text-sm">
            已盘点: {{ row.checkedCount }} / {{ row.totalCount }}
          </div>
        </template>
      </el-table-column>
      <el-table-column label="差异数" align="center" min-width="100px">
        <template #default="{ row }">
          <el-tag :type="row.differenceCount > 0 ? 'warning' : 'success'">
            {{ row.differenceCount }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" min-width="120px" show-overflow-tooltip />
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column label="操作" align="center" min-width="180px">
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="handleViewDetail(scope.row.id)"
            v-hasPermi="['wms:inventory-check:query']"
          >
            查看
          </el-button>
          <el-button
            v-if="scope.row.checkStatus === 0"
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
            v-hasPermi="['wms:inventory-check:update']"
          >
            编辑
          </el-button>
          <el-button
            v-if="scope.row.checkStatus === 0"
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['wms:inventory-check:delete']"
          >
            删除
          </el-button>
          <el-button
            v-if="scope.row.checkStatus === 0"
            link
            type="success"
            @click="handleStart(scope.row.id)"
            v-hasPermi="['wms:inventory-check:start']"
          >
            开始盘点
          </el-button>
          <el-button
            v-if="scope.row.checkStatus === 1"
            link
            type="warning"
            @click="handleFinish(scope.row.id)"
            v-hasPermi="['wms:inventory-check:finish']"
          >
            完成盘点
          </el-button>
          <el-button
            v-if="scope.row.checkStatus === 1"
            link
            type="warning"
            @click="handleCancel(scope.row.id)"
            v-hasPermi="['wms:inventory-check:update']"
          >
            取消盘点
          </el-button>
          <el-button
            v-if="scope.row.checkStatus === 2"
            link
            type="danger"
            @click="handleAdjust(scope.row.id)"
            v-hasPermi="['wms:inventory-check:update']"
          >
            调整库存
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <Pagination
      :total="total"
      v-model:page="queryParams.pageNo"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
  </ContentWrap>

  <!-- 表单弹窗：添加/修改 -->
  <InventoryCheckForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import { InventoryCheckApi, InventoryCheckVO } from '@/api/wms/inventorycheck'
import InventoryCheckForm from './InventoryCheckForm.vue'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { WarehouseApi } from '@/api/wms/warehouse'

/** 库存盘点 列表 */
defineOptions({ name: 'InventoryCheck' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const list = ref<InventoryCheckVO[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const warehouseOptions = ref<any[]>([]) // 仓库选项
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  checkNo: undefined,
  warehouseId: undefined,
  checkType: undefined,
  checkStatus: undefined,
  totalCount: undefined,
  checkedCount: undefined,
  differenceCount: undefined,
  remark: undefined,
  createTime: [],
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中


/** 获取仓库名称 */
const getWarehouseName = (warehouseId: number) => {
  const warehouse = warehouseOptions.value.find(item => item.id === warehouseId)
  return warehouse ? warehouse.warehouseName || `仓库${warehouse.id}` : '-'
}

/** 加载基础数据 */
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

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await InventoryCheckApi.getInventoryCheckPage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.pageNo = 1
  getList()
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value.resetFields()
  handleQuery()
}

/** 添加/修改操作 */
const formRef = ref()
const openForm = (type: string, id?: number) => {
  formRef.value.open(type, id)
}

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await InventoryCheckApi.deleteInventoryCheck(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await InventoryCheckApi.exportInventoryCheck(queryParams)
    download.excel(data, '库存盘点单.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

/** 开始盘点 */
const handleStart = async (id: number) => {
  try {
    await message.confirm('是否确认开始盘点？')
    await InventoryCheckApi.startCheck(id)
    message.success('开始盘点成功')
    await getList()
  } catch {}
}

/** 完成盘点 */
const handleFinish = async (id: number) => {
  try {
    await message.confirm('是否确认完成盘点？')
    await InventoryCheckApi.completeCheck({ id })
    message.success('完成盘点成功')
    await getList()
  } catch {}
}

/** 取消盘点 */
const handleCancel = async (id: number) => {
  try {
    await message.confirm('是否确认取消盘点？')
    await InventoryCheckApi.cancelCheck({ id })
    message.success('取消盘点成功')
    await getList()
  } catch {}
}

/** 调整库存 */
const handleAdjust = async (id: number) => {
  try {
    await message.confirm('是否确认调整库存差异？')
    await InventoryCheckApi.adjustInventory({ id })
    message.success('调整库存成功')
    await getList()
  } catch {}
}

/** 查看明细 */
const router = useRouter()
const handleViewDetail = (id: number) => {
  router.push({ name: 'InventoryCheckDetail', params: { id } })
}

/** 初始化 **/
onMounted(() => {
  getWarehouseOptions()
  getList()
})
</script>
