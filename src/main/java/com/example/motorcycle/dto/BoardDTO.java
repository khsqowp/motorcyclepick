package com.example.motorcycle.dto;

import com.example.motorcycle.form.BoardForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardDTO {

    private String question1;
    private String question2;
    private String question3;
    private String question4;
    private String question5;
    private String question6;
    private String question7;

    // BoardForm -> BoardDTO
    public static BoardDTO fromBoardForm(BoardForm form) {
        BoardDTO dto = new BoardDTO();

        dto.setQuestion1(form.getQuestion1());
        dto.setQuestion2(form.getQuestion2());
        dto.setQuestion3(form.getQuestion3());
        dto.setQuestion4(form.getQuestion4());
        dto.setQuestion5(form.getQuestion5());
        dto.setQuestion6(form.getQuestion6());
        dto.setQuestion7(form.getQuestion7());
        return dto;
    }

    // BoardDTO -> BoardForm
    public BoardForm toBoardForm() {
        BoardForm form = new BoardForm();

        form.setQuestion1(this.question1);
        form.setQuestion2(this.question2);
        form.setQuestion3(this.question3);
        form.setQuestion4(this.question4);
        form.setQuestion5(this.question5);
        form.setQuestion6(this.question6);
        form.setQuestion7(this.question7);
        return form;
    }
}