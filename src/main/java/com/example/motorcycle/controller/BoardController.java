//package com.example.motorcycle.controller;
//
//import com.example.motorcycle.domain.MotorcycleSpec;
//import com.example.motorcycle.dto.BoardForm;
//import com.example.motorcycle.service.BoardService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@Controller
//@RequestMapping("/")
//@RequiredArgsConstructor
//@Slf4j
//@SessionAttributes("boardForm")
//public class BoardController {
//
//    private final BoardService boardService;
//
//    @ModelAttribute("boardForm")
//    public BoardForm boardForm(){
//        return new BoardForm();
//    }
//
//    // 시작 페이지
//    @GetMapping("")
//    public String startPage(Model model) {
//        model.addAttribute("siteTitle", "16Motorbikes");
//        model.addAttribute("heroText", "드디어 제 스타일의 오토바이를 찾을 수 있어서 정말 기뻐요.");
//        model.addAttribute("heroDescription", "테스트를 통해 자신에게 맞는 오토바이를 추천받아보세요!");
//        return "startPage";
//    }
//
//    // 설문조사 1~5번 질문 페이지
//    @GetMapping("/question1to5")
//    public String showQuestion1To5(Model model) {
//        model.addAttribute("boardForm", new BoardForm());
//        return "question1to5";
//    }
//
//    // 설문조사 6~10번 질문 페이지
//    @PostMapping("/question6to10")
//    public String showQuestion6To10(
//            @ModelAttribute("boardForm") BoardForm boardForm){
//        return "question6to10";
//    }
//
//    // 설문조사 11~15번 질문 페이지
//    @PostMapping("/question11to15")
//    public String showQuestion11To15(
//            @ModelAttribute("boardForm") BoardForm boardForm) {
//        return "question11to15";
//    }
//
//    // 설문조사 16~20번 질문 페이지
//    @PostMapping("/question16to20")
//    public String showQuestion16To20(
//            @ModelAttribute("boardForm") BoardForm boardForm) {
//        return "question16to20";
//    }
//
//    // 설문조사 결과를 처리하고, resultPage.html로 이동
//    @PostMapping("/resultPage")
//    public String showResultPage(
//            @ModelAttribute("boardForm") BoardForm boardForm,
//            Model model) {
//        // 바이크 추천 리스트를 데이터베이스에서 가져옴
//        model.addAttribute("recommendedBikes", boardService.getRecommendedBikes(boardForm));
//
//        // resultPage.html로 이동
//        return "resultPage";
//    }
//}
