# Maven Lifecycle 데모 프로젝트

## 📚 프로젝트 개요

이 프로젝트는 **Maven 빌드 라이프사이클**의 각 phase가 실제로 어떻게 동작하는지 명확하게 보여주기 위해 설계되었습니다.

### 목표
- Maven Lifecycle의 각 phase 이해
- `mvn package` 실행 시 어떤 단계를 거치는지 시각화
- Test 실패 시 build가 중단되는 이유 파악
- JAR 파일 생성 과정 확인

---

## 🏗️ 프로젝트 구조

```
maven-lifecycle-demo/
├── pom.xml                          # Maven 설정 파일
├── src/
│   ├── main/
│   │   └── java/com/example/
│   │       ├── Application.java     # Spring Boot 메인 클래스
│   │       └── Calculator.java      # 비즈니스 로직
│   └── test/
│       └── java/com/example/
│           ├── CalculatorTest.java  # 성공 테스트
│           └── CalculatorFailingTest.java  # 실패 테스트 (선택적)
└── target/                          # 빌드 결과물 폴더
```

---

## 🚀 실행 방법

### 1. 성공 케이스 (모든 phase 통과)

#### 명령어
```bash
cd maven-lifecycle-demo
mvn clean package
```

#### 설명
- `clean`: target 폴더 삭제 (이전 빌드 결과 제거)
- `package`: 전체 Maven Lifecycle 실행 (validate → compile → test → package)

### 2. 실패 케이스 (test 실패로 package 중단)

#### 명령어
```bash
# 방법 1: 실패 테스트 클래스로 변경
cd src/test/java/com/example
# CalculatorTest.java 의 이름을 CalculatorTestBackup.java 로 변경
# CalculatorFailingTest.java 의 이름을 CalculatorTest.java 로 변경

# 그 후 실행
mvn clean package

# 방법 2: 특정 테스트만 실행
mvn test -Dtest=CalculatorFailingTest
```

### 3. 각 단계별 개별 실행

```bash
# Validate phase (검증)
mvn validate

# Compile phase (컴파일)
mvn compile

# Test phase (테스트)
mvn test

# Package phase (패키징)
mvn package

# Install phase (로컬 저장소에 설치)
mvn install

# JAR 파일 실행
java -jar target/maven-lifecycle-demo-1.0.0.jar
```

---

## 📋 Maven Lifecycle과 Phase 관계

### Maven Lifecycle 이란?

Maven은 **선형적인 빌드 프로세스**를 정의합니다. 특정 phase를 실행하면, 그 이전의 모든 phase들이 자동으로 실행됩니다.

### Clean Lifecycle (선택적)

```
clean
  ↓
pre-clean
  ↓
clean (target 폴더 삭제)
  ↓
post-clean
```

### Default Lifecycle (필수)

```
1. validate        ← 프로젝트 설정 유효성 검사
2. initialize      ← 빌드 초기화
3. generate-sources
4. process-sources
5. generate-resources
6. process-resources  ← 리소스 파일 처리
7. compile         ← ✅ Java 소스 컴파일 (src/main/java → target/classes)
8. process-classes
9. generate-test-sources
10. process-test-sources
11. generate-test-resources
12. process-test-resources
13. test-compile   ← 테스트 소스 컴파일
14. process-test-classes
15. test           ← ✅ JUnit 테스트 실행
16. process-test-results
17. prepare-package
18. package        ← ✅ JAR 파일 생성 (target/classes → target/*.jar)
19. pre-integration-test
20. integration-test
21. post-integration-test
22. verify
23. install        ← 로컬 Maven 저장소에 설치
24. deploy         ← 원격 저장소에 배포
```

### 중요한 Phase 3가지

