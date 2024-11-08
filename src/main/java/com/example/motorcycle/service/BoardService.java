package com.example.motorcycle.service;

import com.example.motorcycle.form.BoardForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    // 설문 응답 정수값들을 받아옴
    public Object getRecommendedBikes(BoardForm boardForm) {
        // Q1-Q5: 주행 목적 및 스타일
        int ridingStyleScore = formCalculate(
                boardForm.getQ1(),  // 고동감
                boardForm.getQ2(),  // 속도
                boardForm.getQ3(),  // 편안함
                boardForm.getQ4(),  // 투어링
                boardForm.getQ5(),
                boardForm.getQ6(),
                boardForm.getQ7(),
                boardForm.getQ8(),
                boardForm.getQ9(),
                boardForm.getQ10(),
                boardForm.getQ11(),
                boardForm.getQ12(),
                boardForm.getQ13(),
                boardForm.getQ14(),
                boardForm.getQ15(),
                boardForm.getQ16(),
                boardForm.getQ17(),
                boardForm.getQ18(),
                boardForm.getQ19(),
                boardForm.getQ20()
        );
        return null;
    }

    private int formCalculate(Integer q1, Integer q2, Integer q3, Integer q4, Integer q5, Integer q6, Integer q7, Integer q8, Integer q9, Integer q10, Integer q11, Integer q12, Integer q13, Integer q14, Integer q15, Integer q16, Integer q17, Integer q18, Integer q19, Integer q20) {
        double powerWeight = 0.3;     // 고동감 가중치
        double speedWeight = 0.3;     // 속도 가중치
        double comfortWeight = 0.15;  // 편안함 가중치
        double touringWeight = 0.15;  // 투어링 가중치
        double commutingWeight = 0.1; // 통근 가중치
        double fairingWeight = 0.2;    // 카울 가중치
        double lowHandleWeight = 0.2;  // 낮은 핸들 가중치
        double highHandleWeight = 0.2; // 높은 핸들 가중치
        double lowSeatWeight = 0.2;    // 낮은 시트고 가중치
        double safetyWeight = 0.2;     // 안전장비 가중치
        double reliabilityWeight = 0.25;      // 신뢰성 가중치
        double cargoWeight = 0.15;            // 적재 공간 가중치
        double fuelEfficiencyWeight = 0.2;    // 연비 가중치
        double selfMaintenanceWeight = 0.2;   // 자가정비 가중치
        double maintenanceCostWeight = 0.2;   // 정비 비용 가중치
        double budgetWeight = 0.3;          // 예산 가중치
        double brandWeight = 0.15;          // 브랜드 가중치
        double customWeight = 0.15;         // 커스텀 가중치
        double depreciationWeight = 0.2;    // 감가상각 가중치
        double usedBikeWeight = 0.2;        // 중고 바이크 가중치

        return (int) ((q1 * powerWeight) +
                (q2 * speedWeight) +
                (q3 * comfortWeight) +
                (q4 * touringWeight) +
                (q5 * commutingWeight) +
                (q6 * fairingWeight) +
                (q7 * lowHandleWeight) +
                (q8 * highHandleWeight) +
                (q9 * lowSeatWeight) +
                (q10 * safetyWeight) +
                (q6 * fairingWeight) +
                (q7 * lowHandleWeight) +
                (q8 * highHandleWeight) +
                (q9 * lowSeatWeight) +
                (q10 * safetyWeight) +
                (q11 * reliabilityWeight) +
                (q12 * cargoWeight) +
                (q13 * fuelEfficiencyWeight) +
                (q14 * selfMaintenanceWeight) +
                (q15 * maintenanceCostWeight) +
                (q16 * budgetWeight) +
                (q17 * brandWeight) +
                (q18 * customWeight) +
                (q19 * depreciationWeight) +
                (q20 * usedBikeWeight));
    }
}