<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="盘点单号" prop="checkNo">
        <el-input v-model="formData.checkNo" placeholder="请输入盘点单号" />
      </el-form-item>
      <el-form-item label="仓库ID" prop="warehouseId">
        <el-input v-model="formData.warehouseId" placeholder="请输入仓库ID" />
      </el-form-item>
      <el-form-item label="盘点类型" prop="checkType">
        <el-select v-model="formData.checkType" placeholder="请选择盘点类型(0-全部盘点 1-部分盘点)">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="盘点状态" prop="checkStatus">
        <el-radio-group v-model="formData.checkStatus">
          <el-radio value="1">请选择字典生成</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="盘点总数" prop="totalCount">
        <el-input v-model="formData.totalCount" placeholder="请输入盘点总数" />
      </el-form-item>
      <el-form-item label="已盘点数" prop="checkedCount">
        <el-input v-model="formData.checkedCount" placeholder="请输入已盘点数" />
      </el-form-item>
      <el-form-item label="差异数" prop="differenceCount">
        <el-input v-model="formData.differenceCount" placeholder="请输入差异数" />
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
import { InventoryCheckApi, InventoryCheckVO } from '@/api/wms/inventorycheck'

/** 库存盘点单 表单 */
defineOptions({ name: 'InventoryCheckForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  checkNo: undefined,
  warehouseId: undefined,
  checkType: undefined,
  checkStatus: undefined,
  totalCount: undefined,
  checkedCount: undefined,
  differenceCount: undefined,
  remark: undefined,
})
const formRules = reactive({
  checkNo: [{ required: true, message: '盘点单号不能为空', trigger: 'blur' }],
  warehouseId: [{ required: true, message: '仓库ID不能为空', trigger: 'blur' }],
  checkType: [{ required: true, message: '盘点类型不能为空', trigger: 'change' }],
  checkStatus: [{ required: true, message: '盘点状态不能为空', trigger: 'blur' }],
  totalCount: [{ required: true, message: '盘点总数不能为空', trigger: 'blur' }],
  checkedCount: [{ required: true, message: '已盘点数不能为空', trigger: 'blur' }],
  differenceCount: [{ required: true, message: '差异数不能为空', trigger: 'blur' }],
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
      formData.value = await InventoryCheckApi.getInventoryCheck(id)
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
    const data = formData.value as unknown as InventoryCheckVO
    if (formType.value === 'create') {
      await InventoryCheckApi.createInventoryCheck(data)
      message.success(t('common.createSuccess'))
    } else {
      await InventoryCheckApi.updateInventoryCheck(data)
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
    checkNo: undefined,
    warehouseId: undefined,
    checkType: undefined,
    checkStatus: undefined,
    totalCount: undefined,
    checkedCount: undefined,
    differenceCount: undefined,
    remark: undefined,
  }
  formRef.value?.resetFields()
}
</script>
