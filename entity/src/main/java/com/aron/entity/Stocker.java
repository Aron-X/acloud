package com.aron.entity;

import com.aron.utils.Constant;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * description:
 * This Stocker use to XXXXXXXXXX.
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2018/6/25        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2018/6/25 11:31
 * @copyright: 2018, FA Software (Shanghai) Co., Ltd. All Rights Reserved.
 */
@Entity
@Table(name = "FRSTOCKER")
@Data
//@IdentifierColumn("name")
public class Stocker implements Serializable {

    @Id
    @Column(name = "id")
    @GenericGenerator(name = "idGenerator", strategy = Constant.STRATEGY_REFERENCE)
    @GeneratedValue(generator = "idGenerator")
    private String id;

    @Column(name = "stocker_id")
    private String stockerId;

    @Column(name = "name")
    private String name;

    @Column(name = "stocker_size")
    private Double size;

    @Column(name = "status")
    private String status;

    @Column(name = "available_flag")
    private Boolean availableFlag;
}
