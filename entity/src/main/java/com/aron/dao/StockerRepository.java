package com.aron.dao;

import com.aron.entity.Stocker;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * description:
 * This UserRepository use to XXXXXXXXXX.
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2018/6/25        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2018/6/25 12:18
 * @copyright: 2018, FA Software (Shanghai) Co., Ltd. All Rights Reserved.
 */
@Repository
public interface StockerRepository extends JpaRepository<Stocker, String>, IStockerCustomRepository {

    List<Stocker> findByStatus(String status, Pageable pageable);

    @Query("from Stocker s where s.name like CONCAT('%',?1,'%') and s.status = ?2")
    List<Stocker> findByNameAndStatusWithPage(String name, String status, Pageable pageable);

    @Query(value = "select t.name,t.status from FRSTOCKER t where t.name like '%'||?1||'%'", nativeQuery = true)
    List<String[]> findAllByCondition(String name);

    Stocker findByStockerId(String stockerId);

}
