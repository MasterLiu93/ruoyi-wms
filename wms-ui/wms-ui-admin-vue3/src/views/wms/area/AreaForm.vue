<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="货区编码" prop="areaCode" v-if="formType === 'update'">
        <el-input v-model="formData.areaCode" placeholder="请输入货区编码" disabled />
      </el-form-item>
      <el-form-item label="货区名称" prop="areaName">
        <el-input v-model="formData.areaName" placeholder="请输入货区名称" />
      </el-form-item>
      <el-form-item label="所属仓库" prop="warehouseId">
        <el-select 
          v-model="formData.warehouseId" 
          placeholder="请选择所属仓库" 
          clearable 
          filterable
          style="width: 100%"
        >
          <el-option
            v-for="item in warehouseOptions"
            :key="item.id"
            :label="item.warehouseName || `仓库${item.id}`"
            :value="Number(item.id)"
          />
        </el-select>
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
import { WarehouseApi } from '@/api/wms/warehouse'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'

/** 货区 表单 */
defineOptions({ name: 'AreaForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const warehouseOptions = ref<any[]>([]) // 仓库下拉选项
const warehouseMap = ref<Record<number, any>>({}) // 仓库ID映射，用于查找仓库信息
const formData = ref({
  id: undefined as number | undefined,
  areaCode: undefined as string | undefined,
  areaName: undefined as string | undefined,
  warehouseId: undefined as number | undefined,
  areaType: undefined as number | undefined,
  status: 0,
  remark: undefined as string | undefined,
})
const formRules = reactive({
  areaName: [{ required: true, message: '货区名称不能为空', trigger: 'blur' }],
  warehouseId: [{ required: true, message: '所属仓库不能为空', trigger: 'change' }],
  areaType: [{ required: true, message: '货区类型不能为空', trigger: 'change' }],
  status: [{ required: true, message: '状态不能为空', trigger: 'blur' }],
})
const formRef = ref() // 表单 Ref

/** 获取仓库选项 */
const getWarehouseOptions = async () => {
  try {
    // 使用分页接口获取所有仓库数据
    const result = await WarehouseApi.getWarehousePage({
      pageNo: 1,
      pageSize: 100 // 获取足够多的数据
    })
    console.log('仓库分页数据:', result)
    
    if (result && result.list && Array.isArray(result.list)) {
      // 处理返回的仓库数据
      warehouseOptions.value = result.list.map(item => {
        // 注意不要使用重复的属性名
        const displayName = item.warehouseName || item.name || `仓库${item.id}`
        const displayCode = item.warehouseCode || item.code || `WH${item.id}`
        return {
          id: item.id,
          warehouseName: displayName,
          warehouseCode: displayCode
        }
      })
      
      // 构建仓库ID到仓库对象的映射
      warehouseMap.value = {}
      warehouseOptions.value.forEach(item => {
        warehouseMap.value[item.id] = item
      })
      
      console.log('处理后的仓库数据:', warehouseOptions.value)
    } else {
      console.error('仓库数据格式不正确:', result)
      // 如果返回格式不对，尝试使用简单列表接口
      await getSimpleWarehouseList()
    }
  } catch (error) {
    console.error('获取仓库列表失败，尝试使用简单列表:', error)
    // 出错时尝试简单列表接口
    await getSimpleWarehouseList()
  }
}

/** 获取简单仓库列表 */
const getSimpleWarehouseList = async () => {
  try {
    // 使用简单列表接口作为备选
    const data = await WarehouseApi.getSimpleWarehouseList()
    console.log('简单仓库列表数据:', data)
    
    // 处理返回的仓库数据
    const processedData = data.map((item: any) => {
      // 将ID转为显示名称
      return {
        id: item.id,
        warehouseName: `仓库${item.id}`, // 使用ID作为显示名称
        warehouseCode: `WH${item.id}`
      }
    })
    
    warehouseOptions.value = processedData
    
    // 构建仓库ID到仓库对象的映射
    warehouseMap.value = {}
    processedData.forEach((item: any) => {
      warehouseMap.value[item.id] = item
    })
    
    console.log('处理后的简单仓库数据:', processedData)
  } catch (error) {
    console.error('获取简单仓库列表也失败:', error)
    // 如果都失败了，至少提供一个空数组
    warehouseOptions.value = []
  }
}

/** 打开弹窗 */
const open = async (type: string, id?: number, warehouseId?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  
  // 获取仓库下拉选项
  await getWarehouseOptions()
  
  // 如果有传入warehouseId，则设置
  if (type === 'create' && warehouseId) {
    formData.value.warehouseId = Number(warehouseId)
  }
  
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      const data = await AreaApi.getArea(id)
      console.log('获取到的货区数据:', data)
      
      // 确保 warehouseId 是数字类型
      if (data.warehouseId) {
        data.warehouseId = Number(data.warehouseId)
      }
      
      formData.value = data
      console.log('设置的表单数据:', formData.value)
      
      // 如果找不到仓库信息，可能需要单独获取
      if (data.warehouseId && !warehouseMap.value[data.warehouseId]) {
        try {
          // 尝试单独获取仓库信息
          const warehouseData = await WarehouseApi.getWarehouse(data.warehouseId)
          console.log('单独获取的仓库数据:', warehouseData)
          
          if (warehouseData) {
            // 注意不要使用重复的属性名
            const displayName = warehouseData.warehouseName || warehouseData.name || `仓库${warehouseData.id}`
            const displayCode = warehouseData.warehouseCode || warehouseData.code || `WH${warehouseData.id}`
            
            const warehouseItem = {
              id: warehouseData.id,
              warehouseName: displayName,
              warehouseCode: displayCode
            }
            
            // 添加到选项和映射中
            warehouseOptions.value.push(warehouseItem)
            warehouseMap.value[warehouseItem.id] = warehouseItem
          }
        } catch (warehouseError) {
          console.error('获取单个仓库信息失败:', warehouseError)
        }
      }
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
    // 确保提交的 warehouseId 是数字类型
    if (formData.value.warehouseId) {
      formData.value.warehouseId = Number(formData.value.warehouseId)
    }
    
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
