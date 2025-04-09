<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="货架编码" prop="rackCode" v-if="formType === 'update'">
        <el-input v-model="formData.rackCode" placeholder="请输入货架编码" disabled />
      </el-form-item>
      <el-form-item label="货架名称" prop="rackName">
        <el-input v-model="formData.rackName" placeholder="请输入货架名称" />
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
          v-model="formData.areaId" 
          placeholder="请选择所属货区" 
          clearable 
          filterable
          style="width: 100%"
          :disabled="!selectedWarehouseId"
        >
          <el-option
            v-for="item in areaOptions"
            :key="item.id"
            :label="item.areaName || `货区${item.id}`"
            :value="Number(item.id)"
          />
        </el-select>
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
import { WarehouseApi } from '@/api/wms/warehouse'
import { AreaApi } from '@/api/wms/area'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'

/** 货架 表单 */
defineOptions({ name: 'RackForm' })

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
const selectedWarehouseId = ref<number | undefined>(undefined) // 选中的仓库ID
const formData = ref({
  id: undefined as number | undefined,
  rackCode: undefined as string | undefined,
  rackName: undefined as string | undefined,
  areaId: undefined as number | undefined,
  rackType: undefined as number | undefined,
  status: 0,
  remark: undefined as string | undefined,
})
const formRules = reactive({
  rackName: [{ required: true, message: '货架名称不能为空', trigger: 'blur' }],
  areaId: [{ required: true, message: '所属货区不能为空', trigger: 'change' }],
  rackType: [{ required: true, message: '货架类型不能为空', trigger: 'change' }],
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

/** 处理仓库变更 */
const handleWarehouseChange = async (warehouseId?: number) => {
  if (warehouseId) {
    selectedWarehouseId.value = Number(warehouseId)
  } else {
    selectedWarehouseId.value = undefined
  }
  
  formData.value.areaId = undefined // 清空货区选择
  await getAreaOptions(warehouseId)
}

/** 打开弹窗 */
const open = async (type: string, id?: number, areaId?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  
  // 获取仓库下拉选项
  await getWarehouseOptions()
  
  // 如果有传入areaId，则设置
  if (type === 'create' && areaId) {
    formData.value.areaId = Number(areaId)
    
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
  
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      const data = await RackApi.getRack(id)
      console.log('获取到的货架数据:', data)
      
      // 确保 areaId 是数字类型
      if (data.areaId) {
        data.areaId = Number(data.areaId)
      }
      
      formData.value = data
      console.log('设置的表单数据:', formData.value)
      
      // 获取货区信息，找到对应的仓库ID
      if (formData.value.areaId) {
        try {
          const areaInfo = await AreaApi.getArea(formData.value.areaId)
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
    // 确保 areaId 是数字类型
    if (formData.value.areaId) {
      formData.value.areaId = Number(formData.value.areaId)
    }
    
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
  selectedWarehouseId.value = undefined
  areaOptions.value = []
  formRef.value?.resetFields()
}
</script>
