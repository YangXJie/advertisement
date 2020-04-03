package com.demo;

import com.demo.emums.EcpmEnum;
import com.demo.emums.SexEnum;
import com.demo.service.AdService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

/**
 * create on 2020-04-01
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AdServiceTest {

    @Autowired
    private AdService adService;

    @Test
    public void indexAd() {
        String[][] locations = {
                {"广州", "花都区", "广东第二师范学院"},
                {"广州", "海珠区", "广州塔"},
                {"广州", "天河区", "天河体育中心"},
                {"广州", "花都区", "花都广场"},
                {"深圳", "南山区", "腾讯大厦"},
                {"深圳", "龙华区", "深圳北站"},
                {"深圳", "南山区", "深圳西站"},
                {"广州", "海珠区", "广州大学城"},
                {"深圳", "福田区", "荔枝公园"},
                {"广州", "花都区", "广东第二师范学院"}
        };

        SexEnum[] sexEnums = {
                SexEnum.MAN,
                SexEnum.WOMAN,
                SexEnum.WOMAN,
                SexEnum.WOMAN,
                SexEnum.MAN,
                SexEnum.WOMAN,
                SexEnum.MAN,
                SexEnum.WOMAN,
                SexEnum.MAN,
                SexEnum.MAN
        };

        EcpmEnum[] ecpmEnums = {
                EcpmEnum.CPA,
                EcpmEnum.CPA,
                EcpmEnum.CPC,
                EcpmEnum.CPC,
                EcpmEnum.CPC,
                EcpmEnum.CPM,
                EcpmEnum.CPM,
                EcpmEnum.CPM,
                EcpmEnum.CPA,
                EcpmEnum.CPA
        };

        for (int i = 0; i < 10; i++) {
            adService.indexAd(
                    UUID.randomUUID().toString(),
                    locations[i],
                    sexEnums[i],
                    ecpmEnums[i],
                    Math.round(100 * Math.random()));
        }
    }
}
