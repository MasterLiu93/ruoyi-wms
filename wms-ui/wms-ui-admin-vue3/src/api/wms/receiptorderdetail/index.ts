import request from '@/config/axios'

// 入库单明细 VO
export interface ReceiptOrderDetailVO {
  id: number // 入库单明细ID
  receiptOrderId: number // 入库单ID
  itemId: number // 物料ID
  planCount: number // 计划数量
  realCount: number // 实际入库数量
  locationId: number // 入库库位ID
  batchId: number // 批次ID
  price: number // 入库单价
  qualityStatus: number // 质检状态(0-未检验 1-已检验)
  status: number // 状态(0-未入库 1-部分入库 2-已入库)
  remark: string // 备注
}

// 入库单明细 API
export const ReceiptOrderDetailApi = {
  // 查询入库单明细分页
  getReceiptOrderDetailPage: async (params: any) => {
    return await request.get({ url: `/wms/receipt-order-detail/page`, params })
  },

  // 查询入库单明细详情
  getReceiptOrderDetail: async (id: number) => {
    return await request.get({ url: `/wms/receipt-order-detail/get?id=` + id })
  },

  // 新增入库单明细
  createReceiptOrderDetail: async (data: ReceiptOrderDetailVO) => {
    return await request.post({ url: `/wms/receipt-order-detail/create`, data })
  },

  // 修改入库单明细
  updateReceiptOrderDetail: async (data: ReceiptOrderDetailVO) => {
    return await request.put({ url: `/wms/receipt-order-detail/update`, data })
  },

  // 删除入库单明细
  deleteReceiptOrderDetail: async (id: number) => {
    return await request.delete({ url: `/wms/receipt-order-detail/delete?id=` + id })
  },

  // 导出入库单明细 Excel
  exportReceiptOrderDetail: async (params) => {
    return await request.download({ url: `/wms/receipt-order-detail/export-excel`, params })
  },
}