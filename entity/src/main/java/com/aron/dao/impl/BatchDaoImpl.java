package com.aron.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * description:
 * BatchDaoImpl .
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2018/8/16        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2018/8/16 17:36
 * @copyright: 2018, FA Software (Shanghai) Co., Ltd. All Rights Reserved.
 */
@Slf4j
@Repository
public class BatchDaoImpl<T> {

    /**
     * batch size count
     */
    private static final Integer BATCH_SIZE = 3;

    @PersistenceContext
    private EntityManager entityManager;

    public void batchInsert(List<T> list) {
        for (int i = 0; i < list.size(); i++) {
            entityManager.persist(list.get(i));
            if (i % BATCH_SIZE == 0) {
                entityManager.flush();
                entityManager.clear();
                log.info("=====================");
            }
        }
    }

    public void batchUpdate(List<T> list) {
        for (int i = 0; i < list.size(); i++) {
            entityManager.merge(list.get(i));
            if (i % BATCH_SIZE == 0) {
                entityManager.flush();
                entityManager.clear();
                log.info("=====================");
            }
            /*if (i == 5) {
                throw new RuntimeException("test error");
            }*/
        }
    }
}
