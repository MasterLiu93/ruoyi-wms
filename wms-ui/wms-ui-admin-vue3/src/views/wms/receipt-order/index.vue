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
      <el-form-item label="入库单号" prop="receiptOrderNo">
        <el-input
          v-model="queryParams.receiptOrderNo"
          placeholder="请输入入库单号"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="入库类型" prop="receiptType">
        <el-select v-model="queryParams.receiptType" placeholder="请选择入库类型" clearable class="!w-240px">
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.WMS_RECEIPT_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="单据状态" prop="orderStatus">
        <el-select v-model="queryParams.orderStatus" placeholder="请选择单据状态" clearable class="!w-240px">
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.WMS_RECEIPT_ORDER_STATUS)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="入库状态" prop="receiptStatus">
        <el-select v-model="queryParams.receiptStatus" placeholder="请选择入库状态" clearable class="!w-240px">
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.WMS_RECEIPT_STATUS)"
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
          v-hasPermi="['wms:receipt-order:create']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> 新增
        </el-button>
        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['wms:receipt-order:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> 导出
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column label="入库单ID" align="center" prop="id" />
      <el-table-column label="入库单号" align="center" prop="receiptOrderNo" />
      <el-table-column label="入库类型" align="center" prop="receiptType">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.WMS_RECEIPT_TYPE" :value="scope.row.receiptType" />
        </template>
      </el-table-column>
      <el-table-column label="单据状态" align="center" prop="orderStatus">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.WMS_RECEIPT_ORDER_STATUS" :value="scope.row.orderStatus" />
        </template>
      </el-table-column>
      <el-table-column label="入库状态" align="center" prop="receiptStatus">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.WMS_RECEIPT_STATUS" :value="scope.row.receiptStatus" />
        </template>
      </el-table-column>
      <el-table-column 
        label="到货时间" 
        align="center" 
        prop="arrivalTime" 
        width="160" 
        :formatter="dateFormatter" 
      />
      
      <!-- 添加完成时间列 -->
      <el-table-column 
        label="完成时间" 
        align="center" 
        prop="completionTime" 
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
              v-hasPermi="['wms:receipt-order:query']"
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
              v-hasPermi="['wms:receipt-order:approve']"
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
              v-hasPermi="['wms:receipt-order:update']"
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
              v-hasPermi="['wms:receipt-order:submit']"
              class="operation-btn"
            >
              提交
            </el-button>
            
            <!-- 执行入库按钮 - 仅审核通过且未完成状态可入库 -->
            <el-button
              v-if="scope.row.orderStatus === 2 && scope.row.receiptStatus !== 2" 
              link
              type="success"
              @click="handleBatchReceipt(scope.row.id)"
              v-hasPermi="['wms:receipt-order:receipt']"
              class="operation-btn"
            >
              入库
            </el-button>
            
            <!-- 取消按钮 - 仅草稿或待审核状态可取消 -->
            <el-button
              v-if="scope.row.orderStatus < 2"
              link
              type="warning"
              @click="handleCancel(scope.row.id)"
              v-hasPermi="['wms:receipt-order:update']"
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
              v-hasPermi="['wms:receipt-order:delete']"
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
  <ReceiptOrderForm ref="formRef" @success="getList" />
  
  <!-- 审核弹窗 -->
  <ReceiptOrderApproveForm ref="approveFormRef" @success="getList" />

  <!-- 入库明细录入表单 -->
  <ReceiptDetailForm ref="detailFormRef" @success="getList" />

</template>

<script setup lang="ts">
import download from '@/utils/download'
import { ReceiptOrderApi, ReceiptOrderVO } from '@/api/wms/receiptorder'
import ReceiptOrderForm from './ReceiptOrderForm.vue'
import ReceiptOrderApproveForm from './ReceiptOrderApproveForm.vue'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import DictTag from '@/components/DictTag/src/DictTag.vue'
import { ElLoading, ElMessageBox } from 'element-plus'
import { formatDate } from '@/utils/formatTime' // 导入日期格式化函数

/** 入库单 列表 */
defineOptions({ name: 'ReceiptOrder' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const list = ref<ReceiptOrderVO[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  receiptOrderNo: undefined,
  receiptType: undefined,
  supplierId: undefined,
  // supplierName: undefined, // 可能导致问题的字段，暂时注释掉
  orderStatus: undefined,
  receiptStatus: undefined,
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await ReceiptOrderApi.getReceiptOrderPage(queryParams)
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
const approveFormRef = ref()
const detailFormRef = ref()
const openForm = (type: string, id?: number) => {
  const correctedType = type === 'update' ? 'edit' : type
  formRef.value.open(correctedType, id)
}

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    // 删除的二次确认
    await ElMessageBox.confirm(
      '确认永久删除该入库单吗？此操作将从系统中移除所有相关记录且不可恢复！',
      '删除确认',
      {
        type: 'warning',
        confirmButtonText: '确认删除',
        cancelButtonText: '取消'
      }
    )
    // 发起删除
    await ReceiptOrderApi.deleteReceiptOrder(id)
    message.success('入库单已从系统中永久删除')
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
    const data = await ReceiptOrderApi.exportReceiptOrder(queryParams)
    if (data) {
      download.excel(data, '入库单.xls')
    }
  } catch (error) {
    console.error('导出失败:', error)
    message.error('导出失败')
  } finally {
    exportLoading.value = false
  }
}

/** 初始化 **/
onMounted(() => {
  getList()
})

/** 提交审核操作 */
const handleSubmit = async (id: number) => {
  try {
    await message.confirm('确认提交该入库单进行审核吗？提交后将无法修改并等待审核。')
    
    // 显示加载中提示
    const loading = ElLoading.service({
      lock: true,
      text: '提交中...',
      background: 'rgba(0, 0, 0, 0.7)',
    })
    
    try {
      // 调用API提交入库单
      await ReceiptOrderApi.submitReceiptOrder(id)
      
      // 显示成功提示
      message.success('入库单已成功提交审核')
      
      // 刷新列表
      await getList()
    } catch (error: any) {
      // 处理特定错误码
      if (error?.code === 1002005006) {
        message.error('入库单没有明细，请先添加明细后再提交审核')
      } else if (error?.message) {
        message.error(error.message)
      } else {
        message.error('提交失败，请检查入库单状态')
      }
    } finally {
      // 关闭加载提示
      loading.close()
    }
  } catch (error: any) {
    // 用户取消操作，不做处理
  }
}

/** 审核操作 */
const handleApprove = async (id: number, defaultApproved: boolean = true) => {
  try {
    // 打开确认对话框，让用户选择审核通过或拒绝
    await ElMessageBox.confirm(
      '请选择审核操作',
      '审核确认',
      {
        distinguishCancelAndClose: true,
        confirmButtonText: '审核通过',
        cancelButtonText: '审核拒绝',
        type: 'warning'
      }
    ).then(() => {
      // 用户点击"审核通过"
      approveFormRef.value.open(id, true);
    }).catch((action) => {
      if (action === 'cancel') {
        // 用户点击"审核拒绝"
        approveFormRef.value.open(id, false);
      }
      // 如果用户关闭弹窗，不执行任何操作
    });
  } catch (error) {
    // 处理其他错误
    console.error('审核操作异常:', error);
  }
}

/** 执行批量入库操作 */
const handleBatchReceipt = async (id) => {
  try {
    // 确认执行批量入库
    await ElMessageBox.confirm(
      '确认执行此入库单的批量入库操作吗？此操作将按计划数量批量入库所有明细',
      '批量入库确认',
      {
        type: 'warning',
        confirmButtonText: '确认批量入库',
        cancelButtonText: '取消'
      }
    )
    
    // 显示加载中提示
    const loading = ElLoading.service({
      lock: true,
      text: '批量入库中...',
      background: 'rgba(0, 0, 0, 0.7)',
    })
    
    try {
      // 调用批量入库API
      await ReceiptOrderApi.batchExecuteReceipt(id)
      
      // 显示成功消息
      message.success('批量入库操作已成功执行')
      
      // 刷新列表
      await getList()
    } finally {
      // 关闭加载提示
      loading.close()
    }
  } catch (error: any) {
    if (error?.message) {
      message.error(error.message)
    }
    // 取消操作，不做处理
  }
}

/** 取消入库单操作 */
const handleCancel = async (id: number) => {
  try {
    await ElMessageBox.confirm(
      '确认取消该入库单吗？取消后入库单状态将变为"已取消"，不可进行后续操作，但记录仍会保留在系统中。',
      '取消确认',
      {
        type: 'warning',
        confirmButtonText: '确认取消',
        cancelButtonText: '不取消'
      }
    )
    await ReceiptOrderApi.cancelReceiptOrder(id)
    message.success('入库单已取消')
    // 刷新列表
    await getList()
  } catch {}
}

// 日期格式化函数
const dateFormatter = (row, column) => {
  const value = row[column.property]
  if (value) {
    return formatDate(value, 'YYYY-MM-DD HH:mm:ss')
  }
  return ''
}
</script>

<style scoped>
.operation-buttons {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
}

.operation-btn {
  min-width: 50px;
  margin: 2px 4px;
  text-align: center;
}
</style>
