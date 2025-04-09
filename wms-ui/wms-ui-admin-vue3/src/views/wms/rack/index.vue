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
      <el-form-item label="货架编码" prop="rackCode">
        <el-input
          v-model="queryParams.rackCode"
          placeholder="请输入货架编码"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="货架名称" prop="rackName">
        <el-input
          v-model="queryParams.rackName"
          placeholder="请输入货架名称"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="所属仓库" prop="selectedWarehouseId">
        <el-select
          v-model="selectedWarehouseId"
          placeholder="请选择所属仓库"
          clearable
          @change="handleWarehouseChange"
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
      <el-form-item label="所属货区" prop="areaId">
        <el-select
          v-model="queryParams.areaId"
          placeholder="请选择所属货区"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="item in areaOptions"
            :key="item.id"
            :label="item.areaName"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="货架类型" prop="rackType">
        <el-select
          v-model="queryParams.rackType"
          placeholder="请选择货架类型"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.WMS_RACK_TYPE)"
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
          v-hasPermi="['wms:rack:create']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> 新增
        </el-button>
        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['wms:rack:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> 导出
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column label="货架ID" align="center" prop="id" />
      <el-table-column label="货架编码" align="center" prop="rackCode" />
      <el-table-column label="货架名称" align="center" prop="rackName" />
      <el-table-column label="所属货区" align="center" prop="areaId">
        <template #default="scope">
          {{ areaMap[scope.row.areaId] || '未知货区' }}
        </template>
      </el-table-column>
      <el-table-column label="货架类型" align="center" prop="rackType">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.WMS_RACK_TYPE" :value="scope.row.rackType" />
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
            v-hasPermi="['wms:rack:update']"
          >
            编辑
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['wms:rack:delete']"
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
  <RackForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import { RackApi, RackVO } from '@/api/wms/rack'
import { WarehouseApi } from '@/api/wms/warehouse'
import { AreaApi } from '@/api/wms/area'
import RackForm from './RackForm.vue'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import DictTag from '@/components/DictTag/src/DictTag.vue'

/** 货架 列表 */
defineOptions({ name: 'Rack' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const list = ref<RackVO[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const warehouseOptions = ref<any[]>([]) // 仓库下拉选项
const areaOptions = ref<any[]>([]) // 货区下拉选项
const warehouseMap = ref<Record<number, string>>({}) // 仓库ID到名称的映射
const areaMap = ref<Record<number, string>>({}) // 货区ID到名称的映射
const selectedWarehouseId = ref<number | undefined>() // 当前选择的仓库ID
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  rackCode: undefined,
  rackName: undefined,
  areaId: undefined,
  rackType: undefined,
  status: undefined,
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    // 获取仓库和货区列表，用于显示名称
    if (warehouseOptions.value.length === 0) {
      await getWarehouseOptions()
    }
    
    const data = await RackApi.getRackPage(queryParams)
    list.value = data.list
    total.value = data.total
    
    // 获取货区详情信息，用于获取仓库ID
    await getAreaDetails()
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

/** 获取货区选项 */
const getAreaOptions = async (warehouseId?: number) => {
  try {
    const data = await AreaApi.getSimpleAreaList(warehouseId)
    areaOptions.value = data
    
    // 构建货区ID到名称的映射
    data.forEach(item => {
      areaMap.value[item.id] = item.areaName
    })
  } catch (error) {
    console.error('获取货区列表失败', error)
  }
}

/** 获取货区详细信息，用于构建仓库和货区的关联 */
const getAreaDetails = async () => {
  try {
    // 收集所有需要查询的货区ID
    const areaIds = new Set(list.value.map(item => item.areaId))
    
    // 通过API获取这些货区的详细信息
    const areaDetails = await Promise.all([...areaIds].map(id => AreaApi.getArea(id)))
    
    // 将货区信息添加到映射中
    areaDetails.forEach(area => {
      if (area) {
        areaMap.value[area.id] = area.areaName
      }
    })
  } catch (error) {
    console.error('获取货区详情失败', error)
  }
}

/** 处理仓库变更 */
const handleWarehouseChange = async () => {
  queryParams.areaId = undefined // 清空货区选择
  await getAreaOptions(selectedWarehouseId.value)
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
    await RackApi.deleteRack(id)
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
    const data = await RackApi.exportRack(queryParams)
    download.excel(data, '货架.xls')
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
