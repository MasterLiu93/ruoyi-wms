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
      <el-form-item label="物料编码" prop="itemCode">
        <el-input
          v-model="queryParams.itemCode"
          placeholder="请输入物料编码"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="物料名称" prop="itemName">
        <el-input
          v-model="queryParams.itemName"
          placeholder="请输入物料名称"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="物料分类" prop="categoryId">
        <el-tree-select
          v-model="queryParams.categoryId"
          :data="categoryOptions"
          node-key="id"
          :props="{ label: 'categoryName', children: 'children' }"
          placeholder="请选择物料分类"
          clearable
          filterable
          check-strictly
          :default-expand-all="false"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="物料类型" prop="itemType">
        <el-select
          v-model="queryParams.itemType"
          placeholder="请选择物料类型"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.WMS_ITEM_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select
          v-model="queryParams.status"
          placeholder="请选择状态"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.COMMON_STATUS)"
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
          v-hasPermi="['wms:item:create']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> 新增
        </el-button>
        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['wms:item:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> 导出
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column label="物料ID" align="center" prop="id" />
      <el-table-column label="物料编码" align="center" prop="itemCode" />
      <el-table-column label="物料名称" align="center" prop="itemName" />
      <el-table-column label="物料分类" align="center">
        <template #default="scope">
          {{ categoryMap[scope.row.categoryId] || `未知分类(${scope.row.categoryId})` }}
        </template>
      </el-table-column>
      <el-table-column label="物料类型" align="center" prop="itemType">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.WMS_ITEM_TYPE" :value="scope.row.itemType" />
        </template>
      </el-table-column>
      <el-table-column label="单位" align="center" prop="unit" />
      <el-table-column label="规格" align="center" prop="spec" />
      <el-table-column label="参考价格" align="center" prop="price" />
      <el-table-column label="安全库存" align="center" prop="safetyStock" />
      <el-table-column label="状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.COMMON_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
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
            v-hasPermi="['wms:item:update']"
          >
            编辑
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['wms:item:delete']"
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
  <ItemForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import { ItemApi, ItemVO } from '@/api/wms/item'
import { ItemCategoryApi, ItemCategoryVO, ItemCategoryTreeVO } from '@/api/wms/itemcategory'
import ItemForm from './ItemForm.vue'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import DictTag from '@/components/DictTag/src/DictTag.vue'

/** 物料 列表 */
defineOptions({ name: 'Item' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const list = ref<ItemVO[]>([]) // 列表的数据
const categoryOptions = ref<any[]>([]) // 分类下拉选项
const categoryMap = ref<Record<number, string>>({}) // 分类ID到名称的映射
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  itemCode: undefined,
  itemName: undefined,
  categoryId: undefined,
  itemType: undefined,
  unit: undefined,
  spec: undefined,
  price: undefined,
  safetyStock: undefined,
  status: undefined,
  remark: undefined,
  createTime: [],
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    await getCategoryOptions() // 先获取分类选项
    const data = await ItemApi.getItemPage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}

/** 获取分类下拉选项 */
const getCategoryOptions = async () => {
  try {
    // 直接获取树形结构数据
    const data = await ItemCategoryApi.getItemCategoryTree()
    categoryOptions.value = data
    
    // 构建ID到名称的映射
    buildCategoryMap(data)
  } catch (error) {
    console.error('获取分类列表失败', error)
    categoryOptions.value = []
  }
}

/** 构建分类ID到名称的映射 */
const buildCategoryMap = (treeData: ItemCategoryTreeVO[]) => {
  // 递归遍历树形结构，构建映射
  const traverse = (nodes: ItemCategoryTreeVO[]) => {
    nodes.forEach(item => {
      categoryMap.value[item.id] = item.categoryName
      if (item.children && item.children.length > 0) {
        traverse(item.children)
      }
    })
  }
  
  traverse(treeData)
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
    await ItemApi.deleteItem(id)
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
    const data = await ItemApi.exportItem(queryParams)
    download.excel(data, '物料.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>
