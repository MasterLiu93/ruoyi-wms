import request from '@/config/axios'

// 入库单 VO
export interface ReceiptOrderVO {
  id?: number // 入库单ID
  receiptOrderNo: string // 入库单号
  receiptType: number // 入库类型(0-采购入库 1-生产入库 2-退货入库 3-调拨入库)
  supplierId?: number // 供应商ID
  supplierName?: string // 供应商名称
  warehouseId?: number // 仓库ID
  orderStatus: number // 单据状态(0-草稿 1-待审核 2-审核通过 3-审核拒绝 4-已取消)
  receiptStatus: number // 入库状态(0-待入库 1-部分入库 2-已完成)
  expectTime?: Date // 预计到货时间
  arrivalTime?: Date // 实际到货时间
  completionTime?: Date // 完成时间
  totalCount?: number // 商品数量
  totalAmount?: number // 商品金额
  remark?: string // 备注
  details?: any[] // 明细列表
}

// 入库单审核请求VO
export interface ReceiptOrderApproveReqVO {
  id: number // 入库单ID
  approved: boolean // 是否通过
  remark: string // 备注
}

// 入库单明细操作请求VO
export interface ReceiptDetailOperationReqVO {
  detailId: number // 明细ID
  locationId: number // 库位ID
  batchId?: number // 批次ID
  count: number // 数量
  remark?: string // 备注
}

// 入库单 API
export const ReceiptOrderApi = {
  // 查询入库单分页
  getReceiptOrderPage: async (params: any) => {
    return await request.get({ url: `/wms/receipt-order/page`, params })
  },

  // 查询入库单详情
  getReceiptOrder: async (id: number) => {
    return await request.get({ url: `/wms/receipt-order/get?id=` + id })
  },

  // 新增入库单
  createReceiptOrder: async (data: ReceiptOrderVO) => {
    return await request.post({ url: `/wms/receipt-order/create`, data })
  },

  // 新增入库单和明细（一次性保存）
  createReceiptOrderWithDetails: async (data: ReceiptOrderVO) => {
    return await request.post({ url: `/wms/receipt-order/create-with-details`, data })
  },

  // 修改入库单
  updateReceiptOrder: async (data: ReceiptOrderVO) => {
    return await request.put({ url: `/wms/receipt-order/update`, data })
  },

  // 修改入库单和明细（一次性保存）
  updateReceiptOrderWithDetails: async (data: ReceiptOrderVO) => {
    return await request.put({ url: `/wms/receipt-order/update-with-details`, data })
  },

  // 提交入库单并直接入库（一步到位）
  submitAndReceiptOrder: async (data: ReceiptOrderVO) => {
    return await request.post({ url: `/wms/receipt-order/submit-and-receipt`, data })
  },

  // 删除入库单
  deleteReceiptOrder: async (id: number) => {
    return await request.delete({ url: `/wms/receipt-order/delete?id=` + id })
  },

  // 导出入库单 Excel
  exportReceiptOrder: async (params) => {
    return await request.download({ url: `/wms/receipt-order/export-excel`, params })
  },

  // 提交入库单审核
  submitReceiptOrder: async (id: number) => {
    return await request.post({ url: `/wms/receipt-order/submit?id=` + id })
  },

  // 审核入库单
  approveReceiptOrder: async (data: ReceiptOrderApproveReqVO) => {
    return await request.post({ url: `/wms/receipt-order/approve`, data })
  },

  // 执行明细入库操作
  executeReceiptDetail: async (data: ReceiptDetailOperationReqVO) => {
    return await request.post({ url: `/wms/receipt-order/receipt`, data })
  },

  // 批量执行入库操作
  batchExecuteReceipt: async (id: number) => {
    return await request.post({ url: `/wms/receipt-order/batch-receipt?id=` + id })
  },

  // 完成入库单
  completeReceiptOrder: async (id: number) => {
    return await request.post({ url: `/wms/receipt-order/complete?id=` + id })
  },

  // 取消入库单
  cancelReceiptOrder: async (id: number) => {
    return await request.post({ url: `/wms/receipt-order/cancel?id=` + id })
  }
}

/**
 * 提交入库单
 * 
 * @param id 入库单ID
 * @returns 提交结果
 */
export function submitReceiptOrder(id: number) {
  return request.post({
    url: `/wms/receipt-order/${id}/submit`
  })
}

/**
 * 批量执行入库操作
 * 
 * @param id 入库单ID
 * @returns 批量入库结果
 */
export function batchExecuteReceipt(id: number) {
  return request.post({
    url: `/wms/receipt-order/batch-receipt?id=${id}`
  })
}
