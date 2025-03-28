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
}