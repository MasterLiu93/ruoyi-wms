import request from '@/config/axios'

// 库存 VO
export interface InventoryVO {
  id: number // 库存ID
  warehouseId: number // 仓库ID
  locationId: number // 库位ID
  itemId: number // 物料ID
  stockCount: number // 库存数量
  availableCount: number // 可用数量
  lockedCount: number // 锁定数量
  status: number // 状态(0-正常 1-禁用)
  remark: string // 备注
}

// 库存 API
export const InventoryApi = {
  // 查询库存分页
  getInventoryPage: async (params: any) => {
    return await request.get({ url: `/wms/inventory/page`, params })
  },

  // 查询库存详情
  getInventory: async (id: number) => {
    return await request.get({ url: `/wms/inventory/get?id=` + id })
  },

  // 新增库存
  createInventory: async (data: InventoryVO) => {
    return await request.post({ url: `/wms/inventory/create`, data })
  },

  // 修改库存
  updateInventory: async (data: InventoryVO) => {
    return await request.put({ url: `/wms/inventory/update`, data })
  },

  // 删除库存
  deleteInventory: async (id: number) => {
    return await request.delete({ url: `/wms/inventory/delete?id=` + id })
  },

  // 导出库存 Excel
  exportInventory: async (params) => {
    return await request.download({ url: `/wms/inventory/export-excel`, params })
  },
}