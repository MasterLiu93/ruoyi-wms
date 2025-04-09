<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="物料编码" prop="itemCode" v-if="formType === 'update'">
        <el-input v-model="formData.itemCode" placeholder="请输入物料编码" disabled />
      </el-form-item>
      <el-form-item label="物料名称" prop="itemName">
        <el-input v-model="formData.itemName" placeholder="请输入物料名称" />
      </el-form-item>
      <el-form-item label="物料分类" prop="categoryId">
        <el-tree-select
          v-model="formData.categoryId"
          :data="categoryOptions"
          node-key="id"
          :props="{ label: 'categoryName', children: 'children' }"
          placeholder="请选择物料分类"
          clearable
          filterable
          check-strictly
          :default-expand-all="false"
        />
      </el-form-item>
      <el-form-item label="物料类型" prop="itemType">
        <el-select v-model="formData.itemType" placeholder="请选择物料类型">
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.WMS_ITEM_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="单位" prop="unit">
        <el-input v-model="formData.unit" placeholder="请输入单位" />
      </el-form-item>
      <el-form-item label="规格" prop="spec">
        <el-input v-model="formData.spec" placeholder="请输入规格" />
      </el-form-item>
      <el-form-item label="参考价格" prop="price">
        <el-input v-model="formData.price" placeholder="请输入参考价格" />
      </el-form-item>
      <el-form-item label="安全库存" prop="safetyStock">
        <el-input v-model="formData.safetyStock" placeholder="请输入安全库存" />
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
import { ItemApi, ItemVO } from '@/api/wms/item'
import { ItemCategoryApi, ItemCategoryVO, ItemCategoryTreeVO } from '@/api/wms/itemcategory'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'

/** 物料 表单 */
defineOptions({ name: 'ItemForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const categoryOptions = ref<any[]>([]) // 分类下拉选项
const formData = ref({
  id: undefined as number | undefined,
  itemCode: undefined as string | undefined,
  itemName: undefined as string | undefined,
  categoryId: undefined as number | undefined,
  itemType: undefined as number | undefined,
  unit: undefined as string | undefined,
  spec: undefined as string | undefined,
  price: undefined as number | undefined,
  safetyStock: undefined as number | undefined,
  status: 0,
  remark: undefined as string | undefined,
})
const formRules = reactive({
  itemName: [{ required: true, message: '物料名称不能为空', trigger: 'blur' }],
  categoryId: [{ required: true, message: '分类ID不能为空', trigger: 'blur' }],
  itemType: [{ required: true, message: '物料类型不能为空', trigger: 'change' }],
  unit: [{ required: true, message: '单位不能为空', trigger: 'blur' }],
  status: [{ required: true, message: '状态不能为空', trigger: 'blur' }],
})
const formRef = ref() // 表单 Ref

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  
  // 获取分类选项
  await getCategoryOptions()
  
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      formData.value = await ItemApi.getItem(id)
    } finally {
      formLoading.value = false
    }
  }
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 获取分类下拉选项 */
const getCategoryOptions = async () => {
  try {
    // 直接获取树形结构数据
    const data = await ItemCategoryApi.getItemCategoryTree()
    categoryOptions.value = data
  } catch (error) {
    console.error('获取分类列表失败', error)
    categoryOptions.value = []
  }
}

/** 提交表单 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
const submitForm = async () => {
  // 校验表单
  await formRef.value.validate()
  // 提交请求
  formLoading.value = true
  try {
    const data = formData.value as unknown as ItemVO
    if (formType.value === 'create') {
      await ItemApi.createItem(data)
      message.success(t('common.createSuccess'))
    } else {
      await ItemApi.updateItem(data)
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
    itemCode: undefined,
    itemName: undefined,
    categoryId: undefined,
    itemType: undefined,
    unit: undefined,
    spec: undefined,
    price: undefined,
    safetyStock: undefined,
    status: 0,
    remark: undefined,
  }
  formRef.value?.resetFields()
}
</script>
