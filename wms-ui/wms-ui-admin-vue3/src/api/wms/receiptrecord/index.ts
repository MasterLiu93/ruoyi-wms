import request from '@/config/axios'

// 入库记录 VO
export interface ReceiptRecordVO {
  id?: number // 入库记录ID
  receiptOrderId: number // 入库单ID  
  receiptOrderNo?: string // 入库单号
  receiptOrderDetailId: number // 入库单明细ID
  receiptType?: number // 入库类型
  warehouseId?: number // 仓库ID
  warehouseName?: string // 仓库名称
  areaId?: number // 货区ID
  areaName?: string // 货区名称
  rackId?: number // 货架ID
  rackName?: string // 货架名称
  locationId: number // 库位ID
  locationName?: string // 库位名称
  batchId?: number // 批次ID
  batchCode?: string // 批次号
  itemId?: number // 物料ID
  itemCode?: string // 物料编码
  itemName?: string // 物料名称
  spec?: string // 规格
  unit?: string // 单位
  count: number // 入库数量
  supplierId?: number // 供应商ID
  supplierName?: string // 供应商名称
  receiptTime?: Date // 入库时间
  operator?: string // 操作人
  remark?: string // 备注
}

// 入库记录请求VO
export interface ReceiptRecordCreateReqVO {
  receiptOrderId: number // 入库单ID
  receiptOrderDetailId: number // 入库单明细ID
  rackId?: number // 货架ID
  locationId: number // 库位ID
  batchCode?: string // 批次号
  count: number // 入库数量
  remark?: string // 备注
}

// 入库记录 API
export const ReceiptRecordApi = {
  // 查询入库记录分页
  getReceiptRecordPage: async (params: any) => {
    return await request.get({ url: `/wms/receipt-record/page`, params })
  },

  // 查询入库记录详情
  getReceiptRecord: async (id: number) => {
    return await request.get({ url: `/wms/receipt-record/get?id=` + id })
  },

  // 新增入库记录
  createReceiptRecord: async (data: ReceiptRecordCreateReqVO) => {
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

  // 导出入库记录 Excel
  exportReceiptRecord: async (params) => {
    return await request.download({ url: `/wms/receipt-record/export-excel`, params })
  },
}
