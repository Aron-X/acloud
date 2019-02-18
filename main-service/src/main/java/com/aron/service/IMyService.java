package com.aron.service;

import com.aron.entity.Stocker;

import java.util.List;

/**
 * description:
 * This IMyService use to XXXXXXXXXX.
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2018/6/25        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2018/6/25 11:30
 * @copyright: 2018, FA Software (Shanghai) Co., Ltd. All Rights Reserved.
 */
public interface IMyService {

    List<Stocker> queryWithNameAndStatus(String name, String status);

    List<Stocker> getByStatus(String status);

    List getStockersByName(String name);

    void batchUpdate();

    long getStockerCount();

    Stocker testCache();
}
