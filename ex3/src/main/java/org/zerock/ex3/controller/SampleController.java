package org.zerock.ex3.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.ex3.dto.SampleDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/sample")
@Slf4j
public class SampleController {

    @GetMapping("/ex1")
    public void ex1() {
        log.info("ex1");
    }

    @GetMapping({"/ex2", "/exLink"})
    public void exModel(Model model) {

        List<SampleDTO> list = IntStream.rangeClosed(1, 20)
                .asLongStream()
                .mapToObj(i -> {
                    SampleDTO dto = SampleDTO.builder()
                            .sno(i)
                            .first("First" + i)
                            .last("Last" + i)
                            .regTime(LocalDateTime.now())
                            .build();

                    return dto;
                }).collect(Collectors.toList());

        model.addAttribute("list", list);

    }

    @GetMapping("/exInline")
    public String exInline(RedirectAttributes attr) {

        log.info("exInline");

        SampleDTO dto = SampleDTO.builder()
                .sno(100L)
                .first("Fisrt100")
                .last("Last100")
                .regTime(LocalDateTime.now())
                .build();

        attr.addFlashAttribute("result", "success");
        attr.addFlashAttribute("dto", dto);

        return "redirect:/sample/ex3";
    }

    @GetMapping("/ex3")
    public void ex3() {
        log.info("ex3");
    }

    @GetMapping({"/exLayout1", "exLayout2"})
    public void exLayout1() {
        log.info("exLayout");
    }

}
