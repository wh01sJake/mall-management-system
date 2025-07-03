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

    @RequestMapping("/register")
    public Result register(String inviteCode, String username, String email, String phone, String password, HttpSession httpSession){

        // Hardcoded invite codes for registration
        final String[] VALID_INVITE_CODES = {
            "VAPE2024",
            "WELCOME123",
            "NEWUSER2024",
            "VAPEHUB2024",
            "INVITE123"
        };

        // Validate invite code
        boolean validInviteCode = false;
        for (String validCode : VALID_INVITE_CODES) {
            if (validCode.equals(inviteCode)) {
                validInviteCode = true;
                break;
            }
        }

        if (!validInviteCode) {
            return Result.error("Invalid invite code. Please contact support for a valid invite code.");
        }

        // Validate required fields
        if (username == null || username.trim().isEmpty()) {
            return Result.error("Username is required");
        }

        if (email == null || email.trim().isEmpty()) {
            return Result.error("Email is required");
        }

        if (password == null || password.trim().isEmpty()) {
            return Result.error("Password is required");
        }

        if (password.length() < 6) {
            return Result.error("Password must be at least 6 characters long");
        }

        // Check if username already exists
        Customer existingCustomer = customerService.findByUsername(username);
        if (existingCustomer != null) {
            return Result.error("Username already exists. Please choose a different username.");
        }

        // Check if email already exists
        Customer existingEmailCustomer = customerService.findByEmail(email);
        if (existingEmailCustomer != null) {
            return Result.error("Email already exists. Please use a different email address.");
        }

        // Create new customer
        Customer newCustomer = new Customer();
        newCustomer.setUsername(username.trim());
        newCustomer.setEmail(email.trim());
        newCustomer.setPhone(phone != null ? phone.trim() : null);
        newCustomer.setPassword(password); // In production, this should be hashed
        newCustomer.setStatus(1); // Active status

        try {
            customerService.save(newCustomer);
            return Result.ok("Registration successful! Please login with your credentials.");
        } catch (Exception e) {
            return Result.error("Registration failed. Please try again.");
        }
    }
}
