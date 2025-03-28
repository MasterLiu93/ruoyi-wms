<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="移动类型" prop="movementType">
        <el-select v-model="formData.movementType" placeholder="请选择移动类型">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="移动单号" prop="movementNo">
        <el-input v-model="formData.movementNo" placeholder="请输入移动单号" />
      </el-form-item>
      <el-form-item label="仓库ID" prop="warehouseId">
        <el-input v-model="formData.warehouseId" placeholder="请输入仓库ID" />
      </el-form-item>
      <el-form-item label="库位ID" prop="locationId">
        <el-input v-model="formData.locationId" placeholder="请输入库位ID" />
      </el-form-item>
      <el-form-item label="物料ID" prop="itemId">
        <el-input v-model="formData.itemId" placeholder="请输入物料ID" />
      </el-form-item>
      <el-form-item label="移动数量" prop="count">
        <el-input v-model="formData.count" placeholder="请输入移动数量" />
      </el-form-item>
      <el-form-item label="移动前数量" prop="beforeCount">
        <el-input v-model="formData.beforeCount" placeholder="请输入移动前数量" />
      </el-form-item>
      <el-form-item label="移动后数量" prop="afterCount">
        <el-input v-model="formData.afterCount" placeholder="请输入移动后数量" />
      </el-form-item>
      <el-form-item label="业务类型" prop="businessType">
        <el-select v-model="formData.businessType" placeholder="请选择业务类型">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="业务单ID" prop="businessId">
        <el-input v-model="formData.businessId" placeholder="请输入业务单ID" />
      </el-form-item>
      <el-form-item label="业务明细ID" prop="businessDetailId">
        <el-input v-model="formData.businessDetailId" placeholder="请输入业务明细ID" />
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
import { InventoryMovementApi, InventoryMovementVO } from '@/api/wms/inventorymovement'

/** 库存移动记录 表单 */
defineOptions({ name: 'InventoryMovementForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  movementType: undefined,
  movementNo: undefined,
  warehouseId: undefined,
  locationId: undefined,
  itemId: undefined,
  count: undefined,
  beforeCount: undefined,
  afterCount: undefined,
  businessType: undefined,
  businessId: undefined,
  businessDetailId: undefined,
  remark: undefined,
})
const formRules = reactive({
  movementType: [{ required: true, message: '移动类型不能为空', trigger: 'change' }],
  movementNo: [{ required: true, message: '移动单号不能为空', trigger: 'blur' }],
  warehouseId: [{ required: true, message: '仓库ID不能为空', trigger: 'blur' }],
  locationId: [{ required: true, message: '库位ID不能为空', trigger: 'blur' }],
  itemId: [{ required: true, message: '物料ID不能为空', trigger: 'blur' }],
  count: [{ required: true, message: '移动数量不能为空', trigger: 'blur' }],
  beforeCount: [{ required: true, message: '移动前数量不能为空', trigger: 'blur' }],
  afterCount: [{ required: true, message: '移动后数量不能为空', trigger: 'blur' }],
  businessType: [{ required: true, message: '业务类型不能为空', trigger: 'change' }],
  businessId: [{ required: true, message: '业务单ID不能为空', trigger: 'blur' }],
  businessDetailId: [{ required: true, message: '业务明细ID不能为空', trigger: 'blur' }],
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
      formData.value = await InventoryMovementApi.getInventoryMovement(id)
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
    const data = formData.value as unknown as InventoryMovementVO
    if (formType.value === 'create') {
      await InventoryMovementApi.createInventoryMovement(data)
      message.success(t('common.createSuccess'))
    } else {
      await InventoryMovementApi.updateInventoryMovement(data)
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
    movementType: undefined,
    movementNo: undefined,
    warehouseId: undefined,
    locationId: undefined,
    itemId: undefined,
    count: undefined,
    beforeCount: undefined,
    afterCount: undefined,
    businessType: undefined,
    businessId: undefined,
    businessDetailId: undefined,
    remark: undefined,
  }
  formRef.value?.resetFields()
}
</script>
