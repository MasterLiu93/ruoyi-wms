import request from '@/config/axios'

// 物料分类 VO
export interface ItemCategoryVO {
  id: number // 分类ID
  categoryCode: string // 分类编码
  categoryName: string // 分类名称
  parentId: number // 父分类ID
  sort: number // 显示顺序
  status: number // 状态(0-正常 1-禁用)
  remark: string // 备注
}

// 物料分类树形 VO
export interface ItemCategoryTreeVO extends ItemCategoryVO {
  children?: ItemCategoryTreeVO[] // 子分类
}

// 物料分类 API
export const ItemCategoryApi = {
  // 查询物料分类分页
  getItemCategoryPage: async (params: any) => {
    return await request.get({ url: `/wms/item-category/page`, params })
  },

  // 查询物料分类树形结构
  getItemCategoryTree: async (params?: any) => {
    return await request.get({ url: `/wms/item-category/tree`, params })
  },

  // 查询物料分类详情
  getItemCategory: async (id: number) => {
    return await request.get({ url: `/wms/item-category/get?id=` + id })
  },

  // 新增物料分类
  createItemCategory: async (data: ItemCategoryVO) => {
    return await request.post({ url: `/wms/item-category/create`, data })
  },

  // 修改物料分类
  updateItemCategory: async (data: ItemCategoryVO) => {
    return await request.put({ url: `/wms/item-category/update`, data })
  },

  // 删除物料分类
  deleteItemCategory: async (id: number) => {
    return await request.delete({ url: `/wms/item-category/delete?id=` + id })
  },

  // 导出物料分类 Excel
  exportItemCategory: async (params) => {
    return await request.download({ url: `/wms/item-category/export-excel`, params })
  },
}
