import request from '@/config/axios'

// 入库单明细 VO
export interface ReceiptOrderDetailVO {
  id?: number
  receiptOrderId?: number
  itemId?: number
  itemCode?: string
  itemName?: string
  planCount?: number
  realCount?: number
  areaId?: number
  areaName?: string
  rackId?: number
  rackName?: string
  locationId?: number
  locationName?: string
  batchId?: number
  batchCode?: string
  price?: number
  qualityStatus?: number
  status?: number
  spec?: string
  unit?: string
  remark?: string
  warehouseId?: number
  warehouseName?: string
}

// 入库单明细查询参数
export interface ReceiptOrderDetailPageReqVO {
  receiptOrderId?: number
  itemId?: number
  planCount?: number
  realCount?: number
  areaId?: number
  rackId?: number
  locationId?: number
  batchId?: number
  qualityStatus?: number
  status?: number
  createTime?: Date[]
}

// 入库单明细 API
export const ReceiptOrderDetailApi = {
  /**
   * 查询入库单明细分页
   *
   * @param params 查询参数
   * @returns 入库单明细分页
   */
  getReceiptOrderDetailPage: async (params: ReceiptOrderDetailPageReqVO) => {
    return await request.get({ url: `/wms/receipt-order-detail/page`, params })
  },

  /**
   * 查询入库单明细列表（指定入库单ID）
   *
   * @param receiptOrderId 入库单ID
   * @returns 入库单明细列表
   */
  getReceiptOrderDetailList: async (receiptOrderId: number) => {
    return await request.get({ 
      url: `/wms/receipt-order-detail/list-by-order-id`, 
      params: { receiptOrderId } 
    })
  },

  /**
   * 获取入库单明细详情
   *
   * @param id 明细ID
   * @returns 入库单明细详情
   */
  getReceiptOrderDetail: async (id: number) => {
    return await request.get({ url: `/wms/receipt-order-detail/get?id=` + id })
  },

  /**
   * 创建入库单明细
   * 
   * @param data 入库单明细信息
   * @returns 创建结果
   */
  createReceiptOrderDetail: async (data: ReceiptOrderDetailVO) => {
    return await request.post({ url: `/wms/receipt-order-detail/create`, data })
  },

  /**
   * 更新入库单明细
   * 
   * @param data 入库单明细信息
   * @returns 更新结果
   */
  updateReceiptOrderDetail: async (data: ReceiptOrderDetailVO) => {
    return await request.put({ url: `/wms/receipt-order-detail/update`, data })
  },

  /**
   * 删除入库单明细
   * 
   * @param id 明细ID
   * @returns 删除结果
   */
  deleteReceiptOrderDetail: async (id: number) => {
    return await request.delete({ url: `/wms/receipt-order-detail/delete?id=` + id })
  },

  /**
   * 导出入库单明细Excel
   * 
   * @param params 查询参数
   * @returns 
   */
  exportReceiptOrderDetail: async (params: ReceiptOrderDetailPageReqVO) => {
    return await request.download({ url: `/wms/receipt-order-detail/export-excel`, params })
  },

  /**
   * 批量删除入库单明细
   * 
   * @param ids 明细ID数组
   * @returns 删除结果
   */
  batchDeleteReceiptOrderDetail: async (ids: number[]) => {
    return await request.delete({ 
      url: `/wms/receipt-order-detail/batch-delete`, 
      data: ids
    })
  },

  /**
   * 导入入库单明细Excel数据
   * 
   * @param data 表单数据
   * @returns 导入结果
   */
  importExcel: async (data: FormData) => {
    return await request.upload({ url: `/wms/receipt-order-detail/import-excel`, data })
  }
}

// 导出增强版Excel模板
export const exportEnhancedExcelTemplateApi = (warehouseId?: number) => {
  const params = warehouseId ? { warehouseId } : {}
  return request.download({
    url: '/wms/receipt-order-detail/export-enhanced-template',
    params
  })
}

// 导入Excel API函数
export const importExcelApi = (file: File, receiptOrderId: number, warehouseId: number) => {
  const formData = new FormData()
  formData.append('file', file)
  formData.append('receiptOrderId', receiptOrderId.toString())
  formData.append('warehouseId', warehouseId.toString())
  return request.post({ 
    url: '/wms/receipt-order-detail/import-excel', 
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}
