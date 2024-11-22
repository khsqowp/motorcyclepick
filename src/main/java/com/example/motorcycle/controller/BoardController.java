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
        model.addAttribute("heroText", "바이크 입문, 기변병 고민 해결을 도와드려요.");
        model.addAttribute("heroDescription", "테스트를 통해 자신에게 맞는 오토바이를 추천받아보세요!");
        return "startPage";
    }

    @GetMapping ("/surveyMotorcycle")
    public String showSurveyMotorcycle(@ModelAttribute("boardForm") BoardForm boardForm){
        log.info("설문조사 시작!");
        return "surveyMotorcycle";
    }

    @PostMapping("/resultPage")
    public String showResultPage(
            @ModelAttribute("boardForm") BoardForm boardForm, Model model) {
        // 먼저 유효성 검사
        if (!isValidSurvey(boardForm)) {
            log.warn("일부 문항이 작성되지 않았습니다.");
            return "redirect:/surveyMotorcycle";
        }

        // BoardDTO 변환과 추천 처리를 먼저 실행
        Object result = boardService.getRecommendedBikes(boardForm);
        model.addAttribute("result", result);

        // 모든 처리가 끝난 후 로그 출력
        log.info("====== 설문 응답 데이터 ======");
        log.info("====== 설문 응답 데이터 ======");
        log.info("투자 가능 비용: {}", formatPrice(Integer.parseInt(boardForm.getQuestion1())));
        log.info("주행 목적: {}", boardForm.getQuestion2());
        log.info("주행 스타일: {}", boardForm.getQuestion3());
        log.info("평균 주행 속도: {}", boardForm.getQuestion4());
        log.info("평균 주행 거리: {}", boardForm.getQuestion5());
        log.info("희망 주행 RPM: {}", boardForm.getQuestion6());
        log.info("선호하는 바이크 외형: {}", boardForm.getQuestion7());
        log.info("==============================");
        log.info("surveyMotorcycle 문항 완료");

        // resultPage.html로 이동
        return "resultPage";
    }

    // 설문조사 유효성 검사
    private boolean isValidSurvey(BoardForm boardForm) {
        return boardForm.getQuestion1() != null && !boardForm.getQuestion1().isEmpty() &&
                boardForm.getQuestion2() != null && !boardForm.getQuestion2().isEmpty() &&
                boardForm.getQuestion3() != null && !boardForm.getQuestion3().isEmpty() &&
                boardForm.getQuestion4() != null && !boardForm.getQuestion4().isEmpty() &&
                boardForm.getQuestion5() != null && !boardForm.getQuestion5().isEmpty() &&
                boardForm.getQuestion6() != null && !boardForm.getQuestion6().isEmpty() &&
                boardForm.getQuestion7() != null && !boardForm.getQuestion7().isEmpty();
    }

    // 가격 포맷팅을 위한 private 메서드 추가
    private String formatPrice(int price) {
        if (price >= 10000) {
            return String.format("%d억원", price / 10000);
        }
        return String.format("%d만원", price);
    }
}
