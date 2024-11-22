package com.example.motorcycle.service;

import com.example.motorcycle.dto.BoardDTO;
import com.example.motorcycle.form.BoardForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

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
        // 문자열을 정수로 변환 (question1)
        int money = Integer.parseInt(dto.getQuestion1());

        // question2 (주행 목적) 변환
        int purpose = switch (dto.getQuestion2()) {
            case "동네 마실 (125cc)" -> 1;
            case "출퇴근 (125 ~ 300cc)" -> 2;
            case "시내바리 (125 ~ 600cc)" -> 3;
            case "근교바리 (300 ~ 1000cc)" -> 4;
            case "중거리 투어 (500cc ~)" -> 5;
            case "장거리 투어 (650cc ~)" -> 6;
            default -> 0;
        };

        // question3 (주행 스타일) 변환
        int genre = switch (dto.getQuestion3()) {
            case "나는 날카로운 코너링과 스포티한 자세가 최고야" -> 1;
            case "나는 시트고가 낮고 편안하게 앉아서 한적한 도로를 따라 여유롭게 달리고싶어" -> 2;
            case "나는 장거리 투어를 좋아하고, 넉넉한 수납공간과 편안한 시트가 필요해" -> 3;
            case "나는 공도도 주행하고 싶고, 임도도 주행하면서 여기저기 돌아다니고싶어" -> 4;
            case "도심 출퇴근부터, 주말 투어까지 모든걸 딱 한대로 해결하고싶어" -> 5;
            case "도심 사이를 가로지르며 날것 그대로의 라이딩을 즐기고 싶어" -> 6;
            case "옛날 디자인이 좋고, 엔진의 고동감을 느끼면서 여유있게 주행하는게 좋아" -> 7;
            case "오프로드도 좋은데 클래식한 디자인이 내 취향이야" -> 8;
            case "난 산길 어디든 마음대로 다니고, 험난한 지형을 타고싶어" -> 9;
            case "난 도심속에서 가볍고 높은 시트고로 치고 나가면서, 산 속에서 스릴을 느끼고 싶어" -> 10;
            case "난 실용성과 경제성을 모두 갖추고, 무엇보다 편한게 최고야" -> 11;
            case "클래식한 멋과, 레플리카와 같은 스포티한 주행이 내 취향이야" -> 12;
            default -> 0;
        };

        // question4 (주행 속도) 변환
        int topSpeed = switch (dto.getQuestion4()) {
            case "난 자동차랑 비슷한 속도로 다닐거야 (60km)" -> 1;
            case "국도 제한속도는 그래도 달려야지 (80km)" -> 2;
            case "난 세자리 숫자로는 달릴거야 (100km)" -> 3;
            case "100Km에서 추월은 가능해야지 (120km)" -> 4;
            case "빠른건 좋지만 200은 무서워 (170km)" -> 5;
            case "y영역대까지는 달리고싶어 (200km)" -> 6;
            case "난 y영역 이상을 원해 (250km)" -> 7;
            case "y? 나는 z영역도 달릴거야" -> 8;
            default -> 0;
        };

        // question5 (주행 거리) 변환
        int distance = switch (dto.getQuestion5()) {
            case "난 내 동네 주변에서만 탈거야 (언더본, 쿼터)" -> 1;
            case "난 내 도시에서 타고 투어는 안나가 (쿼터 미들)" -> 2;
            case "난 근교까지는 투어 다니고 싶어 (쿼터 미들 리터 오버리터)" -> 3;
            case "난 장거리 위주로 다닐거야 (미들 리터 오버리터)" -> 4;
            default -> 0;
        };

        // question6 (RPM) 변환
        int rpm = switch (dto.getQuestion6()) {
            case "난 조금만 당겨서 여유롭게 다닐거야 (3500rpm)" -> 1;
            case "그래도 차 2배는 써야지 (6000rpm)" -> 2;
            case "난 우와앙 거리는게 좋아 (10000rpm)" -> 3;
            case "나는 10,000rpm은 넘기면서 짜야지 재밌을거같아" -> 4;
            default -> 0;
        };

        // question7 (선호 외형) 변환 - 문자열을 boolean 배열로 변환
        boolean[] preferences = new boolean[10];
        List<String> outLook = Arrays.asList(dto.getQuestion7().split(","));

        preferences[0] = outLook.contains("카울");
        preferences[1] = outLook.contains("엔진노출");
        preferences[2] = outLook.contains("낮은시트");
        preferences[3] = outLook.contains("높은시트");
        preferences[4] = outLook.contains("탱크기대");
        preferences[5] = outLook.contains("키작은");
        preferences[6] = outLook.contains("가벼운");
        preferences[7] = outLook.contains("무거운");
        preferences[8] = outLook.contains("수납");
        preferences[9] = outLook.contains("스크린");

        // TODO: 여기에 추가적인 계산 로직 구현

        //q1. 금액 조회

        //q2. 활용 목적 조회

        //q3. 장르 선호 조회

        //q4. 크루징 속도 조회

        //q5. 활동 범위 조회

        //q6. 평균 사용 rpm 조회

        //q7. 외관 선호도 조회

        

        return 0;
    }
}