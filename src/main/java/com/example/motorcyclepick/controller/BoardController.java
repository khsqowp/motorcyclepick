// 메인 게시판 기능을 처리하는 컨트롤러 패키지
package com.example.motorcyclepick.controller;

// 필요한 의존성들을 임포트

import com.example.motorcyclepick.domain.CategoryDomain;
import com.example.motorcyclepick.domain.MotorcycleDomain;
import com.example.motorcyclepick.dto.DictionaryDTO;
import com.example.motorcyclepick.dto.MotorcycleDTO;
import com.example.motorcyclepick.exception.DataAccessException;
import com.example.motorcyclepick.exception.DataNotFoundException;
import com.example.motorcyclepick.exception.FileUploadException;
import com.example.motorcyclepick.exception.MotorcycleValidationException;
import com.example.motorcyclepick.form.BoardForm;
import com.example.motorcyclepick.service.*;
import com.example.motorcyclepick.utils.FileValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// 스프링 MVC 컨트롤러 선언
@Controller
// 기본 URL 경로를 /로 지정
@RequestMapping("/")
// Lombok을 사용하여 필수 생성자 자동 생성
@RequiredArgsConstructor
// Lombok을 사용한 로깅 기능 활성화
@Slf4j
// 인증된 사용자만 접근 가능하도록 설정
@PreAuthorize("isAuthenticated()")
// CORS 설정
@CrossOrigin(origins = "*", maxAge = 3600)
// 세션에서 관리할 속성들 지정
@SessionAttributes({"boardForm", "results"})
public class BoardController {
    // 게시판 서비스 의존성 주입
    private final BoardService boardService;
    // 이미지 서비스 의존성 주입
    private final ImageService imageService;
    // 용어사전 서비스 의존성 주입
    private final DictionaryService dictionaryService;
    // 게시판 분석 서비스 의존성 주입
    private final BoardAnalService boardAnalService;

    // 보안 서비스 의존성 주입
    @Autowired
    private SecurityService securityService;
    // 파일 유효성 검사 유틸리티 의존성 주입
    @Autowired
    private FileValidator fileValidator;

    // 게시판 폼 모델 속성 초기화
    @ModelAttribute("boardForm")
    public BoardForm boardForm() {
        try {
            return new BoardForm();
        } catch (Exception e) {
            log.error("BoardForm 초기화 중 오류 발생: ", e);
            throw new RuntimeException("폼 초기화 실패", e);
        }
    }

    // 시작 페이지 표시
    @GetMapping({"/", "/startPage"})
    public String startPage(Model model) {
        try {
            // 페이지 기본 정보 설정
            model.addAttribute("siteTitle", "16Motorbikes");
            model.addAttribute("heroText", "바이크 입문, 기변병 고민 해결을 도와드려요.");
            model.addAttribute("heroDescription", "테스트를 통해 자신에게 맞는 오토바이를 추천받아보세요!");
            return "startPage";
        } catch (Exception e) {
            // 오류 로깅 및 처리
            log.error("시작 페이지 로딩 중 오류 발생: ", e);
            model.addAttribute("error", "페이지 로딩 중 오류가 발생했습니다.");
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("errorLocation", "startPage");
            model.addAttribute("lastProcessedData", "시작 페이지 로딩 시도");
            return "error";
        }
    }

