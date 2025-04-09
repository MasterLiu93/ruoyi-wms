<template>
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form
      ref="queryFormRef"
      :model="queryParams"
      :inline="true"
      label-width="68px"
    >
      <el-form-item label="仓库" prop="warehouseId">
        <el-select v-model="queryParams.warehouseId" placeholder="请选择仓库" clearable style="width: 200px">
          <el-option
            v-for="item in warehouseOptions"
            :key="item.id"
            :label="item.warehouseName"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="物料" prop="itemId">
        <el-select v-model="queryParams.itemId" placeholder="请选择物料" clearable style="width: 200px">
          <el-option
            v-for="item in itemOptions"
            :key="item.id"
            :label="item.itemName"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery"><el-icon><Search /></el-icon>搜索</el-button>
        <el-button @click="resetQuery"><el-icon><Refresh /></el-icon>重置</el-button>
      </el-form-item>
    </el-form>
    <!-- 操作工具栏 -->
    <el-row :gutter="10" class="mb-10">
      <el-col :span="1.5">
        <el-button type="primary" @click="openForm('create')" v-hasPermi="['wms:inventory:create']">
          <el-icon><Plus /></el-icon>新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" @click="handleExport" :loading="exportLoading"
          v-hasPermi="['wms:inventory:export']">
          <el-icon><Download /></el-icon>导出
        </el-button>
      </el-col>
    </el-row>
    <!-- 列表 -->
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column label="物料信息" min-width="200">
        <template #default="{ row }">
          <div>
            <div class="font-medium">{{ row.itemName }}</div>
            <div class="text-gray-400 text-xs mt-1">
              <span>编码：{{ row.itemCode }}</span>
              <span class="mx-1">|</span>
              <span>规格：{{ row.spec }}</span>
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="仓库/库位" align="center" min-width="150">
        <template #default="{ row }">
          <div>
            <div>{{ row.warehouseName }}</div>
            <div class="text-gray-400 text-xs mt-1">{{ row.locationName }}</div>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="物料类型" align="center" width="100">
        <template #default="{ row }">
          <dict-tag :type="DICT_TYPE.WMS_ITEM_TYPE" :value="row.itemType" />
        </template>
      </el-table-column>
      <el-table-column label="库存信息" align="right" min-width="180">
        <template #default="{ row }">
          <div class="flex flex-col items-end">
            <div>
              <span class="font-medium">{{ row.stockCount }}</span>
              <span class="text-gray-400 text-xs ml-1">{{ row.unit }}</span>
            </div>
            <div class="text-gray-400 text-xs mt-1">
              <span>可用：{{ row.availableCount }}</span>
              <span class="mx-1">|</span>
              <span>锁定：{{ row.lockedCount }}</span>
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" width="100">
        <template #default="{ row }">
          <dict-tag :type="DICT_TYPE.COMMON_STATUS" :value="row.status" />
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" min-width="150" :show-overflow-tooltip="true">
        <template #default="{ row }">
          {{ row.remark || '-' }}
        </template>
      </el-table-column>
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        :formatter="dateFormatter"
        width="180"
      />
      <el-table-column label="操作" align="center" width="130" fixed="right">
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
            v-hasPermi="['wms:inventory:update']"
          >
            编辑
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['wms:inventory:delete']"
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
  <InventoryForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import { InventoryApi, InventoryVO } from '@/api/wms/inventory'
import { WarehouseApi } from '@/api/wms/warehouse'
import { ItemApi } from '@/api/wms/item'
import InventoryForm from './InventoryForm.vue'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { Search, Plus, Refresh, Download } from '@element-plus/icons-vue'

/** 库存 列表 */
defineOptions({ name: 'Inventory' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const list = ref<InventoryVO[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  warehouseId: undefined,
  itemId: undefined,
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中

// 仓库选项
const warehouseOptions = ref<any[]>([])
// 物料选项
const itemOptions = ref<any[]>([])

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await InventoryApi.getInventoryPage(queryParams)
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
    await InventoryApi.deleteInventory(id)
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
    const data = await InventoryApi.exportInventory(queryParams)
    download.excel(data, '库存.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

/** 加载基础数据 */
const loadBaseData = async () => {
  try {
    // 加载仓库列表
    const warehousesRes = await WarehouseApi.getWarehousePage({
      pageNo: 1,
      pageSize: 100
    })
    warehouseOptions.value = warehousesRes?.list || []
    
    // 加载物料列表
    const itemsRes = await ItemApi.getItemPage({
      pageNo: 1,
      pageSize: 100
    })
    itemOptions.value = itemsRes?.list || []
  } catch (error) {
    console.error('加载基础数据失败', error)
    message.error('加载基础数据失败，请刷新页面重试')
  }
}

/** 初始化 **/
onMounted(async () => {
  await loadBaseData()
  await getList()
})
</script>

<style scoped>
.text-gray-400 {
  color: #9ca3af;
}
.text-xs {
  font-size: 0.75rem;
}
.text-sm {
  font-size: 0.875rem;
}
.font-medium {
  font-weight: 500;
}
.mt-1 {
  margin-top: 0.25rem;
}
.ml-1 {
  margin-left: 0.25rem;
}
.mx-1 {
  margin-left: 0.25rem;
  margin-right: 0.25rem;
}
.flex {
  display: flex;
}
.flex-col {
  flex-direction: column;
}
.items-end {
  align-items: flex-end;
}
</style>
