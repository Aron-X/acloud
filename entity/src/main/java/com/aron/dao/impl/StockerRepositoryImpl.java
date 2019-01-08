package com.aron.dao.impl;

import com.aron.dao.IStockerCustomRepository;
import com.aron.entity.Stocker;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * description:
 * StockerRepositoryImpl .
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2018/9/14        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2018/9/14 13:58
 * @copyright: 2018, FA Software (Shanghai) Co., Ltd. All Rights Reserved.
 */
public class StockerRepositoryImpl implements IStockerCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Stocker getMyCustomData() {
        System.out.println(entityManager.toString());
        Stocker stocker = new Stocker();
        stocker.setName("this is test");
        return stocker;
    }
}
