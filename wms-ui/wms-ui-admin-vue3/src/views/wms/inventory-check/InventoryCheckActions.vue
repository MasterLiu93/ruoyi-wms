<template>
  <el-row :gutter="10" class="mb-4">
    <el-col>
      <el-button 
        v-if="checkStatus === 0" 
        type="primary" 
        @click="handleStartCheck"
        :loading="loading"
      >
        开始盘点
      </el-button>
      <el-button 
        v-if="checkStatus === 1" 
        type="warning" 
        @click="handleCancelCheck"
        :loading="loading"
      >
        取消盘点
      </el-button>
      <el-button 
        v-if="checkStatus === 1" 
        type="success" 
        @click="handleCompleteCheck"
        :loading="loading"
      >
        完成盘点
      </el-button>
      <el-button 
        v-if="checkStatus === 2" 
        type="danger" 
        @click="handleAdjustInventory"
        :loading="loading"
      >
        调整库存
      </el-button>
    </el-col>
  </el-row>
  
  <!-- 取消盘点对话框 -->
  <el-dialog v-model="cancelDialogVisible" title="取消盘点" width="500px">
    <el-form ref="cancelFormRef" :model="cancelForm" label-width="80px">
      <el-form-item label="备注">
        <el-input 
          v-model="cancelForm.remark" 
          type="textarea" 
          placeholder="请输入取消原因"
          :rows="3"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="cancelDialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="submitCancelCheck" :loading="loading">确 定</el-button>
    </template>
  </el-dialog>
  
  <!-- 完成盘点对话框 -->
  <el-dialog v-model="completeDialogVisible" title="完成盘点" width="500px">
    <el-form ref="completeFormRef" :model="completeForm" label-width="120px">
      <el-form-item label="自动调整库存差异">
        <el-switch v-model="completeForm.autoAdjust" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="completeDialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="submitCompleteCheck" :loading="loading">确 定</el-button>
    </template>
  </el-dialog>
  
  <!-- 调整库存对话框 -->
  <el-dialog v-model="adjustDialogVisible" title="调整库存差异" width="500px">
    <el-form ref="adjustFormRef" :model="adjustForm" label-width="80px">
      <el-form-item label="备注">
        <el-input 
          v-model="adjustForm.remark" 
          type="textarea" 
          placeholder="请输入调整说明"
          :rows="3"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="adjustDialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="submitAdjustInventory" :loading="loading">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { InventoryCheckApi, InventoryCheckCancelVO, InventoryCheckCompleteVO, InventoryCheckAdjustVO } from '@/api/wms/inventorycheck'

defineOptions({ name: 'InventoryCheckActions' })

const props = defineProps({
  id: {
    type: [Number, String],
    required: true
  },
  checkStatus: {
    type: Number,
    required: true
  }
})

const emit = defineEmits(['success'])
const message = useMessage()
const { t } = useI18n()

const loading = ref(false)

// 取消盘点相关
const cancelDialogVisible = ref(false)
const cancelForm = ref({
  remark: ''
})
const cancelFormRef = ref()

// 完成盘点相关
const completeDialogVisible = ref(false)
const completeForm = ref({
  autoAdjust: true
})
const completeFormRef = ref()

// 调整库存相关
const adjustDialogVisible = ref(false)
const adjustForm = ref({
  remark: ''
})
const adjustFormRef = ref()

// 开始盘点
const handleStartCheck = async () => {
  try {
    await message.confirm('是否确认开始盘点？')
    loading.value = true
    await InventoryCheckApi.startCheck(Number(props.id))
    message.success('开始盘点成功')
    emit('success')
  } catch {
    // 错误处理由全局处理器处理
  } finally {
    loading.value = false
  }
}

// 显示取消盘点对话框
const handleCancelCheck = () => {
  cancelForm.value.remark = ''
  cancelDialogVisible.value = true
}

// 提交取消盘点
const submitCancelCheck = async () => {
  if (!cancelForm.value.remark) {
    message.warning('请输入取消原因')
    return
  }
  
  try {
    loading.value = true
    const data: InventoryCheckCancelVO = {
      id: Number(props.id),
      remark: cancelForm.value.remark
    }
    await InventoryCheckApi.cancelCheck(data)
    message.success('取消盘点成功')
    cancelDialogVisible.value = false
    emit('success')
  } catch {
    // 错误处理由全局处理器处理
  } finally {
    loading.value = false
  }
}

// 显示完成盘点对话框
const handleCompleteCheck = () => {
  completeForm.value.autoAdjust = true
  completeDialogVisible.value = true
}

// 提交完成盘点
const submitCompleteCheck = async () => {
  try {
    loading.value = true
    const data: InventoryCheckCompleteVO = {
      id: Number(props.id),
      autoAdjust: completeForm.value.autoAdjust
    }
    await InventoryCheckApi.completeCheck(data)
    message.success('完成盘点成功')
    completeDialogVisible.value = false
    emit('success')
  } catch {
    // 错误处理由全局处理器处理
  } finally {
    loading.value = false
  }
}

// 显示调整库存对话框
const handleAdjustInventory = () => {
  adjustForm.value.remark = ''
  adjustDialogVisible.value = true
}

// 提交调整库存
const submitAdjustInventory = async () => {
  if (!adjustForm.value.remark) {
    message.warning('请输入调整说明')
    return
  }
  
  try {
    loading.value = true
    const data: InventoryCheckAdjustVO = {
      id: Number(props.id),
      remark: adjustForm.value.remark
    }
    await InventoryCheckApi.adjustInventory(data)
    message.success('调整库存成功')
    adjustDialogVisible.value = false
    emit('success')
  } catch {
    // 错误处理由全局处理器处理
  } finally {
    loading.value = false
  }
}
</script> 