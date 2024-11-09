package com.example.motorcycle.dto;

import com.example.motorcycle.form.BoardForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardDTO {
    private Integer q1;
    private Integer q2;
    private Integer q3;
    private Integer q4;
    private Integer q5;
    private Integer q6;
    private Integer q7;
    private Integer q8;
    private Integer q9;
    private Integer q10;
    private Integer q11;
    private Integer q12;
    private Integer q13;
    private Integer q14;
    private Integer q15;
    private Integer q16;
    private Integer q17;
    private Integer q18;
    private Integer q19;
    private Integer q20;

    // BoardForm -> BoardDTO
    public static BoardDTO fromBoardForm(BoardForm form) {
        BoardDTO dto = new BoardDTO();
        dto.setQ1(form.getQ1());
        dto.setQ2(form.getQ2());
        dto.setQ3(form.getQ3());
        dto.setQ4(form.getQ4());
        dto.setQ5(form.getQ5());
        dto.setQ6(form.getQ6());
        dto.setQ7(form.getQ7());
        dto.setQ8(form.getQ8());
        dto.setQ9(form.getQ9());
        dto.setQ10(form.getQ10());
        dto.setQ11(form.getQ11());
        dto.setQ12(form.getQ12());
        dto.setQ13(form.getQ13());
        dto.setQ14(form.getQ14());
        dto.setQ15(form.getQ15());
        dto.setQ16(form.getQ16());
        dto.setQ17(form.getQ17());
        dto.setQ18(form.getQ18());
        dto.setQ19(form.getQ19());
        dto.setQ20(form.getQ20());
        return dto;
    }

    // BoardDTO -> BoardForm
    public BoardForm toBoardForm() {
        BoardForm form = new BoardForm();
        form.setQ1(this.q1);
        form.setQ2(this.q2);
        form.setQ3(this.q3);
        form.setQ4(this.q4);
        form.setQ5(this.q5);
        form.setQ6(this.q6);
        form.setQ7(this.q7);
        form.setQ8(this.q8);
        form.setQ9(this.q9);
        form.setQ10(this.q10);
        form.setQ11(this.q11);
        form.setQ12(this.q12);
        form.setQ13(this.q13);
        form.setQ14(this.q14);
        form.setQ15(this.q15);
        form.setQ16(this.q16);
        form.setQ17(this.q17);
        form.setQ18(this.q18);
        form.setQ19(this.q19);
        form.setQ20(this.q20);
        return form;
    }
}