    // 설문조사 페이지 표시
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
            // 오류 로깅 및 처리
            log.error("설문조사 페이지 로딩 중 오류 발생: ", e);
            model.addAttribute("error", "설문조사 페이지 로딩 중 오류가 발생했습니다.");
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("errorLocation", "surveyMotorcycle");
            model.addAttribute("errorType", e.getClass().getSimpleName());
            model.addAttribute("stackTrace", e.getStackTrace());
            return "error";
        }
    }

    // 설문조사 결과 페이지 표시
    @PostMapping("/resultPage")
    public String showResultPage(
            @ModelAttribute("boardForm") BoardForm boardForm,
            Model model,
            HttpSession session) {
        if (!isValidSurvey(boardForm)) {
            throw new MotorcycleValidationException("설문 데이터가 유효하지 않습니다");
        }
        session.setMaxInactiveInterval(1800);
        try {
            List<MotorcycleDomain> domains = boardService.getRecommendedBikes(boardForm);
            List<MotorcycleDTO> dtos = domains.stream()
                    .map(MotorcycleDTO::fromDomain)
                    .collect(Collectors.toList());

            if (dtos.isEmpty()) {
                throw new DataNotFoundException("추천할 수 있는 모터사이클을 찾을 수 없습니다");
            }

            session.setAttribute("results", dtos);
            model.addAttribute("motorcycle", dtos.get(0).toDomain());
            model.addAttribute("currentIndex", 0);
            model.addAttribute("totalResults", dtos.size());

            return "resultPage";
        } catch (DataAccessException e) {
            log.error("데이터 액세스 중 오류 발생: ", e);
            model.addAttribute("error", "데이터를 불러오는 중 오류가 발생했습니다");
            return "error";
        } catch (Exception e) {
            log.error("결과 페이지 처리 중 오류 발생: ", e);
            model.addAttribute("error", "결과 처리 중 오류가 발생했습니다");
            return "error";
        }
    }

    // 결과 페이지 네비게이션 처리
    @GetMapping("/results")
    public String navigateResults(
            @RequestParam(defaultValue = "0") int index,
            Model model,
            HttpSession session) {
        // 세션 검사
        if (session.getAttribute("results") == null) {
            return "redirect:/surveyMotorcycle";
        }
        try {
            // 세션에서 결과 목록 가져오기
            List<MotorcycleDTO> results = (List<MotorcycleDTO>) session.getAttribute("results");

            // 결과 유효성 검사
            if (results == null || results.isEmpty()) {
                throw new IllegalStateException("검색 결과를 찾을 수 없습니다");
            }

            // 인덱스 유효성 검사
            if (index < 0 || index >= results.size()) {
                throw new IllegalArgumentException("유효하지 않은 인덱스입니다");
            }

            // 선택된 결과를 모델에 추가
            MotorcycleDomain motorcycle = results.get(index).toDomain();
            model.addAttribute("motorcycle", motorcycle);
            model.addAttribute("currentIndex", index);
            model.addAttribute("totalResults", results.size());

            return "resultPage";
        } catch (Exception e) {
            // 오류 로깅 및 처리
            log.error("결과 페이지 네비게이션 중 오류 발생: ", e);
            model.addAttribute("error", "결과 페이지 처리 중 오류가 발생했습니다");
            return "error";
        }
    }

    // 설문조사 유효성 검사 메서드
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

    // 가격 포맷팅 메서드
    private String formatPrice(int price) {
        if (price >= 10000) {
            return String.format("%d억원", price / 10000);
        }
        return String.format("%d만원", price);
    }

    // 설문 응답 로깅 메서드
    private void logSurveyResponses(BoardForm boardForm) {
        // 입력값 살균 처리
        String sanitizedData = securityService.sanitizeInput(boardForm.toString());
        log.info("Sanitized survey data: {}", sanitizedData);

        // 설문 응답 데이터 로깅
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

    // 이미지 목록 조회 API
    @GetMapping("/api/images/{brand}/{model}")
    public List<String> getImages(
            @PathVariable @Pattern(regexp = "^[a-zA-Z0-9-]+$") String brand,
            @PathVariable @Pattern(regexp = "^[a-zA-Z0-9-]+$") String model) {
        try {
            // 해당 브랜드와 모델의 이미지 목록 조회
            List<String> images = imageService.getImagesForModel(brand, model);
            log.info("Images for {}/{}: {}", brand, model, images);
            return images;
        } catch (Exception e) {
            // 오류 로깅 및 처리
            log.error("이미지 로딩 중 오류 발생: " + e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    // 오토바이 이미지 업로드 페이지 표시
    @GetMapping("/uploadMotorcycle")
    public String showUploadMotorcycle(Model model) {
        try {
            // 브랜드 목록 조회
            List<String> brands = boardService.getDistinctBrands();
            model.addAttribute("brands", brands);
            return "uploadMotorcycle";
        } catch (Exception e) {
            // 오류 로깅 및 처리
            log.error("오토바이 자랑하기 페이지 로딩 중 오류 발생: ", e);
            model.addAttribute("error", "페이지 로딩 중 오류가 발생했습니다.");
            return "error";
        }
    }

    // 이미지 업로드 처리 API
    @PostMapping("/api/upload")
    @ResponseBody
    public ResponseEntity<?> uploadImage(
            @RequestParam("image") MultipartFile file,
            @RequestParam("brand") String brand,
            @RequestParam("model") String model,
            @RequestParam(value = "instagramId", required = false) String instagramId,
            @RequestParam(value = "isCustom", defaultValue = "false") boolean isCustom) {

        if (!fileValidator.validateFile(file, file.getOriginalFilename())) {
            throw new FileUploadException("유효하지 않은 파일 형식이거나 크기입니다");
        }

        brand = securityService.sanitizeInput(brand);
        model = securityService.sanitizeInput(model);
        instagramId = securityService.sanitizeInput(instagramId);

        try {
            if (file.isEmpty()) {
                throw new FileUploadException("파일이 비어있습니다");
            }

            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new FileUploadException("이미지 파일만 업로드 가능합니다");
            }

            if (instagramId != null && !instagramId.trim().isEmpty()) {
                if (!instagramId.startsWith("@")) {
                    instagramId = "@" + instagramId;
                }
            }

            String savedFileName = imageService.saveImage(file, brand, model, instagramId, isCustom);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "이미지가 성공적으로 업로드되었습니다");
            response.put("fileName", savedFileName);
            response.put("instagramId", instagramId);
            response.put("isCustom", isCustom);

            log.info("Image upload successful - Brand: {}, Model: {}, Instagram: {}, Custom: {}",
                    brand, model, instagramId, isCustom);

            return ResponseEntity.ok(response);

        } catch (FileUploadException e) {
            log.error("파일 업로드 실패: ", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (DataAccessException e) {
            log.error("이미지 저장 중 데이터베이스 오류: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("이미지 저장 중 오류가 발생했습니다");
        } catch (Exception e) {
            log.error("이미지 업로드 중 예기치 않은 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("이미지 업로드 중 오류가 발생했습니다");
        }
    }

    // 브랜드별 모델 목록 조회 API
    @GetMapping("/api/models/{brand}")
    public ResponseEntity<List<String>> getModelsByBrand(
            @PathVariable @Pattern(regexp = "^[a-zA-Z0-9-]+$") String brand) {
        try {
            // 브랜드별 모델 목록 조회
            List<String> models = boardService.getModelsByBrand(brand);
            log.info("Brand: {} - Found {} models", brand, models.size());
            return ResponseEntity.ok(models);
        } catch (Exception e) {
            // 오류 로깅 및 처리
            log.error("모델 목록 조회 중 오류 발생: ", e);
            return ResponseEntity.internalServerError().body(new ArrayList<>());
        }
    }

    // 용어사전 페이지 표시
    @GetMapping("/dictionary")
    public String showDictionary(Model model) {
        try {
            // 공개된 카테고리 목록 조회
            List<CategoryDomain> categories = dictionaryService.findAllCategoriesForPublic();
            model.addAttribute("categories", categories);
            return "dictionary";
        } catch (Exception e) {
            // 오류 로깅 및 처리
            log.error("사전 페이지 로딩 중 오류 발생: ", e);
            model.addAttribute("error", "페이지 로딩 중 오류가 발생했습니다.");
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("errorLocation", "dictionary");
            return "error";
        }
    }

    // 카테고리별 용어 목록 조회 API
    @GetMapping("/api/dictionary/terms/{category}")
    @ResponseBody
    public ResponseEntity<List<DictionaryDTO>> getTermsByCategory(@PathVariable String category) {
        try {
            // 카테고리별 공개 용어 목록 조회
            List<DictionaryDTO> terms = dictionaryService.findByCategoryForPublic(category);
            return ResponseEntity.ok(terms);
        } catch (Exception e) {
            // 오류 로깅 및 처리
            log.error("카테고리별 용어 조회 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 용어 상세 정보 조회 API
    @GetMapping("/api/dictionary/term/{id}")
    @ResponseBody
    public ResponseEntity<DictionaryDTO> getTermById(@PathVariable @Positive Long id) {
        try {
            // 용어 상세 정보 조회
            DictionaryDTO term = dictionaryService.findByIdForPublic(id);
            if (term == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(term);
        } catch (Exception e) {
            // 오류 로깅 및 처리
            log.error("용어 상세 조회 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 용어 추가 요청 처리 API
    @PostMapping("/api/dictionary/request")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")  // 인증된 사용자만 접근 가능
    public ResponseEntity<Map<String, Object>> requestTerm(@Valid @RequestBody DictionaryDTO dictionaryDTO) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 입력값 살균 처리
            if (dictionaryDTO.getTerm() != null) {
                dictionaryDTO.setTerm(securityService.sanitizeInput(dictionaryDTO.getTerm()));
            }
            if (dictionaryDTO.getDefinition() != null) {
                dictionaryDTO.setDefinition(securityService.sanitizeInput(dictionaryDTO.getDefinition()));
            }
            if (dictionaryDTO.getCategory() != null) {
                dictionaryDTO.setCategory(securityService.sanitizeInput(dictionaryDTO.getCategory()));
            }

            // 현재 인증된 사용자 정보 설정
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            dictionaryDTO.setCreatedBy(auth.getName());
            dictionaryDTO.setCreatedAt(LocalDateTime.now());
            dictionaryDTO.setStatus("PENDING");  // 관리자 승인 대기 상태로 설정

            // 용어 요청 저장
            dictionaryService.insertTermRequest(dictionaryDTO);

            // 보안 로깅
            log.info("Dictionary term request created by user: {}, term: {}",
                    auth.getName(), dictionaryDTO.getTerm());

            // 성공 응답 구성
            response.put("success", true);
            response.put("message", "용어 추가 요청이 성공적으로 등록되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 오류 로깅
            log.error("Dictionary term request failed for user: {}, error: {}",
                    SecurityContextHolder.getContext().getAuthentication().getName(), e.getMessage());

            // 실패 응답 구성
            response.put("success", false);
            response.put("message", "용어 추가 요청 처리 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}