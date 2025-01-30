// 관리자 기능을 처리하는 컨트롤러 패키지
package com.example.motorcyclepick.controller;

// 필요한 의존성들을 임포트

import com.example.motorcyclepick.config.SecurityLogger;
import com.example.motorcyclepick.domain.CategoryDomain;
import com.example.motorcyclepick.dto.CategoryDTO;
import com.example.motorcyclepick.dto.DictionaryDTO;
import com.example.motorcyclepick.dto.UserDTO;
import com.example.motorcyclepick.exception.*;
import com.example.motorcyclepick.service.DictionaryService;
import com.example.motorcyclepick.service.ImageService;
import com.example.motorcyclepick.service.SecurityService;
import com.example.motorcyclepick.service.UserService;
import com.example.motorcyclepick.utils.FileValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

// 스프링 MVC 컨트롤러임을 나타내는 어노테이션
@Controller
// 기본 URL 경로를 /admin으로 지정
@RequestMapping("/admin")
// Lombok을 사용하여 필수 생성자 자동 생성
@RequiredArgsConstructor
// Lombok을 사용한 로깅 기능 활성화
@Slf4j
public class AdminController {
    // 이미지 처리 서비스 의존성 주입
    private final ImageService imageService;
    // 파일 유효성 검사 유틸리티 의존성 주입
    private final FileValidator fileValidator;
    // 보안 로깅 서비스 의존성 주입
    private final SecurityLogger securityLogger;
    // 사용자 관리 서비스 의존성 주입
    private final UserService userService;
    // 용어사전 서비스 의존성 주입
    private final DictionaryService dictionaryService;

    // 보안 서비스 의존성 주입 (생성자 주입 대신 필드 주입 사용)
    @Autowired
    private SecurityService securityService;

    // 이미지 관리 페이지 표시 (관리자와 모더레이터 접근 가능)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @GetMapping("/imageAdmin")
    public String showImageAdmin(Model model) {
        // 현재 요청 정보 가져오기
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();

        // 데이터 접근 이벤트 로깅
        securityLogger.logDataAccessEvent(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                "IMAGE_ADMIN_ACCESS",
                request.getRequestURI()
        );

        try {
            List<Map<String, String>> images = imageService.getPendingImages();
            model.addAttribute("images", images);
            return "imageAdmin";
        } catch (DataAccessException e) {
            log.error("이미지 데이터 접근 중 오류 발생", e);
            securityLogger.logSecurityEvent("IMAGE_LOAD_FAILURE",
                    SecurityContextHolder.getContext().getAuthentication().getName(),
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                            .getRequest().getRemoteAddr()
            );
            model.addAttribute("error", "이미지 데이터를 불러오는 중 오류가 발생했습니다.");
            return "error";
        } catch (AuthorizationException e) {
            log.error("이미지 접근 권한 오류", e);
            model.addAttribute("error", "이미지 접근 권한이 없습니다.");
            return "error";
        }
    }

