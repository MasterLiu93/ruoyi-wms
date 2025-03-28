import request from '@/config/axios'

// 出库操作记录 VO
export interface ShipmentRecordVO {
  id: number // 记录ID
  shipmentOrderId: number // 出库单ID
  shipmentOrderDetailId: number // 出库单明细ID
  itemId: number // 物料ID
  locationId: number // 出库库位ID
  batchId: number // 批次ID
  count: number // 出库数量
  remark: string // 备注
}

// 出库操作记录 API
export const ShipmentRecordApi = {
  // 查询出库操作记录分页
  getShipmentRecordPage: async (params: any) => {
    return await request.get({ url: `/wms/shipment-record/page`, params })
  },

  // 查询出库操作记录详情
  getShipmentRecord: async (id: number) => {
    return await request.get({ url: `/wms/shipment-record/get?id=` + id })
  },

  // 新增出库操作记录
  createShipmentRecord: async (data: ShipmentRecordVO) => {
    return await request.post({ url: `/wms/shipment-record/create`, data })
  },

  // 修改出库操作记录
  updateShipmentRecord: async (data: ShipmentRecordVO) => {
    return await request.put({ url: `/wms/shipment-record/update`, data })
  },

  // 删除出库操作记录
  deleteShipmentRecord: async (id: number) => {
    return await request.delete({ url: `/wms/shipment-record/delete?id=` + id })
  },

  // 导出出库操作记录 Excel
  exportShipmentRecord: async (params) => {
    return await request.download({ url: `/wms/shipment-record/export-excel`, params })
  },
}