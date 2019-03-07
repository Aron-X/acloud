package com.aron.factory;

import com.aron.dao.impl.AronRepositoryImpl;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;

import javax.persistence.EntityManager;

/**
 * description:
 * <p>AronRepositoryFactory .</p>
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2019/2/27        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2019/2/27 17:17
 * @copyright: 2019, FA Software (Chengdu) Co., Ltd. All Rights Reserved.
 */
public class AronRepositoryFactory extends JpaRepositoryFactory {

    /**
     * Creates a new {@link JpaRepositoryFactory}.
     *
     * @param entityManager must not be {@literal null}
     */
    AronRepositoryFactory(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected SimpleJpaRepository<?, ?> getTargetRepository(RepositoryInformation information, EntityManager entityManager) {
        return new AronRepositoryImpl(information.getDomainType(), entityManager);
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        return AronRepositoryImpl.class;
    }
}
