<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="货架编码" prop="rackCode">
        <el-input v-model="formData.rackCode" placeholder="请输入货架编码" />
      </el-form-item>
      <el-form-item label="货架名称" prop="rackName">
        <el-input v-model="formData.rackName" placeholder="请输入货架名称" />
      </el-form-item>
      <el-form-item label="所属货区ID" prop="areaId">
        <el-input v-model="formData.areaId" placeholder="请输入所属货区ID" />
      </el-form-item>
      <el-form-item label="货架类型" prop="rackType">
        <el-select v-model="formData.rackType" placeholder="请选择货架类型">
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.WMS_RACK_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="formData.status">
          <el-radio
            v-for="dict in getIntDictOptions(DICT_TYPE.COMMON_STATUS)"
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
import { RackApi, RackVO } from '@/api/wms/rack'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'

/** 货架 表单 */
defineOptions({ name: 'RackForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  rackCode: undefined,
  rackName: undefined,
  areaId: undefined,
  rackType: undefined,
  status: 0,
  remark: undefined,
})
const formRules = reactive({
  rackCode: [{ required: true, message: '货架编码不能为空', trigger: 'blur' }],
  rackName: [{ required: true, message: '货架名称不能为空', trigger: 'blur' }],
  areaId: [{ required: true, message: '所属货区ID不能为空', trigger: 'blur' }],
  rackType: [{ required: true, message: '货架类型不能为空', trigger: 'change' }],
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
      formData.value = await RackApi.getRack(id)
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
    const data = formData.value as unknown as RackVO
    if (formType.value === 'create') {
      await RackApi.createRack(data)
      message.success(t('common.createSuccess'))
    } else {
      await RackApi.updateRack(data)
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
    rackCode: undefined,
    rackName: undefined,
    areaId: undefined,
    rackType: undefined,
    status: 0,
    remark: undefined,
  }
  formRef.value?.resetFields()
}
</script>
