package com.aron.dao;

import com.aron.entity.BaseEntity;
import com.aron.utils.Changeable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * description:
 * <p>AronRepository .</p>
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2019/2/27        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2019/2/27 16:29
 * @copyright: 2019, FA Software (Chengdu) Co., Ltd. All Rights Reserved.
 */
@NoRepositoryBean
public interface AronRepository<T> extends JpaRepository<T, String>, JpaSpecificationExecutor<T> {
    void print();

    /**
     * saveAllWithChangeable
     *
     * @param entities
     * @param <S>
     * @return data
     */
    <S extends T> List<S> modifyAll(Iterable<Changeable<S>> entities);

    <S extends T> S modify(Changeable<S> entity);
}
