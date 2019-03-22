package com.aron.bo;

import com.aron.entity.StockerInfo;
import com.aron.entity.StockerItem;

import java.util.List;

/**
 * description:
 * <p>StockerBO .</p>
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2019/3/5        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2019/3/5 14:48
 * @copyright: 2019, FA Software (Chengdu) Co., Ltd. All Rights Reserved.
 */
public interface StockerBO extends BaseBO {
    void setName(String pName);

    String getName();

    void setStockerItems(List<StockerItem> stockerItems);

    List<StockerItem> getStockerItems();

    StockerInfo getStockerInfo();

    StockerInfo setStockerInfo(StockerInfo data);

}
