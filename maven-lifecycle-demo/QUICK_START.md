# Maven Lifecycle 데모 프로젝트 - Spring 구조 발표용 빠른 시작 가이드

## 📌 프로젝트 개요

**Spring MVC 3-Tier Architecture (계층 구조):**

```
┌─────────────────────────────────────┐
│    Controller (표현 계층)           │  REST API 엔드포인트
│    UserController                   │  
├─────────────────────────────────────┤
│    Service (비즈니스 로직)          │  비즈니스 로직 처리
│    UserService                      │  
├─────────────────────────────────────┤
│    Repository (데이터 접근)         │  데이터 저장/조회
│    UserRepository                   │  
├─────────────────────────────────────┤
│    Entity (데이터 모델)             │  데이터 구조
│    User                             │  
└─────────────────────────────────────┘
```

이 프로젝트는 **Spring의 실제 구조**를 보여주면서 동시에 **Maven Lifecycle**의 각 phase가 명확하게 보이도록 설계되었습니다.

---

## 📁 프로젝트 구조

```
maven-lifecycle-demo/
├── pom.xml                    ← Maven 설정 (Spring Web 의존성)
├── QUICK_START.md            ← 이 파일
├── README_KR.md              ← 전체 실행 가이드
├── MAVEN_LIFECYCLE_GUIDE.md  ← 상세 학습 자료
├── src/
│   ├── main/java/com/example/
│   │   ├── Application.java           ← Spring Boot 메인 클래스
│   │   ├── controller/
│   │   │   └── UserController.java    ← REST API 엔드포인트
│   │   ├── service/
│   │   │   └── UserService.java       ← 비즈니스 로직
│   │   ├── repository/
│   │   │   └── UserRepository.java    ← 데이터 접근
│   │   └── entity/
│   │       └── User.java              ← 데이터 모델 (Entity)
│   └── test/java/com/example/
│       ├── CalculatorTest.java        ← 성공 테스트 (10개) ✅
│       └── CalculatorFailingTest.java ← 실패 테스트 (5개) ❌
└── target/                            ← 빌드 결과
```

---

## 🎯 Spring 계층 구조 설명

### 1️⃣ Entity (User.java) - 데이터 모델
```
├─ id: Long
├─ name: String        (사용자 이름)
├─ email: String       (사용자 이메일)
└─ department: String  (부서)
```
**역할**: 데이터의 구조를 정의합니다.

### 2️⃣ Repository (UserRepository.java) - 데이터 접근 계층
```
메서드:
├─ save(User) → User           // 저장
├─ findById(Long) → Optional   // ID로 조회
├─ findAll() → List            // 모든 사용자 조회
├─ findByDepartment(String)    // 부서별 조회
└─ delete(Long) → boolean      // 삭제
```
**역할**: 데이터베이스와 상호작용합니다 (이 데모에서는 메모리 사용).

### 3️⃣ Service (UserService.java) - 비즈니스 로직 계층
```
메서드:
├─ getAllUsers() → List                 // 모든 사용자 조회
├─ getUserById(Long) → Optional         // ID로 조회
├─ createUser(String, String, String)  // 새 사용자 생성
├─ updateUserName(Long, String)        // 이름 변경
├─ getUsersByDepartment(String)        // 부서별 조회
└─ getTotalUserCount() → long           // 사용자 수
```
**역할**: 비즈니스 로직을 처리하고 데이터를 검증합니다.

### 4️⃣ Controller (UserController.java) - 표현 계층 / REST API
```
API 엔드포인트:
├─ GET  /api/users              // 모든 사용자 조회
├─ GET  /api/users/{id}         // 특정 사용자 조회
├─ POST /api/users              // 새 사용자 생성
├─ PUT  /api/users/{id}/name    // 사용자 이름 변경
├─ GET  /api/users/department/{name}  // 부서별 조회
└─ GET  /api/users/health       // 헬스 체크
```
**역할**: 클라이언트의 HTTP 요청을 받아 처리합니다.

---

## 🚀 실행 전 요구사항

### Java 및 Maven 설치 확인

```bash
# Java 설치 확인 (11 이상 필요)
java -version

# Maven 설치 확인
mvn --version
```

### Maven 미설치 시 설치 방법

**Windows (Chocolatey):**
```powershell
choco install maven
```

**또는 수동 설치:**
1. https://maven.apache.org/download.cgi 에서 다운로드
2. 폴더에 압축 해제
3. MAVEN_HOME 환경변수 설정
4. PATH에 %MAVEN_HOME%\bin 추가

---

## 🎯 발표 시나리오 (총 20분)

### Part 1: Spring 계층 구조 설명 (5분)

1. **프로젝트 구조 보여주기**
   - 각 계층의 폴더 구조 설명
   - 데이터가 흐르는 방향 설명

2. **각 계층의 역할 설명**
   - Entity: 데이터 모델
   - Repository: 데이터 접근
   - Service: 비즈니스 로직
   - Controller: REST API

3. **의존성 관계**
   ```
   Controller → Service → Repository → Entity
   ```

---

### Part 2: 성공 케이스 라이브 데모 (7분)

```bash
# 터미널에서 실행
cd d:\egov-vscode2026\maven-lifecycle-demo

# Maven Lifecycle 실행
mvn clean package
```

**콘솔 로그에서 확인할 내용:**

