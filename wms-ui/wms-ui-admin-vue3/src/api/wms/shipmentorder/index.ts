import request from '@/config/axios'

// 出库单 VO
export interface ShipmentOrderVO {
  id: number // 出库单ID
  shipmentOrderNo: string // 出库单号
  shipmentType: number // 出库类型(0-销售出库 1-生产出库 2-调拨出库 3-其他出库)
  customerId: number // 客户ID
  warehouseId: number // 仓库ID
  orderStatus: number // 单据状态(0-草稿 1-待审核 2-审核通过 3-审核拒绝)
  shipmentStatus: number // 出库状态(0-待出库 1-部分出库 2-已完成)
  expectTime: Date // 预计出库时间
  completeTime: Date // 实际完成时间
  totalCount: number // 商品数量
  totalAmount: number // 商品金额
  remark: string // 备注
}

// 出库单 API
export const ShipmentOrderApi = {
  // 查询出库单分页
  getShipmentOrderPage: async (params: any) => {
    return await request.get({ url: `/wms/shipment-order/page`, params })
  },

  // 查询出库单详情
  getShipmentOrder: async (id: number) => {
    return await request.get({ url: `/wms/shipment-order/get?id=` + id })
  },

  // 新增出库单
  createShipmentOrder: async (data: ShipmentOrderVO) => {
    return await request.post({ url: `/wms/shipment-order/create`, data })
  },

  // 修改出库单
  updateShipmentOrder: async (data: ShipmentOrderVO) => {
    return await request.put({ url: `/wms/shipment-order/update`, data })
  },

  // 删除出库单
  deleteShipmentOrder: async (id: number) => {
    return await request.delete({ url: `/wms/shipment-order/delete?id=` + id })
  },

  // 导出出库单 Excel
  exportShipmentOrder: async (params) => {
    return await request.download({ url: `/wms/shipment-order/export-excel`, params })
  },
}