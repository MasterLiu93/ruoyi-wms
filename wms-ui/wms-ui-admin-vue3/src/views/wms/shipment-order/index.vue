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
      <el-form-item label="出库单号" prop="shipmentOrderNo">
        <el-input
          v-model="queryParams.shipmentOrderNo"
          placeholder="请输入出库单号"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="出库类型" prop="shipmentType">
        <el-select
          v-model="queryParams.shipmentType"
          placeholder="请选择出库类型"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.WMS_SHIPMENT_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="单据状态" prop="orderStatus">
        <el-select
          v-model="queryParams.orderStatus"
          placeholder="请选择单据状态"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.WMS_SHIPMENT_ORDER_STATUS)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="出库状态" prop="shipmentStatus">
        <el-select
          v-model="queryParams.shipmentStatus"
          placeholder="请选择出库状态"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.WMS_SHIPMENT_STATUS)"
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
          v-hasPermi="['wms:shipment-order:create']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> 新增
        </el-button>
        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['wms:shipment-order:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> 导出
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column label="出库单ID" align="center" prop="id" />
      <el-table-column label="出库单号" align="center" prop="shipmentOrderNo" />
      <el-table-column label="出库类型" align="center" prop="shipmentType">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.WMS_SHIPMENT_TYPE" :value="scope.row.shipmentType" />
        </template>
      </el-table-column>
      <el-table-column label="单据状态" align="center" prop="orderStatus">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.WMS_SHIPMENT_ORDER_STATUS" :value="scope.row.orderStatus" />
        </template>
      </el-table-column>
      <el-table-column label="出库状态" align="center" prop="shipmentStatus">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.WMS_SHIPMENT_STATUS" :value="scope.row.shipmentStatus" />
        </template>
      </el-table-column>
      <el-table-column
        label="预计出库时间"
        align="center"
        prop="expectTime"
        width="160"
        :formatter="dateFormatter"
      />
      <el-table-column
        label="完成时间"
        align="center"
        prop="completeTime"
        width="160"
        :formatter="dateFormatter"
      />
      <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" />
      <el-table-column label="操作" min-width="240" align="center" fixed="right">
        <template #default="scope">
          <div class="operation-buttons">
            <!-- 查看按钮 - 任何状态都可查看 -->
            <el-button
              link
              type="primary"
              @click="openForm('view', scope.row.id)"
              v-hasPermi="['wms:shipment-order:query']"
              class="operation-btn"
            >
              查看
            </el-button>
            
            <!-- 审核按钮 - 仅待审核状态可审核 -->
            <el-button
              v-if="scope.row.orderStatus === 1"
              link
              type="primary"
              @click="handleApprove(scope.row.id)"
              v-hasPermi="['wms:shipment-order:approve']"
              class="operation-btn"
            >
              审核
            </el-button>
            
            <!-- 编辑按钮 - 仅草稿状态可编辑 -->
            <el-button
              v-if="scope.row.orderStatus === 0"
              link
              type="primary"
              @click="openForm('edit', scope.row.id)"
              v-hasPermi="['wms:shipment-order:update']"
              class="operation-btn"
            >
              编辑
            </el-button>
            
            <!-- 提交按钮 - 仅草稿状态可提交 -->
            <el-button
              v-if="scope.row.orderStatus === 0"
              link
              type="primary"
              @click="handleSubmit(scope.row.id)"
              v-hasPermi="['wms:shipment-order:submit']"
              class="operation-btn"
            >
              提交
            </el-button>
            
            <!-- 批量出库按钮 - 仅审核通过且未完成状态可出库 -->
            <el-button
              v-if="scope.row.orderStatus === 2 && scope.row.shipmentStatus !== 2" 
              link
              type="success"
              @click="handleBatchShipment(scope.row.id)"
              v-hasPermi="['wms:shipment-order:shipment']"
              class="operation-btn"
            >
              批量出库
            </el-button>
            
            <!-- 取消按钮 - 仅草稿或待审核状态可取消 -->
            <el-button
              v-if="scope.row.orderStatus < 2"
              link
              type="warning"
              @click="handleCancel(scope.row.id)"
              v-hasPermi="['wms:shipment-order:update']"
              class="operation-btn"
            >
              取消
            </el-button>
            
            <!-- 删除按钮 - 仅草稿或已取消状态可删除 -->
            <el-button
              v-if="scope.row.orderStatus === 0 || scope.row.orderStatus === 4"
              link
              type="danger"
              @click="handleDelete(scope.row.id)"
              v-hasPermi="['wms:shipment-order:delete']"
              class="operation-btn"
            >
              删除
            </el-button>
          </div>
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
  <ShipmentOrderForm ref="formRef" @success="getList" />
  
  <!-- 审核弹窗 -->
  <ShipmentOrderApproveForm ref="approveFormRef" @success="getList" />
</template>

<script setup lang="ts">
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import { ShipmentOrderApi, ShipmentOrderVO } from '@/api/wms/shipmentorder'
import ShipmentOrderForm from './ShipmentOrderForm.vue'
import ShipmentOrderApproveForm from './ShipmentOrderApproveForm.vue'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { DictTag } from '@/components/DictTag'
import { ElMessageBox, ElMessage } from 'element-plus'

/** 出库单 列表 */
defineOptions({ name: 'ShipmentOrder' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const list = ref<ShipmentOrderVO[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  shipmentOrderNo: undefined,
  shipmentType: undefined,
  orderStatus: undefined,
  shipmentStatus: undefined,
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await ShipmentOrderApi.getShipmentOrderPage(queryParams)
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
    await ShipmentOrderApi.deleteShipmentOrder(id)
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
    const data = await ShipmentOrderApi.exportShipmentOrder(queryParams)
    download.excel(data, '出库单.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

/** 提交出库单 */
const handleSubmit = async (id: number) => {
  try {
    await ElMessageBox.confirm(
      '确认要提交该出库单吗？',
      '操作确认',
      {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await ShipmentOrderApi.submitShipmentOrder(id)
    ElMessage.success('出库单提交成功')
    await getList()
  } catch (error) {
    console.error(error)
  }
}

/** 审批出库单 */
const approveFormRef = ref()
const handleApprove = (id: number) => {
  approveFormRef.value.open(id)
}

/** 批量出库操作 */
const handleBatchShipment = async (id: number) => {
  try {
    await ElMessageBox.confirm(
      '确认要执行批量出库操作吗？',
      '操作确认',
      {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await ShipmentOrderApi.batchExecuteShipment(id)
    ElMessage.success('批量出库操作成功')
    await getList()
  } catch (error) {
    console.error(error)
  }
}

/** 取消出库单 */
const handleCancel = async (id: number) => {
  try {
    // 使用输入框提示用户输入取消原因
    const { value: remark, action } = await ElMessageBox.prompt(
      '请输入取消原因',
      '取消出库单',
      {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
        inputValidator: (value) => {
          if (value && value.length > 200) {
            return '取消原因不能超过200个字符'
          }
          return true
        }
      }
    )
    
    // 当用户点击确认时，执行取消操作
    if (action === 'confirm') {
      await ShipmentOrderApi.cancelShipmentOrder(id, remark)
      ElMessage.success('出库单已取消')
      await getList()
    }
  } catch (error) {
    if (error !== 'cancel') { // 不显示用户取消操作的错误信息
      console.error(error)
      ElMessage.error('取消出库单失败')
    }
  }
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>

<style scoped>
.operation-buttons {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
}

.operation-btn {
  margin: 3px;
}
</style>
