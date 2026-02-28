# OMS-CORE 배포 가이드

---

## 배포 절차

루트 `CLAUDE.md`의 **배포 준비 필수 규칙 6단계**를 따릅니다.

### oms-core 전용 정보

| 항목 | 값 |
|------|-----|
| 서비스 경로 | `/Users/hglee/Desktop/oms/oms-core` |
| 버전 파일 | `src/main/java/co/oms/core/common/ServerPropertiesController.java` |
| Git 원격 저장소 | (설정 후 업데이트) |
| 배포 포트 | 8081 |

### 배포 6단계 (oms-core)

```bash
# 1. 현재 버전 확인
cat oms-core/src/main/java/co/oms/core/common/ServerPropertiesController.java | grep "return"

# 2. 이전 태그 이후 변경 내용 확인
cd oms-core
git log v{현재버전}..HEAD --oneline

# 3. 버전 업데이트 (Edit 도구 사용)

# 4. 커밋 & push
git add src/main/java/co/oms/core/common/ServerPropertiesController.java
git commit -m "v{새버전}"
git push origin master

# 5. 태그 생성 & push
git tag v{새버전}
git push origin v{새버전}

# 6. 운영배포 요청서 작성
cd /Users/hglee/Desktop/oms/oms-tools
source venv/bin/activate
python3 auto-fill-from-pr.py {PR_URL} -d v{새버전} -r v{이전버전} -t "{배포제목}"
```

---

## 배포 전 체크리스트

- [ ] `./gradlew test` 통과
- [ ] `./gradlew build` 성공
- [ ] PR 리뷰 완료
- [ ] 버전 번호 업데이트 완료
- [ ] 태그 생성 완료
