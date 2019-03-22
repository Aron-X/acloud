package com.aron.bo;

import java.util.ArrayList;
import java.util.List;

/**
 * description:
 * <p>BizThreadHolder .</p>
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2019/3/7        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2019/3/7 10:47
 * @copyright: 2019, FA Software (Chengdu) Co., Ltd. All Rights Reserved.
 */
public final class BizThreadHolder {

    private BizThreadHolder() {

    }

    private final static ThreadLocal<List<BaseBO>> BO_HOLDER = new ThreadLocal<>();

    public static List<BaseBO> getBizList() {
        return BO_HOLDER.get();
    }

    public static void setBizObject(BaseBO bo) {
        List<BaseBO> boList = BO_HOLDER.get();
        if (boList == null) {
            boList = new ArrayList<>();
            BO_HOLDER.set(boList);
        }
        for (BaseBO boItem : boList) {
            if (boItem.getTheIdentify().equals(bo.getTheIdentify())) {
                return;
            }
        }
        boList.add(bo);
    }

    /**
     * clear current thread threadLocalMap element
     */
    public static void clearHolder() {
        BO_HOLDER.remove();
    }
}
