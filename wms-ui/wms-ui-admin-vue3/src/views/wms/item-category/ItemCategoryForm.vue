<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="分类编码" prop="categoryCode" v-if="formType === 'update'">
        <el-input v-model="formData.categoryCode" placeholder="请输入分类编码" disabled />
      </el-form-item>
      <el-form-item label="分类名称" prop="categoryName">
        <el-input v-model="formData.categoryName" placeholder="请输入分类名称" />
      </el-form-item>
      <el-form-item label="父分类" prop="parentId">
        <el-radio-group 
          v-model="isRootNode" 
          v-if="formType === 'create' && !isEditingChild"
          @change="handleRootNodeChange"
        >
          <el-radio :label="true">根节点</el-radio>
          <el-radio :label="false">子节点</el-radio>
        </el-radio-group>
        <el-tree-select
          v-model="formData.parentId"
          :data="categoryOptions"
          node-key="id"
          :props="{ label: 'categoryName', children: 'children' }"
          placeholder="请选择父分类"
          clearable
          filterable
          check-strictly
          :default-expand-all="false"
          v-if="!isRootNode"
          :disabled="isEditingChild"
        />
        <span v-else-if="formType === 'update'">{{ parentCategoryName || '根节点' }}</span>
      </el-form-item>
      <el-form-item label="显示顺序" prop="sort">
        <el-input v-model="formData.sort" placeholder="请输入显示顺序" />
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
import { ItemCategoryApi, ItemCategoryVO, ItemCategoryTreeVO } from '@/api/wms/itemcategory'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'

/** 物料分类 表单 */
defineOptions({ name: 'ItemCategoryForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const categoryOptions = ref<any[]>([]) // 分类下拉选项
const currentId = ref<number | undefined>() // 当前编辑的分类ID
const isRootNode = ref(false) // 是否为根节点
const isEditingChild = ref(false) // 是否是编辑子节点（从新增子分类按钮进入）
const parentCategoryName = ref('') // 父分类名称

const formData = ref({
  id: undefined as number | undefined,
  categoryCode: undefined as string | undefined,
  categoryName: undefined as string | undefined,
  parentId: undefined as number | undefined,
  sort: undefined as number | undefined,
  status: 0,
  remark: undefined as string | undefined,
})

const formRules = reactive({
  categoryName: [{ required: true, message: '分类名称不能为空', trigger: 'blur' }],
  sort: [{ required: true, message: '显示顺序不能为空', trigger: 'blur' }],
  status: [{ required: true, message: '状态不能为空', trigger: 'blur' }],
})

const formRef = ref() // 表单 Ref

/** 处理根节点切换 */
const handleRootNodeChange = (value: boolean) => {
  if (value) {
    formData.value.parentId = 0
  } else {
    formData.value.parentId = undefined
  }
}

/** 打开弹窗 */
const open = async (type: string, id?: number, parentId?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  
  // 设置是否是编辑子节点
  isEditingChild.value = type === 'create' && parentId !== undefined
  
  // 获取分类选项
  await getCategoryOptions()
  
  // 新增时，如果有传入 parentId，设置为默认父分类
  if (type === 'create' && parentId) {
    formData.value.parentId = parentId
    // 查找并设置父分类名称
    const findParentName = (items: ItemCategoryTreeVO[]): string => {
      for (const item of items) {
        if (item.id === parentId) {
          return item.categoryName
        }
        if (item.children) {
          const name = findParentName(item.children)
          if (name) return name
        }
      }
      return ''
    }
    parentCategoryName.value = findParentName(categoryOptions.value)
  }
  
  // 修改时，设置数据
  if (id) {
    currentId.value = id
    formLoading.value = true
    try {
      formData.value = await ItemCategoryApi.getItemCategory(id)
      // 设置是否为根节点
      isRootNode.value = !formData.value.parentId || formData.value.parentId === 0
      // 如果不是根节点，查找并设置父分类名称
      if (!isRootNode.value) {
        const findParentName = (items: ItemCategoryTreeVO[]): string => {
          for (const item of items) {
            if (item.id === formData.value.parentId) {
              return item.categoryName
            }
            if (item.children) {
              const name = findParentName(item.children)
              if (name) return name
            }
          }
          return ''
        }
        parentCategoryName.value = findParentName(categoryOptions.value)
      }
    } finally {
      formLoading.value = false
    }
  } else {
    currentId.value = undefined
    isRootNode.value = false
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
    const data = formData.value as unknown as ItemCategoryVO
    // 确保根节点的 parentId 为 0
    if (isRootNode.value) {
      data.parentId = 0
    }
    if (formType.value === 'create') {
      await ItemCategoryApi.createItemCategory(data)
      message.success(t('common.createSuccess'))
    } else {
      await ItemCategoryApi.updateItemCategory(data)
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
    categoryCode: undefined,
    categoryName: undefined,
    parentId: undefined,
    sort: undefined,
    status: 0,
    remark: undefined,
  }
  isRootNode.value = false
  isEditingChild.value = false
  parentCategoryName.value = ''
  formRef.value?.resetFields()
}

/** 获取分类下拉选项 */
const getCategoryOptions = async () => {
  try {
    // 直接获取树形结构数据
    const data = await ItemCategoryApi.getItemCategoryTree()
    // 过滤掉当前编辑的分类，避免选择自己作为父分类
    if (currentId.value) {
      const filterTree = (items: ItemCategoryTreeVO[]) => {
        return items.filter(item => {
          if (item.id === currentId.value) {
            return false
          }
          if (item.children && item.children.length > 0) {
            item.children = filterTree(item.children)
          }
          return true
        })
      }
      categoryOptions.value = filterTree(data)
    } else {
      categoryOptions.value = data
    }
  } catch (error) {
    console.error('获取分类列表失败', error)
    categoryOptions.value = []
  }
}
</script>
