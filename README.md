## 개발 목적

이 프로젝트의 목표는 로컬 카페 Grids & Circles의 온라인 주문 시스템을 구축하는 것입니다. <br>
고객이 커피 원두 패키지를 온라인으로 주문하면, 해당 주문을 수집하여 일괄 처리하고, <당일 오후 2시 ~ 다음 날 오후 2시> 까지 같은 회원이 요청한 주문을 하나로 합쳐 효율적인 배송을 지원하는 구조를 갖춰 **사용자 및 운영자의 편의성을 향상시키는 것이 목적입니다.**

---

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

## 와이어프레임(Wireframe)

와이어프레임은 [Creatie Link](https://creatie.ai/file/152751559819701?page_id=M&shareId=152751559819701) 에서 확인할 수 있습니다.

![Wireframe](docs/wireframe.png)
