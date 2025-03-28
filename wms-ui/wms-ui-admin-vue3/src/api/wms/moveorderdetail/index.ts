import request from '@/config/axios'

// 移库单明细 VO
export interface MoveOrderDetailVO {
  id: number // 移库单明细ID
  moveOrderId: number // 移库单ID
  itemId: number // 物料ID
  batchId: number // 批次ID
  planCount: number // 计划数量
  realCount: number // 实际移库数量
  status: number // 状态(0-未移库 1-部分移库 2-已移库)
  remark: string // 备注
}

// 移库单明细 API
export const MoveOrderDetailApi = {
  // 查询移库单明细分页
  getMoveOrderDetailPage: async (params: any) => {
    return await request.get({ url: `/wms/move-order-detail/page`, params })
  },

  // 查询移库单明细详情
  getMoveOrderDetail: async (id: number) => {
    return await request.get({ url: `/wms/move-order-detail/get?id=` + id })
  },

  // 新增移库单明细
  createMoveOrderDetail: async (data: MoveOrderDetailVO) => {
    return await request.post({ url: `/wms/move-order-detail/create`, data })
  },

  // 修改移库单明细
  updateMoveOrderDetail: async (data: MoveOrderDetailVO) => {
    return await request.put({ url: `/wms/move-order-detail/update`, data })
  },

  // 删除移库单明细
  deleteMoveOrderDetail: async (id: number) => {
    return await request.delete({ url: `/wms/move-order-detail/delete?id=` + id })
  },

  // 导出移库单明细 Excel
  exportMoveOrderDetail: async (params) => {
    return await request.download({ url: `/wms/move-order-detail/export-excel`, params })
  },
}