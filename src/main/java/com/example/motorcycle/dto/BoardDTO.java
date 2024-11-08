package com.example.motorcycle.dto;

import com.example.motorcycle.form.BoardForm;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class BoardDTO {
    @NotNull(message = "응답을 선택해주세요")
    @Min(value = 1, message = "1-5 사이의 값을 선택해주세요")
    @Max(value = 5, message = "1-5 사이의 값을 선택해주세요")

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

    public static BoardDTO from(BoardForm form) {
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

    // 주행 목적 및 스타일 점수 계산 (Q1-Q5)
    public Integer calculateRidingStyleScore() {
        int powerPreference = q1; // 고동감 선호도
        int speedPreference = q2; // 속도 선호도
        int comfortPreference = q3; // 편안함 선호도
        int touringPreference = q4; // 투어링 선호도
        int commutingPreference = q5; // 통근 선호도

        return (powerPreference + speedPreference + comfortPreference +
                touringPreference + commutingPreference);
    }

    // 바이크 특성 및 디자인 점수 계산 (Q6-Q10)
    public Integer calculateDesignScore() {
        int fairingPreference = q6; // 카울 선호도
        int lowHandlePreference = q7; // 낮은 핸들 선호도
        int highHandlePreference = q8; // 높은 핸들 선호도
        int lowSeatPreference = q9; // 낮은 시트고 선호도
        int safetyFeaturePreference = q10; // 안전장비 선호도

        return (fairingPreference + lowHandlePreference + highHandlePreference +
                lowSeatPreference + safetyFeaturePreference);
    }

    // 실용성 및 유지보수 점수 계산 (Q11-Q15)
    public Integer calculatePracticalityScore() {
        int reliabilityPreference = q11; // 신뢰성 선호도
        int cargoPreference = q12; // 적재 공간 선호도
        int fuelEfficiencyPreference = q13; // 연비 중요도
        int selfMaintenancePreference = q14; // 자가정비 선호도
        int maintenanceCostPreference = q15; // 정비 비용 중요도

        return (reliabilityPreference + cargoPreference + fuelEfficiencyPreference +
                selfMaintenancePreference + maintenanceCostPreference);
    }

    // 경제성 및 가치 점수 계산 (Q16-Q20)
    public Integer calculateEconomicScore() {
        int budgetFlexibility = q16; // 예산 유연성
        int popularBrandPreference = q17; // 인기 브랜드 선호도
        int customizationPreference = q18; // 커스텀 선호도
        int valueRetentionPreference = q19; // 가치 보존 중요도
        int usedBikePreference = q20; // 중고 바이크 선호도

        return (budgetFlexibility + popularBrandPreference + customizationPreference +
                valueRetentionPreference + usedBikePreference);
    }

    // 배기량 범위 계산
    public Integer[] calculateEngineCapacityRange() {
        Integer minCC;
        Integer maxCC;

        // 고동감, 속도 선호도, 통근용 답변을 기반으로 계산
        int powerSpeed = (q1 + q2) / 2;

        if (q5 >= 4) { // 통근용으로 주로 사용
            minCC = 125;
            maxCC = 400;
        } else if (powerSpeed >= 4) {
            minCC = 650;
            maxCC = 1000;
        } else if (powerSpeed >= 3) {
            minCC = 400;
            maxCC = 650;
        } else {
            minCC = 250;
            maxCC = 400;
        }

        return new Integer[]{minCC, maxCC};
    }

    // 예산 범위 계산
    public Integer[] calculatePriceRange() {
        Integer minPrice;
        Integer maxPrice;

        if (q16 >= 4) { // 예산 여유 있음
            minPrice = 10000000;
            maxPrice = 30000000;
        } else if (q16 >= 3) {
            minPrice = 7000000;
            maxPrice = 15000000;
        } else if (q16 >= 2) {
            minPrice = 5000000;
            maxPrice = 10000000;
        } else {
            minPrice = 3000000;
            maxPrice = 7000000;
        }

        // 중고 선호도 반영
        if (q20 >= 4) {
            minPrice = (int) (minPrice * 0.6);
            maxPrice = (int) (maxPrice * 0.8);
        }

        return new Integer[]{minPrice, maxPrice};
    }

    // 권장 바이크 타입 계산
    public String[] calculateRecommendedTypes() {
        // 선호도 점수가 4 이상인 경우 해당 타입 추천
        java.util.List<String> recommendedTypes = new java.util.ArrayList<>();

        if ((q6 + q7) / 2 >= 4) recommendedTypes.add("REPLICA");
        if (q8 >= 4) recommendedTypes.add("CRUISER");
        if (q4 >= 4) recommendedTypes.add("TOURER");
        if ((q12 + q4) / 2 >= 4) recommendedTypes.add("ADVENTURE");
        if ((q3 + q5) / 2 >= 4) recommendedTypes.add("NAKED");
        if (q18 >= 4) recommendedTypes.add("CAFERACER");

        return recommendedTypes.toArray(new String[0]);
    }
}