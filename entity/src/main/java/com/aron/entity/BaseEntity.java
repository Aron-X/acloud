package com.aron.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * description:
 * <p>BaseEntity .<br/></p>
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2018/12/27        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2018/12/27 22:04
 * @copyright: 2018, FA Software (Shanghai) Co., Ltd. All Rights Reserved.
 */
@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {
    /**
     * Serial Version Uid
     */
    private static final long serialVersionUID = 2938092512338745974L;
    /**
     * The identity.
     */
    @Id
    @Column(name = "ID")
    //@GenericGenerator(name = "idGenerator", strategy = Constant.STRATEGY_REFERENCE)
    //@GeneratedValue(generator = "idGenerator")
    private String id;

}
