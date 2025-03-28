import request from '@/config/axios'

// 库存移动记录 VO
export interface InventoryMovementVO {
  id: number // 移动ID
  movementType: number // 移动类型(0-入库 1-出库 2-库存调整)
  movementNo: string // 移动单号
  warehouseId: number // 仓库ID
  locationId: number // 库位ID
  itemId: number // 物料ID
  count: number // 移动数量
  beforeCount: number // 移动前数量
  afterCount: number // 移动后数量
  businessType: string // 业务类型
  businessId: number // 业务单ID
  businessDetailId: number // 业务明细ID
  remark: string // 备注
}

// 库存移动记录 API
export const InventoryMovementApi = {
  // 查询库存移动记录分页
  getInventoryMovementPage: async (params: any) => {
    return await request.get({ url: `/wms/inventory-movement/page`, params })
  },

  // 查询库存移动记录详情
  getInventoryMovement: async (id: number) => {
    return await request.get({ url: `/wms/inventory-movement/get?id=` + id })
  },

  // 新增库存移动记录
  createInventoryMovement: async (data: InventoryMovementVO) => {
    return await request.post({ url: `/wms/inventory-movement/create`, data })
  },

  // 修改库存移动记录
  updateInventoryMovement: async (data: InventoryMovementVO) => {
    return await request.put({ url: `/wms/inventory-movement/update`, data })
  },

  // 删除库存移动记录
  deleteInventoryMovement: async (id: number) => {
    return await request.delete({ url: `/wms/inventory-movement/delete?id=` + id })
  },

  // 导出库存移动记录 Excel
  exportInventoryMovement: async (params) => {
    return await request.download({ url: `/wms/inventory-movement/export-excel`, params })
  },
}