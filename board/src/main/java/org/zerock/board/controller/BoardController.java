package org.zerock.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.service.BoardService;

@Controller
@RequestMapping("/board")
@Slf4j
@RequiredArgsConstructor
public class BoardController {

    private final BoardService service;

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model) {

        log.info("pageRequestDTO = {}", pageRequestDTO);

        model.addAttribute("result", service.getList(pageRequestDTO));
    }

    @GetMapping("/register")
    public void register() {

        log.info("register get");
    }

    @PostMapping("/register")
    public String registerPost(BoardDTO dto, RedirectAttributes attr) {

        log.info("dto = {}", dto);

        // 새로 추가된 엔티티의 번호
        Long bno = service.register(dto);

        log.info("bno = {}", bno);

        attr.addFlashAttribute("msg", bno);

        return "redirect:/board/list";
    }

    @GetMapping({"/read", "modify"})
    public void read(@ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO, Long bno, Model model) {

        log.info("bno = {}", bno);

        BoardDTO boardDTO = service.get(bno);

        log.info("boardDTO = {}", boardDTO);

        model.addAttribute("dto", boardDTO);
    }

    @PostMapping("/remove")
    public String remove(long bno, RedirectAttributes attr) {

        log.info("bno = {}", bno);

        service.removeWithReplies(bno);

        attr.addFlashAttribute("msg", bno);

        return "redirect:/board/list";
    }

    @PostMapping("/modify")
    public String modify(BoardDTO dto,
                         @ModelAttribute("requestDTO") PageRequestDTO requestDTO,
                         RedirectAttributes redirectAttributes) {

        log.info("post modify");
        log.info("dto={}", dto);

        service.modify(dto);

        redirectAttributes.addAttribute("page", requestDTO.getPage());
        redirectAttributes.addAttribute("type", requestDTO.getType());
        redirectAttributes.addAttribute("keyword", requestDTO.getKeyword());

        redirectAttributes.addAttribute("bno", dto.getBno());

        return "redirect:/board/read";
    }

}
