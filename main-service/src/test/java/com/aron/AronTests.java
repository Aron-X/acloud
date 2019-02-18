package com.aron;

import com.aron.dao.AddressRepository;
import com.aron.dao.StockerRepository;
import com.aron.dao.UserRepository;
import com.aron.entity.Address;
import com.aron.entity.Stocker;
import com.aron.entity.User;
import com.aron.entity.pk.AddressPk;
import com.aron.generator.SnowflakeIDWorker;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class AronTests {

    @Autowired
    private StockerRepository stockerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private SnowflakeIDWorker snowflakeIDWorker;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * JpaTransactionManager事务管理 .
     */
    @Resource
    private JpaTransactionManager tm;

    @Test
    public void testSave() {
        //手动提交事务
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        def.setTimeout(30);
        //事务状态
        TransactionStatus status = tm.getTransaction(def);
        try {
            /*User user = new User();
            user.setAddress("chengdu");
            user.setAge(25);
            user.setSex("male");
            for (int i = 0; i < 5; i++) {
                user.setId(null);
                user.setName("user" + i);
                userRepository.save(user);
            }*/

            Stocker stocker = new Stocker();
            stocker.setSize(10.0);
            stocker.setStatus("LIVE");
            stocker.setAvailableFlag(true);
            for (int i = 0; i < 1; i++) {
//                stocker.setId(null);
                stocker.setStockerId(String.valueOf(i));
                stocker.setName("stocker" + i);
                stockerRepository.save(stocker);
            }
            //此处写持久层逻辑
            tm.commit(status);
        } catch (Exception e) {
            log.error("出现异常，事务回滚", e);
            if (!status.isCompleted()) {
                tm.rollback(status);
            }
            throw new RuntimeException("[制卡动作]更新卡状态为制卡审批通过失败。");
        }
    }

    @Test
    public void testSessionCache() {
        for (long i = 0; i < 5L; i++) {
            Stocker stocker = new Stocker();
//            long nextID = snowflakeIDWorker.nextID();
//            System.out.println(nextID);
//            stocker.setId(i);
            stocker.setSize(10.0);
            stocker.setStatus("LIVE");
            stocker.setAvailableFlag(true);
            stocker.setStockerId(String.valueOf(i));
            stocker.setName("stocker" + i);
            stockerRepository.save(stocker);
//            System.out.println(">>>>> " + stocker.getId());
        }
        Stocker stocker1 = new Stocker();
        Optional<Stocker> one = stockerRepository.findOne(Example.of(stocker1));

        /*Stocker stocker1 = stockerRepository.findOne(2L);
        Stocker stocker2 = stockerRepository.findOne(2L);
        Stocker stocker3 = stockerRepository.findOne(2L);
        Stocker stocker4 = stockerRepository.findOne(2L);
        Stocker byStockerId = stockerRepository.findByStockerId(492111949568409600L);*/
    }

    @Test
    public void testRepository() {
        /*List<String[]> allByCondition = stockerRepository.findAllByCondition("stocker0");
        String[] strings = allByCondition.get(0);
        System.out.println(strings[0] + "-" + strings[1]);*/
        long count = userRepository.count((root, query, cb) -> {
            Predicate condition1 = cb.equal(root.get("name"), "aron");
            Predicate condition2 = cb.equal(root.get("sex"), "man");
            Predicate or = cb.or(condition1, condition2);
            query.where(condition1, or);
            return null;
        });
        System.out.println(count);
    }

    @Test
    public void testMultiPk() {
        Address address = new Address();
        AddressPk addressPk = new AddressPk();
        addressPk.setName("test");
        addressPk.setValue("test");
        address.setIdClass(addressPk);
        address.setDetails("this is test");
        addressRepository.save(address);
    }

    @Test
    public void testUpdateMultiPk() {
        AddressPk addressPk = new AddressPk();
        addressPk.setName("test");
        addressPk.setValue("test");
        Address address1 = new Address();
        address1.setIdClass(addressPk);
        addressRepository.findAll();
        Optional<Address> one = addressRepository.findOne(Example.of(address1));
        Address address = one.orElseThrow(() -> new RuntimeException("can not found record"));
        AddressPk idClass = address.getIdClass();
//        one.getIdClass().setValue("changed");
//        one.setDetails("changed");
        addressRepository.save(address);
    }

    @Test
    public void testSql() {
        Query query = generateQuery("select * from FRUSER where name like '%'||?||'%' ", "a");
        List resultList = query.getResultList();

        Query query1 = generateQuery("select count(*) from FRUSER ");
        List resultList1 = query1.getResultList();
        System.out.println(resultList1.toString());

//        Query add = generateQuery("insert into FRUSER values(?,?,?,?,?,?,?)", "id00001", "BOM01", 12, "desc", "desc", "desc", 90);
//        System.out.println(resultList1.toString());
    }

    private Query generateQuery(String nativeSql, Object... params) {
        Query nativeQuery = entityManager.createNativeQuery(nativeSql);
        setParams(nativeQuery, params);
        return nativeQuery;
    }

    private void setParams(Query nativeQuery, Object... params) {
        if (params != null || params.length > 0) {
            for (int position = 0; position < params.length; position++) {
                nativeQuery.setParameter(position + 1, params[position]);
            }
        }
    }

    @Data
    public static class MyUser {
        private String id;

        private String name;

        private String sex;

        private Integer age;

        private String address;

        private Long grade;
    }

}
