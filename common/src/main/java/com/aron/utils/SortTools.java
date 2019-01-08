package com.aron.utils;

import org.springframework.data.domain.Sort;

/**
 * description:
 * SortTools .
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2018/7/31        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2018/7/31 12:54
 * @copyright: 2018, FA Software (Shanghai) Co., Ltd. All Rights Reserved.
 */
public class SortTools {

    public static final String DEFAULT_SORT = "desc";
    public static final String DEFAULT_SORT_COLUMN = "id";

    private SortTools() {

    }

    public static Sort basicSort() {
        return basicSort(DEFAULT_SORT, DEFAULT_SORT_COLUMN);
    }

    public static Sort basicSort(String orderType, String orderField) {
        Sort sort = new Sort(Sort.Direction.fromString(orderType), orderField);
        return sort;
    }

    public static Sort basicSort(SortDto... dtos) {
        Sort result = null;
        for (int i = 0; i < dtos.length; i++) {
            SortDto dto = dtos[i];
            if (result == null) {
                result = new Sort(Sort.Direction.fromString(dto.getOrderType()), dto.getOrderField());
            } else {
                result = result.and(new Sort(Sort.Direction.fromString(dto.getOrderType()), dto.getOrderField()));
            }
        }
        return result;
    }

}
