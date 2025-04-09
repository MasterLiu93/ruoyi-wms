<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="库位编码" prop="locationCode" v-if="formType === 'update'">
        <el-input v-model="formData.locationCode" placeholder="请输入库位编码" disabled />
      </el-form-item>
      <el-form-item label="库位名称" prop="locationName">
        <el-input v-model="formData.locationName" placeholder="请输入库位名称" />
      </el-form-item>
      <el-form-item label="所属仓库" prop="warehouseId">
        <el-select 
          v-model="selectedWarehouseId" 
          placeholder="请选择所属仓库" 
          clearable 
          filterable
          style="width: 100%"
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
      <el-form-item label="所属货区" prop="areaId">
        <el-select 
          v-model="selectedAreaId" 
          placeholder="请选择所属货区" 
          clearable 
          filterable
          style="width: 100%"
          :disabled="!selectedWarehouseId"
          @change="handleAreaChange"
        >
          <el-option
            v-for="item in areaOptions"
            :key="item.id"
            :label="item.areaName || `货区${item.id}`"
            :value="Number(item.id)"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="所属货架" prop="rackId">
        <el-select 
          v-model="formData.rackId" 
          placeholder="请选择所属货架" 
          clearable 
          filterable
          style="width: 100%"
          :disabled="!selectedAreaId"
        >
          <el-option
            v-for="item in rackOptions"
            :key="item.id"
            :label="item.rackName || `货架${item.id}`"
            :value="Number(item.id)"
          />
        </el-select>
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
import { WarehouseApi } from '@/api/wms/warehouse'
import { AreaApi } from '@/api/wms/area'
import { RackApi } from '@/api/wms/rack'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'

