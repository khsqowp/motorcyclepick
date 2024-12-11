# Motorcycle

## To Do
- [ ] 오토바이 용어사전 페이지 개설
- [ ] 오토바이 정비주기 페이지 개설
- [ ] BoardService.java 계산 로직 생성하기
- [ ] 각 html파일에 대응하는 css 파일 생성하기
- [ ] 데이터베이스 추가하기
- [ ] 장르별 점수 재계산 하기
- [ ] MotoGuzzi 데이터베이스 추가
- [ ] MV Agusta 데이터베이스 추가
- [ ] Peugeot 데이터베이스 추가
- [ ] Piaggio 데이터베이스 추가
- [ ] Suzuki 데이터베이스 추가
- [ ] Triumph 데이터베이스 추가
- [ ] Yamaha 데이터베이스 추가
- [ ] Bajaj 데이터베이스 추가
- [ ] Benelli 데이터베이스 추가
- [ ] CF Moto 데이터베이스 추가
- [ ] Daelim 데이터베이스 추가
- [ ] Husqvarna 데이터베이스 추가
- [ ] Indian 데이터베이스 추가
- [ ] KYMCO 데이터베이스 추가
- [ ] Mondial 데이터베이스 추가
- [ ] Royal Enfield 데이터베이스 추가
- [ ] Vespa 데이터베이스 추가
- [ ] Victory 데이터베이스 추가


- [ ] 결과 페이지 디자인 코드 수정
- [ ] 
- [ ] 
- [ ] 
- [ ] 
- [ ] 
- [ ] 


## Done
- [x] Aprillia 데이터베이스 추가
- [x] BMW 데이터베이스 추가
- [x] Ducati 데이터베이스 추가
- [x] Harley-Davidson 데이터베이스 추가
- [x] Honda 데이터베이스 추가
- [x] Kawasaki 데이터베이스 추가
- [x] KTM 데이터베이스 추가
- [x] Mapper.xml 완성하기
- [x] Domain.java 완성하기
- [x] DTO.java 완성하기
- [x] FORM.java 완성하기
- [x] Mapper.java 완성하기
- [x] Cleaned air filter

## Project Structure
```
main/
├── motorcycle/
│   ├── config/
│   │   └── BoardConfig.java
│   ├── controller/
│   │   ├── BoardController.java
│   │   └── MotorcycleController.java
│   ├── domain/
│   │   ├── DimensionsDomain.java
│   │   ├── ElectronicsDomain.java
│   │   ├── EnginesDomain.java
│   │   └── MotorcycleDomain.java
│   ├── dto/
│   │   ├── BoardDTO.java
│   │   ├── DeleteMotorcycleDTO.java
│   │   ├── DimensionsDTO.java
│   │   ├── ElectronicsDTO.java
│   │   ├── EnginesDTO.java
│   │   └── MotorcycleDTO.java
│   ├── form/
│   │   ├── BoardForm.java
│   │   └── MotorcycleForm.java
│   ├── motorcycleData/
│   │   ├── Capacity.java
│   │   ├── Genre.java
│   │   └── Year.java
│   ├── repository/
│   │   └── MotorcycleMapper.java
│   └── service/
│   │   ├── BoardService.java
│   │   └── MotorcycleService.java
└── resources/
│   └──mapper/
│   │   └── MotorcycleMapper.xml
│   └──static/
│   │   ├── list.css
│   │   ├── question1to5.css
│   │   ├── question6to10.css
│   │   ├── question11to15.css
│   │   ├── question16to20.css
│   │   └── startPage.css
│   └──templates/
│   │   ├── create.html
│   │   ├── edit.html
│   │   ├── editList.html
│   │   ├── list.html
│   │   ├── motorcycle.html
│   │   ├── question1to5.html
│   │   ├── question6to10.html
│   │   ├── question11to15.html
│   │   ├── question16to20.html
│   │   ├── resultPage.html
│   │   ├── singleSearchID.html
│   │   └── startPage.html
│   ├── application.properties
│   └── mybatis-config.xml

```

## Important Notes
- **Service Intervals**
    - Oil change: Every 3,000 miles
    - Chain maintenance: Every 500 miles
    - Tire check: Weekly
- **Emergency Contacts**
    - Mechanic: (555) 123-4567
    - Insurance: (555) 987-6543

## Memos
### 2024-11-18
- 데이터베이스 CRUD 페이지 완료
- 모델 추천 로직 생성하기
- 

### 2024-03-15
- Found good deal on new tires
- Remember to keep chain lubricated during rainy season