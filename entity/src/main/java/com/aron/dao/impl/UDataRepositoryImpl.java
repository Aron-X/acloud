package com.aron.dao.impl;

import com.aron.dao.UDataRepository;
import com.aron.entity.UData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Table;
import java.util.List;

/**
 * description:
 * <p>UDataRepositoryImpl .<br/></p>
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2018/11/20        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2018/11/20 16:34
 * @copyright: 2018, FA Software (Shanghai) Co., Ltd. All Rights Reserved.
 */
@Repository
public class UDataRepositoryImpl implements UDataRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<UData> findByRefKey(String refKey, Class<?> domainType) {
        if (domainType == null) {
            return null;
        }
        Table tableAnnotation = domainType.getAnnotation(Table.class);
        String tableName = tableAnnotation.name();
        String uDataTableName = String.format("%s_UDATA", tableName);

        Query nativeQuery = entityManager.createNativeQuery(String.format("select * from %s t where t.REFKEY = ?", uDataTableName), UData.class);
        nativeQuery.setParameter(1, refKey);

        List<UData> resultList = nativeQuery.getResultList();

        return resultList;
    }

    @Override
    public void delete(String id, Class<?> domainType) {

    }

    @Override
    public void delete(UData entity, Class<?> domainType) {

    }

    @Override
    public List<UData> findAll(Class<?> domainType) {
        return null;
    }

    @Override
    public List<UData> findAll(Sort sort, Class<?> domainType) {
        return null;
    }

    @Override
    public Page<UData> findAll(Pageable pageable, Class<?> domainType) {
        return null;
    }

    @Override
    public UData save(UData entity, Class<?> domainType) {
        Table tableAnnotation = domainType.getAnnotation(Table.class);
        String uDataTableName = String.format("%s_UDATA", tableAnnotation.name());
        entityManager.merge(entity);
        return null;
    }

    @Override
    public List<UData> save(Iterable<UData> entities, Class<?> domainType) {
        return null;
    }

    @Override
    public UData findOne(String s, Class<?> domainType) {
        return null;
    }

    /**     
     * description:
     * <p></p>
     * change history:
     * date             defect             person             comments
     * ---------------------------------------------------------------------------------------------------------------------
     *
     * @param null
     * @return 
     * @author PlayBoy
     * @date 2018/11/21 10:14:09
     */
    @Override
    public void deleteInBatch(Iterable<UData> entities, Class<?> domainType) {

    }

}
