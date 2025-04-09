<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="盘点单号" prop="checkNo" v-if="formType === 'update'">
        <el-input v-model="formData.checkNo" placeholder="请输入盘点单号" :disabled="true" />
      </el-form-item>
      <el-form-item label="仓库" prop="warehouseId">
        <el-select 
          v-model="formData.warehouseId" 
          placeholder="请选择仓库" 
          filterable 
          clearable 
          class="!w-240px"
          @change="handleWarehouseChange"
        >
          <el-option 
            v-for="item in warehouseOptions" 
            :key="item.id" 
            :label="item.warehouseName || `仓库${item.id}`"
            :value="Number(item.id)"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="盘点类型" prop="checkType">
        <el-radio-group v-model="formData.checkType">
          <el-radio :label="0">全部盘点</el-radio>
          <el-radio :label="1">部分盘点</el-radio>
        </el-radio-group>
      </el-form-item>
      <template v-if="formData.checkType === 1">
        <el-form-item label="盘点物料" prop="itemIds">
          <el-select 
            v-model="formData.itemIds" 
            multiple 
            filterable 
            placeholder="请选择物料" 
            class="!w-240px"
          >
            <el-option 
              v-for="item in itemOptions" 
              :key="item.id" 
              :label="item.itemName"
              :value="item.id" 
            >
              <span>{{ item.itemName }}</span>
              <span class="text-gray-400 ml-2">({{ item.itemCode }})</span>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="盘点库位" prop="locationIds">
          <el-select 
            v-model="formData.locationIds" 
            multiple 
            filterable 
            placeholder="请选择库位" 
            class="!w-240px"
          >
            <el-option 
              v-for="item in locationOptions" 
              :key="item.id" 
              :label="item.locationName" 
              :value="item.id" 
            >
              <span>{{ item.locationName }}</span>
              <span class="text-gray-400 ml-2">({{ item.locationCode }})</span>
            </el-option>
          </el-select>
        </el-form-item>
      </template>
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
import { InventoryCheckApi, InventoryCheckVO, InventoryCheckPlanVO } from '@/api/wms/inventorycheck'
import { WarehouseApi } from '@/api/wms/warehouse'
import { ItemApi } from '@/api/wms/item'
import { LocationApi } from '@/api/wms/location'

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
  checkType: 0,
  checkStatus: undefined,
  totalCount: undefined,
  checkedCount: undefined,
  differenceCount: undefined,
  locationIds: [] as number[],
  itemIds: [] as number[],
  remark: undefined,
})
const formRules = reactive({
  warehouseId: [{ required: true, message: '仓库不能为空', trigger: 'change' }],
  checkType: [{ required: true, message: '盘点类型不能为空', trigger: 'change' }]
})

// 下拉选项
const warehouseOptions = ref<any[]>([])
const itemOptions = ref<any[]>([])
const locationOptions = ref<any[]>([])
 
/** 获取仓库选项 */
const getWarehouseOptions = async () => {
  try {
    // 使用分页接口获取所有仓库数据
    const result = await WarehouseApi.getWarehousePage({
      pageNo: 1,
      pageSize: 100 // 获取足够多的数据
    })

    if (result && result.list && Array.isArray(result.list)) {
      // 直接使用返回的仓库数据，不做字段映射
      warehouseOptions.value = result.list
    }
  } catch (error) {
    console.error('获取仓库列表失败:', error)
    warehouseOptions.value = []
  }
}

// 获取物料选项
const getItemOptions = async () => {
  try {
    // 使用分页接口获取所有物料数据
    const result = await ItemApi.getItemPage({
      pageNo: 1,
      pageSize: 100 // 获取足够多的数据
    })

    if (result && result.list && Array.isArray(result.list)) {
      itemOptions.value = result.list
    }
  } catch (error) {
    console.error('获取物料列表失败:', error)
    itemOptions.value = []
  }
}

// 获取库位选项
const getLocationOptions = async (warehouseId: any) => {
  if (!warehouseId) {
    locationOptions.value = []
    return
  }
  try {
    // 使用分页接口获取指定仓库的库位数据
    const result = await LocationApi.getLocationPage({
      pageNo: 1,
      pageSize: 100, // 获取足够多的数据
      warehouseId: warehouseId
    })

    if (result && result.list && Array.isArray(result.list)) {
      locationOptions.value = result.list
    }
  } catch (error) {
    console.error('获取库位列表失败:', error)
    locationOptions.value = []
  }
}

// 监听仓库变化，重新加载库位选项
watch(() => formData.value.warehouseId, (newVal) => {
  getLocationOptions(newVal)
  // 清空已选库位
  formData.value.locationIds = []
})

const formRef = ref() // 表单 Ref

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  
  // 加载选项数据
  await Promise.all([
    getWarehouseOptions(),
    getItemOptions()
  ])
  
  // 修改时，设置数据
  if (id && type === 'update') {
    formLoading.value = true
    try {
      const data = await InventoryCheckApi.getInventoryCheck(id)
      formData.value = {
        ...data,
        locationIds: [] as number[],
        itemIds: [] as number[]
      }
      // 加载库位选项
      await getLocationOptions(formData.value.warehouseId)
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
    if (formType.value === 'create') {
      // 新建盘点，调用生成盘点计划接口
      const data: InventoryCheckPlanVO = {
        warehouseId: formData.value.warehouseId!,
        checkType: formData.value.checkType,
        remark: formData.value.remark
      }
      
      // 部分盘点时，需要传入盘点范围
      if (formData.value.checkType === 1) {
        if (formData.value.locationIds && formData.value.locationIds.length > 0) {
          data.locationIds = formData.value.locationIds
        }
        if (formData.value.itemIds && formData.value.itemIds.length > 0) {
          data.itemIds = formData.value.itemIds
        }
        // 校验是否指定了盘点范围
        if ((!data.locationIds || data.locationIds.length === 0) && 
            (!data.itemIds || data.itemIds.length === 0)) {
          message.error('部分盘点必须指定盘点的物料或库位')
          return
        }
      }
      
      await InventoryCheckApi.generateCheckPlan(data)
      message.success(t('common.createSuccess'))
    } else {
      // 更新盘点单基本信息
      const data = {
        id: formData.value.id,
        remark: formData.value.remark
      } as unknown as InventoryCheckVO
      await InventoryCheckApi.updateInventoryCheck(data)
      message.success(t('common.updateSuccess'))
    }
    dialogVisible.value = false
    // 发送操作成功的事件
    emit('success')
  } catch (error) {
    console.error('提交表单失败:', error)
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
    checkType: 0,
    checkStatus: undefined,
    totalCount: undefined,
    checkedCount: undefined,
    differenceCount: undefined,
    locationIds: [] as number[],
    itemIds: [] as number[],
    remark: undefined,
  }
  formRef.value?.resetFields()
}

const handleWarehouseChange = (value: number) => {
  formData.value.warehouseId = value;
  getLocationOptions(value);
  formData.value.locationIds = [];
};
</script>
