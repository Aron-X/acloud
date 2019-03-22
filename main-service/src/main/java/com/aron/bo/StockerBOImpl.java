package com.aron.bo;

import com.aron.annotation.Business;
import com.aron.annotation.DoChild;
import com.aron.annotation.DoMapping;
import com.aron.annotation.DoProperty;
import com.aron.dao.StockerInfoRepository;
import com.aron.dao.StockerItemRepository;
import com.aron.dao.StockerRepository;
import com.aron.entity.Stocker;
import com.aron.entity.StockerInfo;
import com.aron.entity.StockerItem;
import com.aron.utils.Changeable;
import com.aron.utils.ObjectIdentifier;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * description:
 * <p>StockerBO .</p>
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2019/2/28        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2019/2/28 14:10
 * @copyright: 2019, FA Software (Chengdu) Co., Ltd. All Rights Reserved.
 */
@DoMapping(clazz = Stocker.class)
@Business("newStockerBOImpl")
@Scope("prototype")
@ToString
public class StockerBOImpl extends AbstractBO<Stocker> implements StockerBO {

    @Autowired
    private StockerRepository stockerRepository;

    @Autowired
    private StockerInfoRepository stockerInfoRepository;

    @Autowired
    private StockerItemRepository stockerItemRepository;

    public StockerBOImpl(Stocker stockerDo) {
        super(stockerDo);
    }

    @Getter(value = AccessLevel.PUBLIC)
    @Setter(value = AccessLevel.PROTECTED)
    @DoProperty(attributes = {"id", "stockerId"}, mapping = {"referenceKey", "value"}, type = DoProperty.Type.POJO)
    private ObjectIdentifier theIdentify;

    @Getter(value = AccessLevel.PROTECTED)
    @Setter(value = AccessLevel.PROTECTED)
    @DoProperty(attributes = "name")
    private String theName;

    @Getter(value = AccessLevel.PROTECTED)
    @Setter(value = AccessLevel.PROTECTED)
    @DoProperty(attributes = "size")
    private Double theSize;

    @Getter(value = AccessLevel.PROTECTED)
    @Setter(value = AccessLevel.PROTECTED)
    @DoProperty(attributes = "status")
    private String theStatus;

    @Getter(value = AccessLevel.PROTECTED)
    @Setter(value = AccessLevel.PROTECTED)
    @DoProperty(attributes = "availableFlag")
    private Boolean theAvailableFlag;

    @Getter(value = AccessLevel.PROTECTED)
    @Setter(value = AccessLevel.PROTECTED)
    @DoChild(type = DoChild.TYPE.LIST, child = StockerItem.class)
    private List<Changeable<StockerItem>> theStockerItems;

    @Getter(value = AccessLevel.PROTECTED)
    @Setter(value = AccessLevel.PROTECTED)
    @DoChild(type = DoChild.TYPE.SINGLE, child = StockerInfo.class, lazy = false)
    private Changeable<StockerInfo> theStockerInfo;

    @Transactional(rollbackFor = Exception.class)
    protected void pushChanges() {
        System.out.println("==== start push changes ====");
        stockerRepository.save(getEntity());
        stockerInfoRepository.modify(theStockerInfo);
        stockerItemRepository.modifyAll(theStockerItems);
        //TODO: 所有子表更新优化为一行代码
        System.out.println("==== end push changes ====");
    }

    @Override
    public void setName(String pName) {
        this.theName = pName;
        getEntity().setName(pName);//TODO： 优化自动映射
    }

    @Override
    public String getName() {
        return this.theName;
    }

    @Override
    public void setStockerItems(List<StockerItem> stockerItems) {
        if (theStockerItems == null) {
            List<StockerItem> stockerItems1 = getStockerItems();
            if (theStockerItems == null) {
                theStockerItems = new ArrayList<>();
            }
        }
        BigDecimal bigDecimal = new BigDecimal("");
        ///====== below is logic ========
        for (Changeable<StockerItem> theStockerItem : theStockerItems) {
            StockerItem stockerItem = theStockerItem.get();
            if (stockerItem.getMaxCount() == 3) {
                theStockerItem.setStatus(Changeable.Status.DELETED);
            }
        }

        for (StockerItem stockerInfo : stockerItems) {
            boolean exist = false;
            for (Changeable<StockerItem> theStockerItem : theStockerItems) {
                if (theStockerItem.get().getId().equals(stockerInfo.getId())) {
                    exist = true;
                    continue;
                }
            }
            if (!exist) {
                theStockerItems.add(Changeable.of(stockerInfo, Changeable.Status.CHANGED));
            }
        }
    }

    @Override
    public List<StockerItem> getStockerItems() {
        if (this.getTheStockerItems() == null) {
            List<StockerItem> result =
                    stockerItemRepository.findAll((Specification<StockerItem>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                            root.get("refkey"),
                            this.getTheIdentify().getReferenceKey()));
            this.setTheStockerItems(new ArrayList<>());
            for (StockerItem stockerItem : result) {
                this.getTheStockerItems().add(Changeable.of(stockerItem));
            }
            return result;
        }
        List<StockerItem> result = new ArrayList<>();
        for (Changeable<StockerItem> itemChangeable : this.getTheStockerItems()) {
            if (itemChangeable.getStatus() != Changeable.Status.DELETED) {
                result.add(itemChangeable.get());
            }
        }
        return result;
    }

    @Override
    public StockerInfo getStockerInfo() {
        if (theStockerInfo == null) {
            Optional<StockerInfo> result =
                    stockerInfoRepository.findOne((Specification<StockerInfo>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                            root.get("refkey"),
                            theIdentify.getReferenceKey()));
            StockerInfo stockerInfo = result.orElse(null);
            this.theStockerInfo = Changeable.of(stockerInfo);
            return stockerInfo;
        }
        return theStockerInfo.getStatus() == Changeable.Status.DELETED ? null : theStockerInfo.get();
    }

    @Override
    public StockerInfo setStockerInfo(StockerInfo data) {
        if (this.theStockerInfo == null) {
            StockerInfo stockerInfo = new StockerInfo();
            BeanUtils.copyProperties(data, stockerInfo);
            return stockerInfo;
        }
        return theStockerInfo.getStatus() == Changeable.Status.DELETED ? null : theStockerInfo.get();
    }
}
