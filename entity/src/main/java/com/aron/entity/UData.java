package com.aron.entity;

import com.aron.utils.Constant;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * description:
 * <p>UData .<br/></p>
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2018/11/20        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2018/11/20 16:26
 * @copyright: 2018, FA Software (Shanghai) Co., Ltd. All Rights Reserved.
 */
@Data
@Entity
@Table
public class UData {

    @Id
    @Column(name = "id")
    @GenericGenerator(name = "idGenerator", strategy = Constant.STRATEGY_REFERENCE)
    @GeneratedValue(generator = "idGenerator")
    private String id;

    @Column(name = "REFKEY")
    private String referenceKey;

    @Column(name = "D_SEQ")
    private Integer sequenceNo;

    @Column(name = "COLUMN_NAME")
    private String column;

    @Column(name = "COLUMN_VALUE")
    private String columnValue;
}
