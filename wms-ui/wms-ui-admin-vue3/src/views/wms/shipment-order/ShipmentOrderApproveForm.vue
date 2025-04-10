<template>
  <Dialog :title="'审核出库单'" v-model="dialogVisible" width="500px">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="审核结果" prop="approveResult">
        <el-radio-group v-model="formData.approveResult">
          <el-radio :label="true">通过</el-radio>
          <el-radio :label="false">驳回</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="审核意见" prop="remark">
        <el-input
          v-model="formData.remark"
          type="textarea"
          placeholder="请输入审核意见"
          :rows="4"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>

<script setup lang="ts">
import { ShipmentOrderApi } from '@/api/wms/shipmentorder'

/** 出库单审核表单 */
defineOptions({ name: 'ShipmentOrderApproveForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const formLoading = ref(false) // 表单的加载中
const shipmentOrderId = ref<number | null>(null) // 出库单ID

const formData = ref({
  approveResult: true, // 默认通过
  remark: '',
})

const formRules = reactive({
  approveResult: [{ required: true, message: '审核结果不能为空', trigger: 'blur' }],
})

const formRef = ref() // 表单 Ref

/** 打开弹窗 */
const open = async (id: number) => {
  dialogVisible.value = true
  resetForm()
  shipmentOrderId.value = id
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 提交表单 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
const submitForm = async () => {
  // 校验表单
  await formRef.value.validate()
  
  if (!shipmentOrderId.value) {
    message.error('出库单ID不能为空')
    return
  }
  
  // 提交请求
  formLoading.value = true
  try {
    await ShipmentOrderApi.approveShipmentOrder(
      shipmentOrderId.value,
      formData.value.approveResult,
      formData.value.remark
    )
    
    message.success(formData.value.approveResult ? '出库单审核通过' : '出库单已驳回')
    dialogVisible.value = false
    // 发送操作成功的事件
    emit('success')
  } catch (error) {
    console.error('审核出库单出错', error)
  } finally {
    formLoading.value = false
  }
}

/** 重置表单 */
const resetForm = () => {
  formData.value = {
    approveResult: true,
    remark: '',
  }
  shipmentOrderId.value = null
  formRef.value?.resetFields()
}
</script> 