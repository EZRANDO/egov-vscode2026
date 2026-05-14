package com.example.controller;

import com.example.entity.User;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * UserController (REST API 계층)
 * 
 * @Slf4j: 로깅 객체 자동 생성
 * @RestController: REST API 컨트롤러 등록
 * @RequiredArgsConstructor: Lombok이 final 필드 기반 생성자 자동 생성
 * @RequestMapping: 기본 경로 설정
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 모든 사용자 조회
     * GET /api/v1/users
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("모든 사용자 조회 요청");
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * 특정 사용자 조회
     * GET /api/v1/users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        log.info("사용자 조회 요청: id={}", id);
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("사용자를 찾을 수 없음: id={}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    /**
     * 이메일로 사용자 조회
     * GET /api/v1/users/email/{email}
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        log.info("이메일로 사용자 조회: email={}", email);
        return userService.getUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * 새 사용자 생성
     * POST /api/v1/users
     */
    @PostMapping
    public ResponseEntity<?> createUser(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam(required = false) String department) {
        log.info("새 사용자 생성 요청: name={}, email={}", name, email);
        try {
            User createdUser = userService.createUser(name, email, department);
            log.info("사용자 생성 완료: id={}", createdUser.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (IllegalArgumentException e) {
            log.error("사용자 생성 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 사용자 이름 업데이트
     * PUT /api/v1/users/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @RequestParam String name) {
        log.info("사용자 정보 업데이트: id={}, name={}", id, name);
        try {
            return userService.updateUserName(id, name)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> {
                        log.warn("업데이트할 사용자를 찾을 수 없음: id={}", id);
                        return ResponseEntity.notFound().build();
                    });
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 사용자 삭제
     * DELETE /api/v1/users/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        log.info("사용자 삭제 요청: id={}", id);
        try {
            userService.deleteUser(id);
            log.info("사용자 삭제 완료: id={}", id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("사용자 삭제 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 부서별 사용자 조회
     * GET /api/v1/users/department/{department}
     */
    @GetMapping("/department/{department}")
    public ResponseEntity<List<User>> getUsersByDepartment(@PathVariable String department) {
        log.info("부서별 사용자 조회: department={}", department);
        List<User> users = userService.getUsersByDepartment(department);
        return ResponseEntity.ok(users);
    }

    /**
     * 부서별 사용자 수 조회
     * GET /api/v1/users/department/{department}/count
     */
    @GetMapping("/department/{department}/count")
    public ResponseEntity<Long> countByDepartment(@PathVariable String department) {
        log.info("부서별 사용자 수 조회: department={}", department);
        long count = userService.countByDepartment(department);
        return ResponseEntity.ok(count);
    }

    /**
     * 전체 사용자 수
     * GET /api/v1/users/count
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getUserCount() {
        log.info("전체 사용자 수 조회");
        long count = userService.getTotalUserCount();
        return ResponseEntity.ok(count);
    }

    /**
     * 헬스 체크
     * GET /api/v1/users/health
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        log.debug("헬스 체크");
        return ResponseEntity.ok("✅ UserController is healthy!");
    }

    /**
     * 예외 처리
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("비즈니스 로직 오류: {}", e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.error("서버 오류: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("서버 오류가 발생했습니다");
    }
}

