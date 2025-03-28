import request from '@/config/axios'

// 客户 VO
export interface CustomerVO {
  id: number // 客户ID
  customerCode: string // 客户编码
  customerName: string // 客户名称
  customerLevel: number // 客户级别(0-普通客户 1-重要客户 2-VIP客户)
  contact: string // 联系人
  phone: string // 联系电话
  email: string // 邮箱
  address: string // 地址
  bankName: string // 开户行
  bankAccount: string // 银行账号
  status: number // 状态(0-正常 1-禁用)
  remark: string // 备注
}

// 客户 API
export const CustomerApi = {
  // 查询客户分页
  getCustomerPage: async (params: any) => {
    return await request.get({ url: `/wms/customer/page`, params })
  },

  // 查询客户详情
  getCustomer: async (id: number) => {
    return await request.get({ url: `/wms/customer/get?id=` + id })
  },

  // 新增客户
  createCustomer: async (data: CustomerVO) => {
    return await request.post({ url: `/wms/customer/create`, data })
  },

  // 修改客户
  updateCustomer: async (data: CustomerVO) => {
    return await request.put({ url: `/wms/customer/update`, data })
  },

  // 删除客户
  deleteCustomer: async (id: number) => {
    return await request.delete({ url: `/wms/customer/delete?id=` + id })
  },

  // 导出客户 Excel
  exportCustomer: async (params) => {
    return await request.download({ url: `/wms/customer/export-excel`, params })
  },
}