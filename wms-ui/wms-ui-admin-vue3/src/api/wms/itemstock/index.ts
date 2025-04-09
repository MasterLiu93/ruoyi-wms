import request from '@/config/axios'

export interface ItemStockVO {
  id: number
  warehouseId: number
  warehouseName: string
  locationId: number
  locationName: string
  itemId: number
  itemCode: string
  itemName: string
  spec: string
  unit: string
  itemType: number
  stockCount: number
  availableCount: number
  lockedCount: number
  status: number
  remark?: string
  createTime: Date
}

// 查询库存列表
export const ItemStockApi = {
  // 查询库存分页
  getItemStockPage: async (params: any) => {
    return await request.get({ url: '/wms/inventory/page', params })
  },

  // 查询库存列表
  getItemStockList: async (params: any) => {
    return await request.get({ url: '/wms/inventory/list', params })
  },

  // 查询库存详情
  getItemStock: async (id: number) => {
    return await request.get({ url: '/wms/inventory/get?id=' + id })
  },

  // 导出库存 Excel
  exportItemStock: async (params) => {
    return await request.download({ url: '/wms/inventory/export-excel', params })
  }
} 