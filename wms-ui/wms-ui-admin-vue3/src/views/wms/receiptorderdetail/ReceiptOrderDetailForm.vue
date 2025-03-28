<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="入库单ID" prop="receiptOrderId">
        <el-input v-model="formData.receiptOrderId" placeholder="请输入入库单ID" />
      </el-form-item>
      <el-form-item label="物料ID" prop="itemId">
        <el-input v-model="formData.itemId" placeholder="请输入物料ID" />
      </el-form-item>
      <el-form-item label="计划数量" prop="planCount">
        <el-input v-model="formData.planCount" placeholder="请输入计划数量" />
      </el-form-item>
      <el-form-item label="实际入库数量" prop="realCount">
        <el-input v-model="formData.realCount" placeholder="请输入实际入库数量" />
      </el-form-item>
      <el-form-item label="入库库位ID" prop="locationId">
        <el-input v-model="formData.locationId" placeholder="请输入入库库位ID" />
      </el-form-item>
      <el-form-item label="批次ID" prop="batchId">
        <el-input v-model="formData.batchId" placeholder="请输入批次ID" />
      </el-form-item>
      <el-form-item label="入库单价" prop="price">
        <el-input v-model="formData.price" placeholder="请输入入库单价" />
      </el-form-item>
      <el-form-item label="质检状态(0-未检验 1-已检验)" prop="qualityStatus">
        <el-radio-group v-model="formData.qualityStatus">
          <el-radio value="1">请选择字典生成</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="状态(0-未入库 1-部分入库 2-已入库)" prop="status">
        <el-radio-group v-model="formData.status">
          <el-radio value="1">请选择字典生成</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input v-model="formData.remark" placeholder="请输入备注" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import { ReceiptOrderDetailApi, ReceiptOrderDetailVO } from '@/api/wms/receiptorderdetail'

/** 入库单明细 表单 */
defineOptions({ name: 'ReceiptOrderDetailForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  receiptOrderId: undefined,
  itemId: undefined,
  planCount: undefined,
  realCount: undefined,
  locationId: undefined,
  batchId: undefined,
  price: undefined,
  qualityStatus: undefined,
  status: undefined,
  remark: undefined,
})
const formRules = reactive({
  receiptOrderId: [{ required: true, message: '入库单ID不能为空', trigger: 'blur' }],
  itemId: [{ required: true, message: '物料ID不能为空', trigger: 'blur' }],
  planCount: [{ required: true, message: '计划数量不能为空', trigger: 'blur' }],
  realCount: [{ required: true, message: '实际入库数量不能为空', trigger: 'blur' }],
  price: [{ required: true, message: '入库单价不能为空', trigger: 'blur' }],
  qualityStatus: [{ required: true, message: '质检状态(0-未检验 1-已检验)不能为空', trigger: 'blur' }],
  status: [{ required: true, message: '状态(0-未入库 1-部分入库 2-已入库)不能为空', trigger: 'blur' }],
})
const formRef = ref() // 表单 Ref

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      formData.value = await ReceiptOrderDetailApi.getReceiptOrderDetail(id)
    } finally {
      formLoading.value = false
    }
  }
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 提交表单 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
const submitForm = async () => {
  // 校验表单
  await formRef.value.validate()
  // 提交请求
  formLoading.value = true
  try {
    const data = formData.value as unknown as ReceiptOrderDetailVO
    if (formType.value === 'create') {
      await ReceiptOrderDetailApi.createReceiptOrderDetail(data)
      message.success(t('common.createSuccess'))
    } else {
      await ReceiptOrderDetailApi.updateReceiptOrderDetail(data)
      message.success(t('common.updateSuccess'))
    }
    dialogVisible.value = false
    // 发送操作成功的事件
    emit('success')
  } finally {
    formLoading.value = false
  }
}

/** 重置表单 */
const resetForm = () => {
  formData.value = {
    id: undefined,
    receiptOrderId: undefined,
    itemId: undefined,
    planCount: undefined,
    realCount: undefined,
    locationId: undefined,
    batchId: undefined,
    price: undefined,
    qualityStatus: undefined,
    status: undefined,
    remark: undefined,
  }
  formRef.value?.resetFields()
}
</script>