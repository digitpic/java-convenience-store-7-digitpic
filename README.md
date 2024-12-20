# 🏪 편의점

## 🗣 문제 설명

__구매자의 할인 혜택과 재고 상황을 고려하여 최종 결제 금액을 계산하고 안내하는 결제 시스템을 구현한다.__

## 🎯 기능 요구 사항

- 사용자가 입력한 상품의 가격과 수량을 기반으로 최종 결제 금액을 계산한다. 
- 총구매액은 상품별 가격과 수량을 곱하여 계산하며, 프로모션 및 멤버십 할인 정책을 반영하여 최종 결제 금액을 산출한다.
- 구매 내역과 산출한 금액 정보를 영수증으로 출력한다.
- 영수증 출력 후 추가 구매를 진행할지 또는 종료할지를 선택할 수 있다.
- 사용자가 잘못된 값을 입력할 경우 IllegalArgumentException를 발생시키고, "[ERROR]"로 시작하는 에러 메시지를 출력 후 그 부분부터 입력을 다시 받는다.
- Exception이 아닌 IllegalArgumentException, IllegalStateException 등과 같은 명확한 유형을 처리한다.

## 🎯 세부 기능 목록
- [x] 파일 입출력을 통한 파일 읽기

- [x] 환영 인사
- [x] 현재 재고 상태 출력
  - [x] 재고가 0개라면 "재고 없음" 출력 
  
```text
안녕하세요. W편의점입니다.
현재 보유하고 있는 상품입니다.

- 콜라 1,000원 10개 탄산2+1
- 콜라 1,000원 10개
- 사이다 1,000원 8개 탄산2+1
- 사이다 1,000원 7개
- 오렌지주스 1,800원 9개 MD추천상품
- 오렌지주스 1,800원 재고 없음
- 탄산수 1,200원 5개 탄산2+1
- 탄산수 1,200원 재고 없음
- 물 500원 10개
- 비타민워터 1,500원 6개
- 감자칩 1,500원 5개 반짝할인
- 감자칩 1,500원 5개
- 초코바 1,200원 5개 MD추천상품
- 초코바 1,200원 5개
- 에너지바 2,000원 5개
- 정식도시락 6,400원 8개
- 컵라면 1,700원 1개 MD추천상품
- 컵라면 1,700원 10개
```

- [x] 주문 입력을 위한 문구 출력
- [x] 주문 입력 받기

```text
구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])
[콜라-3],[에너지바-5]
```

- [x] 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량보다 적게 가져온 경우, 그 수량만큼 추가 여부를 입력받는다.
  - [x] Y: 증정 받을 수 있는 상품을 추가한다.
  - [x] N: 증정 받을 수 있는 상품을 추가하지 않는다.
  
  ```text
  프로모션 적용이 가능한 상품에 대해 고객이 해당 수량만큼 가져오지 않았을 경우, 혜택에 대한 안내 메시지를 출력한다.
  현재 {상품명}은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)
  ```

- [x] 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 일부 수량에 대해 정가로 결제할지 여부를 입력받는다.
  - [ ] Y: 일부 수량에 대해 정가로 결제한다.
  - [ ] N: 정가로 결제해야하는 수량만큼 제외한 후 결제를 진행한다.
  
  ```text
  현재 {상품명} {수량}개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)
  ```

- [x] 고객이 상품을 구매할 때마다, 결제된 수량만큼 해당 상품의 재고에서 차감하여 수량을 관리한다.
- [x] 재고를 차감함으로써 시스템은 최신 재고 상태를 유지하며, 다음 고객이 구매할 때 정확한 재고 정보를 제공한다.
  
- 프로모션 할인
  - [x] 오늘 날짜가 프로모션 기간 내에 포함된 경우에만 할인을 적용한다.
  - 프로모션은 N개 구매 시 1개 무료 증정(Buy N Get 1 Free)의 형태로 진행된다.
  - [x] 1+1 또는 2+1 프로모션이 각각 지정된 상품에 적용되며, 동일 상품에 여러 프로모션이 적용되지 않는다.
  - [x] 프로모션 혜택은 프로모션 재고 내에서만 적용할 수 있다.
  - [x] 프로모션 기간 중이라면 프로모션 재고를 우선적으로 차감하며, 프로모션 재고가 부족할 경우에는 일반 재고를 사용한다.

- 멤버십 할인 적용 여부를 확인하기 위해 안내 문구를 출력한다.

```text
멤버십 할인을 받으시겠습니까? (Y/N)
```

- 멤버십 할인 
  - [x] 멤버십 할인은 프로모션 미적용 금액의 30%를 할인받는다.
  - [x] 프로모션 적용 후 남은 금액에 대해서 멤버십 할인을 적용한다.
  - [x] 멤버십 할인의 최대 한도는 8,000원이다.

- 영수증 출력
  - [x] 영수증은 고객의 구매 내역과 할인을 요약하여 출력한다.
  - [x] 영수증의 구성 요소를 보기 좋게 정렬하여 고객이 쉽게 금액과 수량을 확인할 수 있게 한다.
  
  ```text
    ===========W 편의점=============
    상품명		수량	금액
    콜라		3 	3,000
    에너지바 		5 	10,000
    ===========증	정=============
    콜라		1
    ==============================
    총구매액		8	13,000
    행사할인			-1,000
    멤버십할인			-3,000
    내실돈			 9,000
  ```

  - [x] 구매 상품 내역: 구매한 상품명, 수량, 가격
  - [x] 증정 상품 내역: 프로모션에 따라 무료로 제공된 증정 상품의 목록
  - [x] 총구매액: 구매한 상품의 총 수량과 총 금액
  - [x] 행사할인: 프로모션에 의해 할인된 금액
  - [x] 멤버십할인: 멤버십에 의해 추가로 할인된 금액
  - [x] 내실돈: 최종 결제 금액

- [x] 추가 구매 여부를 확인하기 위해 안내 문구를 출력한다.

```text
감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)
```

## 예외 사항
- 사용자가 잘못된 값을 입력했을 때, "[ERROR]"로 시작하는 오류 메시지와 함께 상황에 맞는 안내를 출력한다.
- [x] 구매할 상품과 수량 형식이 올바르지 않은 경우: [ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.
- [x] 존재하지 않는 상품을 입력한 경우: [ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.
- [x] 구매 수량이 재고 수량을 초과한 경우: [ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.
- [x] 기타 잘못된 입력의 경우: [ERROR] 잘못된 입력입니다. 다시 입력해 주세요.

## 🎯 프로그래밍 요구사항
- [x] depth 는 2까지 허용
- [x] enum 사용
- [x] 메서드 길이 10라인 이내

## 🎯 라이브러리
- camp.nextstep.edu.missionutils에서 제공하는 DateTimes 및 Console API를 사용하여 구현해야 한다.
- 현재 날짜와 시간을 가져오려면 camp.nextstep.edu.missionutils.DateTimes의 now()를 활용한다.
- 사용자가 입력하는 값은 camp.nextstep.edu.missionutils.Console의 readLine()을 활용한다.
