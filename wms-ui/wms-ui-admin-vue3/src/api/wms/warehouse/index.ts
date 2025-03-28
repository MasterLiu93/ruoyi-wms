import request from '@/config/axios'

// 仓库 VO
export interface WarehouseVO {
  id: number // 仓库ID
  warehouseCode: string // 仓库编码
  warehouseName: string // 仓库名称
  warehouseType: number // 仓库类型(0-原材料 1-半成品 2-成品 3-退货)
  area: number // 面积(平方米)
  address: string // 地址
  chargePerson: string // 负责人
  phone: string // 联系电话
  status: number // 状态(0-正常 1-禁用)
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