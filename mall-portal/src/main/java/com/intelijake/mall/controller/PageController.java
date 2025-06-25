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
        String requestURI = request.getRequestURI();
        System.out.println("requestURI: " + requestURI);
        String[] paths = requestURI.split("/");
        // ["","page","user","list"]
        // ["","page","login"]
        if (paths.length == 4) {
            return paths[2] + "_" + paths[3];
        } else if (paths.length == 3) {
            return paths[2];
        } else {
            return "index";
        }
    }
}
