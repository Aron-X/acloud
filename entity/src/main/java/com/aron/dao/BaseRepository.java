package com.aron.dao;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;

/**
 * description:
 * <p>BaseRepository .<br/></p>
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2018/9/21        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2018/9/21 13:44
 * @copyright: 2018, FA Software (Shanghai) Co., Ltd. All Rights Reserved.
 */
@Deprecated
public class BaseRepository<T> extends SimpleJpaRepository<T, String> {

    public BaseRepository(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
    }

    public BaseRepository(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
    }

    @Override
    public <S extends T> S save(S entity) {
        return super.save(entity);
    }
}
