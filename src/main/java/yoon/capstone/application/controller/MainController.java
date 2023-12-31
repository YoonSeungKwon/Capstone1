package yoon.capstone.application.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import yoon.capstone.application.domain.Members;
import yoon.capstone.application.service.MemberService;
import yoon.capstone.application.vo.request.RegisterDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MainController {

    private final MemberService memberService;

    @GetMapping("/")
    public String mainPage(){
        return "Hello World!";
    }

    @GetMapping("/user")
    public String userTestPage(){
        Members members = (Members) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return members.getEmail();
    }

    @PostMapping("/test")
    public String dtoTest(@RequestBody RegisterDto dto){
        System.out.println(dto.getEmail());
        System.out.println(dto.getPassword());
        System.out.println(dto.getName());
        return "Email: " + dto.getEmail() + "\nPassword: " + dto.getPassword() + "\nName: " + dto.getName();
    }

}
