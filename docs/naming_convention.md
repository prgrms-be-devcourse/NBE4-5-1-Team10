# Naming Convention

이 문서에서는 프로젝트 내에서 일관성을 유지하기 위해 사용하는 명명 규칙에 대해 설명합니다. 규칙은 주로 **SpringBoot의 DTO**, **데이터베이스 테이블 및 속성**에 대해 다룹니다.

## 1. SpringBoot

### DTO Naming Rule

DTO(Data Transfer Object)는 주로 API 요청 및 응답 데이터를 전달하는 데 사용됩니다. 이를 명확하게 구분할 수 있도록 다음과 같은 규칙을 따릅니다:

| 구분             | 규칙                         | 예시                                       |
| ---------------- | ---------------------------- | ------------------------------------------ |
| **Request DTO**  | `{도메인}{메서드명}Request`  | `UserCreateRequest`, `OrderUpdateRequest`  |
| **Response DTO** | `{도메인}{메서드명}Response` | `UserDetailsResponse`, `OrderListResponse` |

이 규칙을 통해 API의 요청 및 응답 객체가 무엇을 다루고 있는지 명확하게 알 수 있습니다.

## 2. Database

### Naming Rule (with JPA)

데이터베이스 테이블과 속성에 대한 명명 규칙은 일관성과 가독성을 유지하기 위해 중요합니다. 아래 규칙을 따릅니다:

| 구분            | 규칙                              | 예시                                             |
| --------------- | --------------------------------- | ------------------------------------------------ |
| **테이블 이름** | 첫 글자만 대문자, 나머지는 소문자 | `User`, `Order`, `ProductCategory`               |
| **속성 이름**   | 모두 소문자 + snake_case          | `user_name`, `order_date`, `product_category_id` |

## 3. Variables and Functions

### Naming Rule

변수명과 함수명은 **camelCase** 형식을 따릅니다. 이를 통해 코드의 일관성과 가독성을 높일 수 있습니다.

| 구분          | 규칙        | 예시                                          |
| ------------- | ----------- | --------------------------------------------- |
| **변수 이름** | `camelCase` | `userName`, `orderDate`                       |
| **함수 이름** | `camelCase` | `getOrderDetails()`, `calculateTotalAmount()` |

## 4. Constants

### Naming Rule

상수 이름은 **대문자 + snake_case** 형식을 따릅니다. 이는 상수와 일반 변수의 구분을 명확하게 합니다.

| 구분          | 규칙                   | 예시                                 |
| ------------- | ---------------------- | ------------------------------------ |
| **상수 이름** | `UPPERCASE_SNAKE_CASE` | `MAX_RETRY_COUNT`, `DEFAULT_TIMEOUT` |

## 5. Classes

### Naming Rule

클래스 이름은 **PascalCase** 형식을 따릅니다. 클래스 이름은 명확하고 직관적으로 작성해야 합니다.

| 구분            | 규칙         | 예시                                                  |
| --------------- | ------------ | ----------------------------------------------------- |
| **클래스 이름** | `PascalCase` | `UserService`, `OrderController`, `ProductRepository` |
