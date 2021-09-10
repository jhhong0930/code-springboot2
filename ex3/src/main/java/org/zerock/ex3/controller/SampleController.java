package org.zerock.ex3.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sample")
@Slf4j
public class SampleController {

    @GetMapping("/ex1")
    public void ex1() {
        log.info("ex1");
    }

}
