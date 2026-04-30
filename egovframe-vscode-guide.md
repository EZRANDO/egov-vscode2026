---
marp: true
theme: default
paginate: true
---

# 전자정부 표준 프레임워크 VS Code 개발 환경 가이드
### 가볍고 강력한 eGovFrame 개발 환경 구축하기

---

## 1. 왜 VS Code 인가?

- **경량화**: 이클립스 기반의 eGovFrame IDE 대비 가볍고 빠른 실행 속도
- **확장성**: 방대한 마켓플레이스를 통한 무한한 확장 가능성 (Copilot, 다양한 Linter 등)
- **통합 환경**: 터미널, Git 연동, 디버깅 등 필수 기능이 내장되어 하나의 창에서 모든 작업 가능

---

## 2. 필수 확장 프로그램 (Extensions) 설치

VS Code에서 Java 및 Spring (eGovFrame) 개발을 위해 다음 확장팩들을 설치합니다.

1. **Extension Pack for Java**: Java 코드 편집, 컴파일, 디버깅, Maven/Gradle 지원
2. **Spring Boot Extension Pack**: Spring 프로젝트 지원, 대시보드, 자동 완성 기능
3. **Lombok Annotations Support for VS Code**: Lombok 사용 시 필수
4. **XML Tools**: MyBatis XML 매퍼 작성 및 포맷팅 시 유용

---

## 3. 프로젝트 가져오기 및 Maven 빌드

1. **프로젝트 열기**: `파일 > 폴더 열기` 로 기존 eGovFrame 프로젝트를 선택
2. **Java 버전 설정**: `settings.json` 에서 `java.configuration.runtimes` 로 JDK 1.8, 11, 17 등 프로젝트에 맞는 버전을 지정
3. **의존성 다운로드**: 하단 상태바 또는 Maven 플러그인을 통해 `pom.xml` 의 라이브러리 (eGovFrame core 등) 다운로드 확인
4. **빌드**: `mvn clean install` 명령어를 통합 터미널에서 실행하여 정상 빌드 확인

---

## 4. 실행 및 디버깅 (Run & Debug)

- **Spring Boot 내장 톰캣 실행**: `Spring Boot Dashboard` 확장이나 `Run Java` 버튼을 클릭하여 메인 클래스 실행
- **외부 Tomcat 연동 (WAR 배포 시)**: `Community Server Connectors` 확장을 통해 로컬 Tomcat 서버를 등록하고 WAR 파일을 배포하여 실행
- **디버깅 모드**: 브레이크포인트를 설정하고 디버그 모드로 실행하여 코드 라인 단위 추적 가능

---

## 5. VS Code Initializr 프로젝트 활용

새로운 eGovFrame 프로젝트를 시작할 때는 eGovFrame 공식 VS Code 확장을 활용하면 매우 편리합니다.
- **저장소**: [eGovFramework/egovframe-vscode-initializr](https://github.com/eGovFramework/egovframe-vscode-initializr)
- **주요 기능**: Spring Initializr처럼 UI를 통해 eGovFrame 템플릿(기본 웹, 공통 컴포넌트 등)을 손쉽게 생성합니다.
- **사용 방법**: 
  1. 확장을 설치 후 명령어 팔레트(`Cmd+Shift+P`) 열기
  2. `eGovFrame: Generate Project` 실행
  3. 기본 설정(Group, Artifact, 버전 등) 입력 후 프로젝트 뼈대 즉시 생성

---

## 6. VS Code Initializr 확장 프로그램 개발 가이드 (Node.js)

eGovFrame VS Code Initializr 오픈소스 확장에 직접 기여하거나 커스텀하고 싶다면 다음 과정을 따릅니다.

- **필수 환경**: Node.js, npm (또는 yarn)이 설치된 환경
- **초기 세팅**: 프로젝트 클론 후 터미널에서 `npm install`을 실행하여 의존성(TypeScript, VS Code API 등) 설치
- **실행 및 디버깅**: 
  - VS Code에서 프로젝트를 열고 `F5` 키를 눌러 **Extension Development Host** 모드로 실행
  - 팝업된 새 VS Code 창(샌드박스)에서 개발 중인 확장 기능을 즉시 테스트하고 중단점(Breakpoint) 디버깅 가능
- **패키징**: `vsce` (VS Code Extension Manager) 도구를 전역으로 설치(`npm i -g @vscode/vsce`)하고, `vsce package` 명령어로 `.vsix` 파일을 생성하여 배포

---

## 7. 결론 및 요약

- VS Code를 사용하면 무거운 이클립스를 벗어나 **빠르고 모던한 개발 환경**을 구축할 수 있습니다.
- 초기 세팅(Java 버전, 확장 프로그램, Maven 세팅)만 잘 맞추면 eGovFrame 프로젝트도 무리 없이 개발 가능합니다.
- 최신 AI 도구(GitHub Copilot 등)와의 연동성도 뛰어나 생산성을 극대화할 수 있습니다.

---

## Q&A
감사합니다! 질문 있으신 분?
