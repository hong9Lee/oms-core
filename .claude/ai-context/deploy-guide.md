# OMS-CORE 배포 가이드

> 배포 6단계 절차는 **루트 `CLAUDE.md`의 배포 준비 필수 규칙**을 따른다.
> 이 문서는 oms-core **고유 값**만 기술한다.

---

## oms-core 배포 정보

| 항목 | 값 |
|------|-----|
| 서비스 경로 | `/Users/hglee/Desktop/oms/oms-core` |
| 버전 파일 | `src/main/java/co/oms/core/common/ServerPropertiesController.java` |
| 배포 포트 | 8081 |
| 기본 브랜치 | main |

---

## 배포 전 체크리스트

- [ ] `./gradlew test` 통과
- [ ] `./gradlew build` 성공
- [ ] PR 리뷰 완료
- [ ] 버전 번호 업데이트 완료
- [ ] 태그 생성 완료
