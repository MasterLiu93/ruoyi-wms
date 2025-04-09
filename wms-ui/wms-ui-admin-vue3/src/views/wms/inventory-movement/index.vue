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
      <el-form-item label="移动类型" prop="movementType">
        <el-select
          v-model="queryParams.movementType"
          placeholder="请选择移动类型"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.WMS_MOVE_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="移动单号" prop="movementNo">
        <el-input
          v-model="queryParams.movementNo"
          placeholder="请输入移动单号"
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
          class="!w-240px"
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
      <el-form-item label="库位" prop="locationId">
        <el-select
          v-model="queryParams.locationId"
          placeholder="请选择库位"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="item in locationOptions"
            :key="item.id"
            :label="item.locationName"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="物料" prop="itemId">
        <el-select
          v-model="queryParams.itemId"
          placeholder="请选择物料"
          clearable
          class="!w-240px"
          filterable
        >
          <el-option
            v-for="item in itemOptions"
            :key="item.id"
            :label="item.itemName"
            :value="item.id"
          >
            <span>{{ item.itemName }}</span>
            <span class="text-gray-400 ml-2">({{ item.itemCode }})</span>
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery"><Icon icon="ep:search" class="mr-5px" /> 搜索</el-button>
        <el-button @click="resetQuery"><Icon icon="ep:refresh" class="mr-5px" /> 重置</el-button>
        <el-button
          type="primary"
          plain
          @click="openForm('create')"
          v-hasPermi="['wms:inventory-movement:create']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> 新增
        </el-button>
        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['wms:inventory-movement:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> 导出
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column label="移动单号" align="center" prop="movementNo" min-width="120px" />
      <el-table-column label="移动类型" align="center" prop="movementType" min-width="100px">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.WMS_MOVE_TYPE" :value="scope.row.movementType" />
        </template>
      </el-table-column>
      <el-table-column label="仓库/库位" align="center" min-width="180px">
        <template #default="scope">
          <div>{{ scope.row.warehouseName }}</div>
          <div class="text-gray-400 text-sm">{{ scope.row.locationCode }}</div>
        </template>
      </el-table-column>
      <el-table-column label="物料信息" align="center" min-width="200px">
        <template #default="scope">
          <div>{{ scope.row.itemName }}</div>
          <div class="text-gray-400 text-sm">{{ scope.row.itemCode }}</div>
        </template>
      </el-table-column>
      <el-table-column label="移动数量" align="center" min-width="180px">
        <template #default="scope">
          <div>移动: {{ scope.row.count }}</div>
          <div class="text-gray-400 text-sm">
            {{ scope.row.beforeCount }} → {{ scope.row.afterCount }}
          </div>
        </template>
      </el-table-column>
      <el-table-column label="业务信息" align="center" min-width="180px">
        <template #default="scope">
          <div>{{ getDictLabel(DICT_TYPE.WMS_MOVE_TYPE, scope.row.businessType) }}</div>
          <div class="text-gray-400 text-sm">{{ scope.row.businessNo }}</div>
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
      <el-table-column label="操作" align="center" min-width="120px">
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
            v-hasPermi="['wms:inventory-movement:update']"
          >
            编辑
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['wms:inventory-movement:delete']"
          >
            删除
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
  <InventoryMovementForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import { InventoryMovementApi, InventoryMovementVO } from '@/api/wms/inventorymovement'
import InventoryMovementForm from './InventoryMovementForm.vue'
import { DICT_TYPE, getIntDictOptions, getDictLabel } from '@/utils/dict'
import { WarehouseApi } from '@/api/wms/warehouse'
import { ItemApi, ItemVO } from '@/api/wms/item'
import { LocationApi, LocationVO } from '@/api/wms/location'

/** 库存移动记录 列表 */
defineOptions({ name: 'InventoryMovement' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const list = ref<InventoryMovementVO[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  movementType: undefined,
  movementNo: undefined,
  warehouseId: undefined,
  locationId: undefined,
  itemId: undefined,
  count: undefined,
  beforeCount: undefined,
  afterCount: undefined,
  businessType: undefined,
  businessId: undefined,
  businessDetailId: undefined,
  remark: undefined,
  createTime: [],
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中
const locationOptions = ref<LocationVO[]>([]) // 库位选项
const itemOptions = ref<ItemVO[]>([]) // 物料选项
const warehouseOptions = ref<any[]>([]) // 仓库下拉选项

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await InventoryMovementApi.getInventoryMovementPage(queryParams)
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
    await InventoryMovementApi.deleteInventoryMovement(id)
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
    const data = await InventoryMovementApi.exportInventoryMovement(queryParams)
    download.excel(data, '库存移动记录.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

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

/** 处理仓库变化 */
const handleWarehouseChange = async () => {
  queryParams.locationId = undefined
  locationOptions.value = []
  if (!queryParams.warehouseId) return
  
  // 加载库位列表
  const locationRes = await LocationApi.getLocationPage({ 
    pageNo: 1, 
    pageSize: 100,
    warehouseId: queryParams.warehouseId 
  })
  locationOptions.value = locationRes.list
}

/** 初始化 **/
onMounted(() => {
  getWarehouseOptions()
  getList()
})
</script>
