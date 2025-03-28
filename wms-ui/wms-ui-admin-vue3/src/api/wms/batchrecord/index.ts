import request from '@/config/axios'

// 批次操作记录 VO
export interface BatchRecordVO {
  id: number // 记录ID
  batchId: number // 批次ID
  movementType: number // 移动类型(0-入库 1-出库 2-库存调整)
  movementId: number // 移动记录ID
  count: number // 操作数量
  beforeCount: number // 操作前数量
  afterCount: number // 操作后数量
  remark: string // 备注
}

// 批次操作记录 API
export const BatchRecordApi = {
  // 查询批次操作记录分页
  getBatchRecordPage: async (params: any) => {
    return await request.get({ url: `/wms/batch-record/page`, params })
  },

  // 查询批次操作记录详情
  getBatchRecord: async (id: number) => {
    return await request.get({ url: `/wms/batch-record/get?id=` + id })
  },

  // 新增批次操作记录
  createBatchRecord: async (data: BatchRecordVO) => {
    return await request.post({ url: `/wms/batch-record/create`, data })
  },

  // 修改批次操作记录
  updateBatchRecord: async (data: BatchRecordVO) => {
    return await request.put({ url: `/wms/batch-record/update`, data })
  },

  // 删除批次操作记录
  deleteBatchRecord: async (id: number) => {
    return await request.delete({ url: `/wms/batch-record/delete?id=` + id })
  },

  // 导出批次操作记录 Excel
  exportBatchRecord: async (params) => {
    return await request.download({ url: `/wms/batch-record/export-excel`, params })
  },
}