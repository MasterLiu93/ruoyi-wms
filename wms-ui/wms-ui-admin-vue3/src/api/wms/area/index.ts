import request from '@/config/axios'

// 货区 VO
export interface AreaVO {
  id: number // 货区ID
  areaCode: string // 货区编码
  areaName: string // 货区名称
  warehouseId: number // 所属仓库ID
  warehouseName?: string
  remark?: string
  createTime?: Date
}

export interface AreaPageReqVO extends PageParam {
  areaCode?: string
  areaName?: string
  warehouseId?: number
  createTime?: Date[]
}

// 货区 API
export const AreaApi = {
  // 查询货区分页
  getAreaPage: async (params: AreaPageReqVO) => {
    return await request.get<PageResult<AreaVO>>({ url: `/wms/area/page`, params })
  },

  // 查询货区详情
  getArea: async (id: number) => {
    return await request.get<AreaVO>({ url: `/wms/area/get?id=${id}` })
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
    return await request.delete({ url: `/wms/area/delete?id=${id}` })
  },

  // 导出货区 Excel
  exportArea: async (params: AreaPageReqVO) => {
    return await request.download({ url: `/wms/area/export-excel`, params })
  },

  // 获取简单货区列表
  getSimpleAreaList: async () => {
    return await request.get<AreaVO[]>({ url: `/wms/area/simple-list` })
  },

  // 按仓库ID获取货区列表
  getAreasByWarehouseId: async (warehouseId: number) => {
    return await request.get<AreaVO[]>({ url: `/wms/area/list-by-warehouse?warehouseId=${warehouseId}` })
  }
}
