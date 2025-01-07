package com.example.motorcycle.utils;

import com.example.motorcycle.form.BoardForm;
import org.springframework.stereotype.Component;

@Component
public class BoardUtils {
    public static boolean isValidSurvey(BoardForm boardForm) {
        return boardForm != null &&
                boardForm.getQuestion1() != null && !boardForm.getQuestion1().isEmpty() &&
                boardForm.getQuestion2() != null && !boardForm.getQuestion2().isEmpty() &&
                boardForm.getQuestion3() != null && !boardForm.getQuestion3().isEmpty() &&
                boardForm.getQuestion4() != null && !boardForm.getQuestion4().isEmpty() &&
                boardForm.getQuestion5() != null && !boardForm.getQuestion5().isEmpty() &&
                boardForm.getQuestion6() != null && !boardForm.getQuestion6().isEmpty() &&
                boardForm.getQuestion7() != null && !boardForm.getQuestion7().isEmpty();
    }

    public static String formatPrice(int price) {
        if (price >= 10000) {
            return String.format("%d억원", price / 10000);
        }
        return String.format("%d만원", price);
    }
}