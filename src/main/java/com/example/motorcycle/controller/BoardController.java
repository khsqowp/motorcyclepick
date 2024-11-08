package com.example.motorcycle.controller;

import com.example.motorcycle.form.BoardForm;
import com.example.motorcycle.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
@SessionAttributes("boardForm")
public class BoardController {

    private final BoardService boardService;

    @ModelAttribute("boardForm")
    public BoardForm boardForm() {
        return new BoardForm();
    }

    // 시작 페이지
    @GetMapping("")
    public String startPage(Model model) {
        model.addAttribute("siteTitle", "16Motorbikes");
        model.addAttribute("heroText", "드디어 제 스타일의 오토바이를 찾을 수 있어서 정말 기뻐요.");
        model.addAttribute("heroDescription", "테스트를 통해 자신에게 맞는 오토바이를 추천받아보세요!");
        return "startPage";
    }

    // 설문조사 1~5번 질문 페이지
    @GetMapping("/question1to5")
    public String showQuestion1To5(@ModelAttribute("boardForm") BoardForm boardForm) {
        return "question1to5";
    }

    // 설문조사 6~10번 질문 페이지
    @PostMapping("/question6to10")
    public String showQuestion6To10(
            @ModelAttribute("boardForm") BoardForm boardForm) {
        if (!isValidQ1toQ5(boardForm)) {
            return "redirect:/question1to5";
        }
        return "question6to10";
    }

    // 설문조사 11~15번 질문 페이지
    @PostMapping("/question11to15")
    public String showQuestion11To15(
            @ModelAttribute("boardForm") BoardForm boardForm) {
        // Q6-Q10 데이터 유효성 검사
        if (!isValidQ6toQ10(boardForm)) {
            return "redirect:/question6to10";
        }
        return "question11to15";
    }

    // 설문조사 16~20번 질문 페이지
    @PostMapping("/question16to20")
    public String showQuestion16To20(
            @ModelAttribute("boardForm") BoardForm boardForm) {
        // Q11-Q15 데이터 유효성 검사
        if (!isValidQ11toQ15(boardForm)) {
            return "redirect:/question11to15";
        }
        return "question16to20";
    }

    // 설문조사 결과를 처리하고, resultPage.html로 이동
    @PostMapping("/resultPage")
    public String showResultPage(
            @ModelAttribute("boardForm") BoardForm boardForm,
            Model model) {
        // 바이크 추천 리스트를 데이터베이스에서 가져옴
        if (!isValidQ16toQ20(boardForm)) {
            return "redirect:/question16to20";
        }

        model.addAttribute("result", boardService.getRecommendedBikes(boardForm));
        // resultPage.html로 이동
        return "resultPage";
    }

    // 유효성 검사 메소드들
    private boolean isValidQ1toQ5(BoardForm form) {
        return form.getQ1() != null && form.getQ2() != null &&
                form.getQ3() != null && form.getQ4() != null &&
                form.getQ5() != null;
    }

    private boolean isValidQ6toQ10(BoardForm form) {
        return form.getQ6() != null && form.getQ7() != null &&
                form.getQ8() != null && form.getQ9() != null &&
                form.getQ10() != null;
    }

    private boolean isValidQ11toQ15(BoardForm form) {
        return form.getQ11() != null && form.getQ12() != null &&
                form.getQ13() != null && form.getQ14() != null &&
                form.getQ15() != null;
    }

    private boolean isValidQ16toQ20(BoardForm form) {
        return form.getQ16() != null && form.getQ17() != null &&
                form.getQ18() != null && form.getQ19() != null &&
                form.getQ20() != null;
    }
}
