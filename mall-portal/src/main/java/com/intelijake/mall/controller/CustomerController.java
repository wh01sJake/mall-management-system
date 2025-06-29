package com.intelijake.mall.controller;

import com.intelijake.mall.service.ICustomerService;
import com.intelijake.mall.util.Result;
import com.intelijake.pojo.Customer;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Jake
 * @since 2025-06-23
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    ICustomerService customerService;

    @RequestMapping("/login")
    public Result login(String username, String password, HttpSession httpSession){

       Customer customer = customerService.login(username,password);

       if (customer == null){
           return Result.error("invalid username or password");
       }
       else {
           httpSession.setAttribute("customer",customer);
           return Result.ok("login success");
       }
    }

    @RequestMapping("/session-info")
    public Result sessionInfo(HttpSession httpSession){
        Customer customer = (Customer) httpSession.getAttribute("customer");
        if (customer == null) {
            return Result.error("No customer in session");
        } else {
            return Result.ok("Customer ID: " + customer.getId() + ", Username: " + customer.getUsername());
        }
    }
}
