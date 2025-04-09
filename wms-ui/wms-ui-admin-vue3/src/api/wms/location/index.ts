import request from '@/config/axios'

// 库位 VO
export interface LocationVO {
  id: number
  warehouseId?: number
  areaId?: number
  rackId?: number
  locationCode: string
  locationName: string
  locationType: number
  status: number
  remark?: string
  createTime: Date
}

// 库位 API
export const LocationApi = {
  // 查询库位分页
  getLocationPage: async (params: any) => {
    return await request.get({ url: '/wms/location/page', params })
  },

  // 查询库位详情
  getLocation: async (id: number) => {
    return await request.get({ url: '/wms/location/get?id=' + id })
  },

  // 新增库位
  createLocation: async (data: LocationVO) => {
    return await request.post({ url: '/wms/location/create', data })
  },

  // 修改库位
  updateLocation: async (data: LocationVO) => {
    return await request.put({ url: '/wms/location/update', data })
  },

  // 删除库位
  deleteLocation: async (id: number) => {
    return await request.delete({ url: '/wms/location/delete?id=' + id })
  },

  // 导出库位 Excel
  exportLocation: async (params) => {
    return await request.download({ url: '/wms/location/export-excel', params })
  },
}
