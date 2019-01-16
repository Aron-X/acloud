package com.aron.controller;

import com.aron.dao.StockerRepository;
import com.aron.dao.UDataRepository;
import com.aron.entity.Stocker;
import com.aron.entity.UData;
import com.aron.entity.User;
import com.aron.lock.CimLock;
import com.aron.service.IMyService;
import com.aron.utils.ThreadContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.zookeeper.lock.ZookeeperLockRegistry;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@Slf4j
@RestController
public class MyController {

//    @Autowired
//    DiscoveryClient discoveryClient;

    private final IMyService myService;

    private final StockerRepository stockerRepository;

    private final UDataRepository uDataRepository;

    private final CimLock cimLock;

    private final ZookeeperLockRegistry zookeeperLockRegistry;

    @Autowired
    public MyController(IMyService myService, StockerRepository stockerRepository, UDataRepository uDataRepository, CimLock cimLock, ZookeeperLockRegistry zookeeperLockRegistry) {
        this.myService = myService;
        this.stockerRepository = stockerRepository;
        this.uDataRepository = uDataRepository;
        this.cimLock = cimLock;
        this.zookeeperLockRegistry = zookeeperLockRegistry;
    }

    @RequestMapping("/show")
    public Stocker show(@RequestBody Stocker stocker, @RequestParam("name") String name) {
        System.out.println(">>>> show <<<");
        log.info("name:{} %s {} --", name, name);
        log.info("stocker:{} %s --", stocker);
        ThreadContextHolder.setTransactionId(name);
//        String services = "Services: " + discoveryClient.getServices();
//        log.info(">>>>>>>>show():", services);
        if (name.equals("e")) {
            throw new RuntimeException("hahahah");
        }
        /*log.info(">>>>>>>>stocker:" + stocker.toString());
        log.info(">>>>>>>>user:" + user.toString());*/
        Stocker stocker2 = new Stocker();
        stocker2.setName(name);
        return stocker2;
    }

    @GetMapping("/print")
    public String print() {
        log.info("test {}", "aron");
//        String services = "Services: " + discoveryClient.getServices();
//        log.info(">>>>>>>>print():" + services);
        return "haha";
    }

    @GetMapping("/query")
    public Map<String, List> query(@RequestParam("status") String status, @RequestParam("name") String name) {
        List<Stocker> byStatus = myService.getByStatus(status);
        log.info("byStatus: " + byStatus);
        List<Stocker> stockers = myService.queryWithNameAndStatus(name, status);
        log.info("stockers: " + stockers);

        List<Stocker> all = stockerRepository.findAll();
        List<String[]> data = stockerRepository.findAllByCondition(name);
        Stocker myCustomData = stockerRepository.getMyCustomData();

        List stockers1 = myService.getStockersByName(name);

        Map<String, List> returnMap = new HashMap<>();
        returnMap.put("byStatus", byStatus);
        returnMap.put("stockers", stockers);
        returnMap.put("data", data);
        return returnMap;
    }

    /**
     * this for zookeeperLockRegistry test case
     *
     * @return result
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @GetMapping("/batch/update")
    public String batchUpdate() {
        Lock lock = zookeeperLockRegistry.obtain("MyController-batchUpdate");
        boolean locked = true;
        log.info(">>>> {} enter in <<<", Thread.currentThread().getName());
        try {
            locked = lock.tryLock();
            if (!locked) {
                return "please tried later ...";
            }
            log.info(">>>> {} get lock success <<<", Thread.currentThread().getName());
            log.info(">>>> batchUpdate processing <<<");
            //sleep here use to analog processing
            Thread.sleep(6000);
        } catch (Exception ex) {
            log.error("Fail to batchUpdate request", ex);
        } finally {
            if (locked) {
                lock.unlock();
            }
        }
        log.info(">>>> {} done <<<", Thread.currentThread().getName());
        return "Success";
    }

    /**
     * this for customized lock test case
     *
     * @return result
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @GetMapping("/my/batch/update")
//    @CacheLock(key = "aron-test")
    public String myBatchUpdate() {
        log.info(">>>> {} enter in <<<", Thread.currentThread().getName());
        boolean locked = false;
        try {
            locked = cimLock.tryLock("MyController-batchUpdate-method");
            if (!locked) {
                return "please tried later ...";
            }
            log.info(">>>> {} get lock success <<<", Thread.currentThread().getName());
            log.info(">>>> batchUpdate processing <<<");
            myService.batchUpdate();
        } catch (Exception ex) {
            log.error("Fail to batchUpdate request", ex);
        } finally {
            if (locked) {
                cimLock.unlock();
            }
        }
        log.info(">>>> {} done <<<", Thread.currentThread().getName());
        return "Success";
    }

    @GetMapping("/udata/{refKey}")
    public List<UData> findUData(@PathVariable("refKey") String refKey) {
        List<UData> uDataList = uDataRepository.findByRefKey(refKey, User.class);
        return uDataList;
    }

}
