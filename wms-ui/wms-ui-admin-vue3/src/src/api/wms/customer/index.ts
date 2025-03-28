import request from '@/config/axios'

// 客户 VO
export interface CustomerVO {
  customerId: number // 客户ID
  customerCode: string // 客户编码
  customerName: string // 客户名称
  customerLevel: string // 客户级别（0普通 1重要 2VIP）
  contact: string // 联系人
  phone: string // 联系电话
  email: string // 邮箱
  address: string // 地址
  bankName: string // 开户行
  bankAccount: string // 银行账号
  status: string // 状态（0正常 1停用）
  delFlag: string // 删除标志（0代表存在 2代表删除）
  createBy: string // 创建者
  updateBy: string // 更新者
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