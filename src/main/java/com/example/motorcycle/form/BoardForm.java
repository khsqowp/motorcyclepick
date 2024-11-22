package com.example.motorcycle.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardForm {
    private String question1;
    private String question2;
    private String question3;
    private String question4;
    private String question5;
    private String question6;
    private String question7;

    // question1만 직접 setter 구현
    public void setQuestion1(String question1) {
        // 혹시 콤마가 있다면 첫 번째 값만 사용
        if(question1 != null && question1.contains(",")) {
            this.question1 = question1.split(",")[0];
        } else {
            this.question1 = question1;
        }
    }
}