/** 库位 表单 */
defineOptions({ name: 'LocationForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const warehouseOptions = ref<any[]>([]) // 仓库下拉选项
const warehouseMap = ref<Record<number, any>>({}) // 仓库ID映射
const areaOptions = ref<any[]>([]) // 货区下拉选项
const areaMap = ref<Record<number, any>>({}) // 货区ID映射
const rackOptions = ref<any[]>([]) // 货架下拉选项
const rackMap = ref<Record<number, any>>({}) // 货架ID映射
const selectedWarehouseId = ref<number | undefined>(undefined) // 选中的仓库ID
const selectedAreaId = ref<number | undefined>(undefined) // 选中的货区ID
const formData = ref({
  id: undefined as number | undefined,
  locationCode: undefined as string | undefined,
  locationName: undefined as string | undefined,
  rackId: undefined as number | undefined,
  locationType: undefined as number | undefined,
  status: 0,
  remark: undefined as string | undefined,
})
const formRules = reactive({
  locationName: [{ required: true, message: '库位名称不能为空', trigger: 'blur' }],
  rackId: [{ required: true, message: '所属货架不能为空', trigger: 'change' }],
  locationType: [{ required: true, message: '库位类型不能为空', trigger: 'change' }],
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
      const processedData = result.list.map(item => {
        // 注意不要使用重复的属性名
        const displayName = item.warehouseName || item.name || `仓库${item.id}`
        const displayCode = item.warehouseCode || item.code || `WH${item.id}`
        return {
          id: Number(item.id),
          warehouseName: displayName,
          warehouseCode: displayCode
        }
      })
      
      warehouseOptions.value = processedData
      
      // 构建仓库ID到仓库对象的映射
      warehouseMap.value = {}
      processedData.forEach(item => {
        warehouseMap.value[item.id] = item
      })
      
      console.log('处理后的仓库数据:', processedData)
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
        id: Number(item.id),
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

/** 获取货区选项 */
const getAreaOptions = async (warehouseId?: number) => {
  if (!warehouseId) {
    areaOptions.value = []
    areaMap.value = {}
    return
  }
  
  try {
    const data = await AreaApi.getSimpleAreaList(warehouseId)
    console.log('货区列表数据:', data)
    
    // 处理返回的货区数据
    const processedData = data.map((item: any) => {
      return {
        id: Number(item.id),
        areaName: item.areaName || `货区${item.id}`,
        areaCode: item.areaCode || `AREA${item.id}`,
        warehouseId: Number(warehouseId)
      }
    })
    
    areaOptions.value = processedData
    
    // 构建货区ID到货区对象的映射
    areaMap.value = {}
    processedData.forEach(item => {
      areaMap.value[item.id] = item
    })
    
    console.log('处理后的货区数据:', processedData)
  } catch (error) {
    console.error('获取货区列表失败', error)
    areaOptions.value = []
  }
}

/** 获取货架选项 */
const getRackOptions = async (areaId?: number) => {
  if (!areaId) {
    rackOptions.value = []
    rackMap.value = {}
    return
  }
  
  try {
    const data = await RackApi.getSimpleRackList(areaId)
    console.log('货架列表数据:', data)
    
    // 处理返回的货架数据
    const processedData = data.map((item: any) => {
      return {
        id: Number(item.id),
        rackName: item.rackName || `货架${item.id}`,
        rackCode: item.rackCode || `RACK${item.id}`,
        areaId: Number(areaId)
      }
    })
    
    rackOptions.value = processedData
    
    // 构建货架ID到货架对象的映射
    rackMap.value = {}
    processedData.forEach(item => {
      rackMap.value[item.id] = item
    })
    
    console.log('处理后的货架数据:', processedData)
  } catch (error) {
    console.error('获取货架列表失败', error)
    rackOptions.value = []
  }
}

/** 处理仓库变更 */
const handleWarehouseChange = async (warehouseId?: number) => {
  if (warehouseId) {
    selectedWarehouseId.value = Number(warehouseId)
  } else {
    selectedWarehouseId.value = undefined
  }
  
  selectedAreaId.value = undefined // 清空货区选择
  formData.value.rackId = undefined // 清空货架选择
  areaOptions.value = [] // 清空货区选项
  rackOptions.value = [] // 清空货架选项
  
  await getAreaOptions(warehouseId)
}

/** 处理货区变更 */
const handleAreaChange = async (areaId?: number) => {
  if (areaId) {
    selectedAreaId.value = Number(areaId)
  } else {
    selectedAreaId.value = undefined
  }
  
  formData.value.rackId = undefined // 清空货架选择
  rackOptions.value = [] // 清空货架选项
  
  await getRackOptions(areaId)
}

/** 打开弹窗 */
const open = async (type: string, id?: number, rackId?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  
  // 获取仓库下拉选项
  await getWarehouseOptions()
  
  // 如果有传入rackId，则设置
  if (type === 'create' && rackId) {
    formData.value.rackId = Number(rackId)
    
    // 获取货架信息，找到对应的仓库ID和货区ID
    try {
      const rackInfo = await RackApi.getRack(rackId)
      console.log('获取到的货架信息:', rackInfo)
      
      if (rackInfo && rackInfo.areaId) {
        const areaId = Number(rackInfo.areaId)
        selectedAreaId.value = areaId
        // 获取该货区下的货架列表
        await getRackOptions(areaId)
        
        // 获取货区信息，找到对应的仓库ID
        try {
          const areaInfo = await AreaApi.getArea(areaId)
          console.log('获取到的货区信息:', areaInfo)
          
          if (areaInfo && areaInfo.warehouseId) {
            const warehouseId = Number(areaInfo.warehouseId)
            selectedWarehouseId.value = warehouseId
            // 获取该仓库下的货区列表
            await getAreaOptions(warehouseId)
          }
        } catch (error) {
          console.error('获取货区信息失败', error)
        }
      }
    } catch (error) {
      console.error('获取货架信息失败', error)
    }
  }
  
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      const data = await LocationApi.getLocation(id)
      console.log('获取到的库位数据:', data)
      
      // 确保 rackId 是数字类型
      if (data.rackId) {
        data.rackId = Number(data.rackId)
      }
      
      formData.value = data
      console.log('设置的表单数据:', formData.value)
      
      // 获取货架信息，找到对应的货区ID和仓库ID
      if (formData.value.rackId) {
        try {
          const rackInfo = await RackApi.getRack(formData.value.rackId)
          console.log('获取到的货架信息:', rackInfo)
          
          if (rackInfo && rackInfo.areaId) {
            const areaId = Number(rackInfo.areaId)
            selectedAreaId.value = areaId
            // 获取该货区下的货架列表
            await getRackOptions(areaId)
            
            // 获取货区信息，找到对应的仓库ID
            try {
              const areaInfo = await AreaApi.getArea(areaId)
              console.log('获取到的货区信息:', areaInfo)
              
              if (areaInfo && areaInfo.warehouseId) {
                const warehouseId = Number(areaInfo.warehouseId)
                selectedWarehouseId.value = warehouseId
                // 获取该仓库下的货区列表
                await getAreaOptions(warehouseId)
              }
            } catch (error) {
              console.error('获取货区信息失败', error)
            }
          }
        } catch (error) {
          console.error('获取货架信息失败', error)
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
    // 确保 rackId 是数字类型
    if (formData.value.rackId) {
      formData.value.rackId = Number(formData.value.rackId)
    }
    
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
  selectedWarehouseId.value = undefined
  selectedAreaId.value = undefined
  areaOptions.value = []
  rackOptions.value = []
  formRef.value?.resetFields()
}
</script>
