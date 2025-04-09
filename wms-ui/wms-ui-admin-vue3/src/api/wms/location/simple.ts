import request from '@/config/axios'

// 简单库位 VO
export interface SimpleLocationVO {
  id: number // 库位ID
  code: string // 库位编码
  name: string // 库位名称
  warehouseId: number // 仓库ID
}

// 简单库位 API
export const SimpleLocationApi = {
  // 获取简单库位列表
  getSimpleLocationList: async (params: any) => {
    return await request.get({ url: `/wms/location/simple-list`, params })
  }
} 