| Phase | 설명 | 입력 | 출력 |
|-------|------|------|------|
| **compile** | Java 소스 코드를 컴파일 | src/main/java/*.java | target/classes/*.class |
| **test** | JUnit 테스트 실행 | src/test/java/*Test.java | 테스트 결과 보고 |
| **package** | 컴파일된 클래스와 리소스를 JAR로 압축 | target/classes + pom.xml | target/*.jar |

---

## ⚠️ 왜 Test 실패 시 Package가 중단되는가?

### Maven Phase의 선형성

`mvn package` 를 실행하면:

```
compile → test → package
   ↓       ↓        ↓
  성공    실패    실행 안 됨
```

**핵심 원칙**: Maven은 **모든 phase의 성공을 보장**하려고 합니다.
- Phase 1 실패 → Phase 2, 3 실행 안 됨
- 이를 통해 **미완성/오류가 있는 코드는 배포되지 않음**

### 콘솔 로그에서 확인

**성공 시**
```
[INFO] BUILD SUCCESS
```

**실패 시**
```
[ERROR] Tests run: 3, Failures: 3, Errors: 0, Skipped: 0
[INFO] BUILD FAILURE
```

---

## 📊 예상 콘솔 로그 (성공 케이스)

```
$ mvn clean package

[INFO] Scanning for projects...
[INFO]
[INFO] ----< com.example:maven-lifecycle-demo:jar:1.0.0 >----
[INFO] Building Maven Lifecycle Demo 1.0.0
[INFO] ================================[ jar ]================================

[INFO]
[INFO] --- maven-clean-plugin:3.1.0:clean (default-clean) @ maven-lifecycle-demo ---
[INFO] Deleting D:\egov-vscode2026\maven-lifecycle-demo\target

[INFO]
[INFO] --- maven-antrun-plugin:3.1.0:run (validate-phase) @ maven-lifecycle-demo ---
[INFO] Executing tasks

================================================================
[VALIDATE Phase] 프로젝트 설정 유효성 검사 시작...
- Project: maven-lifecycle-demo
- Version: 1.0.0
================================================================

[INFO]
[INFO] --- maven-resources-plugin:2.6:resources (default-resources) @ maven-lifecycle-demo ---

[INFO]
[INFO] --- maven-compiler-plugin:3.11.0:compile (default-compile) @ maven-lifecycle-demo ---

[INFO]
[INFO] --- maven-antrun-plugin:3.1.0:run (compile-phase) @ maven-lifecycle-demo ---
[INFO] Executing tasks

================================================================
[COMPILE Phase] Java 소스 코드 컴파일 중...
- Source: src/main/java
- Target: target/classes
================================================================

[INFO]
[INFO] --- maven-resources-plugin:2.6:testResources (default-testResources) @ maven-lifecycle-demo ---

[INFO]
[INFO] --- maven-compiler-plugin:3.11.0:testCompile (default-testCompile) @ maven-lifecycle-demo ---

[INFO]
[INFO] --- maven-surefire-plugin:3.0.0-M9:test (default-test) @ maven-lifecycle-demo ---

[INFO]
[INFO] --- maven-antrun-plugin:3.1.0:run (test-phase-start) @ maven-lifecycle-demo ---
[INFO] Executing tasks

================================================================
[TEST Phase] JUnit 테스트 실행 중...
- Test Directory: src/test/java
- Test Classes: src/test/java/com/example/*Test.java
================================================================

Running com.example.CalculatorTest

[TEST SETUP] 계산기 객체 초기화
[TEST] 덧셈 테스트: 5 + 3
계산 중: 5 + 3
[PASSED] 덧셈 테스트 성공!

[TEST SETUP] 계산기 객체 초기화
[TEST] 뺄셈 테스트: 10 - 4
계산 중: 10 - 4
[PASSED] 뺄셈 테스트 성공!

[TEST SETUP] 계산기 객체 초기화
[TEST] 곱셈 테스트: 6 * 7
계산 중: 6 * 7
[PASSED] 곱셈 테스트 성공!

[TEST SETUP] 계산기 객체 초기화
[TEST] 양수 판별 테스트
[PASSED] 양수 판별 테스트 성공!

[TEST SETUP] 계산기 객체 초기화
[TEST] 복합 계산: (10 + 5) * 2
계산 중: 10 + 5
계산 중: 15 * 2
[PASSED] 복합 계산 테스트 성공!

Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.123 s

[INFO]
[INFO] --- maven-jar-plugin:3.3.0:jar (default-jar) @ maven-lifecycle-demo ---
[INFO] Building jar: D:\egov-vscode2026\maven-lifecycle-demo\target\maven-lifecycle-demo-1.0.0.jar

[INFO]
[INFO] --- spring-boot-maven-plugin:2.7.14:repackage (repackage) @ maven-lifecycle-demo ---
[INFO] Replacing main artifact with repackaged archive

[INFO]
[INFO] --- maven-antrun-plugin:3.1.0:run (package-phase-end) @ maven-lifecycle-demo ---
[INFO] Executing tasks

================================================================
[PACKAGE Phase] JAR 파일 생성 완료!
- JAR File: target/maven-lifecycle-demo-1.0.0.jar
- Location: target/
================================================================

[INFO] BUILD SUCCESS
[INFO] Total time:  2.345 s
[INFO] Finished at: 2026-05-14T10:30:45+09:00
[INFO] Final Memory: 42M/128M
```

---

## 📊 예상 콘솔 로그 (실패 케이스)

```
$ mvn clean package

[INFO] Scanning for projects...
[INFO]
[INFO] ----< com.example:maven-lifecycle-demo:jar:1.0.0 >----
[INFO] Building Maven Lifecycle Demo 1.0.0
[INFO] ================================[ jar ]================================

... (validate, compile 단계는 성공) ...

Running com.example.CalculatorFailingTest

[TEST SETUP] 계산기 객체 초기화
[TEST] 실패하는 덧셈 테스트: 5 + 3
계산 중: 5 + 3

[ERROR] Tests run: 3, Failures: 3, Errors: 0, Skipped: 0, Time elapsed: 0.234 s <<< FAILURE!

[ERROR] testAdditionFails(com.example.CalculatorFailingTest)
Expected: <9>
But was:  <8>

[INFO] BUILD FAILURE

[INFO] --- maven-surefire-plugin:3.0.0-M9:test (default-test) @ maven-lifecycle-demo ---

⚠️  ここから先は実行されません！
[INFO] --- maven-jar-plugin:3.3.0:jar は実行されません
[INFO] --- maven-antrun-plugin:3.1.0:run (package-phase-end) は実行されません

[ERROR] Total time:  1.234 s
[ERROR] Finished at: 2026-05-14T10:31:15+09:00
[ERROR] Final Memory: 38M/128M
```

**중요**: package phase가 실행되지 않으므로 JAR 파일이 생성되지 않습니다!

---

## 📁 Target 폴더 결과

### 성공 시

```
target/
├── classes/                          ← compile phase 결과
│   ├── application.properties
│   └── com/
│       └── example/
│           ├── Application.class
│           └── Calculator.class
├── test-classes/                     ← test compile 결과
│   └── com/
│       └── example/
│           └── CalculatorTest.class
├── maven-lifecycle-demo-1.0.0.jar    ← ✅ package phase 결과 (메인 JAR)
├── maven-lifecycle-demo-1.0.0-plain.jar  ← 플레인 JAR
├── surefire-reports/                 ← 테스트 보고서
└── ...
```

### 실패 시

```
target/
├── classes/                          ← compile phase 결과 (있음)
├── test-classes/                     ← test compile 결과 (있음)
├── surefire-reports/                 ← 테스트 실패 보고서
└── ❌ maven-lifecycle-demo-1.0.0.jar (생성되지 않음!)
```

---

## 🎯 발표 시나리오

### 시나리오 1: 성공 흐름 보여주기

1. **명령어 실행**
   ```bash
   mvn clean package
   ```

2. **콘솔 로그 관찰**
   - `[VALIDATE Phase]` 메시지 보이기
   - `[COMPILE Phase]` 메시지 보이기
   - `[TEST Phase]` 메시지와 테스트 실행 보이기
   - `[PACKAGE Phase]` 메시지와 JAR 생성 보이기
   - `BUILD SUCCESS` 메시지 확인

3. **결과 확인**
   ```bash
   dir target\*.jar
   # maven-lifecycle-demo-1.0.0.jar 파일 확인
   
   java -jar target/maven-lifecycle-demo-1.0.0.jar
   ```

### 시나리오 2: 실패 흐름 보여주기

1. **테스트 파일 변경**
   ```bash
   # CalculatorTest.java를 CalculatorTest_Backup.java로 변경
   # CalculatorFailingTest.java를 CalculatorTest.java로 변경
   ```

2. **명령어 실행**
   ```bash
   mvn clean package
   ```

3. **콘솔 로그 관찰**
   - compile phase는 성공
   - test phase에서 실패 메시지 출력
   - `BUILD FAILURE` 메시지 확인
   - **package phase 전혀 실행 안 됨!**

4. **결과 확인**
   ```bash
   dir target\*.jar
   # JAR 파일이 없음!
   ```

---

## 🔍 Maven Lifecycle 핵심 요점

### 1. **순차적 실행 (Sequential Execution)**
```
mvn package 실행 = 
  validate 
  → compile 
  → test 
  → package
```
- 각 phase는 이전 phase가 성공한 후에만 실행
- 하나라도 실패하면 나머지는 실행 안 됨

### 2. **중단 조건 (Failure Condition)**
```
Test Failure Example:
  compile ✅ (Success)
    ↓
  test ❌ (Failure)
    ↓
  package ⛔ (Not Executed)
```

### 3. **설정 우선순위 (Configuration Priority)**
```
pom.xml의 플러그인 설정 > Maven 기본 설정
```

### 4. **JAR 생성 조건**
- 모든 phase (validate ~ package) 성공
- 테스트 실패 시 JAR 생성 안 됨
- 이는 **의도적인 안전 장치**

---

## 💡 학습 포인트

### Q1: 왜 `mvn package`만 실행해도 compile과 test가 모두 실행되나?

**A**: Maven의 각 phase는 **계층적 의존성**을 가지고 있습니다.
- `package` phase를 실행하라 = "package를 실행하기 위한 모든 선행 phase를 실행하라"는 뜻
- 마치 `npm build`가 내부적으로 여러 스크립트를 순서대로 실행하는 것과 유사

### Q2: 왜 test 실패 시 build가 중단되나?

**A**: **품질 보증 메커니즘**입니다.
- 테스트 실패 = 코드에 버그가 있다는 뜻
- 버그가 있는 코드를 package/deploy하면 안 됨
- 따라서 Maven은 의도적으로 build를 중단

### Q3: test를 건너뛰고 package만 할 수는 없나?

**A**: 가능하지만 권장하지 않습니다.
```bash
mvn package -DskipTests
# 또는
mvn clean package -Dmaven.test.skip=true
```
⚠️ **주의**: 이 방법은 테스트를 무시하는 것이므로 위험합니다!

---

## 📝 pom.xml 설정 상세 설명

### 1. Maven Compiler Plugin
```xml
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-compiler-plugin</artifactId>
  <version>3.11.0</version>
  <configuration>
    <source>11</source>      <!-- Java 11로 컴파일 -->
    <target>11</target>      <!-- Java 11로 타겟 -->
    <encoding>UTF-8</encoding>
  </configuration>
</plugin>
```
**역할**: src/main/java의 `.java` 파일을 `.class`로 컴파일

### 2. Maven Surefire Plugin
```xml
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-surefire-plugin</artifactId>
  <version>3.0.0-M9</version>
  <configuration>
    <includes>
      <include>**/*Test.java</include>
    </includes>
  </configuration>
