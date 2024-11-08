package com.example.motorcycle.controller;

import com.example.motorcycle.dto.DeleteMotorcycleDTO;
import com.example.motorcycle.dto.MotorcycleDTO;
import com.example.motorcycle.form.MotorcycleForm;
import com.example.motorcycle.service.MotorcycleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/motorcycle")
public class MotorcycleController {

    private final MotorcycleService motorcycleService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        binder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/")
    public String motorcycleMain(Model model) {
        model.addAttribute("motorcycleForm", new MotorcycleForm());
        model.addAttribute("deleteMotorcycleDTO", new DeleteMotorcycleDTO());
        return "motorcycle";
    }

    @GetMapping("/list")
    public String listMotorcycles(Model model) {
        List<MotorcycleDTO> motorcycles = motorcycleService.findFullMotorcycleList();
        model.addAttribute("motorcycles", motorcycles);
        return "list";
    }

    @GetMapping("/singleSearchID")
    public String viewMotorcycleById(@RequestParam(value = "id", required = false) Long id, Model model) {
        if (id == null) {
            model.addAttribute("error", "ID가 입력되지 않았습니다");
        } else {
            MotorcycleDTO motorcycle = motorcycleService.findOneMotorcycle(id);
            model.addAttribute("motorcycleDTO", motorcycle);
        }
        return "singleSearchID"; // singleSearchID.html 파일을 렌더링
    }

    @GetMapping("/new")
    public String createMotorcycleForm(Model model) {
        model.addAttribute("motorcycleForm", new MotorcycleForm());
        return "create";
    }

    @PostMapping("/new")
    public String createMotorcycle(@ModelAttribute @Valid MotorcycleForm form, RedirectAttributes redirectAttributes) {
        motorcycleService.insertFullMotorcycle(form);
        redirectAttributes.addFlashAttribute("message", "새로운 Motorcycle이 성공적으로 생성되었습니다.");
        return "redirect:/motorcycle/";
    }

    @GetMapping("/edit")  // 이건 그대로 유지
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

    // 전체 Motorcycle 리스트를 불러와서 엑셀처럼 수정할 수 있는 페이지로 이동
    @GetMapping("/editList")
    public String editMotorcycleList(Model model) {
        List<MotorcycleDTO> motorcycles = motorcycleService.findFullMotorcycleList();
        model.addAttribute("motorcycles", motorcycles);
        return "editList";
    }

    // 다수 Motorcycle 수정 요청 처리 (editList에서 제출 시)
    @PostMapping("/editList")
    public String updateMotorcycleList(@ModelAttribute("forms") List<MotorcycleForm> forms) {
        motorcycleService.updateMultipleMotorcycles(forms);
        return "redirect:/motorcycle/list";
    }

    @PostMapping("/edit")  // /edit 에서 /update로 변경
    public String updateMotorcycle(@ModelAttribute @Valid MotorcycleForm form, RedirectAttributes redirectAttributes) {
        try {
            motorcycleService.updateFullMotorcycle(form);
            redirectAttributes.addFlashAttribute("message", "Motorcycle이 성공적으로 수정되었습니다.");
            return "redirect:/motorcycle/";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "수정 중 오류가 발생했습니다:" + e.getMessage());
            return "redirect:/motorcycle/";
        }
    }

    @PostMapping("/delete")
    public String deleteMotorcycle(@ModelAttribute DeleteMotorcycleDTO dto, RedirectAttributes redirectAttributes) {
        try{
            motorcycleService.deleteFullMotorcycle(dto.getMotorcycleID());
            redirectAttributes.addFlashAttribute("message", "Motorcycle이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "삭제중 오류가 발생했습니다 : " + e.getMessage());
        }

        return "redirect:/motorcycle/";
    }
}
