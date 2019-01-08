package com.aron.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * description:
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 *
 * @author Panda
 * @date: 2018/5/11
 */

public class PageUtils {

    public static final Integer DEFAULT_SIZE = 10;

    private PageUtils() {
    }

    /**
     * description:
     * <p>
     * change history:
     * date             defect#             person             comments
     * ---------------------------------------------------------------------------------------------------------------------
     *
     * @param data      dataList
     * @param page      this page
     * @param size      this size
     * @param totalSize totalSize
     * @return org.springframework.data.domain.Page
     * @author Panda
     * @date: 2018/5/11
     */
    public static Page getPage(List data, Integer page, Integer size, Long totalSize) {
        if (null == data || null == totalSize) {
            return null;
        }
        page = null == page ? 0 : page;
        size = null == size ? DEFAULT_SIZE : size;
        return new PageImpl(data, getPageable(page, size), totalSize);
    }

    /**
     * description:
     * <p>
     * change history:
     * date             defect#             person             comments
     * ---------------------------------------------------------------------------------------------------------------------
     *
     * @param page this page
     * @param size this size
     * @return org.springframework.data.domain.Pageable
     * @author Panda
     * @date: 2018/5/11
     */
    public static Pageable getPageable(Integer page, Integer size) {
        Integer pageIndex = (null == page) ? 0 : (page - 1 <= 0 ? 0 : page - 1);
        Pageable pageable = new PageRequest(pageIndex, size);
        return pageable;
    }

    /**
     * description:
     * 获取基础分页对象
     * change history:
     * date             defect             person             comments
     * ---------------------------------------------------------------------------------------------------------------------
     *
     * @param page 获取第几页
     * @param size 每页条数
     * @param dtos 排序对象数组
     * @return
     * @author PlayBoy
     * @date 2018/7/31
     */
    public static Pageable basicPage(Integer page, Integer size, SortDto... dtos) {
        Sort sort = SortTools.basicSort(dtos);
        page = (page == null || page < 0) ? 0 : page;
        size = (size == null || size <= 0) ? DEFAULT_SIZE : size;
        Pageable pageable = new PageRequest(page, size, sort);
        return pageable;
    }

    /**
     * description:
     * 获取基础分页对象，每页条数默认10条
     * - 默认以id降序排序
     * change history:
     * date             defect             person             comments
     * ---------------------------------------------------------------------------------------------------------------------
     *
     * @param page 获取第几页
     * @return
     * @author PlayBoy
     * @date 2018/7/31
     */
    public static Pageable basicPage(Integer page) {
        return basicPage(page, 0, new SortDto(SortTools.DEFAULT_SORT, SortTools.DEFAULT_SORT_COLUMN));
    }

    /**
     * description:
     * 获取基础分页对象，每页条数默认10条
     * change history:
     * date             defect             person             comments
     * ---------------------------------------------------------------------------------------------------------------------
     *
     * @param page 获取第几页
     * @param dtos 排序对象数组
     * @return
     * @author PlayBoy
     * @date 2018/7/31
     */
    public static Pageable basicPage(Integer page, SortDto... dtos) {
        return basicPage(page, 0, dtos);
    }

    /**
     * description:
     * 获取基础分页对象，排序方式默认降序
     * change history:
     * date             defect             person             comments
     * ---------------------------------------------------------------------------------------------------------------------
     *
     * @param page       获取第几页
     * @param size       每页条数
     * @param orderField 排序字段
     * @return
     * @author PlayBoy
     * @date 2018/7/31
     */
    public static Pageable basicPage(Integer page, Integer size, String orderField) {
        return basicPage(page, size, new SortDto(SortTools.DEFAULT_SORT, orderField));
    }

    /**
     * description:
     * 获取基础分页对象
     * - 每页条数默认10条
     * - 排序方式默认降序
     * change history:
     * date             defect             person             comments
     * ---------------------------------------------------------------------------------------------------------------------
     *
     * @param page       获取第几页
     * @param orderField 排序字段
     * @return
     * @author PlayBoy
     * @date 2018/7/31
     */
    public static Pageable basicPage(Integer page, String orderField) {
        return basicPage(page, 0, new SortDto(SortTools.DEFAULT_SORT, orderField));
    }

}
