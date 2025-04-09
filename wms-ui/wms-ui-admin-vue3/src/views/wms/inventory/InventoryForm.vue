<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible" width="600px">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="仓库" prop="warehouseId">
        <el-select 
          v-model="formData.warehouseId" 
          placeholder="请选择仓库" 
          clearable 
          filterable 
          style="width: 100%"
          @change="handleWarehouseChange"
        >
          <el-option
            v-for="item in warehouseOptions"
            :key="item.id"
            :label="item.warehouseName"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="库位" prop="locationId">
        <el-select 
          v-model="formData.locationId" 
          placeholder="请选择库位" 
          clearable 
          filterable 
          style="width: 100%"
          :disabled="!formData.warehouseId"
        >
          <el-option
            v-for="item in locationOptions"
            :key="item.id"
            :label="item.locationName"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="物料" prop="itemId">
        <el-select 
          v-model="formData.itemId" 
          placeholder="请选择物料" 
          clearable 
          filterable 
          style="width: 100%"
          @change="handleItemChange"
        >
          <el-option
            v-for="item in itemOptions"
            :key="item.id"
            :label="`${item.itemName} (${item.itemCode})`"
            :value="item.id"
          >
            <div>
              <div>{{ item.itemName }}</div>
              <div class="text-gray-400 text-sm">
                编码：{{ item.itemCode }} | 规格：{{ item.spec }} | 单位：{{ item.unit }}
              </div>
            </div>
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="库存数量" prop="stockCount">
        <el-input-number 
          v-model="formData.stockCount" 
          :min="0"
          :precision="2"
          :step="1"
          style="width: 100%"
          controls-position="right"
        />
      </el-form-item>
      <el-form-item label="可用数量" prop="availableCount">
        <el-input-number 
          v-model="formData.availableCount" 
          :min="0"
          :precision="2"
          :step="1"
          style="width: 100%"
          controls-position="right"
        />
      </el-form-item>
      <el-form-item label="锁定数量" prop="lockedCount">
        <el-input-number 
          v-model="formData.lockedCount" 
          :min="0"
          :precision="2"
          :step="1"
          style="width: 100%"
          controls-position="right"
        />
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
        <el-input v-model="formData.remark" type="textarea" placeholder="请输入备注" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>

<script setup lang="ts">
import { InventoryApi, InventoryVO } from '@/api/wms/inventory'
import { WarehouseApi } from '@/api/wms/warehouse'
import { ItemApi } from '@/api/wms/item'
import { LocationApi } from '@/api/wms/location'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'

/** 库存 表单 */
defineOptions({ name: 'InventoryForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改

// 仓库选项
const warehouseOptions = ref<any[]>([])
// 物料选项
const itemOptions = ref<any[]>([])
// 库位选项
const locationOptions = ref<any[]>([])

// 表单数据
const formData = ref({
  id: undefined,
  warehouseId: undefined,
  locationId: undefined,
  itemId: undefined,
  stockCount: 0,
  availableCount: 0,
  lockedCount: 0,
  status: 0,
  remark: undefined,
})

// 表单校验规则
const formRules = reactive({
  warehouseId: [{ required: true, message: '仓库不能为空', trigger: 'change' }],
  locationId: [{ required: true, message: '库位不能为空', trigger: 'change' }],
  itemId: [{ required: true, message: '物料不能为空', trigger: 'change' }],
  stockCount: [{ required: true, message: '库存数量不能为空', trigger: 'blur' }],
  availableCount: [{ required: true, message: '可用数量不能为空', trigger: 'blur' }],
  lockedCount: [{ required: true, message: '锁定数量不能为空', trigger: 'blur' }],
  status: [{ required: true, message: '状态不能为空', trigger: 'change' }],
})

const formRef = ref() // 表单 Ref

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  
  // 加载基础数据
  await loadBaseData()
  
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      const data = await InventoryApi.getInventory(id)
      formData.value = data
      // 加载库位选项
      if (data.warehouseId) {
        await loadLocationOptions(data.warehouseId)
      }
    } catch (error) {
      console.error('获取库存数据失败', error)
      message.error('获取库存数据失败')
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
    const data = {
      ...formData.value,
      id: formData.value.id || undefined
    } as unknown as InventoryVO
    if (formType.value === 'create') {
      await InventoryApi.createInventory(data)
      message.success(t('common.createSuccess'))
    } else {
      await InventoryApi.updateInventory(data)
      message.success(t('common.updateSuccess'))
    }
    dialogVisible.value = false
    // 发送操作成功的事件
    emit('success')
  } catch (error) {
    console.error('提交失败', error)
    message.error('提交失败，请重试')
  } finally {
    formLoading.value = false
  }
}

/** 重置表单 */
const resetForm = () => {
  formData.value = {
    id: undefined,
    warehouseId: undefined,
    locationId: undefined,
    itemId: undefined,
    stockCount: 0,
    availableCount: 0,
    lockedCount: 0,
    status: 0,
    remark: undefined,
  }
  formRef.value?.resetFields()
  // 清空库位选项
  locationOptions.value = []
}

/** 加载基础数据 */
const loadBaseData = async () => {
  try {
    // 加载仓库列表
    const warehousesRes = await WarehouseApi.getWarehousePage({
      pageNo: 1,
      pageSize: 100
    })
    warehouseOptions.value = warehousesRes?.list || []
    
    // 加载物料列表
    const itemsRes = await ItemApi.getItemPage({
      pageNo: 1,
      pageSize: 100
    })
    itemOptions.value = itemsRes?.list || []
  } catch (error) {
    console.error('加载基础数据失败', error)
    message.error('加载基础数据失败，请刷新页面重试')
  }
}

/** 加载库位选项 */
const loadLocationOptions = async (warehouseId: number) => {
  try {
    const res = await LocationApi.getLocationPage({
      pageNo: 1,
      pageSize: 100,
      warehouseId
    })
    locationOptions.value = res?.list || []
  } catch (error) {
    console.error('加载库位数据失败', error)
    message.error('加载库位数据失败')
  }
}

/** 处理仓库变更 */
const handleWarehouseChange = async (warehouseId: number) => {
  formData.value.locationId = undefined
  locationOptions.value = []
  if (warehouseId) {
    await loadLocationOptions(warehouseId)
  }
}

/** 处理物料变更 */
const handleItemChange = (itemId: number) => {
  const item = itemOptions.value.find(item => item.id === itemId)
  if (item) {
    // 可以在这里处理物料相关的逻辑
  }
}
</script>

<style scoped>
.text-gray-400 {
  color: #9ca3af;
}
.text-sm {
  font-size: 0.875rem;
}
</style>
