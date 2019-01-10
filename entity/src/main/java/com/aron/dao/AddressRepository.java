package com.aron.dao;

import com.aron.entity.Address;
import com.aron.entity.pk.AddressPk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * description:
 * <p>AddressRepository .<br/></p>
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2018/12/10        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2018/12/10 10:48
 * @copyright: 2018, FA Software (Shanghai) Co., Ltd. All Rights Reserved.
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, AddressPk>, JpaSpecificationExecutor<Address> {
}
