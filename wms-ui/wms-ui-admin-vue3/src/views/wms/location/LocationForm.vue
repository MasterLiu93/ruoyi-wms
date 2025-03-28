<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="库位编码" prop="locationCode">
        <el-input v-model="formData.locationCode" placeholder="请输入库位编码" />
      </el-form-item>
      <el-form-item label="库位名称" prop="locationName">
        <el-input v-model="formData.locationName" placeholder="请输入库位名称" />
      </el-form-item>
      <el-form-item label="所属货架ID" prop="rackId">
        <el-input v-model="formData.rackId" placeholder="请输入所属货架ID" />
      </el-form-item>
      <el-form-item label="库位类型" prop="locationType">
        <el-select v-model="formData.locationType" placeholder="请选择库位类型">
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.WMS_LOCATION_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="formData.status">
          <el-radio
            v-for="dict in getIntDictOptions(DICT_TYPE.WMS_LOCATION_STATUS)"
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
import { LocationApi, LocationVO } from '@/api/wms/location'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'

/** 库位 表单 */
defineOptions({ name: 'LocationForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  locationCode: undefined,
  locationName: undefined,
  rackId: undefined,
  locationType: undefined,
  status: 0,
  remark: undefined,
})
const formRules = reactive({
  locationCode: [{ required: true, message: '库位编码不能为空', trigger: 'blur' }],
  locationName: [{ required: true, message: '库位名称不能为空', trigger: 'blur' }],
  rackId: [{ required: true, message: '所属货架ID不能为空', trigger: 'blur' }],
  locationType: [{ required: true, message: '库位类型不能为空', trigger: 'change' }],
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
      formData.value = await LocationApi.getLocation(id)
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
    const data = formData.value as unknown as LocationVO
    if (formType.value === 'create') {
      await LocationApi.createLocation(data)
      message.success(t('common.createSuccess'))
    } else {
      await LocationApi.updateLocation(data)
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
    locationCode: undefined,
    locationName: undefined,
    rackId: undefined,
    locationType: undefined,
    status: 0,
    remark: undefined,
  }
  formRef.value?.resetFields()
}
</script>
