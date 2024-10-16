package com.example.motorcycle.controller;

import com.example.motorcycle.dto.*;
import com.example.motorcycle.service.MotorcycleService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/motorcycle")
public class MotorcycleController {

    @Autowired
    private MotorcycleService motorcycleService;  // MotorcycleService 주입

    // WebDataBinder 초기화: 문자열 입력값을 trim하여 null로 변환
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        binder.registerCustomEditor(String.class, stringTrimmerEditor); // 문자열 데이터를 trim하여 null로 설정
    }

    // 메인 페이지로 이동 (Motorcycle 메인 페이지 표시)
    // 사용자가 URL "/motorcycle/"로 접근할 때 처리
    @GetMapping("/")
    public String motorcycleMain(Model model) {
        model.addAttribute("motorcycleDTO", new MotorcycleDTO()); // 빈 MotorcycleDTO 객체를 모델에 추가하여 폼 데이터로 사용
        model.addAttribute("deleteMotorcycleDTO", new DeleteMotorcycleDTO()); // 삭제용 DTO 객체 추가하여 삭제 요청 처리
        return "motorcycle"; // motorcycle.html로 이동하여 데이터를 표시 (뷰 렌더링)
    }

    // 리스트 페이지로 이동 (모든 Motorcycle 데이터 리스트를 표시)
    // 사용자가 URL "/motorcycle/list"로 접근할 때 처리
    @GetMapping("/list")
    public String listMotorcycles(Model model) {
        List<MotorcycleDTO> motorcycles = motorcycleService.findFullMotorcycleList(); // 모든 Motorcycle 데이터를 DTO 형태로 조회
        model.addAttribute("motorcycles", motorcycles); // 모델에 리스트 데이터 추가하여 뷰에 전달
        return "list"; // list.html로 이동하여 데이터를 표시 (타임리프 템플릿 사용)
    }

    // 특정 Motorcycle 데이터를 조회하는 페이지 매핑
    @GetMapping("/{motorcycleID}")
    public String viewMotorcycle(@PathVariable Long motorcycleID, Model model) {
        MotorcycleDTO motorcycle = motorcycleService.findFullMotorcycle(motorcycleID); // 수정: DTO로 조회
        model.addAttribute("motorcycle", motorcycle); // 모델에 데이터 추가
        return "view"; // view.html 페이지 반환
    }

    // 생성 페이지로 이동 (새로운 Motorcycle 생성 폼 제공)
    // 사용자가 URL "/motorcycle/new"로 접근할 때 처리
    @GetMapping("/new")
    public String createMotorcycleForm(Model model) {
        model.addAttribute("motorcycleDTO", new MotorcycleDTO()); // 빈 MotorcycleDTO 객체를 모델에 추가하여 폼 초기화
        return "create"; // create.html로 이동하여 폼을 제공 (새로운 Motorcycle 생성)
    }

    // 새로운 Motorcycle 생성 (사용자가 폼을 제출했을 때 데이터를 처리)
    // 사용자가 폼 제출 시 데이터 전달받아 저장 (POST 요청 처리)
    @PostMapping("/new")
    public String createMotorcycle(@ModelAttribute MotorcycleDTO motorcycleDTO, RedirectAttributes redirectAttributes) {
        motorcycleService.insertFullMotorcycle(motorcycleDTO); // 서비스에 DTO를 전달하여 새로운 Motorcycle 저장 (DB에 삽입)
        redirectAttributes.addFlashAttribute("motorcycleDTO", "motorcycleDTO");
        return "redirect:/motorcycle"; // 생성 후 메인 페이지로 리다이렉트하여 사용자에게 결과 표시
    }

    // 수정 페이지로 이동 (특정 Motorcycle 수정 폼 제공)
    // 사용자가 URL "/motorcycle/edit/{id}"로 접근할 때 처리
    @GetMapping("/edit/{motorcycleID}")
    public String editMotorcycleForm(@PathVariable Long motorcycleID, Model model) {
        MotorcycleDTO motorcycle = motorcycleService.findFullMotorcycle(motorcycleID); // ID로 특정 Motorcycle 데이터 조회 (DTO 형태)
        model.addAttribute("motorcycleDTO", motorcycle); // 조회된 Motorcycle 데이터를 모델에 추가하여 폼에 초기값으로 사용
        return "edit"; // edit.html로 이동하여 수정 폼을 제공
    }

    // Motorcycle 업데이트 (사용자가 수정된 폼을 제출했을 때 데이터 처리)
    // 사용자가 폼 제출 시 수정된 데이터를 전달받아 업데이트 (POST 요청 처리)
    @PostMapping("/edit/{motorcycleID}")
    public String updateMotorcycle(@PathVariable Long motorcycleID, @Valid @ModelAttribute MotorcycleDTO motorcycleDTO) {
        motorcycleDTO.setMotorcycleID(motorcycleID); // 업데이트할 Motorcycle ID 설정
        motorcycleService.updateFullMotorcycle(motorcycleDTO); // 서비스에 DTO를 전달하여 기존 Motorcycle 업데이트 (DB 업데이트)
        return "redirect:/motorcycle"; // 수정 후 메인 페이지로 리다이렉트하여 사용자에게 결과 표시
    }

    // Motorcycle 삭제 (삭제 요청 처리)
    // 사용자가 삭제 요청을 했을 때 DeleteMotorcycleDTO로 데이터 처리 (POST 요청 처리)
    @PostMapping("/delete/{motorcycleID}")
    public String deleteMotorcycle(@PathVariable Long motorcycleID, RedirectAttributes redirectAttributes) {
        motorcycleService.deleteFullMotorcycle(motorcycleID); // DTO에서 삭제할 ID를 받아 서비스에서 삭제 처리 (DB 삭제)
        redirectAttributes.addFlashAttribute("motorcycleDTO", "motorcycleDTO");
        return "redirect:/motorcycle"; // 삭제 후 메인 페이지로 리다이렉트하여 결과 표시
    }
}

// 주요 수정 사항:
// 1. `findFullMotorcycleList`와 `updateMultipleMotorcycles` 메서드를 추가하여 서비스를 통해 데이터베이스와 통신하도록 수정하였습니다.
// 2. 각 메서드와 줄마다 기능에 대한 주석을 추가하여 코드의 가독성을 높였습니다.