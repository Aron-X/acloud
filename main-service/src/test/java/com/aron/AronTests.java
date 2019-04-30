package com.aron;

import com.alibaba.fastjson.JSONObject;
import com.aron.bo.BoFactory;
import com.aron.bo.StockerBO;
import com.aron.bo.StockerBOImpl;
import com.aron.dao.AddressRepository;
import com.aron.dao.StockerRepository;
import com.aron.dao.UserRepository;
import com.aron.entity.Address;
import com.aron.entity.Stocker;
import com.aron.entity.StockerItem;
import com.aron.entity.pk.AddressPk;
import com.aron.service.IMyService;
import com.aron.utils.ObjectIdentifier;
import com.aron.utils.SpringContextUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.Predicate;
import java.io.Serializable;
import java.util.ArrayList;
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

    @Autowired
    private IMyService myService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private BoFactory boFactory;

    /*@Autowired
    private MessageSender messageSender;*/

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

    @Test
    public void test10() {
        Query nativeQuery = entityManager.createNativeQuery("select name from FRSTOCKER where id=?", String.class);
        nativeQuery.setParameter(1, "Stocker.97307915685199872");
        List resultList = nativeQuery.getResultList();
        String singleResult = (String) nativeQuery.getSingleResult();
    }

    @Test
    public void test11() {
        String sid = SnowflakeIDWorker.getInstance().generateId(Stocker.class);

        Stocker stocker = stockerRepository.findById("123").orElse(null);
        stocker.setName("update1");
        stocker.setId(sid);
        stockerRepository.save(stocker);
    }

    @Test
    public void test12() {
        ObjectIdentifier objectIdentifier = new ObjectIdentifier();
        objectIdentifier.setReferenceKey("123");

        StockerBO stockerBO1 = boFactory.convertObjectIdentifierToBO(objectIdentifier, StockerBO.class);

//        List<StockerItem> stockerInfos = stockerBO1.getStockerItems();
        //StockerInfo stockerInfo = stockerBO1.getStockerInfo();
        stockerBO1.setName("this is Aron XXXX");
        List<StockerItem> stockerInfos = new ArrayList<>();
        StockerItem stockerItem = new StockerItem();
        stockerItem.setId("StockerItem.155109141474443264-test");
        stockerItem.setMaxCount(100);
        stockerItem.setRefkey("123");
        stockerItem.setStockerInfo("lalala");

        stockerInfos.add(stockerItem);

        stockerBO1.setStockerItems(stockerInfos);

        List<StockerItem> stockerItems = stockerBO1.getStockerItems();
        System.out.println(">>>>>" + stockerBO1.toString());
    }

    @Test
    public void test13() {
        ObjectIdentifier objectIdentifier = new ObjectIdentifier();
        objectIdentifier.setReferenceKey("123");
        StockerBO stockerBO1 = boFactory.convertObjectIdentifierToBO(objectIdentifier, StockerBO.class);

        StockerBO bean1 = SpringContextUtil.getBean(StockerBOImpl.class, new Stocker());
        StockerBO bean2 = SpringContextUtil.getBean(StockerBOImpl.class, new Stocker());
        StockerBO bean3 = SpringContextUtil.getBean(StockerBOImpl.class, new Stocker());

        List<StockerItem> stockerItems = stockerBO1.getStockerItems();

        System.out.println(stockerBO1.toString());
        System.out.println(bean1.toString());
        System.out.println(bean2.toString());
        System.out.println(bean3.toString());
    }

    @Test
    //@Transactional(rollbackFor = Exception.class)
    public void test14() {
        Optional<Stocker> stocker = stockerRepository.findById("123");

        /*List<StockerItem> stockerItems = stocker.get().getStockerItems();
        StockerItem stockerItem = stockerItems.get(0);
        stockerItem.setStockerInfo("asdasd");

        stocker.get().setName("this is my test");
        stockerRepository.save(stocker.get());
        System.out.println(stocker.get().toString());*/
    }

    /*@Test
    public void test15() {
        Request request = new Request();
        Stocker stocker = stockerRepository.findById("123").orElse(null);
        request.setMessageBody(stocker);
        request.setFunctionId("release");
        request.setUser(new RequestUser("aron.xu"));

        String json = JSONObject.toJSONString(RequestReply.request(request), SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullBooleanAsFalse);
        System.out.println("---" + json);
        JSONObject jsonObject = JSONObject.parseObject(json);
        messageSender.sendAndReceive("OM-REQUEST", jsonObject);
        *//*MsgBean msgBean = new MsgBean();
        MsgBean.RequestBean requestBean = new MsgBean.RequestBean();
        requestBean.setMessageBody(stocker);
        requestBean.setUser(new MsgBean.RequestBean.UserBean("aron.xu"));
        msgBean.setRequest(requestBean);*//*
        //messageSender.sendAndReceive("release-request", msgBean);
    }*/

    /*@Test
    public void test16() {
        UserReleaseBean userReleaseBean = new UserReleaseBean();
        userReleaseBean.setAction("create");
        userReleaseBean.setReleaseType("user");

        String json = "{\"userId\":\"aron\",\"userName\":\"aron\",\"company\":\"fa\",\"department\":\"pdc\",\"password\":\"aron123\"," +
                "\"expiredPeriod\":1,\"supervisorFlag\":true,\"eMailAddress\":\"aron.xu@fa-software.com\",\"phoneNumber\":\"123456789\"," +
                "\"privilegeGroups\":[\"OM\"],\"pptAreaGroups\":[\"Fab_A\"],\"brmUserGroups\":[\"MMer\"],\"brmReleasePermissionFlag\":true," +
                "\"brmReleaseConditionFlag\":true,\"brmDeletePermissionFlag\":true,\"brmDeleteConditionFlag\":true," +
                "\"brmActivatePermissionFlag\":true,\"userDataSets\":{\"type\":\"string\",\"value\":\"123\",\"name\":\"test\"}}";
        UserReleaseBean.BodyBean bodyBean = JSONObject.parseObject(json, UserReleaseBean.BodyBean.class);
        userReleaseBean.setBody(bodyBean);

        Request request = new Request();
        request.setMessageBody(userReleaseBean);
        request.setFunctionId("release");
        request.setUser(new RequestUser("aron.xu"));
        String sendJson = JSONObject.toJSONString(RequestReply.request(request), SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullBooleanAsFalse);
        System.out.println("---" + sendJson);
        JSONObject jsonObject = JSONObject.parseObject(sendJson);
        messageSender.sendAndReceive("OM-REQUEST", jsonObject);
    }*/

    @Test
    public void test17() {
        MyBean myBean = new MyBean();
        myBean.setName("asd");
        String jsonString = JSONObject.toJSONString(myBean);
        System.out.println(jsonString);
    }

    @Data
    public static class MyBean implements Serializable {

        private static final long serialVersionUID = -3151873736216750404L;

        private static String ASDSD = "12112";
        private String name;
    }

}
