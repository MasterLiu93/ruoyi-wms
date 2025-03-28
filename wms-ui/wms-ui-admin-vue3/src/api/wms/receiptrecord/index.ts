import request from '@/config/axios'

// 入库操作记录 VO
export interface ReceiptRecordVO {
  id: number // 记录ID
  receiptOrderId: number // 入库单ID
  receiptOrderDetailId: number // 入库单明细ID
  itemId: number // 物料ID
  locationId: number // 入库库位ID
  batchId: number // 批次ID
  count: number // 入库数量
  remark: string // 备注
}

// 入库操作记录 API
export const ReceiptRecordApi = {
  // 查询入库操作记录分页
  getReceiptRecordPage: async (params: any) => {
    return await request.get({ url: `/wms/receipt-record/page`, params })
  },

  // 查询入库操作记录详情
  getReceiptRecord: async (id: number) => {
    return await request.get({ url: `/wms/receipt-record/get?id=` + id })
  },

  // 新增入库操作记录
  createReceiptRecord: async (data: ReceiptRecordVO) => {
    return await request.post({ url: `/wms/receipt-record/create`, data })
  },

  // 修改入库操作记录
  updateReceiptRecord: async (data: ReceiptRecordVO) => {
    return await request.put({ url: `/wms/receipt-record/update`, data })
  },

  // 删除入库操作记录
  deleteReceiptRecord: async (id: number) => {
    return await request.delete({ url: `/wms/receipt-record/delete?id=` + id })
  },

  // 导出入库操作记录 Excel
  exportReceiptRecord: async (params) => {
    return await request.download({ url: `/wms/receipt-record/export-excel`, params })
  },
}