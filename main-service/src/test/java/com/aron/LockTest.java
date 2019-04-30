package com.aron;

import com.aron.dao.StockerRepository;
import com.aron.entity.Stocker;
import com.aron.lock.OmZookeeperLock;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * description:
 * <p>LockTest .</p>
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2019/4/4        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2019/4/4 13:19
 * @copyright: 2019, FA Software (Chengdu) Co., Ltd. All Rights Reserved.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class LockTest {

    @Autowired
    private OmZookeeperLock zkLock;

    @Autowired
    private StockerRepository stockerRepository;

    @Test
    public void test() {
        log.info(">>>> {} enter in <<<", Thread.currentThread().getName());
        Stocker stocker = stockerRepository.findById("Stocker.97307915077025792").orElse(null);

        Lock lock = zkLock.obtain(stocker);

        boolean locked = true;
        try {
            locked = lock.tryLock(5, TimeUnit.SECONDS);
            if (!locked) {
                log.info(">>>> {} get lock failed <<<", Thread.currentThread().getName());
                log.info("please tried later ...");
            }
            log.info(">>>> {} get lock success <<<", Thread.currentThread().getName());
            log.info(">>>> {} batchUpdate processing <<<", Thread.currentThread().getName());
            //sleep here use to analog processing

            Thread.sleep(5000);
        } catch (Exception ex) {
            log.error("Fail to batchUpdate request", ex);
        } finally {
            if (locked) {
                lock.unlock();
            }
        }
        log.info(">>>> {} done <<<", Thread.currentThread().getName());
    }

}
