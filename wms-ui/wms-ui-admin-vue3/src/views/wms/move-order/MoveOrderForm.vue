<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="移库单号" prop="moveOrderNo">
        <el-input v-model="formData.moveOrderNo" placeholder="请输入移库单号" />
      </el-form-item>
      <el-form-item label="移库类型" prop="moveType">
        <el-select v-model="formData.moveType" placeholder="请选择移库类型">
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.WMS_MOVE_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="源仓库ID" prop="fromWarehouseId">
        <el-input v-model="formData.fromWarehouseId" placeholder="请输入源仓库ID" />
      </el-form-item>
      <el-form-item label="目标仓库ID" prop="toWarehouseId">
        <el-input v-model="formData.toWarehouseId" placeholder="请输入目标仓库ID" />
      </el-form-item>
      <el-form-item label="源库位ID" prop="fromLocationId">
        <el-input v-model="formData.fromLocationId" placeholder="请输入源库位ID" />
      </el-form-item>
      <el-form-item label="目标库位ID" prop="toLocationId">
        <el-input v-model="formData.toLocationId" placeholder="请输入目标库位ID" />
      </el-form-item>
      <el-form-item label="单据状态" prop="orderStatus">
        <el-radio-group v-model="formData.orderStatus">
          <el-radio
            v-for="dict in getIntDictOptions(DICT_TYPE.WMS_MOVE_ORDER_STATUS)"
            :key="dict.value"
            :label="dict.value"
          >{{ dict.label }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="移库状态" prop="moveStatus">
        <el-radio-group v-model="formData.moveStatus">
          <el-radio
            v-for="dict in getIntDictOptions(DICT_TYPE.WMS_MOVE_STATUS)"
            :key="dict.value"
            :label="dict.value"
          >{{ dict.label }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="商品数量" prop="totalCount">
        <el-input v-model="formData.totalCount" placeholder="请输入商品数量" />
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
import { MoveOrderApi, MoveOrderVO } from '@/api/wms/moveorder'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'

/** 移库单 表单 */
defineOptions({ name: 'MoveOrderForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  moveOrderNo: undefined,
  moveType: undefined,
  fromWarehouseId: undefined,
  toWarehouseId: undefined,
  fromLocationId: undefined,
  toLocationId: undefined,
  orderStatus: 0,
  moveStatus: 0,
  totalCount: undefined,
  remark: undefined,
})
const formRules = reactive({
  moveOrderNo: [{ required: true, message: '移库单号不能为空', trigger: 'blur' }],
  moveType: [{ required: true, message: '移库类型不能为空', trigger: 'change' }],
  fromWarehouseId: [{ required: true, message: '源仓库ID不能为空', trigger: 'blur' }],
  toWarehouseId: [{ required: true, message: '目标仓库ID不能为空', trigger: 'blur' }],
  orderStatus: [{ required: true, message: '单据状态不能为空', trigger: 'blur' }],
  moveStatus: [{ required: true, message: '移库状态不能为空', trigger: 'blur' }],
  totalCount: [{ required: true, message: '商品数量不能为空', trigger: 'blur' }],
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
      formData.value = await MoveOrderApi.getMoveOrder(id)
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
    const data = formData.value as unknown as MoveOrderVO
    if (formType.value === 'create') {
      await MoveOrderApi.createMoveOrder(data)
      message.success(t('common.createSuccess'))
    } else {
      await MoveOrderApi.updateMoveOrder(data)
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
    moveOrderNo: undefined,
    moveType: undefined,
    fromWarehouseId: undefined,
    toWarehouseId: undefined,
    fromLocationId: undefined,
    toLocationId: undefined,
    orderStatus: 0,
    moveStatus: 0,
    totalCount: undefined,
    remark: undefined,
  }
  formRef.value?.resetFields()
}
</script>
