import request from '@/config/axios'

// 库存盘点单 VO
export interface InventoryCheckVO {
  id: number // 盘点单ID
  checkNo: string // 盘点单号
  warehouseId: number // 仓库ID
  checkType: number // 盘点类型(0-全部盘点 1-部分盘点)
  checkStatus: number // 盘点状态(0-新建 1-盘点中 2-已完成)
  totalCount: number // 盘点总数
  checkedCount: number // 已盘点数
  differenceCount: number // 差异数
  remark: string // 备注
}

// 盘点计划 VO
export interface InventoryCheckPlanVO {
  warehouseId: number // 仓库ID
  checkType: number // 盘点类型(0-全部盘点 1-部分盘点)
  locationIds?: number[] // 库位ID列表
  itemIds?: number[] // 物料ID列表
  remark?: string // 备注
}

// 盘点结果提交 VO
export interface InventoryCheckResultVO {
  detailId: number // 盘点明细ID
  checkCount: number // 盘点数量
  remark?: string // 备注
}

// 盘点明细提交 VO
export interface InventoryCheckDetailSubmitVO {
  detailId: number // 盘点明细ID
  checkCount: number // 盘点数量
  remark?: string // 备注
}

// 批量盘点结果提交 VO
export interface InventoryCheckBatchResultVO {
  checkId: number // 盘点单ID
  checkDetails: InventoryCheckDetailSubmitVO[] // 盘点明细列表
}

// 盘点完成 VO
export interface InventoryCheckCompleteVO {
  id: number // 盘点单ID
  autoAdjust?: boolean // 是否自动调整库存差异
}

// 库存差异调整 VO
export interface InventoryCheckAdjustVO {
  id: number // 盘点单ID
  remark?: string // 备注
}

// 盘点取消 VO
export interface InventoryCheckCancelVO {
  id: number // 盘点单ID
  remark?: string // 备注
}

// 库存盘点单 API
export const InventoryCheckApi = {
  // 查询库存盘点单分页
  getInventoryCheckPage: async (params: any) => {
    return await request.get({ url: `/wms/inventory-check/page`, params })
  },

  // 查询库存盘点单详情
  getInventoryCheck: async (id: number) => {
    return await request.get({ url: `/wms/inventory-check/get?id=` + id })
  },

  // 新增库存盘点单
  createInventoryCheck: async (data: InventoryCheckVO) => {
    return await request.post({ url: `/wms/inventory-check/create`, data })
  },

  // 修改库存盘点单
  updateInventoryCheck: async (data: InventoryCheckVO) => {
    return await request.put({ url: `/wms/inventory-check/update`, data })
  },

  // 删除库存盘点单
  deleteInventoryCheck: async (id: number) => {
    return await request.delete({ url: `/wms/inventory-check/delete?id=` + id })
  },

  // 导出库存盘点单 Excel
  exportInventoryCheck: async (params) => {
    return await request.download({ url: `/wms/inventory-check/export-excel`, params })
  },
  
  // 生成盘点计划
  generateCheckPlan: async (data: InventoryCheckPlanVO) => {
    return await request.post({ url: `/wms/inventory-check/generate-plan`, data })
  },
  
  // 开始盘点
  startCheck: async (id: number) => {
    return await request.post({ url: `/wms/inventory-check/start?id=` + id })
  },
  
  // 提交盘点结果
  submitCheckResult: async (data: InventoryCheckResultVO) => {
    return await request.post({ url: `/wms/inventory-check/submit-result`, data })
  },
  
  // 批量提交盘点结果
  batchSubmitCheckResult: async (data: InventoryCheckBatchResultVO) => {
    return await request.post({ url: `/wms/inventory-check/batch-submit-result`, data })
  },
  
  // 完成盘点
  completeCheck: async (data: InventoryCheckCompleteVO) => {
    return await request.post({ url: `/wms/inventory-check/complete`, data })
  },
  
  // 调整库存差异
  adjustInventory: async (data: InventoryCheckAdjustVO) => {
    return await request.post({ url: `/wms/inventory-check/adjust`, data })
  },
  
  // 取消盘点
  cancelCheck: async (data: InventoryCheckCancelVO) => {
    return await request.post({ url: `/wms/inventory-check/cancel`, data })
  },
  
  // 获取盘点进度
  getCheckProgress: async (id: number) => {
    return await request.get({ url: `/wms/inventory-check/progress?id=` + id })
  }
}
