package com.example.motorcycle.controller;

import com.example.motorcycle.domain.MotorcycleDomain;
import com.example.motorcycle.form.BoardForm;
import com.example.motorcycle.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
@SessionAttributes("boardForm")
public class BoardController {

    private final BoardService boardService;
    private final ImageService imageService;

    @ModelAttribute("boardForm")
    public BoardForm boardForm() {
        try {
            return new BoardForm();
        } catch (Exception e) {
            log.error("BoardForm 초기화 중 오류 발생: ", e);
            throw new RuntimeException("폼 초기화 실패", e);
        }
    }

//    _________________________________________________________________________________________________________
    // 시작 페이지
    @GetMapping("")
    public String startPage(Model model) {
        try {
            model.addAttribute("siteTitle", "16Motorbikes");
            model.addAttribute("heroText", "바이크 입문, 기변병 고민 해결을 도와드려요.");
            model.addAttribute("heroDescription", "테스트를 통해 자신에게 맞는 오토바이를 추천받아보세요!");
            return "startPage";
        } catch (Exception e) {
            log.error("시작 페이지 로딩 중 오류 발생: ", e);
            model.addAttribute("error", "페이지 로딩 중 오류가 발생했습니다.");
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("errorLocation", "startPage");
            model.addAttribute("lastProcessedData", "시작 페이지 로딩 시도");
            return "error";
        }
    }



    // 설문조사 페이지
    @GetMapping("/surveyMotorcycle")
    public String showSurveyMotorcycle(
            @ModelAttribute("boardForm") BoardForm boardForm,
            Model model,
            HttpSession session) {
        try {
            log.info("설문조사 시작!");
            session.setAttribute("lastProcessedData", "설문조사 페이지 진입");
            return "surveyMotorcycle";
        } catch (Exception e) {
            log.error("설문조사 페이지 로딩 중 오류 발생: ", e);
            model.addAttribute("error", "설문조사 페이지 로딩 중 오류가 발생했습니다.");
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("errorLocation", "surveyMotorcycle");
            model.addAttribute("errorType", e.getClass().getSimpleName());
            model.addAttribute("stackTrace", e.getStackTrace());
            return "error";
        }
    }

    // 결과 페이지
    @PostMapping("/resultPage")
    public String showResultPage(
            @ModelAttribute("boardForm") BoardForm boardForm,
            Model model,
            HttpSession session) {
        try {
            // 현재 처리 중인 데이터를 세션에 저장
            String processedData = String.format(
                    "투자 가능 비용: %s\n" +
                            "주행 목적: %s\n" +
                            "주행 스타일: %s\n" +
                            "평균 주행 속도: %s\n" +
                            "평균 주행 거리: %s\n" +
                            "희망 주행 RPM: %s\n" +
                            "선호하는 바이크 외형: %s",
                    formatPrice(boardForm.getQuestion1() != null ? Integer.parseInt(boardForm.getQuestion1()) : 0),
                    boardForm.getQuestion2(),
                    boardForm.getQuestion3(),
                    boardForm.getQuestion4(),
                    boardForm.getQuestion5(),
                    boardForm.getQuestion6(),
                    boardForm.getQuestion7()
            );
            session.setAttribute("lastProcessedData", processedData);

            // 유효성 검사
            if (!isValidSurvey(boardForm)) {
                log.warn("일부 문항이 작성되지 않았습니다.");
                model.addAttribute("error", "필수 설문 항목이 누락되었습니다.");
                model.addAttribute("errorMessage", "모든 문항을 작성해주세요.");
                model.addAttribute("errorLocation", "surveyValidation");
                model.addAttribute("lastProcessedData", processedData);
                return "error";
            }

            // 추천 바이크 조회
            List<MotorcycleDomain> results = boardService.getRecommendedBikes(boardForm);
            if (results == null || results.isEmpty()) {
                model.addAttribute("noResults", true);
                model.addAttribute("message", "조건에 맞는 추천 결과가 없습니다.");
                model.addAttribute("motorcycle", new MotorcycleDomain());
                logSurveyResponses(boardForm);
                return "resultPage";
            }

            session.setAttribute("results", results);


            // 결과 데이터 모델에 추가
            model.addAttribute("motorcycle", results.get(0));
            model.addAttribute("results", results);
            model.addAttribute("currentIndex", 0);
            model.addAttribute("totalResults", results.size());

            // 로그 기록
            logSurveyResponses(boardForm);
            log.info("추천 결과 수: {}", results.size());

            return "resultPage";

        } catch (NumberFormatException e) {
            log.error("가격 데이터 처리 중 오류 발생: ", e);
            model.addAttribute("error", "가격 정보가 올바르지 않습니다.");
            model.addAttribute("errorMessage", "숫자 형식으로 입력해주세요.");
            model.addAttribute("errorLocation", "priceFormatting");
            model.addAttribute("errorType", "NumberFormatException");
            model.addAttribute("stackTrace", e.getStackTrace());
            return "error";

        } catch (IllegalArgumentException e) {
            log.error("잘못된 입력값 처리 중 오류 발생: ", e);
            model.addAttribute("error", "입력값이 올바르지 않습니다.");
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("errorLocation", "inputValidation");
            model.addAttribute("errorType", "IllegalArgumentException");
            model.addAttribute("stackTrace", e.getStackTrace());
            return "error";

        } catch (Exception e) {
            log.error("바이크 추천 처리 중 오류 발생: ", e);
            model.addAttribute("error", "바이크 추천 처리 중 오류가 발생했습니다.");
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("errorLocation", e.getStackTrace()[0].getFileName());
            model.addAttribute("errorLine", e.getStackTrace()[0].getLineNumber());
            model.addAttribute("errorType", e.getClass().getSimpleName());
            model.addAttribute("stackTrace", e.getStackTrace());
            model.addAttribute("lastProcessedData", session.getAttribute("lastProcessedData"));
            return "error";
        }
    }

