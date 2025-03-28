<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="入库单号" prop="receiptOrderNo">
        <el-input v-model="formData.receiptOrderNo" placeholder="请输入入库单号" />
      </el-form-item>
      <el-form-item label="入库类型" prop="receiptType">
        <el-select v-model="formData.receiptType" placeholder="请选择入库类型">
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.WMS_RECEIPT_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="供应商ID" prop="supplierId">
        <el-input v-model="formData.supplierId" placeholder="请输入供应商ID" />
      </el-form-item>
      <el-form-item label="仓库ID" prop="warehouseId">
        <el-input v-model="formData.warehouseId" placeholder="请输入仓库ID" />
      </el-form-item>
      <el-form-item label="单据状态" prop="orderStatus">
        <el-radio-group v-model="formData.orderStatus">
          <el-radio
            v-for="dict in getIntDictOptions(DICT_TYPE.WMS_RECEIPT_ORDER_STATUS)"
            :key="dict.value"
            :label="dict.value"
          >{{ dict.label }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="入库状态" prop="receiptStatus">
        <el-radio-group v-model="formData.receiptStatus">
          <el-radio
            v-for="dict in getIntDictOptions(DICT_TYPE.WMS_RECEIPT_ORDER_STATUS)"
            :key="dict.value"
            :label="dict.value"
          >{{ dict.label }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="预计到货时间" prop="expectTime">
        <el-date-picker
          v-model="formData.expectTime"
          type="date"
          value-format="x"
          placeholder="选择预计到货时间"
        />
      </el-form-item>
      <el-form-item label="实际到货时间" prop="arrivalTime">
        <el-date-picker
          v-model="formData.arrivalTime"
          type="date"
          value-format="x"
          placeholder="选择实际到货时间"
        />
      </el-form-item>
      <el-form-item label="商品数量" prop="totalCount">
        <el-input v-model="formData.totalCount" placeholder="请输入商品数量" />
      </el-form-item>
      <el-form-item label="商品金额" prop="totalAmount">
        <el-input v-model="formData.totalAmount" placeholder="请输入商品金额" />
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
import { ReceiptOrderApi, ReceiptOrderVO } from '@/api/wms/receiptorder'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'

/** 入库单 表单 */
defineOptions({ name: 'ReceiptOrderForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  receiptOrderNo: undefined,
  receiptType: undefined,
  supplierId: undefined,
  warehouseId: undefined,
  orderStatus: 0,
  receiptStatus: 0,
  expectTime: undefined,
  arrivalTime: undefined,
  totalCount: undefined,
  totalAmount: undefined,
  remark: undefined,
})
const formRules = reactive({
  receiptOrderNo: [{ required: true, message: '入库单号不能为空', trigger: 'blur' }],
  receiptType: [{ required: true, message: '入库类型不能为空', trigger: 'change' }],
  warehouseId: [{ required: true, message: '仓库ID不能为空', trigger: 'blur' }],
  orderStatus: [{ required: true, message: '单据状态不能为空', trigger: 'blur' }],
  receiptStatus: [{ required: true, message: '入库状态不能为空', trigger: 'blur' }],
  totalCount: [{ required: true, message: '商品数量不能为空', trigger: 'blur' }],
  totalAmount: [{ required: true, message: '商品金额不能为空', trigger: 'blur' }],
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
      formData.value = await ReceiptOrderApi.getReceiptOrder(id)
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
    const data = formData.value as unknown as ReceiptOrderVO
    if (formType.value === 'create') {
      await ReceiptOrderApi.createReceiptOrder(data)
      message.success(t('common.createSuccess'))
    } else {
      await ReceiptOrderApi.updateReceiptOrder(data)
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
    receiptOrderNo: undefined,
    receiptType: undefined,
    supplierId: undefined,
    warehouseId: undefined,
    orderStatus: 0,
    receiptStatus: 0,
    expectTime: undefined,
    arrivalTime: undefined,
    totalCount: undefined,
    totalAmount: undefined,
    remark: undefined,
  }
  formRef.value?.resetFields()
}
</script>