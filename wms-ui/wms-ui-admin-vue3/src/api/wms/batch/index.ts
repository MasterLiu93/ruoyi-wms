import request from '@/config/axios'

// 批次信息 VO
export interface BatchVO {
  id: number // 批次ID
  batchNo: string // 批次号
  itemId: number // 物料ID
  warehouseId: number // 仓库ID
  supplierId: number // 供应商ID
  productionDate: Date // 生产日期
  expiryDate: Date // 过期日期
  batchAttr1: string // 批次属性1
  batchAttr2: string // 批次属性2
  batchAttr3: string // 批次属性3
  batchAttr4: string // 批次属性4
  totalCount: number // 入库总数量
  availableCount: number // 可用数量
  lockedCount: number // 锁定数量
  status: number // 状态(0-正常 1-冻结 2-禁用)
  remark: string // 备注
}

// 批次信息 API
export const BatchApi = {
  // 查询批次信息分页
  getBatchPage: async (params: any) => {
    return await request.get({ url: `/wms/batch/page`, params })
  },

  // 查询批次信息详情
  getBatch: async (id: number) => {
    return await request.get({ url: `/wms/batch/get?id=` + id })
  },

  // 新增批次信息
  createBatch: async (data: BatchVO) => {
    return await request.post({ url: `/wms/batch/create`, data })
  },

  // 修改批次信息
  updateBatch: async (data: BatchVO) => {
    return await request.put({ url: `/wms/batch/update`, data })
  },

  // 删除批次信息
  deleteBatch: async (id: number) => {
    return await request.delete({ url: `/wms/batch/delete?id=` + id })
  },

  // 导出批次信息 Excel
  exportBatch: async (params) => {
    return await request.download({ url: `/wms/batch/export-excel`, params })
  },
}