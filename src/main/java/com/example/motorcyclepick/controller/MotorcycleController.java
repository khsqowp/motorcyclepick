package com.example.motorcyclepick.controller;

// 필요한 클래스들 import
import com.example.motorcyclepick.config.SecurityLogger;
import com.example.motorcyclepick.dto.DeleteMotorcycleDTO;
import com.example.motorcyclepick.dto.MotorcycleDTO;
import com.example.motorcyclepick.exception.*;
import com.example.motorcyclepick.form.MotorcycleForm;
import com.example.motorcyclepick.service.MotorcycleService;
import com.example.motorcyclepick.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

// 오토바이 관련 요청을 처리하는 컨트롤러
@Controller
@RequiredArgsConstructor // final 필드를 위한 생성자 자동 생성
@RequestMapping("/motorcycle") // 기본 URL 경로 설정
public class MotorcycleController {
    // 필요한 서비스 클래스들 주입
    private final MotorcycleService motorcycleService;
    private final SecurityLogger securityLogger;

    @Autowired
    private SecurityService securityService;

    // 모든 요청에 대해 관리자 권한 체크
    @ModelAttribute
    public void addAuthChecks(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            model.addAttribute("isAdmin",
                    userDetails.getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
        }
    }

    // 문자열 입력값의 앞뒤 공백 제거를 위한 설정
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        binder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    // 메인 페이지 표시
    @GetMapping({"", "/"})
    public String motorcycleMain(Model model) {
        model.addAttribute("motorcycleForm", new MotorcycleForm());
        model.addAttribute("deleteMotorcycleDTO", new DeleteMotorcycleDTO());
        return "motorcycle";
    }

    // 전체 오토바이 목록 조회
    @GetMapping("/list")
    public String listMotorcycles(Model model) {
        List<MotorcycleDTO> motorcycles = motorcycleService.findFullMotorcycleList();
        model.addAttribute("motorcycles", motorcycles);
        return "list";
    }

    // ID로 단일 오토바이 조회
    @GetMapping("/singleSearchID")
    public String viewMotorcycleById(@RequestParam(value = "id", required = false) Long id, Model model) {
        try {
            if (id == null) {
                throw new DataNotFoundException("ID가 입력되지 않았습니다");
            }
            MotorcycleDTO motorcycle = motorcycleService.findOneMotorcycle(id);
            if (motorcycle == null) {
                throw new MotorcycleNotFoundException("해당 ID의 오토바이를 찾을 수 없습니다: " + id);
            }
            model.addAttribute("motorcycleDTO", motorcycle);
            return "singleSearchID";
        } catch (DataNotFoundException | MotorcycleNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "singleSearchID";
        }
    }

    // 입력 폼 데이터 검증
    private void validateMotorcycleForm(MotorcycleForm form) {
        if (form == null) {
            throw new IllegalArgumentException("폼 데이터가 없습니다.");
        }
        if (form.getModel() == null || form.getModel().trim().isEmpty()) {
            throw new IllegalArgumentException("모델명은 필수입니다.");
        }
        if (form.getMaker() == null || form.getMaker().trim().isEmpty()) {
            throw new IllegalArgumentException("제조사명은 필수입니다.");
        }

        // XSS 방지를 위한 입력값 살균
        String sanitizedModel = securityService.sanitizeInput(form.getModel());
        String sanitizedMaker = securityService.sanitizeInput(form.getMaker());
        form.setModel(sanitizedModel);
        form.setMaker(sanitizedMaker);
    }

