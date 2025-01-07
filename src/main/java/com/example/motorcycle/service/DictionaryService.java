package com.example.motorcycle.service;

import com.example.motorcycle.config.SecurityLogger;
import com.example.motorcycle.domain.CategoryDomain;
import com.example.motorcycle.domain.DictionaryDomain;
import com.example.motorcycle.dto.DictionaryDTO;
import com.example.motorcycle.repository.DictionaryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

            // 생성자 정보 설정
            dictionaryDTO.setCreatedBy(auth.getName());

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
            // 입력값 검증
            validateTermRequest(dictionaryDTO);

            // XSS 방지를 위한 입력값 정제
            DictionaryDTO sanitizedDTO = new DictionaryDTO();
            sanitizedDTO.setTerm(sanitizeInput(dictionaryDTO.getTerm()));
            sanitizedDTO.setDefinition(sanitizeInput(dictionaryDTO.getDefinition()));
            sanitizedDTO.setCategory(sanitizeInput(dictionaryDTO.getCategory()));

            // 기본값 설정
            sanitizedDTO.setStatus("DEACTIVE");
            sanitizedDTO.setCreatedAt(LocalDateTime.now());
            sanitizedDTO.setCreatedBy("GUEST");

            // IP 주소 로깅
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            log.info("Term request from IP: {}", request.getRemoteAddr());

            // 중복 체크
            DictionaryDomain existingTerm = dictionaryMapper.findByTerm(sanitizedDTO.getTerm());
            if (existingTerm != null) {
                throw new IllegalStateException("이미 존재하는 용어입니다.");
            }

            dictionaryMapper.insertTerm(sanitizedDTO.toDomain());

        } catch (Exception e) {
            log.error("용어 추가 요청 처리 중 오류 발생: ", e);
            throw new RuntimeException("용어 추가 요청 처리에 실패했습니다.", e);
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
        if (dto == null) {
            throw new IllegalArgumentException("요청 데이터가 없습니다.");
        }

        if (dto.getTerm() == null || dto.getTerm().trim().isEmpty()) {
            throw new IllegalArgumentException("용어는 필수입니다.");
        }

        if (dto.getDefinition() == null || dto.getDefinition().trim().isEmpty()) {
            throw new IllegalArgumentException("정의는 필수입니다.");
        }

        if (dto.getTerm().length() > 200 || dto.getDefinition().length() > 2000) {
            throw new IllegalArgumentException("용어는 200자, 정의는 2000자를 초과할 수 없습니다.");
        }

        if (!dto.getTerm().matches("^[가-힣a-zA-Z0-9\\s,.()\\-]+$")) {
            throw new IllegalArgumentException("용어에 허용되지 않는 특수문자가 포함되어 있습니다.");
        }

        if (dto.getCategory() != null && !dto.getCategory().matches("^[가-힣a-zA-Z0-9\\s]+$")) {
            throw new IllegalArgumentException("카테고리에 허용되지 않는 특수문자가 포함되어 있습니다.");
        }
    }

    public List<DictionaryDTO> findAllPendingRequests() {
        return dictionaryMapper.findByStatus("DEACTIVE").stream()
                .map(DictionaryDTO::fromDomain)
                .collect(Collectors.toList());
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

    public void rejectTermRequest(Long id) {
        dictionaryMapper.deleteTerm(id);
    }
}