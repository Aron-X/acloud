package com.aron.service;

import com.aron.annotation.IdentifierColumn;
import com.aron.dao.impl.BatchDaoImpl;
import com.aron.dao.StockerRepository;
import com.aron.entity.Stocker;
import com.aron.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * description:
 * This MyServiceImpl use to XXXXXXXXXX.
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2018/6/25        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2018/6/25 11:29
 * @copyright: 2018, FA Software (Shanghai) Co., Ltd. All Rights Reserved.
 */
@Service
public class MyServiceImpl implements IMyService {

    @Autowired
    private StockerRepository stockerRepository;

    @Autowired
    private BatchDaoImpl<Stocker> batchDao;

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<Stocker> queryWithNameAndStatus(String name, String status) {
        return stockerRepository.findByNameAndStatusWithPage(name, status, PageUtils.basicPage(0, 2));
    }

    @Override
    public List<Stocker> getByStatus(String status) {
        List<Stocker> stockers = stockerRepository.findByStatus(status, PageUtils.basicPage(0, 2));
        return stockers;
    }

    @Override
    public List getStockersByName(String name) {
        List datas = stockerRepository.findAllByCondition(name);
        Class<Stocker> stockerClass = Stocker.class;
        String entity = stockerClass.getSimpleName();
        IdentifierColumn identifierColumn = stockerClass.getAnnotation(IdentifierColumn.class);
        Query query = entityManager.createQuery("from " + entity + " where " + identifierColumn.value() + " = ?");
        Query nativeQuery = entityManager.createNativeQuery("select * from FRSTOCKER t where t.name = ?", stockerClass);
        query.setParameter(1, name);
        nativeQuery.setParameter(1, name);
        List<Stocker> resultList = query.getResultList();
        List<Stocker> nativeResultList = nativeQuery.getResultList();
        System.out.println(resultList.get(0).toString());
        return datas;
    }

    @Override
    public void batchUpdate() {
        /*List<Stocker> stockers = stockerRepository.findAll();
        stockers.forEach(stocker -> stocker.setSize(20D));*/
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        System.out.println(1 / 0);
//        batchDao.batchUpdate(stockers);
    }
}
