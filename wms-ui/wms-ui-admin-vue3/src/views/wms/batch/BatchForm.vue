<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="批次号" prop="batchNo">
        <el-input v-model="formData.batchNo" placeholder="请输入批次号" />
      </el-form-item>
      <el-form-item label="物料ID" prop="itemId">
        <el-input v-model="formData.itemId" placeholder="请输入物料ID" />
      </el-form-item>
      <el-form-item label="仓库ID" prop="warehouseId">
        <el-input v-model="formData.warehouseId" placeholder="请输入仓库ID" />
      </el-form-item>
      <el-form-item label="供应商ID" prop="supplierId">
        <el-input v-model="formData.supplierId" placeholder="请输入供应商ID" />
      </el-form-item>
      <el-form-item label="生产日期" prop="productionDate">
        <el-date-picker
          v-model="formData.productionDate"
          type="date"
          value-format="x"
          placeholder="选择生产日期"
        />
      </el-form-item>
      <el-form-item label="过期日期" prop="expiryDate">
        <el-date-picker
          v-model="formData.expiryDate"
          type="date"
          value-format="x"
          placeholder="选择过期日期"
        />
      </el-form-item>
      <el-form-item label="批次属性1" prop="batchAttr1">
        <el-input v-model="formData.batchAttr1" placeholder="请输入批次属性1" />
      </el-form-item>
      <el-form-item label="批次属性2" prop="batchAttr2">
        <el-input v-model="formData.batchAttr2" placeholder="请输入批次属性2" />
      </el-form-item>
      <el-form-item label="批次属性3" prop="batchAttr3">
        <el-input v-model="formData.batchAttr3" placeholder="请输入批次属性3" />
      </el-form-item>
      <el-form-item label="批次属性4" prop="batchAttr4">
        <el-input v-model="formData.batchAttr4" placeholder="请输入批次属性4" />
      </el-form-item>
      <el-form-item label="入库总数量" prop="totalCount">
        <el-input v-model="formData.totalCount" placeholder="请输入入库总数量" />
      </el-form-item>
      <el-form-item label="可用数量" prop="availableCount">
        <el-input v-model="formData.availableCount" placeholder="请输入可用数量" />
      </el-form-item>
      <el-form-item label="锁定数量" prop="lockedCount">
        <el-input v-model="formData.lockedCount" placeholder="请输入锁定数量" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="formData.status">
          <el-radio
            v-for="dict in getIntDictOptions(DICT_TYPE.WMS_BATCH_STATUS)"
            :key="dict.value"
            :label="dict.value"
          >{{ dict.label }}</el-radio>
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
import { BatchApi, BatchVO } from '@/api/wms/batch'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'

/** 批次信息 表单 */
defineOptions({ name: 'BatchForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  batchNo: undefined,
  itemId: undefined,
  warehouseId: undefined,
  supplierId: undefined,
  productionDate: undefined,
  expiryDate: undefined,
  batchAttr1: undefined,
  batchAttr2: undefined,
  batchAttr3: undefined,
  batchAttr4: undefined,
  totalCount: undefined,
  availableCount: undefined,
  lockedCount: undefined,
  status: 0,
  remark: undefined,
})
const formRules = reactive({
  batchNo: [{ required: true, message: '批次号不能为空', trigger: 'blur' }],
  itemId: [{ required: true, message: '物料ID不能为空', trigger: 'blur' }],
  warehouseId: [{ required: true, message: '仓库ID不能为空', trigger: 'blur' }],
  totalCount: [{ required: true, message: '入库总数量不能为空', trigger: 'blur' }],
  availableCount: [{ required: true, message: '可用数量不能为空', trigger: 'blur' }],
  lockedCount: [{ required: true, message: '锁定数量不能为空', trigger: 'blur' }],
  status: [{ required: true, message: '状态不能为空', trigger: 'blur' }],
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
      formData.value = await BatchApi.getBatch(id)
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
    const data = formData.value as unknown as BatchVO
    if (formType.value === 'create') {
      await BatchApi.createBatch(data)
      message.success(t('common.createSuccess'))
    } else {
      await BatchApi.updateBatch(data)
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
    batchNo: undefined,
    itemId: undefined,
    warehouseId: undefined,
    supplierId: undefined,
    productionDate: undefined,
    expiryDate: undefined,
    batchAttr1: undefined,
    batchAttr2: undefined,
    batchAttr3: undefined,
    batchAttr4: undefined,
    totalCount: undefined,
    availableCount: undefined,
    lockedCount: undefined,
    status: 0,
    remark: undefined,
  }
  formRef.value?.resetFields()
}
</script>
