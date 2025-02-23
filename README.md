# NBE4-5-1-Team10

4기 5회차 10팀 커피프린스 10호점 1차 프로젝트

## 구성원

[윤원석](https://github.com/wonseokyoon)
[이화영](https://github.com/2hwayoung)
[김형준](https://github.com/Hyung-Junn)
[정준호](https://github.com/junho1131)

## 컨벤션

[컨벤션](https://github.com/prgrms-be-devcourse/NBE4-5-1-Team10/blob/main/docs/commit_convention.md)

## 명세서

x

## ERD

![image](https://github.com/user-attachments/assets/1926e840-1ea1-4524-9d3c-51d05ec91b96)

## 주요 기능

- 회원가입
- 로그인/로그아웃
- 주문
- 상품 관리
- 주문 관리
- 장바구니
- 배송 관리

## 브랜치 전략

- **브랜치 종류 및 역할**
  - `master` → 최종 결과물 브랜치
  - `develop` → 개발 과정에서 병합하는 브랜치
  - `feature` → 기능 개발 브랜치

## 개발 목적

이 프로젝트의 목표는 로컬 카페 Grids & Circles의 온라인 주문 시스템을 구축하는 것입니다. <br>
고객이 커피 원두 패키지를 온라인으로 주문하면, 해당 주문을 수집하여 일괄 처리하고, <당일 오후 2시 ~ 다음 날 오후 2시> 까지 같은 회원이 요청한 주문을 하나로 합쳐 효율적인 배송을 지원하는 구조를 갖춰 **사용자 및 운영자의 편의성을 향상시키는 것이 목적입니다.**

---

## 개발 환경

- DB: MySQL
- 프론트: NextJs

## 프로젝트 구조

- 도메인형
  - domain
  - controller
  - service
  - repository
  - dto

## 테스트

- local 환경 단위 테스트
- 유저 스토리 기반으로 최종 테스트 진행(Swagger,Postman)

## API 문서화

- Swagger API 문서화

### 📌 Running MySQL with Docker Compose & Enabling Query Logs

Start the container:

```bash
docker-compose up -d
```

Since logs are mapped to your local machine in `./mysql/conf`, you can monitor them directly:

```bash
tail -f ./mysql_logs/general.log
```

Stop the container:

```bash
docker-compose down
```

### OpenAPI TypeScript Code Generation

`openapi-typescript`를 사용하여 OpenAPI 스펙(`apiV1.json`)을 기반으로 TypeScript 타입을 자동 생성합니다.  
이를 통해 백엔드 API의 변경 사항을 자동으로 반영하고, 타입 안정성을 유지할 수 있습니다.

- ./apiV1.json 파일을 기반으로 TypeScript 타입을 생성합니다.

- 생성된 타입은 **./frontend/src/lib/backend/generated/schema.d.ts**에 저장됩니다.

---

1️⃣ **Codegen 실행 (`npm run codegen`)**

```bash
npm run codegen
```

2️⃣ **Codegen 실시간 감지 (`npm run codegen --watch`)**

```bash
npm run codegen:watch
```

## 와이어프레임(Wireframe)

와이어프레임은 [Creatie Link](https://creatie.ai/file/152751559819701?page_id=M&shareId=152751559819701) 에서 확인할 수 있습니다.

![Wireframe](docs/wireframe.png)

```

```
