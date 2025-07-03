package com.intelijake.mall.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

// /page/user/list   user_list.html
@Controller
@RequestMapping("/page")
public class PageController {

    @RequestMapping("/**")
    public String path(HttpServletRequest request) {
        // /page/user/list
        // /page/login
        // /page/order/detail/123456
        String requestURI = request.getRequestURI();
        System.out.println("requestURI: " + requestURI);
        String[] paths = requestURI.split("/");
        // ["","page","user","list"]
        // ["","page","login"]
        // ["","page","order","detail","123456"]
        if (paths.length == 5) {
            // Handle /page/order/detail/{orderNo} -> order_detail.html
            return paths[2] + "_" + paths[3];
        } else if (paths.length == 4) {
            return paths[2] + "_" + paths[3];
        } else if (paths.length == 3) {
            return paths[2];
        } else {
            return "index";
        }
    }
}
