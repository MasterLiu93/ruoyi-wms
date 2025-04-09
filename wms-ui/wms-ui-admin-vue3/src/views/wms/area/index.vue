<template>
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <ContentWrap>
      <el-form
        class="-mb-15px"
        :model="queryParams"
        ref="queryFormRef"
        :inline="true"
        label-width="68px"
      >
        <el-form-item label="货区编码" prop="areaCode">
          <el-input
            v-model="queryParams.areaCode"
            placeholder="请输入货区编码"
            clearable
            @keyup.enter="handleQuery"
            class="!w-240px"
          />
        </el-form-item>
        <el-form-item label="货区名称" prop="areaName">
          <el-input
            v-model="queryParams.areaName"
            placeholder="请输入货区名称"
            clearable
            @keyup.enter="handleQuery"
            class="!w-240px"
          />
        </el-form-item>
        <el-form-item label="所属仓库" prop="warehouseId">
          <el-select
            v-model="queryParams.warehouseId"
            placeholder="请选择所属仓库"
            clearable
            class="!w-240px"
          >
            <el-option
              v-for="item in warehouseOptions"
              :key="item.id"
              :label="item.warehouseName"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="货区类型" prop="areaType">
          <el-select
            v-model="queryParams.areaType"
            placeholder="请选择货区类型"
            clearable
            class="!w-240px"
          >
            <el-option
              v-for="dict in getIntDictOptions(DICT_TYPE.WMS_AREA_TYPE)"
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
            v-hasPermi="['wms:area:create']"
          >
            <Icon icon="ep:plus" class="mr-5px" /> 新增
          </el-button>
          <el-button
            type="success"
            plain
            @click="handleExport"
            :loading="exportLoading"
            v-hasPermi="['wms:area:export']"
          >
            <Icon icon="ep:download" class="mr-5px" /> 导出
          </el-button>
        </el-form-item>
      </el-form>
    </ContentWrap>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column label="货区ID" align="center" prop="id" />
      <el-table-column label="货区编码" align="center" prop="areaCode" />
      <el-table-column label="货区名称" align="center" prop="areaName" />
      <el-table-column label="所属仓库" align="center" prop="warehouseName" />
      <el-table-column label="货区类型" align="center" prop="areaType">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.WMS_AREA_TYPE" :value="scope.row.areaType" />
        </template>
      </el-table-column>
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
            v-hasPermi="['wms:area:update']"
          >
            编辑
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['wms:area:delete']"
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
  <AreaForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import { AreaApi, AreaVO } from '@/api/wms/area'
import { WarehouseApi } from '@/api/wms/warehouse'
import AreaForm from './AreaForm.vue'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import DictTag from '@/components/DictTag/src/DictTag.vue'
import Search from '@/components/Search/src/Search.vue'

/** 货区 列表 */
defineOptions({ name: 'Area' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const list = ref<AreaVO[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const warehouseOptions = ref<any[]>([]) // 仓库下拉选项
const warehouseMap = ref<Record<number, string>>({}) // 仓库ID到名称的映射
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  areaCode: undefined,
  areaName: undefined,
  warehouseId: undefined,
  areaType: undefined,
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
    // 获取仓库列表，用于显示仓库名称
    if (warehouseOptions.value.length === 0) {
      await getWarehouseOptions()
    }
    
    const data = await AreaApi.getAreaPage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}

/** 获取仓库选项 */
const getWarehouseOptions = async () => {
  try {
    const data = await WarehouseApi.getSimpleWarehouseList()
    warehouseOptions.value = data
    
    // 构建仓库ID到名称的映射
    warehouseMap.value = {}
    data.forEach(item => {
      warehouseMap.value[item.id] = item.warehouseName
    })
  } catch (error) {
    console.error('获取仓库列表失败', error)
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
    await AreaApi.deleteArea(id)
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
    const data = await AreaApi.exportArea(queryParams)
    download.excel(data, '货区.xls')
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
