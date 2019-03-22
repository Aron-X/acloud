package com.aron.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

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
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "FRSTOCKER")
@Data
@CacheConfig(cacheNames = "aronCache")
@Cacheable
@ToString(exclude = "stockerItems")
@JSONType(ignores = "stockerItems")
//@IdentifierColumn("name")
public class Stocker extends BaseEntity implements Serializable {

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

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "refkey")
    private List<StockerItem> stockerItems;
}
