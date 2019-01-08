package com.aron.dao;

import com.aron.entity.UData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * description:
 * <p>UDataRepository .<br/></p>
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2018/11/20        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2018/11/20 16:32
 * @copyright: 2018, FA Software (Shanghai) Co., Ltd. All Rights Reserved.
 */
public interface UDataRepository {
    List<UData> findByRefKey(String refKey, Class<?> domainType);

    void delete(String id, Class<?> domainType);

    void delete(UData entity, Class<?> domainType);

    List<UData> findAll(Class<?> domainType);

    List<UData> findAll(Sort sort, Class<?> domainType);

    Page<UData> findAll(Pageable pageable, Class<?> domainType);

    UData save(UData entity, Class<?> domainType);

    List<UData> save(Iterable<UData> entities, Class<?> domainType);

    UData findOne(String s, Class<?> domainType);

    void deleteInBatch(Iterable<UData> entities, Class<?> domainType);
}
