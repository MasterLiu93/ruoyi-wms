import request from '@/config/axios'

// 移库单 VO
export interface MoveOrderVO {
  id: number // 移库单ID
  moveOrderNo: string // 移库单号
  moveType: number // 移库类型(0-仓库间移库 1-库区间移库 2-库位间移库)
  fromWarehouseId: number // 源仓库ID
  toWarehouseId: number // 目标仓库ID
  fromLocationId: number // 源库位ID
  toLocationId: number // 目标库位ID
  orderStatus: number // 单据状态(0-草稿 1-待审核 2-审核通过 3-审核拒绝)
  moveStatus: number // 移库状态(0-待移库 1-部分移库 2-已完成)
  totalCount: number // 商品数量
  remark: string // 备注
}

// 移库单 API
export const MoveOrderApi = {
  // 查询移库单分页
  getMoveOrderPage: async (params: any) => {
    return await request.get({ url: `/wms/move-order/page`, params })
  },

  // 查询移库单详情
  getMoveOrder: async (id: number) => {
    return await request.get({ url: `/wms/move-order/get?id=` + id })
  },

  // 新增移库单
  createMoveOrder: async (data: MoveOrderVO) => {
    return await request.post({ url: `/wms/move-order/create`, data })
  },

  // 修改移库单
  updateMoveOrder: async (data: MoveOrderVO) => {
    return await request.put({ url: `/wms/move-order/update`, data })
  },

  // 删除移库单
  deleteMoveOrder: async (id: number) => {
    return await request.delete({ url: `/wms/move-order/delete?id=` + id })
  },

  // 导出移库单 Excel
  exportMoveOrder: async (params) => {
    return await request.download({ url: `/wms/move-order/export-excel`, params })
  },
}