<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="出库单ID" prop="shipmentOrderId">
        <el-input v-model="formData.shipmentOrderId" placeholder="请输入出库单ID" />
      </el-form-item>
      <el-form-item label="出库单明细ID" prop="shipmentOrderDetailId">
        <el-input v-model="formData.shipmentOrderDetailId" placeholder="请输入出库单明细ID" />
      </el-form-item>
      <el-form-item label="物料ID" prop="itemId">
        <el-input v-model="formData.itemId" placeholder="请输入物料ID" />
      </el-form-item>
      <el-form-item label="出库库位ID" prop="locationId">
        <el-input v-model="formData.locationId" placeholder="请输入出库库位ID" />
      </el-form-item>
      <el-form-item label="批次ID" prop="batchId">
        <el-input v-model="formData.batchId" placeholder="请输入批次ID" />
      </el-form-item>
      <el-form-item label="出库数量" prop="count">
        <el-input v-model="formData.count" placeholder="请输入出库数量" />
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
import { ShipmentRecordApi, ShipmentRecordVO } from '@/api/wms/shipmentrecord'

/** 出库操作记录 表单 */
defineOptions({ name: 'ShipmentRecordForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  shipmentOrderId: undefined,
  shipmentOrderDetailId: undefined,
  itemId: undefined,
  locationId: undefined,
  batchId: undefined,
  count: undefined,
  remark: undefined,
})
const formRules = reactive({
  shipmentOrderId: [{ required: true, message: '出库单ID不能为空', trigger: 'blur' }],
  shipmentOrderDetailId: [{ required: true, message: '出库单明细ID不能为空', trigger: 'blur' }],
  itemId: [{ required: true, message: '物料ID不能为空', trigger: 'blur' }],
  locationId: [{ required: true, message: '出库库位ID不能为空', trigger: 'blur' }],
  count: [{ required: true, message: '出库数量不能为空', trigger: 'blur' }],
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
      formData.value = await ShipmentRecordApi.getShipmentRecord(id)
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
    const data = formData.value as unknown as ShipmentRecordVO
    if (formType.value === 'create') {
      await ShipmentRecordApi.createShipmentRecord(data)
      message.success(t('common.createSuccess'))
    } else {
      await ShipmentRecordApi.updateShipmentRecord(data)
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
    shipmentOrderId: undefined,
    shipmentOrderDetailId: undefined,
    itemId: undefined,
    locationId: undefined,
    batchId: undefined,
    count: undefined,
    remark: undefined,
  }
  formRef.value?.resetFields()
}
</script>