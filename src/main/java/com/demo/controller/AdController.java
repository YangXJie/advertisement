//package com.demo.controller;
//
//import com.demo.emums.EcpmEnum;
//import com.demo.emums.SexEnum;
//import com.demo.service.AdService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import javax.xml.stream.events.DTD;
//import java.util.Map;
//
///**
// * create on 2020-05-11
// */
//@RestController
//@RequestMapping("ad")
//public class AdController {
//
//    @Autowired
//    private AdService adService;
//
////    @PostMapping("insert")
////    public void indexAd(@RequestParam String id,
////                        @RequestParam String[] locations,
////                        @RequestParam SexEnum sexType,
////                        @RequestParam EcpmEnum ecpmType,
////                        @RequestParam(defaultValue = "0.0") double value) {
////        adService.indexAd(id, locations, sexType, ecpmType, value);
////    }
//
//    @PostMapping("insert")
//    public void indexAd(
//            @RequestBody Map<String, String> criteria) {
//        String id = criteria.get("id");
//        String[] locations = criteria.get("locations");
//        SexEnum sexType = criteria.get("sexType");
//        EcpmEnum ecpmType = criteria.get("ecpmType");
//        double value = Double.parseDouble(criteria.get("value"));
//
//        adService.indexAd(id, locations, sexType, ecpmType, value);
//    }
//
//    @GetMapping("find")
//    public String findByUser(@PathVariable String[] locations, SexEnum sexType) {
//        return adService.matchedAds(locations, sexType);
//    }
//
//    @PostMapping("/test")
//    public void test(@RequestParam("id") String id) {
//        System.out.println(id);
//    }
//}
