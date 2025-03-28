import request from '@/config/axios'

// 供应商 VO
export interface SupplierVO {
  id: number // 供应商ID
  supplierCode: string // 供应商编码
  supplierName: string // 供应商名称
  supplierLevel: number // 供应商级别(0-普通 1-重要 2-战略)
  contact: string // 联系人
  phone: string // 联系电话
  email: string // 邮箱
  address: string // 地址
  bankName: string // 开户行
  bankAccount: string // 银行账号
  status: number // 状态(0-正常 1-禁用)
  remark: string // 备注
}

// 供应商 API
export const SupplierApi = {
  // 查询供应商分页
  getSupplierPage: async (params: any) => {
    return await request.get({ url: `/wms/supplier/page`, params })
  },

  // 查询供应商详情
  getSupplier: async (id: number) => {
    return await request.get({ url: `/wms/supplier/get?id=` + id })
  },

  // 新增供应商
  createSupplier: async (data: SupplierVO) => {
    return await request.post({ url: `/wms/supplier/create`, data })
  },

  // 修改供应商
  updateSupplier: async (data: SupplierVO) => {
    return await request.put({ url: `/wms/supplier/update`, data })
  },

  // 删除供应商
  deleteSupplier: async (id: number) => {
    return await request.delete({ url: `/wms/supplier/delete?id=` + id })
  },

  // 导出供应商 Excel
  exportSupplier: async (params) => {
    return await request.download({ url: `/wms/supplier/export-excel`, params })
  },
}