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
}