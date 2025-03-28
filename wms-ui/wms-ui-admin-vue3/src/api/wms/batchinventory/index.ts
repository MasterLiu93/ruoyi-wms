import request from '@/config/axios'

// 批次库存 VO
export interface BatchInventoryVO {
  id: number // 批次库存ID
  batchId: number // 批次ID
  itemId: number // 物料ID
  warehouseId: number // 仓库ID
  locationId: number // 库位ID
  stockCount: number // 库存数量
  availableCount: number // 可用数量
  lockedCount: number // 锁定数量
  status: number // 状态(0-正常 1-冻结)
  remark: string // 备注
}

// 批次库存 API
export const BatchInventoryApi = {
  // 查询批次库存分页
  getBatchInventoryPage: async (params: any) => {
    return await request.get({ url: `/wms/batch-inventory/page`, params })
  },

  // 查询批次库存详情
  getBatchInventory: async (id: number) => {
    return await request.get({ url: `/wms/batch-inventory/get?id=` + id })
  },

  // 新增批次库存
  createBatchInventory: async (data: BatchInventoryVO) => {
    return await request.post({ url: `/wms/batch-inventory/create`, data })
  },

  // 修改批次库存
  updateBatchInventory: async (data: BatchInventoryVO) => {
    return await request.put({ url: `/wms/batch-inventory/update`, data })
  },

  // 删除批次库存
  deleteBatchInventory: async (id: number) => {
    return await request.delete({ url: `/wms/batch-inventory/delete?id=` + id })
  },

  // 导出批次库存 Excel
  exportBatchInventory: async (params) => {
    return await request.download({ url: `/wms/batch-inventory/export-excel`, params })
  },
}