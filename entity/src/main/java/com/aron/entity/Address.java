package com.aron.entity;

import com.aron.entity.pk.AddressPk;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * description:
 * <p>Address .<br/></p>
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2018/12/10        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2018/12/10 10:40
 * @copyright: 2018, FA Software (Shanghai) Co., Ltd. All Rights Reserved.
 */
@Entity
@Table(name = "ADDRESS")
@Data
public class Address {

    @EmbeddedId
    private AddressPk idClass;

    @Column(name = "DETAILS")
    private String details;

}
