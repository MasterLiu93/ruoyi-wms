import request from '@/config/axios'

// 仓库 VO
export interface WarehouseVO {
  id: number // 仓库ID
  name: string // 仓库名称
  code: string // 仓库编码
  status: number // 状态(0-禁用 1-启用)
  remark: string // 备注
}

// 仓库 API
export const WarehouseApi = {
  // 查询仓库分页
  getWarehousePage: async (params: any) => {
    return await request.get({ url: `/wms/warehouse/page`, params })
  },

  // 查询仓库详情
  getWarehouse: async (id: number) => {
    return await request.get({ url: `/wms/warehouse/get?id=` + id })
  },

  // 新增仓库
  createWarehouse: async (data: WarehouseVO) => {
    return await request.post({ url: `/wms/warehouse/create`, data })
  },

  // 修改仓库
  updateWarehouse: async (data: WarehouseVO) => {
    return await request.put({ url: `/wms/warehouse/update`, data })
  },

  // 删除仓库
  deleteWarehouse: async (id: number) => {
    return await request.delete({ url: `/wms/warehouse/delete?id=` + id })
  },

  // 导出仓库 Excel
  exportWarehouse: async (params) => {
    return await request.download({ url: `/wms/warehouse/export-excel`, params })
  },
}
