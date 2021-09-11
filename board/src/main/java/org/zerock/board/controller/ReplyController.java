package org.zerock.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.board.dto.ReplyDTO;
import org.zerock.board.service.ReplyService;

import java.util.List;

@RestController
@RequestMapping("/replies")
@Slf4j
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService service;

    @GetMapping(value = "/board/{bno}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReplyDTO>> getListByBoard(@PathVariable("bno") Long bno) {

        log.info("bno = {}", bno);

        return new ResponseEntity<>(service.getList(bno), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Long> register(@RequestBody ReplyDTO replyDTO) {

        log.info("replyDTO={}", replyDTO);

        Long rno = service.register(replyDTO);

        return new ResponseEntity<>(rno, HttpStatus.OK);
    }

    @DeleteMapping("/{rno}")
    public ResponseEntity<String> remove(@PathVariable("rno") Long rno) {

        log.info("rno = {}", rno);

        service.remove(rno);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @PutMapping("/{rno}")
    public ResponseEntity<String> modify(@RequestBody ReplyDTO replyDTO) {

        log.info("replyDTO = {}", replyDTO);

        service.modify(replyDTO);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

}
