package com.demo;

import com.demo.emums.EcpmEnum;
import com.demo.emums.SexEnum;
import com.demo.service.AdService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
        String id = "1111";
        String[] locations = {"深圳", "广州", "北京"};
        adService.indexAd(id, locations, SexEnum.MAN, EcpmEnum.CPA, .50);
    }
}
