package com.example.motorcycle.service;

import com.example.motorcycle.dto.BoardDTO;
import com.example.motorcycle.form.BoardForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    public Object getRecommendedBikes(BoardForm boardForm) {
        // Form -> DTO 변환
        BoardDTO dto = BoardDTO.fromBoardForm(boardForm);

        // DTO 데이터로 계산
        return calculatePreference(dto);
    }

    private int calculatePreference(BoardDTO dto) {
        // =============== 계산식 작성하는 곳 =============== //
        /*
        1. dto.getQ1() ~ dto.getQ20() 값을 이용해서
        2. 선호도에 따른 가중치를 계산하고
        3. 계산된 결과값을 반환
        */

        int q1 = dto.getQ1();
        int q2 = dto.getQ2();
        int q3 = dto.getQ3();
        int q4 = dto.getQ4();
        int q5 = dto.getQ5();
        int q6 = dto.getQ6();
        int q7 = dto.getQ7();
        int q8 = dto.getQ8();
        int q9 = dto.getQ9();
        int q10 = dto.getQ10();
        int q11 = dto.getQ11();
        int q12 = dto.getQ12();
        int q13 = dto.getQ13();
        int q14 = dto.getQ14();
        int q15 = dto.getQ15();
        int q16 = dto.getQ16();
        int q17 = dto.getQ17();
        int q18 = dto.getQ18();
        int q19 = dto.getQ19();
        int q20 = dto.getQ20();


        return 0; // 계산된 결과값 반환
    }
}