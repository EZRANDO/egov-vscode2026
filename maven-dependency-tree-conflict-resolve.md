## 정세호
# 📦 Java Dependency Conflict Troubleshooting Guide

자바 개발 중 발생하는 의존성 충돌은 **"컴파일은 되는데 실행하면 터지는"** 가장 골치 아픈 문제입니다. 이 문서는 그 진단부터 해결까지의 과정을 정리합니다.

---

## 1. 주요 에러 증상 (Symptoms)

런타임에 다음 에러가 발생한다면 십중팔구 **버전 충돌**입니다.

* **`java.lang.NoSuchMethodError`**
    * A 라이브러리가 필요한 메서드가 포함된 B-v1을 찾는데, 실제로는 해당 메서드가 없는 B-v2가 로드됨.
* **`java.lang.NoClassDefFoundError`**
    * 컴파일 시점에는 존재하던 클래스가 실행 시점 클래스패스에서 사라짐.
* **`java.lang.ClassCastException`**
    * 서로 다른 경로로 들어온 동일 이름의 클래스가 서로 형변환될 때 발생.

---

## 2. 의존성 진단 (Diagnosis)

가장 먼저 할 일은 전체 의존성 지도를 그려보는 것입니다.

```bash
# -Dverbose 옵션은 생략된 항목까지 모두 보여줍니다. 
# 따라서 의존성 충돌을 해결하고자 한다음 해당 옵션을 적용해야합니다.

# > *.txt : -Dverbose를 쓰면 내용이 매우 길기 때문입니다. 
mvn dependency:tree -Dverbose > tree.txt
```

---

## 3. 의존성 트리 읽는 법 (Analysis)

```
[INFO] com.example:complex-dependency-lab:jar:1.0-SNAPSHOT
[INFO] +- org.seleniumhq.selenium:selenium-java:jar:4.8.3:compile
[INFO] |  +- org.seleniumhq.selenium:selenium-api:jar:4.8.3:compile
[INFO] |  +- org.seleniumhq.selenium:selenium-chrome-driver:jar:4.8.3:compile
[INFO] |  |  +- com.google.auto.service:auto-service-annotations:jar:1.0.1:compile
[INFO] |  |  +- com.google.auto.service:auto-service:jar:1.0.1:compile
[INFO] |  |  |  \- com.google.auto:auto-common:jar:1.2:compile

-Dverbose의 경우
\- (com.google.guava:guava:jar:31.0.1-jre:compile - omitted for conflict with 31.1-jre)
```
1. **+-** : 이 라이브러리 아래에 하위 의존성이 더 있음을 의미합니다.
2. **\\-** : 내 부모의 마지막 자식
3. **()** : 최종적으로 생략된 의존성

### 📏 Maven의 우선순위: 가까운 정의 전략 (Nearest Definition)
Maven은 트리의 **깊이(Depth)가 더 얕은 쪽**의 버전을 선택합니다.
1.  **Direct:** 내가 직접 `pom.xml`에 적은 버전 (최우선)
2.  **Transitive:** 남이(라이브러리가) 끌고 들어온 버전 (차선)

메이븐은 트리를 그리다가 "어? httpclient 안에도 똑같은 게 있네? 그럼 더 깊이 들어갈 필요 없이 얕은 곳에 있는 걸 쓸게"라고 판단한 것이죠.
```
# 예시 
 +- org.slf4j:slf4j-api:jar:1.7.10:compile
 |  |  |  +- (org.slf4j:slf4j-api:jar:1.7.30:compile - omitted for conflict with 1.7.10)
```

### Dverbose

| 표시 문구 | 의미 |
| :--- | :--- |
| **omitted for conflict** | "A 버전과 B 버전이 충돌했을 때, Maven의 전략(거리가 더 가깝거나 먼저 선언됨 등)에 따라 우선순위가 낮은 버전이 제외되었다"는 뜻입니다. |
| **omitted for duplicate** | "이미 있어서 안 가져온다" |

"어차피 답은 정해져 있는데 왜 굳이 충돌했다고 시끄럽게 알려주느냐"
메이븐은 아주 단순한 규칙(가까운 정의 전략)으로 승자를 정합니다.
A가 1.0을 원하고 B가 2.0을 원하네? 근데 A가 더 가까우니까 난 1.0을 쓸래!"

---

## 4. 해결 방법 (Solutions)

높은 버전으로 통일
org.slf4j:slf4j-api:jar:1.7.10:compile

