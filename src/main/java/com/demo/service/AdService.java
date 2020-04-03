package com.demo.service;

import com.demo.emums.EcpmEnum;
import com.demo.emums.SexEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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
        for (String location : locations) {
            redisTemplate.opsForSet().add("idx:req:" + location, id);
        }
        redisTemplate.opsForSet().add("idx:sex:" + sexType.name().toLowerCase(), id);

        double avg = AVERAGE_PER_1K.containsKey(ecpmType) ? AVERAGE_PER_1K.get(ecpmType) : 1;
        double rvalue = toEcpm(ecpmType, AVERAGE, avg, value);

        redisTemplate.opsForHash().put("type:", id, ecpmType.name().toLowerCase());
        redisTemplate.opsForZSet().add("idx:ad:value", id, rvalue);
        redisTemplate.opsForZSet().add("ad:base_value", id, value);
    }

    /**
     * 计算不同类型广告的ecpm
     *
     * @param ecpmType 广告类型
     * @param views    展示次数
     * @param avg      动作执行次数或点击次数
     * @param value    价格
     */
    public double toEcpm(EcpmEnum ecpmType, double views, double avg, double value) {
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
