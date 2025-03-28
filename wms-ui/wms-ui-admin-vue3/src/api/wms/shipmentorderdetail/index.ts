import request from '@/config/axios'

// 出库单明细 VO
export interface ShipmentOrderDetailVO {
  id: number // 出库单明细ID
  shipmentOrderId: number // 出库单ID
  itemId: number // 物料ID
  planCount: number // 计划数量
  realCount: number // 实际出库数量
  locationId: number // 出库库位ID
  batchId: number // 批次ID
  price: number // 出库单价
  status: number // 状态(0-未出库 1-部分出库 2-已出库)
  remark: string // 备注
}

// 出库单明细 API
export const ShipmentOrderDetailApi = {
  // 查询出库单明细分页
  getShipmentOrderDetailPage: async (params: any) => {
    return await request.get({ url: `/wms/shipment-order-detail/page`, params })
  },

  // 查询出库单明细详情
  getShipmentOrderDetail: async (id: number) => {
    return await request.get({ url: `/wms/shipment-order-detail/get?id=` + id })
  },

  // 新增出库单明细
  createShipmentOrderDetail: async (data: ShipmentOrderDetailVO) => {
    return await request.post({ url: `/wms/shipment-order-detail/create`, data })
  },

  // 修改出库单明细
  updateShipmentOrderDetail: async (data: ShipmentOrderDetailVO) => {
    return await request.put({ url: `/wms/shipment-order-detail/update`, data })
  },

  // 删除出库单明细
  deleteShipmentOrderDetail: async (id: number) => {
    return await request.delete({ url: `/wms/shipment-order-detail/delete?id=` + id })
  },

  // 导出出库单明细 Excel
  exportShipmentOrderDetail: async (params) => {
    return await request.download({ url: `/wms/shipment-order-detail/export-excel`, params })
  },
}