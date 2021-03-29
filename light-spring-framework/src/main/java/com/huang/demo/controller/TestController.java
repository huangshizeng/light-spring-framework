package com.huang.demo.controller;

import com.huang.springframework.core.annotation.Controller;
import com.huang.springframework.mvc.annotation.RequestMapping;
import com.huang.springframework.mvc.annotation.RequestParam;
import com.huang.springframework.mvc.view.ModelAndView;

/**
 * @author: hsz
 * @date: 2021/3/18 18:12
 * @description:
 */

@Controller
@RequestMapping("/test")
public class TestController {

//    @Autowired
//    private TestService testService;

    @RequestMapping("/print")
    public String print(@RequestParam("p1") String p1, @RequestParam("p2") int p2, ModelAndView mv) {
        System.out.println(p1);
        System.out.println(p2);
        mv.addAttribute("p1", p1);
        mv.addAttribute("p2", p2);
        return "/test";
    }
}
