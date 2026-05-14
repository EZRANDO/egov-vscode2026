package com.example;

import com.example.entity.User;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UserService 실패 테스트 (의도적으로 실패)
 * 
 * 이 테스트는 의도적으로 실패하도록 작성되었습니다.
 * 이를 통해 Maven Lifecycle에서 test 단계 실패 시
 * package 단계가 실행되지 않는 것을 보여줍니다.
 * 
 * 사용 방법:
 * mvn test -Pfailure-demo
 */
@Slf4j
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
@DisplayName("UserService 실패 테스트")
class CalculatorFailingTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        log.info("[TEST SETUP] 실패 데모용 테스트 데이터 초기화");
        userRepository.deleteAll();

        userRepository.save(User.builder()
                .name("김철수")
                .email("kim@example.com")
                .department("개발팀")
                .build());
        userRepository.save(User.builder()
                .name("이영희")
                .email("lee@example.com")
                .department("기획팀")
                .build());
        userRepository.save(User.builder()
                .name("박민수")
                .email("park@example.com")
                .department("디자인팀")
                .build());
    }

    /**
     * 실패 테스트 1: 사용자 수 검증 실패
     * 기대값: 5명 | 실제값: 3명 → FAILURE!
     */
    @Test
    @DisplayName("사용자 수 검증 실패")
    void testUserCountFails() {
        log.info("[TEST] 실패 테스트 1: 사용자 수 검증");
        long count = userService.getTotalUserCount();
        
        log.info("[TEST] 실제 사용자 수: {}, 기대값: 5", count);
        assertEquals(5, count, "사용자가 5명이어야 함 (틀렸습니다!)");
        
        log.info("[FAILED] 이 메시지는 출력되지 않습니다!");
    }

    /**
     * 실패 테스트 2: 사용자 이름 검증 실패
     * 기대값: "이영희" | 실제값: "김철수" → FAILURE!
     */
    @Test
    @DisplayName("사용자 이름 검증 실패")
    void testGetUserByIdFails() {
        log.info("[TEST] 실패 테스트 2: 사용자 이름 검증");
        User firstUser = userRepository.findAll().get(0);
        Optional<User> user = userService.getUserById(firstUser.getId());
        
        assertTrue(user.isPresent(), "사용자가 존재해야 함");
        log.info("[TEST] 실제 사용자 이름: {}, 기대값: 이영희", user.get().getName());
        assertEquals("이영희", user.get().getName(), "사용자 이름이 '이영희'여야 함 (틀렸습니다!)");
        
        log.info("[FAILED] 이 메시지는 출력되지 않습니다!");
    }

    /**
     * 실패 테스트 3: 부서 검증 실패
     * 기대값: "개발팀에 5명" | 실제값: "개발팀에 1명" → FAILURE!
     */
    @Test
    @DisplayName("부서별 사용자 수 검증 실패")
    void testGetUsersByDepartmentFails() {
        log.info("[TEST] 실패 테스트 3: 부서별 사용자 수 검증");
        List<User> devTeamUsers = userService.getUsersByDepartment("개발팀");
        
        log.info("[TEST] 실제 개발팀 사용자 수: {}, 기대값: 5", devTeamUsers.size());
        assertEquals(5, devTeamUsers.size(), "개발팀에 5명이 있어야 함 (틀렸습니다!)");
        
        log.info("[FAILED] 이 메시지는 출력되지 않습니다!");
    }

    /**
     * 실패 테스트 4: 사용자 이메일 검증 실패
     * 기대값: "kim@wrong.com" | 실제값: "kim@example.com" → FAILURE!
     */
    @Test
    @DisplayName("사용자 이메일 검증 실패")
    void testUserEmailFails() {
        log.info("[TEST] 실패 테스트 4: 사용자 이메일 검증");
        Optional<User> user = userService.getUserByEmail("kim@example.com");
        
        assertTrue(user.isPresent(), "사용자가 존재해야 함");
        log.info("[TEST] 실제 이메일: {}, 기대값: kim@wrong.com", user.get().getEmail());
        assertEquals("kim@wrong.com", user.get().getEmail(), "이메일이 'kim@wrong.com'이어야 함 (틀렸습니다!)");
        
        log.info("[FAILED] 이 메시지는 출력되지 않습니다!");
    }

    /**
     * 실패 테스트 5: 사용자 생성 후 ID 검증 실패
     * 기대값: ID가 100 이상 | 실제값: ID는 4 → FAILURE!
     */
    @Test
    @DisplayName("생성된 사용자 ID 검증 실패")
    void testCreateUserIdFails() {
        log.info("[TEST] 실패 테스트 5: 생성된 사용자 ID 검증");
        User newUser = userService.createUser("테스트", "test@example.com", "팀");
        
        log.info("[TEST] 실제 ID: {}, 기대값: >= 100", newUser.getId());
        assertTrue(newUser.getId() >= 100, "사용자 ID가 100 이상이어야 함 (틀렸습니다!)");
        
        log.info("[FAILED] 이 메시지는 출력되지 않습니다!");
    }
}