(com.google.guava:guava:jar:11.0.2:compile - omitted for conflict with 31.1-jre)
위험도: ⚠️ 매우 높음. Guava는 버전 간 하위 호환성이 깨지는 경우가 많기로 악명이 높습니다. 만약 Hadoop 내부 코드가 Guava 11에만 있던 (지금은 사라진) 메서드를 호출한다면 NoSuchMethodError가 터질 확률이 아주 높습니다.
버전을 강제로 하나로 맞췄을 때, 너무 낮은 버전을 원하는 라이브러리(Hadoop 등)가 최신 버전에서 에러를 낼 수도 있습니다. * 해결법: 만약 실행 중 NoSuchMethodError가 난다면, 버전 숫자를 조금씩 조정해 보며 모든 라이브러리가 적당히 만족하는 '합의점'을 찾아야 합니다. 보통은 최신 버전을 선택하는 것이 보안과 성능 면에서 유리합니다.

[INFO] |  +- com.google.guava:listenablefuture:jar:9999.0-empty-to-avoid-conflict-with-guava:compile
작동 원리: 메이븐(Maven)이 여러 경로에서 listenablefuture 라이브러리를 발견했을 때, "오, 9999.0 버전이 제일 최신이네!"라며 이 빈 껍데기를 선택하게 만듭니다.
Guava (안에 ListenableFuture 클래스가 있음)
Standalone ListenableFuture (안에도 ListenableFuture 클래스가 있음)

기능,<dependencyManagement>,그냥 <dependency>
주요 역할,버전 정책 수립 (지도 역할),실제 라이브러리 도입 (도구 가져오기)
버전 우선순위,매우 높음 (전이 의존성 제어),최우선 (가장 가까운 정의)
권장 상황,전이 의존성의 버전을 강제할 때,내 코드에서 직접 import 해서 쓸 때

트리를 내려가며 exclude를 수십 개 다는 게 귀찮다면, 그냥 최상위 pom.xml의 <dependencyManagement>에 딱 한 줄만 적으세요.
핵심: 트리의 깊이가 10단계든 100단계든 상관없습니다. 관리자(management)가 버전을 명시하는 순간, 모든 하위 계층의 싸움은 그 즉시 종료됩니다.
전략: tree.txt에서 가장 많이 보이는 말썽꾸러기(Guava, Jackson 등) 몇 개만 골라 최상단에서 버전을 고정해버리면 노란 줄의 80%가 사라집니다.

4.1.90(승자)을 골랐을 때: "아무것도 안 해도 되잖아?"
4.1.60(패자)을 골랐을 때: "이건 강제로 바꿔야 해!"
결론부터 말씀드리면, IDE는 "이미 이기고 있는 놈은 굳이 관리(Management)할 필요가 없다"고 제멋대로 판단하고 있기 때문입니다.


### ① 원치 않는 의존성 제거 (`<exclusions>`)
특정 라이브러리가 불필요하게 낮은 버전을 끌고 올 때 사용합니다.

```xml
<dependency>
    <groupId>com.example</groupId>
    <artifactId>some-library</artifactId>
    <version>1.0.0</version>
    <exclusions>
        <exclusion>
            <groupId>org.bad-version</groupId>
            <artifactId>conflict-module</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

### ② 버전 강제 고정 (`<dependencyManagement>`)
프로젝트 전체에 적용되는 **"버전 가이드라인"**을 선언합니다.

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.15.2</version> </dependency>
    </dependencies>
</dependencyManagement>
```

---

## 5. 실무 팁 (Pro-Tips)

* **IDE 적극 활용:**
    * **IntelliJ:** `pom.xml` 우클릭 -> `Diagrams` -> `Show Dependencies`로 시각적 확인 가능.
    * **Dependency Analyzer:** 별도 플러그인이나 내장 기능을 통해 `Conflict`만 따로 필터링 가능.
* **Spring Boot Parent:** 가급적 직접 버전을 명시하지 말고, 스프링 부트가 관리하는 BOM(Bill of Materials)에 의존하는 것이 가장 안전합니다.
* **Scope 확인:** `compile`, `runtime`, `provided`, `test` 범위를 명확히 하여 불필요한 라이브러리가 배포본에 포함되지 않게 관리하세요.

관리는 전문가에게" - BOM 활용
가장 똑똑한 방법은 검증된 버전 세트인 BOM(Bill of Materials)을 가져오는 것입니다.
원리: 스프링 부트(spring-boot-starter-parent)나 구글 클라우드(libraries-bom) 같은 라이브러리들은 수백 개의 라이브러리 간의 "충돌 없는 버전 조합"을 이미 다 맞춰두었습니다

상황,메이븐의 판단,결과
"A가 1.0, B가 2.0 원함","""A가 가까우니 1.0을 쓰자""",B가 2.0 전용 기능을 쓰면 프로그램 중단
Conflict 표시,"""일단 1.0을 골랐지만, B가 위험해!""",개발자가 확인 후 안전한 버전으로 고정 유도