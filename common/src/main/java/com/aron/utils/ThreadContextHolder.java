package com.aron.utils;

/**
 * description:
 * This ThreadContextHolder used to put some variables based thread .
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2018/6/28        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2018/6/28 11:55
 * @copyright: 2018, FA Software (Shanghai) Co., Ltd. All Rights Reserved.
 */
public class ThreadContextHolder {

    private ThreadContextHolder() {
    }

    private final static ThreadLocal<String> transactionIdHolder = new ThreadLocal<>();

    public static String getTransactionId() {
        String transactionId = transactionIdHolder.get();
        if (transactionId == null) {
            return "";
        }
        return transactionId;
    }

    public static void setTransactionId(String tId) {
        transactionIdHolder.set(tId);
    }

    /**
     * clear current thread threadLocalMap element
     */
    public static void clearHolder() {
        transactionIdHolder.remove();
    }

}
