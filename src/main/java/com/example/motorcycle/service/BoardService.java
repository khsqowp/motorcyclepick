package com.example.motorcycle.service;

import com.example.motorcycle.dto.BoardForm;
import com.example.motorcycle.domain.MotorcycleSpec;
import com.example.motorcycle.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    // 설문 응답을 바탕으로 추천 오토바이를 가져옴
    public List<MotorcycleSpec> getRecommendedBikes(BoardForm boardForm) {
        // boardForm의 값을 바탕으로 추천 로직을 구현


        //
//        /*여기서 q1 ~ q20 값들을 통해 계산하기
//        1. 기통수, CC 기준
//        2. 마력 기준
//        3. 휠베이스 핸들바 무게 기준
//        4. 고배기량, 휠베이스, 연비, 무게, 서스펜션트래블 기준
//        5. 낮은 배기량, 연비, 무게, 유지보수 비용 기준
//        6. 레플리카, 멀티퍼포즈, 어드벤처 등
//        7. 레플리카, 스포츠투어러, 카페레이서 등
//        8. 투어러, 크루저, 어드벤처, 클래식 등
//        9. 시트고 높이 기준
//        10. abs유무, tcs유무
//        11. 브랜드별 수치화, 배기량별 수치화, 모델별 고질병 확인
//        12. 레플리카, 네이키드 제외(추가 가능성 ㅇ)
//        13. 연비와 배기량 비례계산
//        14. 11번 동일
//        15. 11번 동일
//        16. 바이크 금액별 상한치 조사
//        17. 연도별 바이크 판매량 조사해서 수치화
//        18. 바이크 장르별 조사
//        19. 바이크 판매량 + 중고카페 크롤링
//        20. 16번 데이터와 연결하여 계산
//        주행 목적 및 스타일
//1. 나는 고동감이 넘치는 바이크를 원한다.
//2. 나는 빠르게 달릴 수 있는 바이크를 원한다.
//3. 나는 편하게 탈 수 있는 바이크를 원한다.
//4. 나는 투어와 같은 장거리 목적으로 탈 생각이다.
//5. 나는 출퇴근용으로 바이크를 사용할 계획이다.
//
//바이크 특성 및 디자인
//6. 나는 카울이 있는 바이크가 좋다.
//7. 나는 핸들이 낮고 숙이는 바이크가 좋다.
//8. 나는 핸들이 높아서 편하게 탈 수 있으면 좋겠다.
//9. 나는 시트고가 낮은 바이크를 원한다.
//10. 나는 ABS와 같은 안전 기능이 탑재된 바이크를 원한다.
//
//실용성 및 유지보수
//11. 나는 잔고장이 적은 바이크를 원한다.
//12. 나는 짐을 많이 실을 수 있는 바이크를 원한다.
//13. 나는 연비를 중요하게 생각한다.
//14. 나는 자가정비를 할 예정이라 정비성이 좋았으면 좋겠다.
//15. 나는 정비를 받을 때 금전적인 부담이 적었으면 좋겠다.
//
//경제성 및 가치
//16. 나는 바이크 구매에 금전적인 부담은 없다.
//17. 나는 사람들이 많이 구매하는 브랜드의 바이크를 원한다.
//18. 나는 커스텀을 할 수 있는 종류가 다양했으면 좋겠다.
//19. 나는 감가방어가 잘 되는 바이크를 구매하고 싶다.
//20. 나는 중고 바이크 구매를 고려하고 있다.

        int score = boardForm.getQ1() + boardForm.getQ2();
        return boardRepository.findRecommendedBikes(score);
    }
}
