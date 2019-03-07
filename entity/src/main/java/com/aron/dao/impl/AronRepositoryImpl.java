package com.aron.dao.impl;

import com.aron.SnowflakeIDWorker;
import com.aron.dao.AronRepository;
import com.aron.entity.BaseEntity;
import com.aron.utils.Changeable;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

/**
 * description:
 * <p>AronRepositoryImpl .</p>
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2019/2/27        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2019/2/27 16:30
 * @copyright: 2019, FA Software (Chengdu) Co., Ltd. All Rights Reserved.
 */
public class AronRepositoryImpl<T extends BaseEntity> extends SimpleJpaRepository<T, String> implements AronRepository<T> {

    private final EntityManager entityManager;

    /*public AronRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
    }*/

    public AronRepositoryImpl(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
        this.entityManager = em;
    }

    @Override
    public <S extends T> S save(S entity) {
        SnowflakeIDWorker instance = SnowflakeIDWorker.getInstance();
        if (StringUtils.isEmpty(entity.getId())) {
            System.out.println("====== set id by aron ====");
            entity.setId(instance.generateId(entity.getClass()));
        }
        return super.save(entity);
    }

    @Override
    public <S extends T> List<S> modifyAll(Iterable<Changeable<S>> entities) {
        if (entities == null) {
            return null;
        }
        List<S> result = new ArrayList<>();
        for (Changeable<S> entity : entities) {
            S modifyEntity = modify(entity);
            if (modifyEntity != null) {
                result.add(modifyEntity);
            }
        }
        return result;
    }

    @Override
    public <S extends T> S modify(Changeable<S> entity) {
        if (entity == null) {
            return null;
        }
        if (entity.getStatus() == Changeable.Status.CHANGED) {
            return this.save(entity.get());
        }
        if (entity.getStatus() == Changeable.Status.DELETED) {
            this.delete(entity.get());
            return null;
        }
        return entity.get();
    }

    @Override
    public void print() {
        System.out.println(">>>>>" + entityManager.toString());
    }
}
