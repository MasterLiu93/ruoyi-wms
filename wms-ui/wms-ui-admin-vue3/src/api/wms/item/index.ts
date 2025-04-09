import request from '@/config/axios'

// 物料 VO
export interface ItemVO {
  id: number // 物料ID
  itemCode: string // 物料编码
  itemName: string // 物料名称
  categoryId: number // 分类ID
  itemType: number // 物料类型(0-原材料 1-半成品 2-成品 3-包装材料)
  unit: string // 单位
  spec: string // 规格
  price: number // 参考价格
  safetyStock: number // 安全库存
  status: number // 状态(0-正常 1-禁用)
  remark: string // 备注
}

// 物料 API
export const ItemApi = {
  // 查询物料分页
  getItemPage: async (params: any) => {
    return await request.get({ url: `/wms/item/page`, params })
  },

  // 查询物料详情
  getItem: async (id: number) => {
    return await request.get({ url: `/wms/item/get?id=` + id })
  },

  // 新增物料
  createItem: async (data: ItemVO) => {
    return await request.post({ url: `/wms/item/create`, data })
  },

  // 修改物料
  updateItem: async (data: ItemVO) => {
    return await request.put({ url: `/wms/item/update`, data })
  },

  // 删除物料
  deleteItem: async (id: number) => {
    return await request.delete({ url: `/wms/item/delete?id=` + id })
  },

  // 导出物料 Excel
  exportItem: async (params) => {
    return await request.download({ url: `/wms/item/export-excel`, params })
  },
}
