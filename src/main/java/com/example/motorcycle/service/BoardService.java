package com.example.motorcycle.service;

import com.example.motorcycle.domain.MotorcycleDomain;
import com.example.motorcycle.dto.BoardDTO;
import com.example.motorcycle.form.BoardForm;
import com.example.motorcycle.repository.MotorcycleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final MotorcycleMapper motorcycleMapper;

    public List<MotorcycleDomain> getRecommendedBikes(BoardForm boardForm) {
        BoardDTO dto = BoardDTO.fromBoardForm(boardForm);
        return calculatePreferences(dto);
    }

    private List<MotorcycleDomain> calculatePreferences(BoardDTO dto) {
        Map<String, Object> params = new HashMap<>();

        // Q1: 가격 처리
        try {
            float price = Float.parseFloat(dto.getQuestion1()) * 10000;
            params.put("price", price);
        } catch (NumberFormatException e) {
            // 가격이 입력되지 않은 경우 null로 설정하여 쿼리에서 제외
            params.put("price", null);
        }

        // Q2: 배기량 범위 처리
        Map<String, Float> capacityRange = switch (dto.getQuestion2()) {
            case "동네 마실 (125cc)" -> Map.of("minCapacity", 0f, "maxCapacity", 125f);
            case "출퇴근 (125 ~ 350cc)" -> Map.of("minCapacity", 125f, "maxCapacity", 350f);
            case "시내바리 (125 ~ 650cc)" -> Map.of("minCapacity", 125f, "maxCapacity", 650f);
            case "근교바리 (300 ~ 1000cc)" -> Map.of("minCapacity", 300f, "maxCapacity", 1000f);
            case "중거리 투어 (500cc ~)" -> Map.of("minCapacity", 500f, "maxCapacity", 9999f);
            case "장거리 투어 (650cc ~)" -> Map.of("minCapacity", 650f, "maxCapacity", 9999f);
            default -> Map.of("minCapacity", null, "maxCapacity", null);
        };
        params.putAll(capacityRange);

        // Q3: 장르 선호도 처리
        String primaryGenre = switch (dto.getQuestion3()) {
            case "나는 날카로운 코너링과 스포티한 자세가 최고야" -> "replica";
            case "나는 시트고가 낮고 편안하게 앉아서 한적한 도로를 따라 여유롭게 달리고싶어" -> "cruiser";
            case "나는 장거리 투어를 좋아하고, 넉넉한 수납공간과 편안한 시트가 필요해" -> "tourer";
            case "나는 공도도 주행하고 싶고, 임도도 주행하면서 여기저기 돌아다니고싶어" -> "adventure";
            case "도심 출퇴근부터, 주말 투어까지 모든걸 딱 한대로 해결하고싶어" -> "multiPurpose";
            case "도심 사이를 가로지르며 날것 그대로의 라이딩을 즐기고 싶어" -> "naked";
            case "옛날 디자인이 좋고, 엔진의 고동감을 느끼면서 여유있게 주행하는게 좋아" -> "classic";
            case "오프로드도 좋은데 클래식한 디자인이 내 취향이야" -> "scrambler";
            case "난 산길 어디든 마음대로 다니고, 험난한 지형을 타고싶어" -> "offRoad";
            case "난 도심속에서 가볍고 높은 시트고로 치고 나가면서, 산 속에서 스릴을 느끼고 싶어" -> "motard";
            case "난 실용성과 경제성을 모두 갖추고, 무엇보다 편한게 최고야" -> "scooter";
            case "클래식한 멋과, 레플리카와 같은 스포티한 주행이 내 취향이야" -> "cafeRacer";
            default -> null;
        };
        params.put("primaryGenre", primaryGenre);

        // Q4: 최고 속도 범위 처리
        Map<String, Float> speedRange = switch (dto.getQuestion4()) {
            case "난 자동차랑 비슷한 속도로 다닐거야 (60 ~ 110km)" -> Map.of("minSpeed", 60f, "maxSpeed", 110f);
            case "국도 제한속도는 그래도 달려야지 (80 ~ 120km)" -> Map.of("minSpeed", 80f, "maxSpeed", 120f);
            case "난 세자리 숫자로는 달릴거야 (100 ~ 130km)" -> Map.of("minSpeed", 100f, "maxSpeed", 130f);
            case "100Km에서 추월은 가능해야지 (120 ~ 170km)" -> Map.of("minSpeed", 120f, "maxSpeed", 170f);
            case "빠른건 좋지만 200은 무서워 (150 ~ 199km)" -> Map.of("minSpeed", 150f, "maxSpeed", 199f);
            case "y영역대까지는 달리고싶어 (200 ~ 240km)" -> Map.of("minSpeed", 200f, "maxSpeed", 240f);
            case "난 y영역 이상을 원해 (250 ~ 299km)" -> Map.of("minSpeed", 250f, "maxSpeed", 299f);
            case "y? 나는 z영역도 달릴거야(300 ~ )" -> Map.of("minSpeed", 300f, "maxSpeed", 999f);
            default -> Map.of("minSpeed", null, "maxSpeed", null);
        };
        params.putAll(speedRange);

        // Q5: 등급 범위 처리
        List<String> classGrades = switch (dto.getQuestion5()) {
            case "난 내 동네 주변에서만 탈거야 (언더본, 쿼터)" -> Arrays.asList("underbone", "quarter");
            case "난 내 도시에서 타고 투어는 안나가 (쿼터 미들)" -> Arrays.asList("quarter", "middle");
            case "난 근교까지는 투어 다니고 싶어 (쿼터 미들 리터 오버리터)" -> Arrays.asList("quarter", "middle", "liter", "overLiter");
            case "난 장거리 위주로 다닐거야 (미들 리터 오버리터)" -> Arrays.asList("middle", "liter", "overLiter");
            default -> null;
        };
        if (classGrades != null && !classGrades.isEmpty()) {
            params.put("classGrades", classGrades);
        }

        // Q6: RPM 범위 처리
        Map<String, Float> rpmRange = switch (dto.getQuestion6()) {
            case "난 조금만 당겨서 여유롭게 다닐거야 (3500rpm)" -> Map.of("minRpm", 0f, "maxRpm", 3500f);
            case "그래도 차 2배는 써야지 (6000rpm)" -> Map.of("minRpm", 3500f, "maxRpm", 6000f);
            case "난 우와앙 거리는게 좋아 (10000rpm)" -> Map.of("minRpm", 6000f, "maxRpm", 10000f);
            case "나는 10,000rpm은 넘기면서 짜야지 재밌을거같아" -> Map.of("minRpm", 10000f, "maxRpm", 99999f);
            default -> Map.of("minRpm", null, "maxRpm", null);
        };
        params.putAll(rpmRange);


        // Q7: 스타일 선호도에 따른 필터링
        List<MotorcycleDomain> motorcycles = motorcycleMapper.findByAllRangePreferences(params);

        if (dto.getQuestion7() != null && !dto.getQuestion7().isEmpty()) {
            List<String> selectedPreferences = Arrays.asList(dto.getQuestion7().split(","));
            if (selectedPreferences.size() == 2) {
                motorcycles = filterByStylePreferences(motorcycles, selectedPreferences);

                if (motorcycles.isEmpty()) {
                    motorcycles = motorcycleMapper.findByAllRangePreferences(params);
                    motorcycles = filterByStylePreferencesWithRelaxedCriteria(motorcycles, selectedPreferences);
                }
            }
        }

        return motorcycles;
    }

    // 클래스 레벨 private 메서드들
    private List<MotorcycleDomain> filterByStylePreferences(List<MotorcycleDomain> motorcycles, List<String> preferences) {
        if (motorcycles == null) return new ArrayList<>();
        return motorcycles.stream()
                .filter(bike -> matchesStylePreferences(bike, preferences))
                .collect(Collectors.toList());
    }

    private boolean matchesStylePreferences(MotorcycleDomain bike, List<String> preferences) {
        if (bike == null || bike.getEnginesDomain() == null || bike.getDimensionsDomain() == null) {
            return false;
        }

        for (String preference : preferences) {
            boolean matches = switch (preference) {
                case "토크중시" -> {
                    Float torque = bike.getEnginesDomain().getMaxTorqueNm();
                    Float power = bike.getEnginesDomain().getMaxPowerHp();
                    yield (torque != null && power != null) && torque > power;
                }
                case "낮은시트" -> {
                    Float seatHeight = bike.getDimensionsDomain().getSeatHeight();
                    yield seatHeight != null && seatHeight < 700;
                }
                case "경량화" -> {
                    Float weight = bike.getDimensionsDomain().getWetWeight();
                    yield weight != null && weight < 190;
                }
                case "대형바이크" -> {
                    Float width = bike.getDimensionsDomain().getWidth();
                    Float length = bike.getDimensionsDomain().getLength();
                    yield (width != null && length != null) && (width >= 850 || length >= 2100);
                }
                case "연비중시" -> {
                    Float mileage = bike.getEnginesDomain().getMileage();
                    Float capacity = bike.getEnginesDomain().getCapacity();
                    yield (mileage != null && capacity != null) &&
                            (mileage * Math.sqrt(capacity) > 30);
                }
                case "대용량" -> {
                    Float fuelCapacity = bike.getDimensionsDomain().getFuelCapacity();
                    Float mileage = bike.getEnginesDomain().getMileage();
                    Float capacity = bike.getEnginesDomain().getCapacity();
                    yield (fuelCapacity != null && mileage != null && capacity != null) &&
                            ((fuelCapacity * mileage) / Math.sqrt(capacity) > 50);
                }
                default -> false;
            };
            if (!matches) return false;
        }
        return true;
    }

    private List<MotorcycleDomain> filterByStylePreferencesWithRelaxedCriteria(
            List<MotorcycleDomain> motorcycles, List<String> preferences) {
        if (motorcycles == null) return new ArrayList<>();
        return motorcycles.stream()
                .filter(bike -> matchesRelaxedStylePreferences(bike, preferences))
                .collect(Collectors.toList());
    }

    private boolean matchesRelaxedStylePreferences(MotorcycleDomain bike, List<String> preferences) {
        if (bike == null || bike.getEnginesDomain() == null || bike.getDimensionsDomain() == null) {
            return false;
        }

        for (String preference : preferences) {
            boolean matches = switch (preference) {
                case "토크중시" -> {
                    Float torque = bike.getEnginesDomain().getMaxTorqueNm();
                    Float power = bike.getEnginesDomain().getMaxPowerHp();
                    yield (torque != null && power != null) && torque > power * 0.8;
                }
                case "낮은시트" -> {
                    Float seatHeight = bike.getDimensionsDomain().getSeatHeight();
                    yield seatHeight != null && seatHeight < 770;
                }
                case "경량화" -> {
                    Float weight = bike.getDimensionsDomain().getWetWeight();
                    yield weight != null && weight < 209;
                }
                case "대형바이크" -> {
                    Float width = bike.getDimensionsDomain().getWidth();
                    Float length = bike.getDimensionsDomain().getLength();
                    yield (width != null && length != null) && (width >= 765 || length >= 1890);
                }
                case "연비중시" -> {
                    Float mileage = bike.getEnginesDomain().getMileage();
                    Float capacity = bike.getEnginesDomain().getCapacity();
                    yield (mileage != null && capacity != null) &&
                            (mileage * Math.sqrt(capacity) > 24);
                }
                case "대용량" -> {
                    Float fuelCapacity = bike.getDimensionsDomain().getFuelCapacity();
                    Float mileage = bike.getEnginesDomain().getMileage();
                    Float capacity = bike.getEnginesDomain().getCapacity();
                    yield (fuelCapacity != null && mileage != null && capacity != null) &&
                            ((fuelCapacity * mileage) / Math.sqrt(capacity) > 40);
                }
                default -> false;
            };
            if (!matches) return false;
        }
        return true;
    }
}