package com.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Maven Lifecycle Demo - Spring Boot Application
 * 
 * 이 애플리케이션은:
 * 1. Maven Lifecycle의 각 phase를 명확하게 보여줍니다
 * 2. Spring의 계층 구조를 실제로 구현합니다:
 *    Entity → Repository → Service → Controller
 * 3. JPA, Lombok, Logging을 사용한 전문가 수준의 Spring 애플리케이션입니다
 * 
 * 주요 기능:
 * - H2 인메모리 데이터베이스 사용
 * - JPA를 통한 데이터 접근
 * - RESTful API 제공 (/api/v1/users)
 * - 트랜잭션 관리
 * - 로깅 및 예외 처리
 */
@Slf4j
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        log.info("==============================================");
        log.info("🚀 Spring Boot Maven Lifecycle Demo 시작");
        log.info("==============================================");

        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);

        log.info("==============================================");
        log.info("✅ 애플리케이션이 성공적으로 시작되었습니다");
        log.info("==============================================");
        log.info("API 문서: http://localhost:8080/api/v1/users");
        log.info("H2 콘솔: http://localhost:8080/h2-console");
        log.info("==============================================");
    }
}

