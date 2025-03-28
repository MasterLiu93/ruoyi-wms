import request from '@/config/axios'

// 移库操作记录 VO
export interface MoveRecordVO {
  id: number // 记录ID
  moveOrderId: number // 移库单ID
  moveOrderDetailId: number // 移库单明细ID
  itemId: number // 物料ID
  batchId: number // 批次ID
  fromLocationId: number // 源库位ID
  toLocationId: number // 目标库位ID
  count: number // 移动数量
  remark: string // 备注
}

// 移库操作记录 API
export const MoveRecordApi = {
  // 查询移库操作记录分页
  getMoveRecordPage: async (params: any) => {
    return await request.get({ url: `/wms/move-record/page`, params })
  },

  // 查询移库操作记录详情
  getMoveRecord: async (id: number) => {
    return await request.get({ url: `/wms/move-record/get?id=` + id })
  },

  // 新增移库操作记录
  createMoveRecord: async (data: MoveRecordVO) => {
    return await request.post({ url: `/wms/move-record/create`, data })
  },

  // 修改移库操作记录
  updateMoveRecord: async (data: MoveRecordVO) => {
    return await request.put({ url: `/wms/move-record/update`, data })
  },

  // 删除移库操作记录
  deleteMoveRecord: async (id: number) => {
    return await request.delete({ url: `/wms/move-record/delete?id=` + id })
  },

  // 导出移库操作记录 Excel
  exportMoveRecord: async (params) => {
    return await request.download({ url: `/wms/move-record/export-excel`, params })
  },
}