```
================================================================
[VALIDATE Phase] 프로젝트 설정 유효성 검사 시작...
- Project: maven-lifecycle-demo
- Version: 1.0.0
================================================================

================================================================
[COMPILE Phase] Java 소스 코드 컴파일 중...
- Source: src/main/java
  ├─ com/example/Application.java
  ├─ com/example/controller/UserController.java
  ├─ com/example/service/UserService.java
  ├─ com/example/repository/UserRepository.java
  └─ com/example/entity/User.java
- Target: target/classes
================================================================

================================================================
[TEST Phase] JUnit 테스트 실행 중...
- Test Classes:
  ├─ CalculatorTest.java (10개 테스트)
  └─ Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
================================================================

Running com.example.CalculatorTest

[TEST] 테스트 1: 모든 사용자 조회
[PASSED] ✅ 모든 사용자 조회 테스트 성공!

[TEST] 테스트 2: ID로 사용자 조회
[PASSED] ✅ ID로 사용자 조회 테스트 성공!

... (총 10개 테스트) ...

Tests run: 10, Failures: 0, Errors: 0, Skipped: 0

================================================================
[PACKAGE Phase] JAR 파일 생성 완료!
- JAR File: target/maven-lifecycle-demo-1.0.0.jar
- Location: target/
================================================================

[INFO] BUILD SUCCESS
```

**결과 확인:**
```bash
# 생성된 JAR 파일 확인
dir target\*.jar

# 응답:
# Volume in drive D is Data
# Directory of d:\egov-vscode2026\maven-lifecycle-demo\target
#
# 05/14/2026  10:30 AM    45,123,456  maven-lifecycle-demo-1.0.0.jar
# 05/14/2026  10:30 AM    12,345,678  maven-lifecycle-demo-1.0.0-plain.jar
```

---

### Part 3: 실패 케이스 라이브 데모 (8분)

**테스트 파일 변경:**
```bash
cd src/test/java/com/example

# 성공 테스트를 백업으로 변경
ren CalculatorTest.java CalculatorTest.java.bak

# 실패 테스트를 활성화
ren CalculatorFailingTest.java CalculatorTest.java

cd ..\..\..\..\
```

**빌드 실행:**
```bash
mvn clean package
```

**예상 콘솔 출력:**
```
================================================================
[VALIDATE Phase] 프로젝트 설정 유효성 검사 시작...
================================================================

================================================================
[COMPILE Phase] Java 소스 코드 컴파일 중...
================================================================

================================================================
[TEST Phase] JUnit 테스트 실행 중...
================================================================

Running com.example.CalculatorTest

[TEST] 실패 테스트 1: 사용자 수 검증
[TEST] 실제 사용자 수: 3, 기대값: 5

[ERROR] Tests run: 5, Failures: 5, Errors: 0, Skipped: 0

[ERROR] testUserCountFails(com.example.CalculatorFailingTest)
Expected: <5>
But was:  <3>

⚠️  이 아래부터는 실행되지 않습니다!

[INFO] --- maven-jar-plugin:3.3.0:jar 는 실행 안 됨
[INFO] --- spring-boot-maven-plugin:2.7.14:repackage 는 실행 안 됨

[INFO] BUILD FAILURE
```

**결과 확인:**
```bash
# JAR 파일이 생성되지 않았는지 확인
dir target\*.jar

# 응답: 파일을 찾을 수 없음!
# (이전의 JAR 파일은 target 폴더가 clean되어 삭제됨)
```

**원래 상태로 복원:**
```bash
cd src/test/java/com/example

ren CalculatorTest.java CalculatorFailingTest.java
ren CalculatorTest.java.bak CalculatorTest.java

cd ..\..\..\..\

# 다시 성공 확인
mvn clean package
```

---

### Part 4: 핵심 메시지 전달 (2분)

**Spring MVC 계층 구조의 중요성:**

1. **계층 분리의 이점**
   - 각 계층이 독립적으로 테스트 가능
   - 코드 재사용성 증가
   - 유지보수성 향상

2. **Maven Lifecycle의 자동화**
   - `mvn package` 한 번으로 validate → compile → test → package 모두 실행
   - Test 실패 시 package가 자동으로 중단
   - 코드 품질 보장

3. **결론**
   > "Spring의 계층 구조와 Maven의 라이프사이클은  
   > 함께 작동하여 안정적인 애플리케이션을 보장합니다!"

---

## 📊 Maven Lifecycle 순서

```
mvn package 실행
    ↓
[VALIDATE] ✅
    ↓
[COMPILE] ✅ 소스 코드 컴파일
    ↓
[TEST] ✅ 또는 ❌ 테스트 실행
    ↓
성공 시: [PACKAGE] JAR 생성 ✅
실패 시: [PACKAGE] 실행 안 함 ❌
```

---

## 🎓 발표 준비 체크리스트

- [ ] Java 설치 확인 (`java -version`)
- [ ] Maven 설치 확인 (`mvn --version`)
- [ ] 성공 케이스 한 번 실행 (`mvn clean package`)
- [ ] 프로젝트 구조 파악 (src 폴더 구조)
- [ ] Spring 계층 구조 이해 (Entity → Repository → Service → Controller)
- [ ] 테스트 파일 변경 명령어 미리 테스트
- [ ] 콘솔 로그 스크린샷 준비
- [ ] 시간 측정 (전체 20분)
- [ ] 백업 계획 (배포 버전 준비)

---

## 💡 발표 팁

1. **실시간 데모**
   - 각 단계에서 콘솔 로그를 천천히 읽어주기
   - 색상이 나타나는 부분 강조하기

2. **계층 구조 설명**
   - 화살표로 흐름 표시
   - 각 계층의 책임 명확히 하기

3. **Test 실패 케이스**
   - 왜 package가 실행 안 되는지 강조
   - Maven의 의도적인 설계임을 설명

4. **질문 대비**
   - Q: "왜 test를 건너뛸 수 없나?"
   - A: "Maven은 품질을 보장하기 위해 의도적으로 설계했습니다"

---

**준비 완료!** 위의 순서대로 진행하면 20분 안에 발표를 마칠 수 있습니다.
