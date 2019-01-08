package com.aron.utils;

/**
 * description:
 * SortDto .
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2018/7/31        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2018/7/31 12:55
 * @copyright: 2018, FA Software (Shanghai) Co., Ltd. All Rights Reserved.
 */
public class SortDto {
    /**
     * 排序方式
     */
    private String orderType;

    /**
     * 排序字段
     */
    private String orderField;

    public String getOrderField() {
        return orderField;
    }

    public void setOrderField(String orderField) {
        this.orderField = orderField;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public SortDto(String orderType, String orderField) {
        this.orderType = orderType;
        this.orderField = orderField;
    }

    public SortDto(String orderField) {
        this.orderField = orderField;
        //默认为DESC排序
        this.orderType = SortTools.DEFAULT_SORT;
    }

}
