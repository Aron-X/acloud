package com.aron.lock;

/**
 * description:
 * <p>CimLock .<br/></p>
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2019/1/13        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2019/1/13 14:43
 * @copyright: 2019, FA Software (Chengdu) Co., Ltd. All Rights Reserved.
 */
public interface CimLock {

    /**
     * tryLock
     *
     * @param waitingTime waitingTime
     * @return
     */
    boolean tryLock(Object lockKey, long waitingTime);

    /**
     * tryLock
     *
     * @return
     */
    boolean tryLock(Object lockKey);

    /**
     * tryLockNoWaiting
     *
     * @return
     */
    boolean tryLockNoWaiting(Object lockKey);

    /**
     * unlock
     */
    void unlock();

    void unlockAll();
}
