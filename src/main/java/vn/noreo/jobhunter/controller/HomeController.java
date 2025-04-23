package vn.noreo.jobhunter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import vn.noreo.jobhunter.service.error.IdInvalidExeption;

public class HomeController {
    @GetMapping("/")
    public String homepage() throws IdInvalidExeption {
        if (true) {
            throw new IdInvalidExeption("Id is invalid");
        }
        return "Home";
    }
}
