import request from '@/config/axios'

// 货架 VO
export interface RackVO {
  id: number
  rackCode: string
  rackName: string
  areaId: number
  areaName?: string
  warehouseId: number
  warehouseName?: string
  remark?: string
  createTime?: Date
}

export interface RackPageReqVO extends PageParam {
  rackCode?: string
  rackName?: string
  areaId?: number
  warehouseId?: number
  createTime?: Date[]
}

// 货架 API
export const RackApi = {
  // 创建货架
  createRack(data: RackVO) {
    return request.post({ url: '/wms/rack/create', data })
  },
  // 更新货架
  updateRack(data: RackVO) {
    return request.put({ url: '/wms/rack/update', data })
  },
  // 删除货架
  deleteRack(id: number) {
    return request.delete({ url: `/wms/rack/delete?id=${id}` })
  },
  // 获取货架
  getRack(id: number) {
    return request.get<RackVO>({ url: `/wms/rack/get?id=${id}` })
  },
  // 获取货架分页
  getRackPage(params: RackPageReqVO) {
    return request.get<PageResult<RackVO>>({ url: '/wms/rack/page', params })
  },
  // 导出货架 Excel
  exportRack(params: RackPageReqVO) {
    return request.download({ url: '/wms/rack/export-excel', params })
  },
  // 获取货架精简信息列表
  getSimpleRackList() {
    return request.get<RackVO[]>({ url: '/wms/rack/simple-list' })
  },
  // 按货区ID获取货架列表
  getRacksByAreaId(areaId: number) {
    return request.get<RackVO[]>({ url: `/wms/rack/list-by-area?areaId=${areaId}` })
  }
}
