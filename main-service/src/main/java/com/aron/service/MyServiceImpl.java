package com.aron.service;

import com.aron.annotation.BizCaller;
import com.aron.annotation.IdentifierColumn;
import com.aron.bo.BoFactory;
import com.aron.bo.StockerBO;
import com.aron.dao.impl.BatchDaoImpl;
import com.aron.dao.StockerRepository;
import com.aron.entity.Stocker;
import com.aron.entity.StockerItem;
import com.aron.utils.ObjectIdentifier;
import com.aron.utils.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
@Slf4j
//指明用哪一个缓存策略
@CacheConfig(cacheNames = "aronCache")
public class MyServiceImpl implements IMyService {

    private static final String CACHE_KEY = "'stocker'";

//    private static final String CACHE_NAME = "aronCache";

    private final StockerRepository stockerRepository;

    private final BatchDaoImpl<Stocker> batchDao;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private BoFactory boFactory;

//    private final ZookeeperLockRegistry zkLock;

    @Autowired
    public MyServiceImpl(StockerRepository stockerRepository, BatchDaoImpl<Stocker> batchDao) {
        this.stockerRepository = stockerRepository;
        this.batchDao = batchDao;
//        this.zkLock = zkLock;
    }


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
    @Transactional(rollbackFor = Exception.class)
    //缓存的key
    @Cacheable(key = CACHE_KEY)
    public long getStockerCount() {
        Query nativeQuery = entityManager.createNativeQuery("select count(*) from FRSTOCKER");
        List<?> result = nativeQuery.getResultList();
        if (result == null || result.isEmpty()) {
            return 0L;
        }
        if (result.size() > 1) {
            throw new NonUniqueResultException("count query not return a single result");
        }
        BigDecimal count = (BigDecimal) result.get(0);
        long countVal = count.longValue();
        log.info("count is {}", countVal);
        return countVal;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Cacheable(key = CACHE_KEY)
    public Stocker testCache() {
        Query nativeQuery = entityManager.createQuery("from Stocker s where s.id ='123'");
        List<Stocker> result = nativeQuery.getResultList();

        Query nativeQuery2 = entityManager.createQuery("from Stocker s where s.id ='123'");
        List<Stocker> result2 = nativeQuery2.getResultList();
        if (result == null || result.isEmpty()) {
            return null;
        }
        if (result.size() > 1) {
            throw new NonUniqueResultException("count query not return a single result");
        }
        Stocker stocker = result.get(0);
        log.info("stocker is {}", stocker.toString());
        return stocker;
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
       /* Stocker byStockerId = stockerRepository.findByStockerId("123");
        Lock lock = zkLock.obtain(byStockerId.getId());
        boolean locked = false;
        try {
            locked = lock.tryLock(3, TimeUnit.SECONDS);
            Thread.sleep(16000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (locked) {
                lock.unlock();
            }
        }*/
//        System.out.println(1 / 0);
//        batchDao.batchUpdate(stockers);
    }

    @Override
    @BizCaller
    @Transactional(rollbackFor = Exception.class)
    public void invokeBo(String name) {
        ObjectIdentifier objectIdentifier = new ObjectIdentifier();
        objectIdentifier.setReferenceKey("123");

        StockerBO stockerBO1 = boFactory.convertObjectIdentifierToBO(objectIdentifier, StockerBO.class);

//        List<StockerItem> stockerInfos = stockerBO1.getStockerItems();
        //StockerInfo stockerInfo = stockerBO1.getStockerInfo();
        stockerBO1.setName(name);
        List<StockerItem> stockerInfos = new ArrayList<>();
        StockerItem stockerItem = new StockerItem();
        stockerItem.setId("StockerItem.155109141474443264-test");
        stockerItem.setMaxCount(100);
        stockerItem.setRefkey("123");
        stockerItem.setStockerInfo("lalala");
        stockerInfos.add(stockerItem);

        stockerBO1.setStockerItems(stockerInfos);

        List<StockerItem> stockerItems = stockerBO1.getStockerItems();
        System.out.println(">>>>>" + stockerBO1.toString());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void myTest() {
        Optional<Stocker> stocker = stockerRepository.findById("123");
        /*List<StockerItem> stockerItems = stocker.get().getStockerItems();
        StockerItem stockerItem = stockerItems.get(0);
        stockerItem.setStockerInfo("444");

        stocker.get().setName("this is my 444");
        stockerRepository.save(stocker.get());
        System.out.println(stocker.get().toString());
        
        System.out.println(1 / 0);*/
    }

}
