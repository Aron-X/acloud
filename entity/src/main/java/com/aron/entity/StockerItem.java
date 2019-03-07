package com.aron.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * description:
 * <p>StockerInfo .</p>
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2019/2/28        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2019/2/28 15:56
 * @copyright: 2019, FA Software (Chengdu) Co., Ltd. All Rights Reserved.
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "FRSTOCKER_ITEM")
@Data
public class StockerItem extends BaseEntity implements Serializable {
    @Column(name = "sinfo")
    private String stockerInfo;

    @Column(name = "max_count")
    private Integer maxCount;

    @Column(name = "refkey")
    private String refkey;
}
