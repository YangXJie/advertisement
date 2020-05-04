package com.demo.service;

import com.demo.emums.EcpmEnum;
import com.demo.emums.SexEnum;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * create on 2020-03-31
 */
@Service
public class AdService {

    //用于存储广告每一千次展示的平均点击次数或平均展示次数
    private Map<EcpmEnum, Double> AVERAGE_PER_1K = new HashMap<>();
    private static final Integer AVERAGE = 1000;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    /**
     * 根据性别地址对广告进行索引
     *
     * @param id        广告id
     * @param locations 地址数组
     * @param sexType   性别
     * @param ecpmType  广告类型
     * @param value     价格
     */
    public void indexAd(
            String id, String[] locations, SexEnum sexType,
            EcpmEnum ecpmType, double value) {
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.multi();
        for (String location : locations) {
            redisTemplate.opsForSet().add("idx:req:" + location, id);
        }
        redisTemplate.opsForSet().add("idx:sex:" + sexType.name().toLowerCase(), id);

        double avg = AVERAGE_PER_1K.containsKey(ecpmType) ? AVERAGE_PER_1K.get(ecpmType) : 1;
        double rvalue = toEcpm(ecpmType, AVERAGE, avg, value);

        redisTemplate.opsForHash().put("type:", id, ecpmType.name().toLowerCase());
        redisTemplate.opsForZSet().add("idx:ad:value", id, rvalue);
        redisTemplate.opsForZSet().add("ad:base_value", id, value);

        Gson gson = new Gson();
        redisTemplate.opsForHash().put("idx:ad:" + id, "locations", gson.toJson(locations));
        redisTemplate.opsForHash().put("idx:ad:" + id, "value", String.valueOf(value));
        redisTemplate.opsForHash().put("idx:ad:" + id, "sex", sexType.name().toLowerCase());
        redisTemplate.exec();
    }

    /**
     * 根据用户特征对用户定向广告
     *
     * @param locations 用户地址
     * @param sexType   用户性别
     * @return 广告ID
     */
    public String matchedAds(String[] locations, SexEnum sexType) {
        List<String> userKey = new ArrayList<>();
        for (String location : locations) {
            userKey.add("idx:req:" + location);
        }
        String sexKey = "idx:sex:" + sexType.name().toLowerCase();
        Set<String> unionIdSet = redisTemplate.opsForSet().union(sexKey, userKey);
        userKey.clear();
        assert unionIdSet != null;
        Gson gson = new Gson();
        List<List<String>> allList = new ArrayList<>();
        for (String id : unionIdSet) {
            String adJson = (String) redisTemplate.opsForHash().get("idx:ad:" + id, "locations");
            String[] adLocations = gson.fromJson(adJson, new TypeToken<String[]>() {
            }.getType());
            String adSex = (String) redisTemplate.opsForHash().get("idx:ad:" + id, "sex");
            String rValue = (String) redisTemplate.opsForHash().get("idx:ad:" + id, "value");
            assert adLocations != null;
            List<String> list = new ArrayList<>();
            list.add(id);
            list.add(rValue);
            list.addAll(Arrays.asList(adLocations));
            list.add(adSex);
            allList.add(list);
        }
        //交集
        List<String> userFeatures = new ArrayList<>(Arrays.asList(locations));
        userFeatures.add(sexType.name().toLowerCase());
        String bestAdId = "";
        double bestAdIdRValue = .0;
        for (List<String> list : allList) {
            List<String> subList = list.subList(2, list.size());
            double featuresSize = userFeatures.size();
            subList.retainAll(userFeatures);
            double newValue = subList.size() / featuresSize * Double.parseDouble(list.get(1));
            if (bestAdIdRValue < newValue) {
                bestAdId = list.get(0);
                bestAdIdRValue = newValue;
            }
        }
        return bestAdId;
    }

    /**
     * 计算不同类型广告的ecpm
     *
     * @param ecpmType 广告类型
     * @param views    展示次数
     * @param avg      动作执行次数或点击次数
     * @param value    价格
     */
    private double toEcpm(EcpmEnum ecpmType, double views, double avg, double value) {
        switch (ecpmType) {
            case CPC:
            case CPA:
                return 1000. * value * avg / views;
            case CPM:
                return value;
        }
        return value;
    }
}
