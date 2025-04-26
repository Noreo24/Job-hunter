package vn.noreo.jobhunter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.noreo.jobhunter.util.error.IdInvalidExeption;

@RestController
public class HomeController {
    @GetMapping("/")
    public String homepage() throws IdInvalidExeption {
        return "Welcome to Job Hunter API!";
    }
}
