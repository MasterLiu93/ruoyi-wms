import request from '@/config/axios'

// 简单物料 VO
export interface SimpleItemVO {
  id: number // 物料ID
  name: string // 物料名称
  code: string // 物料编码
  spec: string // 规格
  unit: string // 单位
}

// 简单物料 API
export const SimpleItemApi = {
  // 获取简单物料列表
  getSimpleItemList: async () => {
    return await request.get({ url: `/wms/item/simple-list` })
  }
} 