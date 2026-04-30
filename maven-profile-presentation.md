---
marp: true
theme: gaia
paginate: true
---

# Maven Profile을 활용한 환경별 설정 분리

---

## 1. 들어가며: 왜 환경 분리가 필요한가?

- **문제 상황:** 개발(Dev), 테스트(Staging), 운영(Prod) 환경은 서로 다른 설정을 요구합니다.
  - 데이터베이스 접속 정보 (URL, 계정, 비밀번호)
  - 외부 API 키 및 서비스 엔드포인트
  - 로깅 레벨 및 디버그 모드 설정
- **해결책:** **Maven Profile**을 사용하여 빌드 시점에 각 대상 환경에 맞는 설정을 동적으로 주입합니다.

---

## 2. Maven Profile이란?

- Maven의 `pom.xml` 파일 내에 정의할 수 있는 **조건부 설정 블록**입니다.
- 실행 환경, OS, JDK 버전, 또는 명령어 인자에 따라 선택적으로 활성화됩니다.
- 프로파일이 활성화되면 해당 블록에 정의된 의존성(Dependencies), 플러그인, 환경 변수(Properties) 등이 전체 빌드 과정에 병합됩니다.

---

## 3. pom.xml 구성하기 (프로파일 정의)

`pom.xml`에 `<profiles>` 태그를 추가하여 각 환경을 정의합니다.

```xml
<profiles>
    <!-- 개발 환경 (기본값으로 활성화) -->
    <profile>
        <id>dev</id>
        <activation>
            <activeByDefault>true</activeByDefault>
        </activation>
        <properties>
            <environment>dev</environment>
        </properties>
    </profile>

    <!-- 스테이징 환경 -->
    <profile>
        <id>staging</id>
        <properties>
            <environment>staging</environment>
        </properties>
    </profile>

    <!-- 운영 환경 -->
    <profile>
        <id>prod</id>
        <properties>
            <environment>prod</environment>
        </properties>
    </profile>
</profiles>
```

---

## 4. 리소스 필터링 (Resource Filtering) 설정

Maven에 정의된 프로퍼티 변수를 `application.properties`나 `application.yml` 파일로 주입하려면 리소스 필터링 기능을 활성화해야 합니다.

```xml
<build>
    <resources>
        <resource>
            <directory>src/main/resources</directory>
            <filtering>true</filtering> <!-- 변수 치환(필터링) 활성화 -->
        </resource>
    </resources>
</build>
```

---

## 5. 설정 파일 분리 및 적용

스프링 부트(Spring Boot) 환경의 경우, Maven 빌드 시 생성된 변수를 활용해 Spring Profile을 활성화할 수 있습니다.

**`src/main/resources/application.properties`**
```properties
# Maven에서 설정한 environment 변수(@environment@) 치환
spring.profiles.active=@environment@
```

이후 환경별로 설정 파일을 나누어 관리합니다:
- `application-dev.properties`
- `application-staging.properties`
- `application-prod.properties`

---

## 6. 빌드 및 실행 방법

빌드 시 Maven 명령어에 `-P` 옵션을 주어 원하는 프로파일을 명시적으로 활성화합니다.

```bash
# 기본(dev) 환경으로 빌드 (activeByDefault 적용)
mvn clean package

# 스테이징 환경으로 빌드
mvn clean package -Pstaging

# 운영 환경으로 빌드
mvn clean package -Pprod
```

> **💡 Tip:** CI/CD 파이프라인 (Jenkins, GitHub Actions 등)에서 환경별 배포 스크립트를 작성할 때 이 `-P` 옵션을 활용하면 자동화가 매우 쉬워집니다.

---

## 7. 마무리 및 기대 효과

- **유연성:** 단일 소스 코드(Single Source of Truth)로 여러 환경에 유연하게 대응합니다.
- **안전성:** 실수로 운영 서버가 개발 DB를 바라보거나 하는 휴먼 에러 및 대형 사고를 미연에 방지합니다.
- **자동화:** CI/CD와 완벽하게 통합되어 빌드 및 배포 프로세스를 간소화할 수 있습니다.

<br>

**감사합니다.** 
Q&A
