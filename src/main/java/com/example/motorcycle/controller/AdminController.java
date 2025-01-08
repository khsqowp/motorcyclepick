package com.example.motorcycle.controller;

import com.example.motorcycle.config.SecurityLogger;
import com.example.motorcycle.domain.CategoryDomain;
import com.example.motorcycle.domain.UserDomain;
import com.example.motorcycle.dto.CategoryDTO;
import com.example.motorcycle.dto.DictionaryDTO;
import com.example.motorcycle.dto.UserDTO;
import com.example.motorcycle.service.DictionaryService;
import com.example.motorcycle.service.ImageService;
import com.example.motorcycle.service.UserService;
import com.example.motorcycle.utils.FileValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final ImageService imageService;
    private final FileValidator fileValidator;
    private final SecurityLogger securityLogger;
    private final UserService userService;
    private final DictionaryService dictionaryService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")  // SecurityAspect의 logSecureAccess 적용
    @GetMapping("/imageAdmin")
    public String showImageAdmin(Model model) {
        try {
            List<Map<String, String>> images = imageService.getPendingImages();
            model.addAttribute("images", images);
            return "imageAdmin";
        } catch (Exception e) {
            log.error("이미지 로딩 중 오류 발생", e);
            securityLogger.logSecurityEvent("IMAGE_LOAD_FAILURE",
                    SecurityContextHolder.getContext().getAuthentication().getName(),
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr()
            );
            model.addAttribute("error", "페이지 로딩 중 오류가 발생했습니다.");
            return "error";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @PostMapping("/approve")
    @ResponseBody
    public ResponseEntity<String> approveImage(@RequestParam String fileName) {
        try {
            // FileValidator.generateSecureFilename() 호출 제거
            imageService.approveImage(fileName);  // 원본 파일명 그대로 사용
            securityLogger.logSecurityEvent("IMAGE_APPROVE_SUCCESS",
                    SecurityContextHolder.getContext().getAuthentication().getName(),
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr()
            );
            return ResponseEntity.ok("승인 완료");
        } catch (Exception e) {
            log.error("이미지 승인 중 오류: " + fileName, e);
            securityLogger.logSecurityEvent("IMAGE_APPROVE_FAILURE",
                    SecurityContextHolder.getContext().getAuthentication().getName(),
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("승인 실패");
        }
    }


    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @PostMapping("/reject")
    @ResponseBody
    public ResponseEntity<String> rejectImage(@RequestParam String fileName) {
        try {
            // FileValidator.generateSecureFilename() 호출 제거
            imageService.rejectImageToTrashCan(fileName); // 원본 파일명 그대로 사용
            securityLogger.logSecurityEvent("IMAGE_REJECT_SUCCESS",
                    SecurityContextHolder.getContext().getAuthentication().getName(),
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr()
            );
            return ResponseEntity.ok("거절 완료");
        } catch (Exception e) {
            log.error("이미지 거절 중 오류: " + fileName, e);
            securityLogger.logSecurityEvent("IMAGE_REJECT_FAILURE",
                    SecurityContextHolder.getContext().getAuthentication().getName(),
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("거절 실패");
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/memberList")
    public String listMembers(Model model, HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            securityLogger.logSecurityEvent(
                    "MEMBER_LIST_ACCESS",
                    username,
                    remoteAddr
            );

            List<UserDTO> userDTOs = userService.getAllUsers();
            model.addAttribute("users", userDTOs);

            return "memberList";
        } catch (Exception e) {
            log.error("회원 목록 조회 중 오류 발생", e);
            securityLogger.logSecurityEvent(
                    "MEMBER_LIST_ACCESS_FAILURE",
                    username,
                    remoteAddr
            );

            model.addAttribute("error", "회원 목록 조회 중 오류가 발생했습니다.");
            return "error";
        }
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/editMember/{id}")
    public String editMemberForm(@PathVariable String id, Model model) {
        try {
            UserDTO userDTO = userService.findById(id);
            if (userDTO == null) {
                securityLogger.logSecurityEvent(
                        "MEMBER_EDIT_NOT_FOUND",
                        SecurityContextHolder.getContext().getAuthentication().getName(),
                        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr()
                );
                model.addAttribute("error", "사용자를 찾을 수 없습니다.");
                return "redirect:/admin/memberList";
            }
            model.addAttribute("user", userDTO);
            return "editMember";
        } catch (Exception e) {
            securityLogger.logSecurityEvent(
                    "MEMBER_EDIT_FAILURE",
                    SecurityContextHolder.getContext().getAuthentication().getName(),
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr()
            );
            model.addAttribute("error", "사용자 정보 조회 중 오류가 발생했습니다.");
            return "redirect:/admin/memberList";
        }
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/editMember/{id}")
    public String editMember(@PathVariable String id, @ModelAttribute UserDTO userDTO, RedirectAttributes redirectAttributes) {
        try {
            UserDTO existingUser = userService.findById(id);
            if (existingUser == null) {
                securityLogger.logSecurityEvent(
                        "MEMBER_UPDATE_NOT_FOUND",
                        SecurityContextHolder.getContext().getAuthentication().getName(),
                        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr()
                );
                redirectAttributes.addFlashAttribute("error", "사용자를 찾을 수 없습니다.");
                return "redirect:/admin/memberList";
            }

            // 휴대폰 번호 포맷팅 (if not null)
            if (userDTO.getPhoneNumber() != null) {
                String formattedPhone = userDTO.getPhoneNumber().replaceAll("[^0-9]", "");
                if (formattedPhone.length() == 11) {
                    userDTO.setPhoneNumber(String.format("%s-%s-%s",
                            formattedPhone.substring(0, 3),
                            formattedPhone.substring(3, 7),
                            formattedPhone.substring(7)));
                }
            }

            // 기본값 설정
            if (userDTO.getBirthDate() == null) {
                userDTO.setBirthDate(existingUser.getBirthDate());
            }
            if (userDTO.getRegion() == null || userDTO.getRegion().isEmpty()) {
                userDTO.setRegion(existingUser.getRegion());
            }

            userService.updateUser(userDTO);
            securityLogger.logSecurityEvent(
                    "MEMBER_UPDATE_SUCCESS",
                    SecurityContextHolder.getContext().getAuthentication().getName(),
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr()
            );
            redirectAttributes.addFlashAttribute("message", "회원 정보가 성공적으로 수정되었습니다.");
            return "redirect:/admin/memberList";
        } catch (Exception e) {
            securityLogger.logSecurityEvent(
                    "MEMBER_UPDATE_FAILURE",
                    SecurityContextHolder.getContext().getAuthentication().getName(),
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr()
            );
            redirectAttributes.addFlashAttribute("error", "회원 정보 수정 중 오류가 발생했습니다.");
            return "redirect:/admin/memberList";
        }
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/deleteMember/{id}")
    public ResponseEntity<Map<String, Object>> deleteMember(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();

        try {
            String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Collection<? extends GrantedAuthority> authorities = auth != null ? auth.getAuthorities() : Collections.emptyList();
            boolean isModerator = authorities.stream()
                    .anyMatch(a -> "ROLE_MODERATOR".equals(a.getAuthority()));

            UserDTO userDTO = userService.findById(id);
            if (userDTO == null) {
                throw new RuntimeException("사용자를 찾을 수 없습니다.");
            }

            if (isModerator && ("ROLE_ADMIN".equals(userDTO.getRole()) ||
                    "ROLE_MODERATOR".equals(userDTO.getRole()))) {
                throw new RuntimeException("모더레이터는 관리자나 다른 모더레이터를 삭제할 수 없습니다.");
            }

            if (currentUser.equals(id)) {
                throw new RuntimeException("현재 로그인된 계정은 삭제할 수 없습니다.");
            }

            userService.deleteUser(id);
            response.put("success", true);
            response.put("message", "회원이 성공적으로 삭제되었습니다.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @GetMapping("/dictionaryAdmin")
    public String showDictionary(Model model, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String remoteAddr = request.getRemoteAddr();
        String username = auth.getName();

        try {
            securityLogger.logSecurityEvent(
                    "DICTIONARY_PAGE_ACCESS",
                    username,
                    remoteAddr
            );

            List<CategoryDomain> categoryDomains = dictionaryService.findAllCategories();
            List<CategoryDTO> categories = categoryDomains.stream()
                    .map(CategoryDTO::fromDomain)
                    .collect(Collectors.toList());
            model.addAttribute("categories", categories);
            return "dictionaryAdmin";

        } catch (Exception e) {
            log.error("용어사전 페이지 로딩 중 오류 발생", e);
            securityLogger.logSecurityEvent(
                    "DICTIONARY_PAGE_ACCESS_FAILURE",
                    username,
                    remoteAddr
            );

            model.addAttribute("error", "용어사전 페이지 로딩 중 오류가 발생했습니다.");
            return "error";
        }
    }


    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @PostMapping("/dictionary/category")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addCategory(
            @RequestBody CategoryDTO categoryDTO,
            HttpServletRequest request) {

        Map<String, Object> response = new HashMap<>();

        try {
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

    // 용어 존재 여부 확인
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @GetMapping("/dictionary/check-term")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> checkTermExists(@RequestParam String term) {
        Map<String, Boolean> response = new HashMap<>();
        try {
            DictionaryDTO existingTerm = dictionaryService.findByTerm(term);
            response.put("exists", existingTerm != null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("exists", false));
        }
    }

    // 용어 검색
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @GetMapping("/dictionary/search")
    @ResponseBody
    public ResponseEntity<List<DictionaryDTO>> searchTerms(@RequestParam String term) {
        try {
            List<DictionaryDTO> terms = dictionaryService.findAll().stream()
                    .filter(dto -> dto.getTerm().toLowerCase().contains(term.toLowerCase()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(terms);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }

    // 용어 상세 조회
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @GetMapping("/dictionary/{id}")
    @ResponseBody
    public ResponseEntity<DictionaryDTO> getTerm(@PathVariable Long id) {
        try {
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

    // 용어 등록
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @PostMapping("/dictionary/term")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addTerm(@RequestBody DictionaryDTO dictionaryDTO) {
        Map<String, Object> response = new HashMap<>();
        try {
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

    // 용어 수정
    @PostMapping("/dictionary/update")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateTerm(@RequestBody DictionaryDTO dictionaryDTO) {
        Map<String, Object> response = new HashMap<>();
        try {
            // ID가 없는 경우 체크
            if (dictionaryDTO.getId() == null) {
                throw new IllegalArgumentException("용어 ID가 필요합니다.");
            }

            // 해당 ID의 용어가 존재하는지 확인
            DictionaryDTO existingTerm = dictionaryService.findById(dictionaryDTO.getId());
            if (existingTerm == null) {
                throw new IllegalArgumentException("해당 용어를 찾을 수 없습니다.");
            }

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

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @PostMapping("/dictionary/request/{id}/approve")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> approveTermRequest(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            dictionaryService.approveTermRequest(id);
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @PostMapping("/dictionary/request/{id}/reject")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> rejectTermRequest(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            dictionaryService.rejectTermRequest(id);
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @ModelAttribute("termRequests")
    public List<DictionaryDTO> getTermRequests() {
        return dictionaryService.findAllPendingRequests();
    }
}