</plugin>
```
**역할**: `*Test.java` 패턴의 테스트 클래스 자동 발견 및 실행

### 3. Maven JAR Plugin
```xml
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-jar-plugin</artifactId>
  <version>3.3.0</version>
  <configuration>
    <archive>
      <manifest>
        <mainClass>com.example.Application</mainClass>
      </manifest>
    </archive>
  </configuration>
</plugin>
```
**역할**: target/classes의 모든 `.class`를 JAR로 압축 (메인 클래스 지정)

### 4. Spring Boot Maven Plugin
```xml
<plugin>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-maven-plugin</artifactId>
  <version>2.7.14</version>
  <executions>
    <execution>
      <goals>
        <goal>repackage</goal>
      </goals>
    </execution>
  </executions>
</plugin>
```
**역할**: 기본 JAR를 독립 실행 가능한 "Fat JAR"로 재패키징

---

## 🎓 Maven 관련 명령어 치트시트

```bash
# 전체 라이프사이클 실행 (권장)
mvn clean package

# 특정 phase까지만 실행
mvn compile          # compile phase까지
mvn test            # test phase까지
mvn package         # package phase까지

# 특정 phase 재실행 (clean 없음)
mvn clean compile
mvn clean test
mvn clean package

# 테스트 건너뛰기 (비권장)
mvn clean package -DskipTests

