<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="盘点单ID" prop="checkId">
        <el-input v-model="formData.checkId" placeholder="请输入盘点单ID" />
      </el-form-item>
      <el-form-item label="物料ID" prop="itemId">
        <el-input v-model="formData.itemId" placeholder="请输入物料ID" />
      </el-form-item>
      <el-form-item label="库位ID" prop="locationId">
        <el-input v-model="formData.locationId" placeholder="请输入库位ID" />
      </el-form-item>
      <el-form-item label="账面数量" prop="bookCount">
        <el-input v-model="formData.bookCount" placeholder="请输入账面数量" />
      </el-form-item>
      <el-form-item label="盘点数量" prop="checkCount">
        <el-input v-model="formData.checkCount" placeholder="请输入盘点数量" />
      </el-form-item>
      <el-form-item label="差异数量" prop="differenceCount">
        <el-input v-model="formData.differenceCount" placeholder="请输入差异数量" />
      </el-form-item>
      <el-form-item label="盘点状态(0-未盘点 1-已盘点)" prop="checkStatus">
        <el-radio-group v-model="formData.checkStatus">
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
import { InventoryCheckDetailApi, InventoryCheckDetailVO } from '@/api/wms/inventorycheckdetail'

/** 库存盘点明细 表单 */
defineOptions({ name: 'InventoryCheckDetailForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  checkId: undefined,
  itemId: undefined,
  locationId: undefined,
  bookCount: undefined,
  checkCount: undefined,
  differenceCount: undefined,
  checkStatus: undefined,
  remark: undefined,
})
const formRules = reactive({
  checkId: [{ required: true, message: '盘点单ID不能为空', trigger: 'blur' }],
  itemId: [{ required: true, message: '物料ID不能为空', trigger: 'blur' }],
  locationId: [{ required: true, message: '库位ID不能为空', trigger: 'blur' }],
  bookCount: [{ required: true, message: '账面数量不能为空', trigger: 'blur' }],
  checkStatus: [{ required: true, message: '盘点状态(0-未盘点 1-已盘点)不能为空', trigger: 'blur' }],
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
      formData.value = await InventoryCheckDetailApi.getInventoryCheckDetail(id)
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
    const data = formData.value as unknown as InventoryCheckDetailVO
    if (formType.value === 'create') {
      await InventoryCheckDetailApi.createInventoryCheckDetail(data)
      message.success(t('common.createSuccess'))
    } else {
      await InventoryCheckDetailApi.updateInventoryCheckDetail(data)
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
    checkId: undefined,
    itemId: undefined,
    locationId: undefined,
    bookCount: undefined,
    checkCount: undefined,
    differenceCount: undefined,
    checkStatus: undefined,
    remark: undefined,
  }
  formRef.value?.resetFields()
}
</script>