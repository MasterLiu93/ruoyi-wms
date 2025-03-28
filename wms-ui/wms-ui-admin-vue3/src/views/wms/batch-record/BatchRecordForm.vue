<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="批次ID" prop="batchId">
        <el-input v-model="formData.batchId" placeholder="请输入批次ID" />
      </el-form-item>
      <el-form-item label="移动类型" prop="movementType">
        <el-select v-model="formData.movementType" placeholder="请选择移动类型(0-入库 1-出库 2-库存调整)">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="移动记录ID" prop="movementId">
        <el-input v-model="formData.movementId" placeholder="请输入移动记录ID" />
      </el-form-item>
      <el-form-item label="操作数量" prop="count">
        <el-input v-model="formData.count" placeholder="请输入操作数量" />
      </el-form-item>
      <el-form-item label="操作前数量" prop="beforeCount">
        <el-input v-model="formData.beforeCount" placeholder="请输入操作前数量" />
      </el-form-item>
      <el-form-item label="操作后数量" prop="afterCount">
        <el-input v-model="formData.afterCount" placeholder="请输入操作后数量" />
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
import { BatchRecordApi, BatchRecordVO } from '@/api/wms/batchrecord'

/** 批次操作记录 表单 */
defineOptions({ name: 'BatchRecordForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  batchId: undefined,
  movementType: undefined,
  movementId: undefined,
  count: undefined,
  beforeCount: undefined,
  afterCount: undefined,
  remark: undefined,
})
const formRules = reactive({
  batchId: [{ required: true, message: '批次ID不能为空', trigger: 'blur' }],
  movementType: [{ required: true, message: '移动类型不能为空', trigger: 'change' }],
  movementId: [{ required: true, message: '移动记录ID不能为空', trigger: 'blur' }],
  count: [{ required: true, message: '操作数量不能为空', trigger: 'blur' }],
  beforeCount: [{ required: true, message: '操作前数量不能为空', trigger: 'blur' }],
  afterCount: [{ required: true, message: '操作后数量不能为空', trigger: 'blur' }],
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
      formData.value = await BatchRecordApi.getBatchRecord(id)
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
    const data = formData.value as unknown as BatchRecordVO
    if (formType.value === 'create') {
      await BatchRecordApi.createBatchRecord(data)
      message.success(t('common.createSuccess'))
    } else {
      await BatchRecordApi.updateBatchRecord(data)
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
    batchId: undefined,
    movementType: undefined,
    movementId: undefined,
    count: undefined,
    beforeCount: undefined,
    afterCount: undefined,
    remark: undefined,
  }
  formRef.value?.resetFields()
}
</script>
