<template>
  <Dialog :title="title" v-model="dialogVisible" width="500px">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="审核结果" prop="approved">
        <el-radio-group v-model="formData.approved">
          <el-radio :label="true">审核通过</el-radio>
          <el-radio :label="false">审核拒绝</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="审核备注" prop="remark">
        <el-input v-model="formData.remark" type="textarea" placeholder="请输入审核备注" :rows="4" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>

<script setup lang="ts">
import { ReceiptOrderApi } from '@/api/wms/receiptorder'
import { ElLoading } from 'element-plus'

/** 审核表单 */
defineOptions({ name: 'ReceiptOrderApproveForm' })

const message = useMessage() // 消息弹窗

// 表单校验规则
const formRules = reactive({
  approved: [{ required: true, message: '请选择审核结果', trigger: 'change' }],
  remark: [{ required: true, message: '请输入审核备注', trigger: 'blur' }]
})

// 弹窗显示
const dialogVisible = ref(false)
const title = ref('入库单审核')
const formLoading = ref(false)

// 表单数据
const formData = ref({
  id: undefined as number | undefined,
  approved: true,
  remark: ''
})

// 表单ref
const formRef = ref()

// 打开表单
const open = async (id: number, defaultApproved?: boolean) => {
  dialogVisible.value = true
  // 重置表单
  formData.value = {
    id: id,
    approved: defaultApproved !== undefined ? defaultApproved : true,
    remark: ''
  }
}

// 提交表单
const emit = defineEmits(['success']) // 定义事件
const submitForm = async () => {
  // 校验表单
  await formRef.value.validate()
  
  formLoading.value = true
  try {
    // 提交审核
    const res = await ReceiptOrderApi.approveReceiptOrder({
      id: formData.value.id!,
      approved: formData.value.approved,
      remark: formData.value.remark
    })
    
    // 提示成功并关闭弹窗
    const action = formData.value.approved ? '审核通过' : '审核拒绝'
    message.success(`入库单${action}成功`)
    
    // 如果审核通过，自动执行批量入库操作
    if (formData.value.approved) {
      try {
        // 使用 ElLoading 替代 message.loading
        const loadingInstance = ElLoading.service({
          lock: true,
          text: '正在执行批量入库操作...',
          background: 'rgba(0, 0, 0, 0.7)'
        })
        
        await ReceiptOrderApi.batchExecuteReceipt(formData.value.id!)
        
        // 关闭loading
        loadingInstance.close()
        
        message.success('批量入库操作已执行成功')
      } catch (error: any) {
        console.error('批量入库操作失败:', error)
        message.error(error.message || '批量入库操作失败，请手动执行入库操作')
      }
    } else {
      message.info('入库单状态已变更为「审核拒绝」，不可进行入库操作')
    }
    
    dialogVisible.value = false
    
    // 触发成功事件，刷新列表
    emit('success')
  } catch (error: any) {
    console.error('审核失败:', error)
    // 处理特定错误码
    if (error?.code === 1002005007) {
      message.error('审核失败：入库单没有明细，无法审核通过')
    } else {
      message.error(error.message || '审核失败，请联系管理员')
    }
  } finally {
    formLoading.value = false
  }
}

// 暴露open方法给父组件调用
defineExpose({
  open
})
</script> 