# 특정 테스트만 실행
mvn test -Dtest=CalculatorTest
mvn test -Dtest=CalculatorFailingTest

# 의존성 관련
mvn dependency:tree         # 의존성 트리 보기
mvn dependency:resolve      # 의존성 다운로드

# 빌드 결과 확인
mvn clean package
java -jar target/maven-lifecycle-demo-1.0.0.jar

# IDE 설정 생성 (IDE에서 인식하도록)
mvn idea:idea       # IntelliJ IDEA
mvn eclipse:eclipse # Eclipse
```

---

## 🌟 결론

이 프로젝트를 통해 배울 수 있는 것:

1. **Maven Lifecycle의 순차성** - 각 phase는 선형적으로 실행됨
2. **build 실패 원인** - 테스트 실패가 build 중단의 주요 원인
3. **품질 보증** - Maven의 자동 실패 메커니즘이 코드 품질을 보장
4. **JAR 생성 조건** - 모든 phase의 성공이 필수 조건

**발표 시 핵심 메시지**:
> "Maven은 단순한 빌드 도구가 아니라, 품질을 보장하는 안전 장치입니다!"

---

**작성일**: 2026-05-14  
**프로젝트**: maven-lifecycle-demo  
**Java 버전**: 11+  
**Spring Boot 버전**: 2.7.14
