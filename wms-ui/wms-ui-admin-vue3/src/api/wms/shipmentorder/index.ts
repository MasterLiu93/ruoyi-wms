import request from '@/config/axios'

// 出库单API类型
export interface ShipmentOrderVO {
  id?: number // 出库单ID
  shipmentOrderNo?: string // 出库单号
  customerId: number // 客户ID
  customerName?: string // 客户名称
  warehouseId: number // 仓库ID
  warehouseName?: string // 仓库名称
  shipmentType: number // 出库类型
  orderStatus: number // 单据状态(0-草稿 1-待审核 2-审核通过 3-审核拒绝 4-已取消)
  shipmentStatus: number // 出库状态(0-待出库 1-部分出库 2-已完成)
  expectTime: number // 预计出库时间
  completeTime?: number // 实际完成时间
  remark?: string // 备注
  totalCount: number // 商品数量
  totalAmount: number // 商品金额
  details: ShipmentOrderDetailVO[] // 明细列表
  createTime?: Date // 创建时间
  updateTime?: Date // 更新时间
  creator?: string // 创建者
  updater?: string // 更新者
}

// 出库单明细VO
export interface ShipmentOrderDetailVO {
  id?: number // 明细ID
  shipmentOrderId?: number // 出库单ID
  warehouseId?: number // 仓库ID
  areaId?: number // 货区ID
  rackId?: number // 货架ID
  itemId: number // 商品ID，原为productId
  itemCode?: string // 商品编码
  itemName: string // 商品名称
  itemType: number // 商品类型
  spec?: string // 规格
  unit?: string // 单位
  planCount: number // 计划数量，原为planQuantity
  realCount?: number // 实际数量，原为realQuantity
  locationId?: number // 出库库位ID
  batchId?: number // 批次ID
  price: number // 出库单价，必填字段
  status?: number // 状态
  remark?: string // 备注
}

// 出库单审核请求VO
export interface ShipmentOrderApproveReqVO {
  id: number // 出库单ID
  approveResult: boolean // 是否审核通过
  remark?: string // 审核备注
}

// 出库操作请求VO
export interface ShipmentOperationReqVO {
  shipmentOrderId: number // 出库单ID
  detailId: number // 明细ID
  quantity: number // 数量
  warehouseId: number // 仓库ID
  areaId?: number // 货区ID
  rackId?: number // 货架ID
  locationId?: number // 库位ID，可选
  remark?: string // 备注
}

// 出库单API
export const ShipmentOrderApi = {
  // 查询出库单分页
  getShipmentOrderPage: async (params: any) => {
    return await request.get({ url: '/wms/shipment-order/page', params })
  },

  // 查询出库单详情
  getShipmentOrder: async (id: number) => {
    return await request.get({ url: '/wms/shipment-order/get?id=' + id })
  },

  // 新增出库单
  createShipmentOrder: async (data: ShipmentOrderVO) => {
    return await request.post({ url: '/wms/shipment-order/create', data })
  },

  // 新增出库单及其明细
  createShipmentOrderWithDetails: (data: ShipmentOrderVO) => {
    return request.post({ url: `/wms/shipment-order/create-with-details`, data })
  },

  // 更新出库单
  updateShipmentOrder: async (data: ShipmentOrderVO) => {
    return await request.put({ url: '/wms/shipment-order/update', data })
  },

  // 更新出库单及其明细
  updateShipmentOrderWithDetails: (data: ShipmentOrderVO) => {
    return request.put({ url: `/wms/shipment-order/update-with-details`, data })
  },

  // 提交出库单并直接出库
  submitAndShipmentOrder: (data: ShipmentOrderVO) => {
    return request.post({ url: `/wms/shipment-order/submit-and-shipment`, data })
  },

  // 删除出库单
  deleteShipmentOrder: async (id: number) => {
    return await request.delete({ url: '/wms/shipment-order/delete?id=' + id })
  },

  // 导出出库单
  exportShipmentOrder: async (params: any) => {
    return await request.download({ url: '/wms/shipment-order/export-excel', params })
  },
  
  // 提交出库单
  submitShipmentOrder: (id: number) => {
    return request.put({ url: `/wms/shipment-order/submit?id=` + id })
  },
  
  // 审核出库单
  approveShipmentOrder: async (id: number, approved: boolean, remark?: string) => {
    return await request.put({ 
      url: '/wms/shipment-order/approve', 
      params: { id, approved, remark }
    })
  },
  
  // 批量执行出库
  batchExecuteShipment: (id: number) => {
    return request.put({ url: `/wms/shipment-order/batch-execute?id=` + id })
  },
  
  // 取消出库单
  cancelShipmentOrder: async (id: number, remark?: string) => {
    return await request.put({ 
      url: '/wms/shipment-order/cancel',
      data: { id, remark }
    })
  },
  
  // 执行出库
  executeShipment: (data: ShipmentOperationReqVO) => {
    return request.post({ 
      url: `/wms/shipment-order/execute-shipment`, 
      data
    })
  },
  
  // 获取客户列表
  getCustomerList: () => {
    return request.get({ url: '/wms/customer/list-all' })
  },
  
  // 获取仓库列表
  getWarehouseList: () => {
    return request.get({ url: '/wms/warehouse/simple-list' })
  },
  
  // 根据仓库ID获取可用库存列表
  getInventoryListByWarehouse: (warehouseId: number) => {
    return request.get({ url: `/wms/inventory/list-by-warehouse?warehouseId=${warehouseId}` })
  },

  // 完成出库单
  completeShipmentOrder: async (id: number) => {
    return await request.put({ url: '/wms/shipment-order/complete', params: { id } })
  }
}
