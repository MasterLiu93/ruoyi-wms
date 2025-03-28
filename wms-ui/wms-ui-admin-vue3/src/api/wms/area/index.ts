import request from '@/config/axios'

// 货区 VO
export interface AreaVO {
  id: number // 货区ID
  areaCode: string // 货区编码
  areaName: string // 货区名称
  warehouseId: number // 所属仓库ID
  areaType: number // 货区类型(0-存储区 1-暂存区 2-拣货区 3-出货区)
  status: number // 状态(0-正常 1-禁用)
  remark: string // 备注
}

// 货区 API
export const AreaApi = {
  // 查询货区分页
  getAreaPage: async (params: any) => {
    return await request.get({ url: `/wms/area/page`, params })
  },

  // 查询货区详情
  getArea: async (id: number) => {
    return await request.get({ url: `/wms/area/get?id=` + id })
  },

  // 新增货区
  createArea: async (data: AreaVO) => {
    return await request.post({ url: `/wms/area/create`, data })
  },

  // 修改货区
  updateArea: async (data: AreaVO) => {
    return await request.put({ url: `/wms/area/update`, data })
  },

  // 删除货区
  deleteArea: async (id: number) => {
    return await request.delete({ url: `/wms/area/delete?id=` + id })
  },

  // 导出货区 Excel
  exportArea: async (params) => {
    return await request.download({ url: `/wms/area/export-excel`, params })
  },
}