    // 결과 페이지 네비게이션
    @GetMapping("/results")
    public String navigateResults(
            @RequestParam(defaultValue = "0") int index,
            Model model,
            HttpSession session) {
        try {
            List<MotorcycleDomain> results = (List<MotorcycleDomain>) session.getAttribute("results");

            if (results == null || results.isEmpty()) {
                model.addAttribute("error", "검색 결과를 찾을 수 없습니다.");
                model.addAttribute("errorMessage", "새로운 검색을 시작해주세요.");
                model.addAttribute("errorLocation", "resultsNavigation");
                return "error";
            }

            if (index < 0 || index >= results.size()) {
                model.addAttribute("error", "잘못된 결과 페이지 접근");
                model.addAttribute("errorMessage", "유효하지 않은 인덱스입니다.");
                model.addAttribute("errorLocation", "resultsNavigation");
                return "error";
            }

            model.addAttribute("motorcycle", results.get(index));
            model.addAttribute("currentIndex", index);
            model.addAttribute("totalResults", results.size());

            return "resultPage";

        } catch (Exception e) {
            log.error("결과 페이지 네비게이션 중 오류 발생: ", e);
            model.addAttribute("error", "결과 페이지 처리 중 오류가 발생했습니다.");
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("errorLocation", "resultsNavigation");
            model.addAttribute("errorType", e.getClass().getSimpleName());
            model.addAttribute("stackTrace", e.getStackTrace());
            return "error";
        }
    }

    // 설문조사 유효성 검사
    private boolean isValidSurvey(BoardForm boardForm) {
        return boardForm != null &&
                boardForm.getQuestion1() != null && !boardForm.getQuestion1().isEmpty() &&
                boardForm.getQuestion2() != null && !boardForm.getQuestion2().isEmpty() &&
                boardForm.getQuestion3() != null && !boardForm.getQuestion3().isEmpty() &&
                boardForm.getQuestion4() != null && !boardForm.getQuestion4().isEmpty() &&
                boardForm.getQuestion5() != null && !boardForm.getQuestion5().isEmpty() &&
                boardForm.getQuestion6() != null && !boardForm.getQuestion6().isEmpty() &&
                boardForm.getQuestion7() != null && !boardForm.getQuestion7().isEmpty();
    }

    // 가격 포맷팅
    private String formatPrice(int price) {
        if (price >= 10000) {
            return String.format("%d억원", price / 10000);
        }
        return String.format("%d만원", price);
    }

    // 설문 응답 로깅
    private void logSurveyResponses(BoardForm boardForm) {
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
    }



    //    ________________________________________________________________________________________________

    @GetMapping("/api/images/{brand}/{model}")
    @ResponseBody
    public List<String> getImages(@PathVariable String brand, @PathVariable String model) {
        try {
            List<String> images = imageService.getImagesForModel(brand, model);
            log.info("Brand: {}, Model: {} - Found {} images", brand, model, images.size());
            return images;
        } catch (Exception e) {
            log.error("이미지 목록 조회 중 오류 발생: ", e);
            return new ArrayList<>();
        }
    }

    @GetMapping("/uploadMotorcycle")
    public String showUploadMotorcycle(Model model) {
        try {
            List<String> brands = boardService.getDistinctBrands();
            model.addAttribute("brands", brands);
            return "uploadMotorcycle";
        } catch (Exception e) {
            log.error("오토바이 자랑하기 페이지 로딩 중 오류 발생: ", e);
            model.addAttribute("error", "페이지 로딩 중 오류가 발생했습니다.");
            return "error";
        }
    }

    @PostMapping("/api/upload")
    @ResponseBody  // 추가
    public ResponseEntity<?> uploadImage(
            @RequestParam("image") MultipartFile file,
            @RequestParam("brand") String brand,
            @RequestParam("model") String model) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty");
            }

            // 파일 타입 검증
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest().body("Only image files are allowed");
            }

            // 이미지 저장
            String savedFileName = imageService.saveImage(file, brand, model);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Image uploaded successfully");
            response.put("fileName", savedFileName);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Failed to upload image", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload image: " + e.getMessage());
        }
    }

    @GetMapping("/api/models/{brand}")
    @ResponseBody
    public List<String> getModelsByBrand(@PathVariable String brand) {
        try {
            List<String> models = boardService.getModelsByBrand(brand);
            log.info("Brand: {} - Found {} models", brand, models.size());
            return models;
        } catch (Exception e) {
            log.error("모델 목록 조회 중 오류 발생: ", e);
            return new ArrayList<>();
        }
    }

}