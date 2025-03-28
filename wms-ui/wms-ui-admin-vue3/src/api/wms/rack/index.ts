import request from '@/config/axios'

// 货架 VO
export interface RackVO {
  id: number // 货架ID
  rackCode: string // 货架编码
  rackName: string // 货架名称
  areaId: number // 所属货区ID
  rackType: number // 货架类型(0-标准货架 1-重型货架 2-悬臂货架)
  status: number // 状态(0-正常 1-禁用)
  remark: string // 备注
}

// 货架 API
export const RackApi = {
  // 查询货架分页
  getRackPage: async (params: any) => {
    return await request.get({ url: `/wms/rack/page`, params })
  },

  // 查询货架详情
  getRack: async (id: number) => {
    return await request.get({ url: `/wms/rack/get?id=` + id })
  },

  // 新增货架
  createRack: async (data: RackVO) => {
    return await request.post({ url: `/wms/rack/create`, data })
  },

  // 修改货架
  updateRack: async (data: RackVO) => {
    return await request.put({ url: `/wms/rack/update`, data })
  },

  // 删除货架
  deleteRack: async (id: number) => {
    return await request.delete({ url: `/wms/rack/delete?id=` + id })
  },

  // 导出货架 Excel
  exportRack: async (params) => {
    return await request.download({ url: `/wms/rack/export-excel`, params })
  },
}