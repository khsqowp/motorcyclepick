package com.example.motorcyclepick.service;

import com.example.motorcyclepick.config.SecurityLogger;
import com.example.motorcyclepick.domain.CategoryDomain;
import com.example.motorcyclepick.domain.DictionaryDomain;
import com.example.motorcyclepick.dto.DictionaryDTO;
import com.example.motorcyclepick.exception.AuthorizationException;
import com.example.motorcyclepick.exception.DataAccessException;
import com.example.motorcyclepick.exception.DataIntegrityException;
import com.example.motorcyclepick.repository.DictionaryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class DictionaryService {
    private final DictionaryMapper dictionaryMapper;
    private final SecurityLogger securityLogger;

    @Autowired
    private SecurityService securityService;

    // 용어 단건 조회 메서드 추가 (기존 코드에 누락)
    public DictionaryDTO findById(Long id) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "DICTIONARY_FINDBYID_ACCESS",
                    auth.getName(),
                    request.getRemoteAddr()
            );

            DictionaryDomain domain = dictionaryMapper.findById(id);
            return domain != null ? DictionaryDTO.fromDomain(domain) : null;

        } catch (Exception e) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "DICTIONARY_FINDBYID_FAILURE",
                    auth.getName(),
                    request.getRemoteAddr()
            );
            throw e;
        }
    }

    public List<DictionaryDTO> findAll() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "DICTIONARY_FINDALL_ACCESS",
                    auth.getName(),
                    request.getRemoteAddr()
            );

            return dictionaryMapper.findAll().stream()
                    .map(DictionaryDTO::fromDomain)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "DICTIONARY_FINDALL_FAILURE",
                    auth.getName(),
                    request.getRemoteAddr()
            );
            throw e;
        }
    }

    public List<DictionaryDTO> findByCategory(String category) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "DICTIONARY_FINDBYCATEGORY_ACCESS",
                    auth.getName(),
                    request.getRemoteAddr()
            );

            return dictionaryMapper.findByCategory(category).stream()
                    .map(DictionaryDTO::fromDomain)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "DICTIONARY_FINDBYCATEGORY_FAILURE",
                    auth.getName(),
                    request.getRemoteAddr()
            );
            throw e;
        }
    }

    // 용어 검색 메서드 추가
    public DictionaryDTO findByTerm(String term) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "DICTIONARY_FINDBYTERM_ACCESS",
                    auth.getName(),
                    request.getRemoteAddr()
            );

            DictionaryDomain domain = dictionaryMapper.findByTerm(term);
            return domain != null ? DictionaryDTO.fromDomain(domain) : null;

        } catch (Exception e) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "DICTIONARY_FINDBYTERM_FAILURE",
                    auth.getName(),
                    request.getRemoteAddr()
            );
            throw e;
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public void insertTerm(DictionaryDTO dictionaryDTO) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            // 필수 필드 설정
            dictionaryDTO.setCreatedBy(auth.getName());
            dictionaryDTO.setCreatedAt(LocalDateTime.now());
            dictionaryDTO.setStatus("ACTIVE");  // 기본 상태 설정

            securityLogger.logSecurityEvent(
                    "DICTIONARY_INSERT_ACCESS",
                    auth.getName(),
                    request.getRemoteAddr()
            );

            dictionaryMapper.insertTerm(dictionaryDTO.toDomain());

        } catch (Exception e) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "DICTIONARY_INSERT_FAILURE",
                    auth.getName(),
                    request.getRemoteAddr()
            );
            throw e;
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public void updateTerm(DictionaryDTO dictionaryDTO) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            dictionaryDTO.setUpdatedBy(auth.getName());
            dictionaryDTO.setUpdatedAt(LocalDateTime.now());
            dictionaryDTO.setStatus("ACTIVE");

            securityLogger.logSecurityEvent(
                    "DICTIONARY_UPDATE_ACCESS",
                    auth.getName(),
                    request.getRemoteAddr()
            );

            dictionaryMapper.updateTerm(dictionaryDTO.toDomain());
        } catch (Exception e) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "DICTIONARY_UPDATE_FAILURE",
                    auth.getName(),
                    request.getRemoteAddr()
            );
            throw e;
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public void deleteTerm(Long id) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "DICTIONARY_DELETE_ACCESS",
                    auth.getName(),
                    request.getRemoteAddr()
            );

            dictionaryMapper.deleteTerm(id);

        } catch (Exception e) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "DICTIONARY_DELETE_FAILURE",
                    auth.getName(),
                    request.getRemoteAddr()
            );
            throw e;
        }
    }

    // 카테고리 관련 메서드들 추가
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public List<CategoryDomain> findAllCategories() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "DICTIONARY_CATEGORY_LIST_ACCESS",
                    auth.getName(),
                    request.getRemoteAddr()
            );

            return dictionaryMapper.findAllCategories();

        } catch (Exception e) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "DICTIONARY_CATEGORY_LIST_FAILURE",
                    auth.getName(),
                    request.getRemoteAddr()
            );
            throw e;
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public void insertCategory(CategoryDomain categoryDomain) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "DICTIONARY_CATEGORY_INSERT_ACCESS",
                    auth.getName(),
                    request.getRemoteAddr()
            );

            dictionaryMapper.insertCategory(categoryDomain);

        } catch (Exception e) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "DICTIONARY_CATEGORY_INSERT_FAILURE",
                    auth.getName(),
                    request.getRemoteAddr()
            );
            throw e;
        }
    }

    public List<CategoryDomain> findAllCategoriesForPublic() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "DICTIONARY_PUBLIC_CATEGORY_LIST_ACCESS",
                    auth.getName(),
                    request.getRemoteAddr()
            );

            return dictionaryMapper.findAllCategories();
        } catch (Exception e) {
            log.error("모든 카테고리 조회 중 오류 발생: ", e);
            throw new RuntimeException("카테고리 조회 실패", e);
        }
    }

    // 일반 사용자용 용어 ID 조회 메소드 추가
    public DictionaryDTO findByIdForPublic(Long id) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "DICTIONARY_PUBLIC_FINDBYID_ACCESS",
                    auth.getName(),
                    request.getRemoteAddr()
            );

            DictionaryDomain domain = dictionaryMapper.findById(id);
            return domain != null ? DictionaryDTO.fromDomain(domain) : null;
        } catch (Exception e) {
            log.error("용어 ID 조회 중 오류 발생: ", e);
            throw new RuntimeException("용어 조회 실패", e);
        }
    }

    // 일반 사용자용 카테고리별 용어 조회 메소드 추가
    public List<DictionaryDTO> findByCategoryForPublic(String category) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "DICTIONARY_PUBLIC_FINDBYCATEGORY_ACCESS",
                    auth.getName(),
                    request.getRemoteAddr()
            );

            List<DictionaryDomain> domains = dictionaryMapper.findByCategory(category);
            return domains.stream()
                    .map(DictionaryDTO::fromDomain)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("카테고리별 용어 조회 중 오류 발생: ", e);
            throw new RuntimeException("카테고리별 용어 조회 실패", e);
        }
    }

    @Transactional
    public void insertTermRequest(DictionaryDTO dictionaryDTO) {
        try {
            // 보안 검증 추가
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            securityLogger.logSecurityEvent(
                    "DICTIONARY_TERM_REQUEST_ACCESS",
                    auth.getName(),
                    ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getRemoteAddr()
            );

            // 입력값 검증
            validateTermRequest(dictionaryDTO);

            // XSS 방지를 위한 입력값 정제
            DictionaryDTO sanitizedDTO = new DictionaryDTO();
            sanitizedDTO.setTerm(securityService.sanitizeInput(dictionaryDTO.getTerm()));
            sanitizedDTO.setDefinition(securityService.sanitizeInput(dictionaryDTO.getDefinition()));
            sanitizedDTO.setCategory(securityService.sanitizeInput(dictionaryDTO.getCategory()));

            // 기본값 설정
            sanitizedDTO.setStatus("DEACTIVE");
            sanitizedDTO.setCreatedAt(LocalDateTime.now());
            sanitizedDTO.setCreatedBy(auth.getName());

            // IP 주소 로깅
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            log.info("Term request from IP: {}", request.getRemoteAddr());

            // 중복 체크
            DictionaryDomain existingTerm = dictionaryMapper.findByTerm(sanitizedDTO.getTerm());
            if (existingTerm != null) {
                throw new IllegalStateException("이미 존재하는 용어입니다.");
            }

            dictionaryMapper.insertTerm(sanitizedDTO.toDomain());

        } catch (Exception e) {
            log.error("용어 추가 요청 처리 중 오류 발생: ", e);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

            log.error("보안 위반 시도 감지: {}", e.getMessage());
            securityLogger.logSecurityEvent(
                    "SECURITY_VIOLATION",
                    auth != null ? auth.getName() : "anonymous",
                    request.getRemoteAddr()
            );

            if (e instanceof AccessDeniedException) {
                throw new AuthorizationException("용어 추가 요청 권한이 없습니다.", e);
            } else if (e instanceof DataAccessException) {
                throw new DataAccessException("용어 추가 요청 데이터 접근 중 오류가 발생했습니다.", e);
            }
            throw new DataIntegrityException("용어 추가 요청 처리 중 오류가 발생했습니다.", e);
        }
    }

    private String sanitizeInput(String input) {
        if (input == null) return null;

        // HTML 태그 제거
        String sanitized = input.replaceAll("<[^>]*>", "");

        // XSS 취약점 방지를 위한 특수문자 이스케이프
        sanitized = sanitized
                .replaceAll("&", "&amp;")
                .replaceAll("\"", "&quot;")
                .replaceAll("'", "&#x27;")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("/", "&#x2F;")
                .replaceAll("\\\\", "&#x5C;")
                .replaceAll("`", "&#x60;");

        // 개행문자 유지
        sanitized = sanitized.replaceAll("\n", "<br>");

        return sanitized.trim();
    }

    private void validateTermRequest(DictionaryDTO dto) {
        try {
            if (dto == null) {
                throw new IllegalArgumentException("요청 데이터가 없습니다.");
            }

            String sanitizedTerm = securityService.sanitizeInput(dto.getTerm());
            String sanitizedDefinition = securityService.sanitizeInput(dto.getDefinition());
            String sanitizedCategory = securityService.sanitizeInput(dto.getCategory());

            // 1. 필수값 검증
            if (sanitizedTerm == null || sanitizedTerm.trim().isEmpty()) {
                throw new IllegalArgumentException("용어는 필수입니다.");
            }

            if (sanitizedDefinition == null || sanitizedDefinition.trim().isEmpty()) {
                throw new IllegalArgumentException("정의는 필수입니다.");
            }

            // 2. 길이 검증
            if (sanitizedTerm.length() > 200 || sanitizedDefinition.length() > 2000) {
                throw new IllegalArgumentException("용어는 200자, 정의는 2000자를 초과할 수 없습니다.");
            }

            // 3. 카테고리 형식 검증 - 한글, 영문, 숫자, 공백, 쉼표 허용
            if (sanitizedCategory != null && !sanitizedCategory.trim().isEmpty()) {
                String categoryPattern = "^[가-힣a-zA-Z0-9\\s,]+$";
                if (!sanitizedCategory.matches(categoryPattern)) {
                    log.warn("유효하지 않은 카테고리 형식: {}", sanitizedCategory);
                    throw new IllegalArgumentException("카테고리에 허용되지 않는 문자가 포함되어 있습니다.");
                }
            }

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            log.error("용어 검증 중 예상치 못한 오류 발생: ", e);
            throw new IllegalArgumentException("용어 검증 중 오류가 발생했습니다.");
        }
    }

    private boolean containsXSSPayload(String input) {
        String[] xssPatterns = {
                "<script>", "javascript:", "onload=", "onerror=", "onclick=",
                "eval(", "document.cookie", "document.write"
        };

        input = input.toLowerCase();
        for (String pattern : xssPatterns) {
            if (input.contains(pattern)) {
                return true;
            }
        }
        return false;
    }

    public List<DictionaryDTO> findAllPendingRequests() {
        try {
            // 보안 검증 추가
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();


            securityLogger.logSecurityEvent(
                    "PENDING_REQUESTS_ACCESS",
                    auth.getName(),
                    ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getRemoteAddr()
            );

            return dictionaryMapper.findByStatus("DEACTIVE").stream()
                    .map(DictionaryDTO::fromDomain)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

            log.error("보안 위반 시도 감지: {}", e.getMessage());
            securityLogger.logSecurityEvent(
                    "SECURITY_VIOLATION",
                    auth != null ? auth.getName() : "anonymous",
                    request.getRemoteAddr()
            );

            if (e instanceof AccessDeniedException) {
                throw new AuthorizationException("대기중인 요청 조회 권한이 없습니다.", e);
            } else if (e instanceof DataAccessException) {
                throw new DataAccessException("대기중인 요청 데이터 접근 중 오류가 발생했습니다.", e);
            }
            throw new DataIntegrityException("대기중인 요청 처리 중 오류가 발생했습니다.", e);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public void approveTermRequest(Long id) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            DictionaryDomain term = dictionaryMapper.findById(id);
            if (term != null) {
                term.setStatus("ACTIVE");
                term.setUpdatedAt(LocalDateTime.now());
                term.setUpdatedBy(auth.getName());

                securityLogger.logSecurityEvent(
                        "DICTIONARY_APPROVE_REQUEST",
                        auth.getName(),
                        request.getRemoteAddr()
                );

                dictionaryMapper.updateTerm(term);
            }
        } catch (Exception e) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "DICTIONARY_APPROVE_REQUEST_FAILURE",
                    auth.getName(),
                    request.getRemoteAddr()
            );
            throw e;
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public void rejectTermRequest(Long id) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            securityLogger.logSecurityEvent(
                    "TERM_REJECT_REQUEST",
                    auth.getName(),
                    ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getRemoteAddr()
            );

            dictionaryMapper.deleteTerm(id);

        } catch (Exception e) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

            log.error("보안 위반 시도 감지: {}", e.getMessage());
            securityLogger.logSecurityEvent(
                    "SECURITY_VIOLATION",
                    auth != null ? auth.getName() : "anonymous",
                    request.getRemoteAddr()
            );

            if (e instanceof AccessDeniedException) {
                throw new AuthorizationException("용어 요청 거절 권한이 없습니다.", e);
            } else if (e instanceof DataAccessException) {
                throw new DataAccessException("용어 요청 거절 데이터 접근 중 오류가 발생했습니다.", e);
            }
            throw new DataIntegrityException("용어 요청 거절 처리 중 오류가 발생했습니다.", e);
        }
    }
}