import request from '@/config/axios'

// 入库单 VO
export interface ReceiptOrderVO {
  id: number // 入库单ID
  receiptOrderNo: string // 入库单号
  receiptType: number // 入库类型(0-采购入库 1-生产入库 2-退货入库 3-调拨入库)
  supplierId: number // 供应商ID
  warehouseId: number // 仓库ID
  orderStatus: number // 单据状态(0-草稿 1-待审核 2-审核通过 3-审核拒绝)
  receiptStatus: number // 入库状态(0-待入库 1-部分入库 2-已完成)
  expectTime: Date // 预计到货时间
  arrivalTime: Date // 实际到货时间
  totalCount: number // 商品数量
  totalAmount: number // 商品金额
  remark: string // 备注
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

  // 修改入库单
  updateReceiptOrder: async (data: ReceiptOrderVO) => {
    return await request.put({ url: `/wms/receipt-order/update`, data })
  },

  // 删除入库单
  deleteReceiptOrder: async (id: number) => {
    return await request.delete({ url: `/wms/receipt-order/delete?id=` + id })
  },

  // 导出入库单 Excel
  exportReceiptOrder: async (params) => {
    return await request.download({ url: `/wms/receipt-order/export-excel`, params })
  },
}