import request from '@/config/axios'

// 库存盘点明细 VO
export interface InventoryCheckDetailVO {
  id: number // 盘点明细ID
  checkId: number // 盘点单ID
  itemId: number // 物料ID
  locationId: number // 库位ID
  bookCount: number // 账面数量
  checkCount: number // 盘点数量
  differenceCount: number // 差异数量
  checkStatus: number // 盘点状态(0-未盘点 1-已盘点)
  remark: string // 备注
}

// 库存盘点明细 API
export const InventoryCheckDetailApi = {
  // 查询库存盘点明细分页
  getInventoryCheckDetailPage: async (params: any) => {
    return await request.get({ url: `/wms/inventory-check-detail/page`, params })
  },

  // 查询库存盘点明细详情
  getInventoryCheckDetail: async (id: number) => {
    return await request.get({ url: `/wms/inventory-check-detail/get?id=` + id })
  },

  // 新增库存盘点明细
  createInventoryCheckDetail: async (data: InventoryCheckDetailVO) => {
    return await request.post({ url: `/wms/inventory-check-detail/create`, data })
  },

  // 修改库存盘点明细
  updateInventoryCheckDetail: async (data: InventoryCheckDetailVO) => {
    return await request.put({ url: `/wms/inventory-check-detail/update`, data })
  },

  // 删除库存盘点明细
  deleteInventoryCheckDetail: async (id: number) => {
    return await request.delete({ url: `/wms/inventory-check-detail/delete?id=` + id })
  },

  // 导出库存盘点明细 Excel
  exportInventoryCheckDetail: async (params) => {
    return await request.download({ url: `/wms/inventory-check-detail/export-excel`, params })
  },
}