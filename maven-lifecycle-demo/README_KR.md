# Maven Lifecycle 데모 프로젝트 - 실행 가이드

## 📋 프로젝트 준비 완료 체크리스트

✅ pom.xml 설정 완료
✅ 메인 애플리케이션 클래스 작성 (Application.java)
✅ 비즈니스 로직 클래스 작성 (Calculator.java)
✅ 성공 테스트 작성 (CalculatorTest.java)
✅ 실패 테스트 작성 (CalculatorFailingTest.java)
✅ 콘솔 로그 명확화 설정 완료

## 🚀 실행 전 요구사항

### Windows 환경에서 Maven 설치

#### 1. Java 설치 확인
```bash
java -version
javac -version
```

#### 2. Maven 설치 (Windows)

**방법 1: Chocolatey 사용 (권장)**
```powershell
choco install maven
```

**방법 2: 수동 설치**
- Apache Maven 공식 사이트: https://maven.apache.org/download.cgi
- 최신 버전 다운로드 (apache-maven-3.9.4-bin.zip)
- 적절한 폴더에 압축 해제 (예: C:\apache-maven-3.9.4)
- 환경변수 설정:
  - MAVEN_HOME=C:\apache-maven-3.9.4
  - PATH에 %MAVEN_HOME%\bin 추가

#### 3. Maven 설치 확인
```bash
mvn --version
```

## 🎯 프로젝트 빌드 및 실행

### 위치 이동
```bash
cd d:\egov-vscode2026\maven-lifecycle-demo
```

### 성공 시나리오 (모든 phase 통과)

```bash
mvn clean package
```

**예상 결과:**
- [VALIDATE Phase] 메시지 출력
- [COMPILE Phase] 메시지와 함께 소스 코드 컴파일
- [TEST Phase] 메시지와 함께 5개의 테스트 실행
- [PACKAGE Phase] 메시지와 함께 JAR 생성
- BUILD SUCCESS 메시지

**JAR 파일 확인:**
```bash
dir target\*.jar
# 또는
ls target/*.jar
```

**JAR 파일 실행:**
```bash
java -jar target/maven-lifecycle-demo-1.0.0.jar
```

### 실패 시나리오 (test 실패로 package 중단)

**테스트 파일 변경:**
```bash
# 현재 디렉토리: d:\egov-vscode2026\maven-lifecycle-demo

# 1. 성공 테스트 파일을 백업으로 변경
cd src/test/java/com/example
ren CalculatorTest.java CalculatorTest.java.bak
ren CalculatorFailingTest.java CalculatorTest.java
cd ../../../

# 또는 특정 테스트만 실행
mvn test -Dtest=CalculatorFailingTest
```

**빌드 실행:**
```bash
mvn clean package
```

**예상 결과:**
- [VALIDATE Phase] 성공
- [COMPILE Phase] 성공
- [TEST Phase]에서 테스트 실패
- **[PACKAGE Phase] 실행되지 않음! ⚠️**
- BUILD FAILURE 메시지

**JAR 파일 미생성 확인:**
```bash
dir target\*.jar
# JAR 파일이 없음을 확인
```

## 📊 각 Phase별 개별 실행

발표에서 각 phase를 차례대로 보여줄 때 유용합니다:

```bash
# 1. Validate Phase 실행
mvn validate
# 출력: [VALIDATE Phase] 프로젝트 설정 유효성 검사 시작...

# 2. Compile Phase 실행
mvn clean compile
# 출력: [VALIDATE Phase] + [COMPILE Phase]

# 3. Test Phase 실행
mvn clean test
# 출력: [VALIDATE Phase] + [COMPILE Phase] + [TEST Phase]

# 4. Package Phase 실행
mvn clean package
# 출력: [VALIDATE Phase] + [COMPILE Phase] + [TEST Phase] + [PACKAGE Phase]
```

## 🔍 발표 시나리오별 실행 명령어

### 시나리오 1: 전체 빌드 프로세스 보여주기 (5분)

```bash
# Step 1: 깨끗한 상태에서 시작
mvn clean

# Step 2: Compile까지만 보여주기
mvn compile

# Step 3: Test까지 보여주기
mvn test

# Step 4: 전체 Package까지 보여주기
mvn clean package

# Step 5: 생성된 JAR 확인
dir target\*.jar
java -jar target/maven-lifecycle-demo-1.0.0.jar
```

### 시나리오 2: 테스트 실패 케이스 보여주기 (5분)

```bash
# Step 1: 테스트 파일 백업 및 변경
cd src/test/java/com/example
ren CalculatorTest.java CalculatorTest.java.bak
ren CalculatorFailingTest.java CalculatorTest.java
cd ../../../

# Step 2: 실패하는 빌드 실행
mvn clean package

# Step 3: JAR 파일 미생성 확인
dir target\*.jar
# (JAR 파일이 없음을 보여주기)

# Step 4: 원래대로 복원
cd src/test/java/com/example
ren CalculatorTest.java CalculatorFailingTest.java
ren CalculatorTest.java.bak CalculatorTest.java
cd ../../../
```

## 📁 프로젝트 구조 확인

```bash
# 프로젝트 디렉토리 구조 보기
tree /F

# 또는 PowerShell에서
Get-ChildItem -Recurse
```

## 💾 빌드 결과물 위치

성공 시 생성되는 파일들:

```
target/
├── classes/                          # 컴파일된 메인 클래스
│   ├── com/example/Application.class
│   ├── com/example/Calculator.class
│   └── ...
├── test-classes/                     # 컴파일된 테스트 클래스
│   └── com/example/CalculatorTest.class
├── surefire-reports/                 # 테스트 실행 보고서
│   ├── com.example.CalculatorTest.txt
│   └── TEST-com.example.CalculatorTest.xml
├── maven-lifecycle-demo-1.0.0.jar    # 메인 JAR (Spring Boot Fat JAR)
└── maven-lifecycle-demo-1.0.0-plain.jar  # 플레인 JAR
```

## ⚠️ 주의사항

1. **Test 건너뛰기 (비권장)**
   ```bash
   mvn package -DskipTests
   # 이 방법은 테스트를 무시하므로 데모에서는 사용하지 마세요
   ```

2. **특정 테스트만 실행**
   ```bash
   mvn test -Dtest=CalculatorTest
   mvn test -Dtest=CalculatorFailingTest
   ```

3. **Verbose 로그 (더 많은 정보)**
   ```bash
   mvn clean package -X
   ```

## 🎓 학습 포인트 정리

### Maven Lifecycle 순서 (Default Lifecycle)
```
validate → compile → test → package
```

### 각 Phase의 의미

| Phase | 명령어 | 역할 |
|-------|--------|------|
| validate | `mvn validate` | 프로젝트 설정 검사 |
| compile | `mvn compile` | Java 소스 → .class로 컴파일 |
| test | `mvn test` | JUnit 테스트 실행 |
| package | `mvn package` | 컴파일된 파일 → JAR로 압축 |

### 왜 Test 실패 시 Build가 중단되나?

```
mvn package 실행
    ↓
validate ✅
    ↓
compile ✅
    ↓
test ❌ (FAILURE!)
    ↓
package ⛔ (Not Executed!)
```

**이유**: Maven은 모든 단계의 성공을 보장합니다.
- 테스트 실패 = 버그가 있다는 뜻
- 버그가 있는 코드는 패키징되면 안 됨
- 따라서 의도적으로 build를 중단

---

**준비 완료!** 발표에서 사용할 준비가 다 되었습니다. 위의 실행 명령어를 순서대로 따라가면서 콘솔 로그와 결과를 보여주면 됩니다.
