package com.aron.entity;

import com.aron.utils.Constant;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * description:
 * This User use to XXXXXXXXXX.
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2018/6/27        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2018/6/27 12:49
 * @copyright: 2018, FA Software (Shanghai) Co., Ltd. All Rights Reserved.
 */
@Entity
@Table(name = "FRUSER")
@Data
@ToString
public class User {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = Constant.STRATEGY_REFERENCE)
    @GeneratedValue(generator = "idGenerator")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "sex")
    private String sex;

    @Column(name = "age", length = 3)
    private Integer age;

    @Column(name = "address")
    private String address;

    @Column(name = "grade")
    private Long grade;
}