    // 신규 오토바이 등록 폼 표시
    @PostMapping("/new")
    public String createMotorcycle(@ModelAttribute @Valid MotorcycleForm form,
                                   RedirectAttributes redirectAttributes,
                                   @AuthenticationPrincipal UserDetails userDetails) {
        try {
            validateMotorcycleForm(form);
            motorcycleService.insertFullMotorcycle(form);
            logMotorcycleAccess("CREATE", form.getMotorcycleID(), userDetails);
            redirectAttributes.addFlashAttribute("message", "새로운 Motorcycle이 성공적으로 생성되었습니다.");
            return "redirect:/motorcycle/";
        } catch (IllegalArgumentException e) {
            throw new MotorcycleValidationException(e.getMessage(), e);
        } catch (MotorcycleValidationException | DataIntegrityException e) {
            redirectAttributes.addFlashAttribute("error", "생성 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/motorcycle/";
        }
    }

    // 오토바이 수정 폼 표시
    @GetMapping("/edit")
    public String editMotorcycle(@RequestParam("editId") Long motorcycleID, Model model) {
        try {
            MotorcycleDTO motorcycleDTO = motorcycleService.findOneMotorcycle(motorcycleID);
            MotorcycleForm motorcycleForm = MotorcycleForm.fromDTO(motorcycleDTO);
            model.addAttribute("motorcycleDTO", motorcycleDTO);
            model.addAttribute("motorcycleForm", motorcycleForm);
            return "edit";
        } catch (IllegalArgumentException e) {
            return "redirect:/motorcycle?error=" + e.getMessage();
        }
    }

    // 전체 오토바이 목록 수정 페이지 표시
    @GetMapping("/editList")
    public String editMotorcycleList(Model model) {
        List<MotorcycleDTO> motorcycles = motorcycleService.findFullMotorcycleList();
        model.addAttribute("motorcycles", motorcycles);
        return "editList";
    }

    // 다수 오토바이 정보 일괄 수정 처리
    @PostMapping("/editList")
    public String updateMotorcycleList(@ModelAttribute("forms") List<MotorcycleForm> forms) {
        motorcycleService.updateMultipleMotorcycles(forms);
        return "redirect:/motorcycle/list";
    }

    // 단일 오토바이 정보 수정 처리
    @PostMapping("/edit")
    public String updateMotorcycle(@ModelAttribute @Valid MotorcycleForm form,
                                   RedirectAttributes redirectAttributes,
                                   @AuthenticationPrincipal UserDetails userDetails) {
        try {
            validateMotorcycleForm(form);
            MotorcycleDTO existingData = motorcycleService.findOneMotorcycle(form.getMotorcycleID());
            if (existingData == null) {
                throw new IllegalArgumentException("존재하지 않는 데이터입니다.");
            }

            motorcycleService.updateFullMotorcycle(form);
            logMotorcycleAccess("UPDATE", form.getMotorcycleID(), userDetails);
            redirectAttributes.addFlashAttribute("message", "Motorcycle이 성공적으로 수정되었습니다.");
            return "redirect:/motorcycle/";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "수정 중 오류가 발생했습니다:" + e.getMessage());
            return "redirect:/motorcycle/";
        }
    }

    // 오토바이 삭제 처리 (관리자 권한 필요)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete")
    public String deleteMotorcycle(@ModelAttribute DeleteMotorcycleDTO dto,
                                   RedirectAttributes redirectAttributes,
                                   @AuthenticationPrincipal UserDetails userDetails) {
        try {
            // 관리자 권한 재확인
            if (!userDetails.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                throw new AuthorizationException("관리자 권한이 필요합니다.");
            }

            motorcycleService.deleteFullMotorcycle(dto.getMotorcycleID());
            redirectAttributes.addFlashAttribute("message", "Motorcycle이 성공적으로 삭제되었습니다.");
        } catch (AuthorizationException e) {
            redirectAttributes.addFlashAttribute("error", "권한이 없습니다: " + e.getMessage());
        } catch (DataNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "삭제할 데이터를 찾을 수 없습니다: " + e.getMessage());
        } catch (DataAccessException e) {
            redirectAttributes.addFlashAttribute("error", "삭제중 오류가 발생했습니다: " + e.getMessage());
        }
        return "redirect:/motorcycle/";
    }

    // 보안 로깅을 위한 유틸리티 메서드
    private void logMotorcycleAccess(String action, Long motorcycleId, UserDetails userDetails) {
        String username = userDetails != null ? userDetails.getUsername() : "anonymous";
        securityLogger.logDataAccessEvent(
                username,
                action,
                "Motorcycle ID: " + motorcycleId
        );
    }

    // 테스트 결과 페이지 표시
    @GetMapping("/testCode/resultPage")
    public String testResultPage(Model model, HttpSession session) {
        try {
            List<MotorcycleDTO> motorcycles = motorcycleService.findFullMotorcycleList();

            if (motorcycles.isEmpty()) {
                model.addAttribute("noResults", true);
                model.addAttribute("message", "데이터베이스에 등록된 모터사이클이 없습니다.");
                return "resultPage";
            }

            session.setAttribute("results", motorcycles);
            model.addAttribute("motorcycle", motorcycles.get(0));
            model.addAttribute("results", motorcycles);
            model.addAttribute("currentIndex", 0);
            model.addAttribute("totalResults", motorcycles.size());

            return "resultPage";

        } catch (Exception e) {
            model.addAttribute("error", "데이터 로딩 중 오류가 발생했습니다.");
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("errorLocation", "testResultPage");
            model.addAttribute("errorType", e.getClass().getSimpleName());
            return "error";
        }
    }
}