package com.aron.bo;

import com.aron.entity.BaseEntity;
import lombok.Getter;

/**
 * description:
 * <p>AbstractBO .</p>
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2019/3/11        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2019/3/11 15:51
 * @copyright: 2019, FA Software (Chengdu) Co., Ltd. All Rights Reserved.
 */
public abstract class AbstractBO<T extends BaseEntity> implements BaseBO {

    @Getter
    private final T entity;

    AbstractBO(T entity) {
        this.entity = entity;
    }
}
