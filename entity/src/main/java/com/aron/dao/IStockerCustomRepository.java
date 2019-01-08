package com.aron.dao;

import com.aron.entity.Stocker;

/**
 * description:
 * IStockerCustomRepository .
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2018/9/14        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2018/9/14 13:57
 * @copyright: 2018, FA Software (Shanghai) Co., Ltd. All Rights Reserved.
 */
public interface IStockerCustomRepository {
    Stocker getMyCustomData();

    default void myTestMethod() {

    }
}