    // 허용된 이미지 확장자 정의
    public static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "png", "gif", "bmp");

    // 이미지 승인 처리 (관리자와 모더레이터 접근 가능)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @PostMapping("/approve")
    @ResponseBody
    public ResponseEntity<String> approveImage(@RequestParam String fileName) {
        // 현재 사용자 정보 가져오기
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String remoteAddr = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest().getRemoteAddr();

        // 파일명 유효성 검사
        if (fileName == null || fileName.trim().isEmpty()) {
            securityLogger.logSecurityEvent("INVALID_FILE_APPROVE_ATTEMPT", username, remoteAddr);
            return ResponseEntity.badRequest().body("파일명이 누락되었습니다.");
        }

        try {
            String extension = StringUtils.getFilenameExtension(fileName);
            if (extension == null || !ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
                securityLogger.logSecurityEvent("INVALID_FILE_EXTENSION_ATTEMPT", username, remoteAddr);
                throw new FileUploadException("지원하지 않는 파일 형식입니다.");
            }

            String sanitizedFilename = new File(fileName).getName();
            if (!sanitizedFilename.equals(fileName)) {
                securityLogger.logSecurityEvent("PATH_INJECTION_ATTEMPT", username, remoteAddr);
                throw new FileUploadException("잘못된 파일명입니다.");
            }

            imageService.approveImage(sanitizedFilename);
            securityLogger.logSecurityEvent("IMAGE_APPROVE_SUCCESS", username, remoteAddr);
            return ResponseEntity.ok("승인 완료");
        } catch (FileUploadException e) {
            log.error("파일 업로드 검증 오류: " + fileName, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (AuthorizationException e) {
            log.error("승인 권한 오류: " + fileName, e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("승인 권한이 없습니다.");
        } catch (DataAccessException e) {
            log.error("이미지 승인 중 데이터 접근 오류: " + fileName, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("데이터 처리 중 오류가 발생했습니다.");
        }
    }

    // 이미지 거절 처리 (관리자와 모더레이터 접근 가능)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @PostMapping("/reject")
    @ResponseBody
    public ResponseEntity<String> rejectImage(@RequestParam String fileName) {
        try {
            // 이미지 거절 처리
            imageService.rejectImageToTrashCan(fileName);
            // 보안 이벤트 로깅
            securityLogger.logSecurityEvent("IMAGE_REJECT_SUCCESS",
                    SecurityContextHolder.getContext().getAuthentication().getName(),
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                            .getRequest().getRemoteAddr()
            );
            return ResponseEntity.ok("거절 완료");
        } catch (Exception e) {
            // 오류 로깅
            log.error("이미지 거절 중 오류: " + fileName, e);
            // 보안 이벤트 로깅
            securityLogger.logSecurityEvent("IMAGE_REJECT_FAILURE",
                    SecurityContextHolder.getContext().getAuthentication().getName(),
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                            .getRequest().getRemoteAddr()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("거절 실패");
        }
    }

    // 회원 목록 조회 (관리자만 접근 가능)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/memberList")
    public String listMembers(Model model, HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            // 보안 이벤트 로깅
            securityLogger.logSecurityEvent(
                    "MEMBER_LIST_ACCESS",
                    username,
                    remoteAddr
            );

            // 모든 사용자 정보 조회
            List<UserDTO> userDTOs = userService.getAllUsers();
            model.addAttribute("users", userDTOs);

            return "memberList";
        } catch (Exception e) {
            // 오류 로깅
            log.error("회원 목록 조회 중 오류 발생", e);
            // 보안 이벤트 로깅
            securityLogger.logSecurityEvent(
                    "MEMBER_LIST_ACCESS_FAILURE",
                    username,
                    remoteAddr
            );

            model.addAttribute("error", "회원 목록 조회 중 오류가 발생했습니다.");
            return "error";
        }
    }

    // 회원 정보 수정 폼 표시 (관리자만 접근 가능)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/editMember/{id}")
    public String editMemberForm(@PathVariable String id, Model model) {
        try {
            // 사용자 정보 조회
            UserDTO userDTO = userService.findById(id);
            if (userDTO == null) {
                // 사용자를 찾을 수 없는 경우 보안 이벤트 로깅
                securityLogger.logSecurityEvent(
                        "MEMBER_EDIT_NOT_FOUND",
                        SecurityContextHolder.getContext().getAuthentication().getName(),
                        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                                .getRequest().getRemoteAddr()
                );
                model.addAttribute("error", "사용자를 찾을 수 없습니다.");
                return "redirect:/admin/memberList";
            }
            model.addAttribute("user", userDTO);
            return "editMember";
        } catch (Exception e) {
            // 오류 발생 시 보안 이벤트 로깅
            securityLogger.logSecurityEvent(
                    "MEMBER_EDIT_FAILURE",
                    SecurityContextHolder.getContext().getAuthentication().getName(),
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                            .getRequest().getRemoteAddr()
            );
            model.addAttribute("error", "사용자 정보 조회 중 오류가 발생했습니다.");
            return "redirect:/admin/memberList";
        }
    }

    // 회원 정보 수정 처리 (관리자만 접근 가능)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/editMember/{id}")
    public String editMember(@PathVariable String id,
                             @ModelAttribute UserDTO userDTO,
                             RedirectAttributes redirectAttributes) {
        try {
            UserDTO existingUser = userService.findById(id);
            if (existingUser == null) {
                throw new DataNotFoundException("사용자를 찾을 수 없습니다.");
            }

            userDTO.setId(id);
            if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
                userDTO.setPassword(existingUser.getPassword());
            }

            if (!SecurityContextHolder.getContext().getAuthentication()
                    .getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                throw new AuthorizationException("권한 변경 권한이 없습니다.");
            }

            userService.updateUser(userDTO);
            redirectAttributes.addFlashAttribute("message", "회원 정보가 성공적으로 수정되었습니다.");
            return "redirect:/admin/memberList";
        } catch (DataNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/memberList";
        } catch (AuthorizationException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/memberList";
        } catch (DataIntegrityException e) {
            redirectAttributes.addFlashAttribute("error", "회원 정보 수정 중 데이터 무결성 오류가 발생했습니다.");
            return "redirect:/admin/editMember/" + id;
        } catch (DataAccessException e) {
            redirectAttributes.addFlashAttribute("error", "데이터베이스 접근 중 오류가 발생했습니다.");
            return "redirect:/admin/editMember/" + id;
        }
    }

    // 회원 삭제 처리 (관리자만 접근 가능)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/deleteMember/{id}")
    public ResponseEntity<Map<String, Object>> deleteMember(@PathVariable String id) {
        // 입력값 검증
        String sanitizedId = securityService.sanitizeInput(id);
        if (!sanitizedId.equals(id)) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("message", "잘못된 사용자 ID 형식입니다."));
        }

        // 데이터 접근 이벤트 로깅
        securityLogger.logDataAccessEvent(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                "MEMBER_DELETE_ATTEMPT",
                "User ID: " + sanitizedId
        );
        // 응답 데이터를 저장할 Map 생성
        Map<String, Object> response = new HashMap<>();

        try {
            // 현재 로그인한 사용자 정보 가져오기
            String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            // 현재 사용자의 권한 목록 가져오기
            Collection<? extends GrantedAuthority> authorities = auth != null ? auth.getAuthorities() : Collections.emptyList();
            // 모더레이터 권한 여부 확인
            boolean isModerator = authorities.stream()
                    .anyMatch(a -> "ROLE_MODERATOR".equals(a.getAuthority()));

            // 삭제할 사용자 정보 조회
            UserDTO userDTO = userService.findById(id);
            if (userDTO == null) {
                throw new RuntimeException("사용자를 찾을 수 없습니다.");
            }

            // 모더레이터가 관리자나 다른 모더레이터를 삭제하려는 경우 예외 처리
            if (isModerator && ("ROLE_ADMIN".equals(userDTO.getRole()) ||
                    "ROLE_MODERATOR".equals(userDTO.getRole()))) {
                throw new RuntimeException("모더레이터는 관리자나 다른 모더레이터를 삭제할 수 없습니다.");
            }

            // 자기 자신을 삭제하려는 경우 예외 처리
            if (currentUser.equals(id)) {
                throw new RuntimeException("현재 로그인된 계정은 삭제할 수 없습니다.");
            }

            // 사용자 삭제 처리
            userService.deleteUser(id);
            response.put("success", true);
            response.put("message", "회원이 성공적으로 삭제되었습니다.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // 오류 응답 생성
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 용어사전 관리 페이지 표시 (관리자와 모더레이터 접근 가능)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @GetMapping("/dictionaryAdmin")
    public String showDictionary(Model model, HttpServletRequest request) {
        // 현재 인증 정보 가져오기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String remoteAddr = request.getRemoteAddr();
        String username = auth.getName();

        try {
            // 보안 이벤트 로깅
            securityLogger.logSecurityEvent(
                    "DICTIONARY_PAGE_ACCESS",
                    username,
                    remoteAddr
            );

            // 모든 카테고리 조회 및 DTO 변환
            List<CategoryDomain> categoryDomains = dictionaryService.findAllCategories();
            List<CategoryDTO> categories = categoryDomains.stream()
                    .map(CategoryDTO::fromDomain)
                    .collect(Collectors.toList());
            model.addAttribute("categories", categories);
            return "dictionaryAdmin";

        } catch (Exception e) {
            // 오류 로깅
            log.error("용어사전 페이지 로딩 중 오류 발생", e);
            // 보안 이벤트 로깅
            securityLogger.logSecurityEvent(
                    "DICTIONARY_PAGE_ACCESS_FAILURE",
                    username,
                    remoteAddr
            );

            model.addAttribute("error", "용어사전 페이지 로딩 중 오류가 발생했습니다.");
            return "error";
        }
    }

    // 카테고리 추가 처리 (관리자와 모더레이터 접근 가능)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @PostMapping("/dictionary/category")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addCategory(@RequestBody CategoryDTO categoryDTO,
                                                           HttpServletRequest request) {
        // 입력값 검증
        if (categoryDTO.getCategoryName() == null || categoryDTO.getCategoryName().trim().isEmpty() ||
                categoryDTO.getDescription() == null || categoryDTO.getDescription().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("message", "카테고리 이름과 설명은 필수입니다."));
        }

        // 입력값 보안 처리
        categoryDTO.setCategoryName(securityService.sanitizeInput(categoryDTO.getCategoryName()));
        categoryDTO.setDescription(securityService.sanitizeInput(categoryDTO.getDescription()));

        // 응답 데이터 저장용 Map 생성
        Map<String, Object> response = new HashMap<>();

        try {
            // DTO를 도메인 객체로 변환하여 저장
            CategoryDomain categoryDomain = CategoryDomain.fromDTO(categoryDTO);
            dictionaryService.insertCategory(categoryDomain);

            response.put("success", true);
            response.put("message", "카테고리가 성공적으로 추가되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 용어 존재 여부 확인 (관리자와 모더레이터 접근 가능)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @GetMapping("/dictionary/check-term")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> checkTermExists(@RequestParam String term) {
        Map<String, Boolean> response = new HashMap<>();
        try {
            // 용어 조회
            DictionaryDTO existingTerm = dictionaryService.findByTerm(term);
            response.put("exists", existingTerm != null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("exists", false));
        }
    }

    // 용어 검색 (관리자와 모더레이터 접근 가능)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @GetMapping("/dictionary/search")
    @ResponseBody
    public ResponseEntity<List<DictionaryDTO>> searchTerms(@RequestParam String term) {
        try {
            // 용어 검색 수행
            List<DictionaryDTO> terms = dictionaryService.findAll().stream()
                    .filter(dto -> dto.getTerm().toLowerCase().contains(term.toLowerCase()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(terms);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }

    // 용어 상세 조회 (관리자와 모더레이터 접근 가능)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @GetMapping("/dictionary/{id}")
    @ResponseBody
    public ResponseEntity<DictionaryDTO> getTerm(@PathVariable Long id) {
        try {
            // 용어 상세 정보 조회
            DictionaryDTO term = dictionaryService.findById(id);
            if (term != null) {
                return ResponseEntity.ok(term);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 용어 등록 처리 (관리자와 모더레이터 접근 가능)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @PostMapping("/dictionary/term")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addTerm(@RequestBody DictionaryDTO dictionaryDTO) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 용어 등록
            dictionaryService.insertTerm(dictionaryDTO);
            response.put("success", true);
            response.put("message", "용어가 성공적으로 등록되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 용어 수정 처리
    @PostMapping("/dictionary/update")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateTerm(@RequestBody DictionaryDTO dictionaryDTO) {
        // 입력값 검증
        if (dictionaryDTO.getTerm() == null || dictionaryDTO.getTerm().trim().isEmpty() ||
                dictionaryDTO.getDefinition() == null || dictionaryDTO.getDefinition().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("message", "용어와 정의는 필수입니다."));
        }

        // 입력값 보안 처리
        dictionaryDTO.setTerm(securityService.sanitizeInput(dictionaryDTO.getTerm()));
        dictionaryDTO.setDefinition(securityService.sanitizeInput(dictionaryDTO.getDefinition()));

        Map<String, Object> response = new HashMap<>();
        try {
            // ID 유효성 검사
            if (dictionaryDTO.getId() == null) {
                throw new IllegalArgumentException("용어 ID가 필요합니다.");
            }

            // 기존 용어 존재 여부 확인
            DictionaryDTO existingTerm = dictionaryService.findById(dictionaryDTO.getId());
            if (existingTerm == null) {
                throw new IllegalArgumentException("해당 용어를 찾을 수 없습니다.");
            }

            // 용어 수정
            dictionaryService.updateTerm(dictionaryDTO);
            response.put("success", true);
            response.put("message", "용어가 성공적으로 수정되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 용어 요청 승인 처리 (관리자와 모더레이터 접근 가능)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @PostMapping("/dictionary/request/{id}/approve")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> approveTermRequest(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 용어 요청 승인 처리
            dictionaryService.approveTermRequest(id);
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 용어 요청 거절 처리 (관리자와 모더레이터 접근 가능)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @PostMapping("/dictionary/request/{id}/reject")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> rejectTermRequest(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 용어 요청 거절 처리
            dictionaryService.rejectTermRequest(id);
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 모델에 용어 요청 목록 추가
    @ModelAttribute("termRequests")
    public List<DictionaryDTO> getTermRequests() {
        // 대기 중인 모든 용어 요청 조회
        return dictionaryService.findAllPendingRequests();
    }
}