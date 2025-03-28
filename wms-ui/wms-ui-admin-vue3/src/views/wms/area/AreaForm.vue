<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="货区编码" prop="areaCode">
        <el-input v-model="formData.areaCode" placeholder="请输入货区编码" />
      </el-form-item>
      <el-form-item label="货区名称" prop="areaName">
        <el-input v-model="formData.areaName" placeholder="请输入货区名称" />
      </el-form-item>
      <el-form-item label="所属仓库ID" prop="warehouseId">
        <el-input v-model="formData.warehouseId" placeholder="请输入所属仓库ID" />
      </el-form-item>
      <el-form-item label="货区类型" prop="areaType">
        <el-select v-model="formData.areaType" placeholder="请选择货区类型">
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.WMS_AREA_TYPE)"
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
import { AreaApi, AreaVO } from '@/api/wms/area'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'

/** 货区 表单 */
defineOptions({ name: 'AreaForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  areaCode: undefined,
  areaName: undefined,
  warehouseId: undefined,
  areaType: undefined,
  status: 0,
  remark: undefined,
})
const formRules = reactive({
  areaCode: [{ required: true, message: '货区编码不能为空', trigger: 'blur' }],
  areaName: [{ required: true, message: '货区名称不能为空', trigger: 'blur' }],
  warehouseId: [{ required: true, message: '所属仓库ID不能为空', trigger: 'blur' }],
  areaType: [{ required: true, message: '货区类型不能为空', trigger: 'change' }],
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
      formData.value = await AreaApi.getArea(id)
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
    const data = formData.value as unknown as AreaVO
    if (formType.value === 'create') {
      await AreaApi.createArea(data)
      message.success(t('common.createSuccess'))
    } else {
      await AreaApi.updateArea(data)
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
    areaCode: undefined,
    areaName: undefined,
    warehouseId: undefined,
    areaType: undefined,
    status: 0,
    remark: undefined,
  }
  formRef.value?.resetFields()
}
</script>
