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
 * UserService 통합 테스트 (성공 케이스)
 * 
 * @SpringBootTest: 전체 Application Context 로드
 * @Transactional: 각 테스트 후 롤백
 * @DisplayName: 테스트 이름 한글화
 */
@Slf4j
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
@DisplayName("UserService 통합 테스트")
class CalculatorTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        log.info("[TEST SETUP] 테스트 환경 초기화");
        userRepository.deleteAll();
        
        // 테스트 데이터 생성
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

    @Test
    @DisplayName("모든 사용자 조회 성공")
    void testGetAllUsers() {
        log.info("[TEST] 모든 사용자 조회");
        List<User> users = userService.getAllUsers();
        
        assertNotNull(users);
        assertEquals(3, users.size());
        log.info("[PASSED] ✅ 모든 사용자 조회 성공!");
    }

    @Test
    @DisplayName("ID로 사용자 조회 성공")
    void testGetUserById() {
        log.info("[TEST] ID로 사용자 조회");
        User user = userRepository.findAll().get(0);
        Optional<User> result = userService.getUserById(user.getId());
        
        assertTrue(result.isPresent());
        assertEquals("김철수", result.get().getName());
        log.info("[PASSED] ✅ ID로 사용자 조회 성공!");
    }

    @Test
    @DisplayName("이메일로 사용자 조회 성공")
    void testGetUserByEmail() {
        log.info("[TEST] 이메일로 사용자 조회");
        Optional<User> result = userService.getUserByEmail("kim@example.com");
        
        assertTrue(result.isPresent());
        assertEquals("김철수", result.get().getName());
        log.info("[PASSED] ✅ 이메일로 사용자 조회 성공!");
    }

    @Test
    @DisplayName("새 사용자 생성 성공")
    void testCreateUser() {
        log.info("[TEST] 새 사용자 생성");
        User newUser = userService.createUser("홍길동", "hong@example.com", "마케팅팀");
        
        assertNotNull(newUser.getId());
        assertEquals("홍길동", newUser.getName());
        assertEquals("hong@example.com", newUser.getEmail());
        assertEquals("마케팅팀", newUser.getDepartment());
        log.info("[PASSED] ✅ 새 사용자 생성 성공!");
    }

    @Test
    @DisplayName("부서별 사용자 조회 성공")
    void testGetUsersByDepartment() {
        log.info("[TEST] 부서별 사용자 조회");
        List<User> devTeamUsers = userService.getUsersByDepartment("개발팀");
        
        assertFalse(devTeamUsers.isEmpty());
        assertTrue(devTeamUsers.stream()
                .allMatch(u -> "개발팀".equals(u.getDepartment())));
        log.info("[PASSED] ✅ 부서별 사용자 조회 성공!");
    }

    @Test
    @DisplayName("사용자 이름 업데이트 성공")
    void testUpdateUserName() {
        log.info("[TEST] 사용자 이름 업데이트");
        User user = userRepository.findAll().get(0);
        Optional<User> updated = userService.updateUserName(user.getId(), "김철수_수정됨");
        
        assertTrue(updated.isPresent());
        assertEquals("김철수_수정됨", updated.get().getName());
        log.info("[PASSED] ✅ 사용자 이름 업데이트 성공!");
    }

    @Test
    @DisplayName("사용자 삭제 성공")
    void testDeleteUser() {
        log.info("[TEST] 사용자 삭제");
        User user = userRepository.findAll().get(0);
        Long userId = user.getId();
        
        userService.deleteUser(userId);
        
        Optional<User> deleted = userService.getUserById(userId);
        assertFalse(deleted.isPresent());
        log.info("[PASSED] ✅ 사용자 삭제 성공!");
    }

    @Test
    @DisplayName("전체 사용자 수 조회 성공")
    void testGetTotalUserCount() {
        log.info("[TEST] 전체 사용자 수 조회");
        long count = userService.getTotalUserCount();
        
        assertEquals(3, count);
        log.info("[PASSED] ✅ 전체 사용자 수 조회 성공!");
    }

    @Test
    @DisplayName("부서별 사용자 수 조회 성공")
    void testCountByDepartment() {
        log.info("[TEST] 부서별 사용자 수 조회");
        long count = userService.countByDepartment("개발팀");
        
        assertEquals(1, count);
        log.info("[PASSED] ✅ 부서별 사용자 수 조회 성공!");
    }

    @Test
    @DisplayName("존재하지 않는 사용자 조회 성공 (Empty 반환)")
    void testGetNonExistentUser() {
        log.info("[TEST] 존재하지 않는 사용자 조회");
        Optional<User> result = userService.getUserById(999L);
        
        assertFalse(result.isPresent());
        log.info("[PASSED] ✅ 존재하지 않는 사용자 조회 성공!");
    }

    @Test
    @DisplayName("중복 이메일로 사용자 생성 실패")
    void testCreateUserWithDuplicateEmail() {
        log.info("[TEST] 중복 이메일로 사용자 생성");
        
        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser("다른사람", "kim@example.com", "팀");
        });
        
        log.info("[PASSED] ✅ 중복 이메일 검증 성공!");
    }

    @Test
    @DisplayName("비어있는 이름으로 사용자 생성 실패")
    void testCreateUserWithoutName() {
        log.info("[TEST] 비어있는 이름으로 사용자 생성");
        
        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser("", "test@example.com", "팀");
        });
        
        log.info("[PASSED] ✅ 이름 필수 검증 성공!");
    }

    @Test
    @DisplayName("JPA Cascading 테스트")
    void testJpaCascading() {
        log.info("[TEST] JPA Cascading 테스트");
        
        // 사용자 생성
        User user = userService.createUser("테스트", "cascade@example.com", "팀");
        long userId = user.getId();
        
        // 생성된 사용자 조회
        Optional<User> retrieved = userService.getUserById(userId);
        assertTrue(retrieved.isPresent());
        
        // 사용자 삭제
        userService.deleteUser(userId);
        
        // 삭제 확인
        assertFalse(userService.getUserById(userId).isPresent());
        log.info("[PASSED] ✅ JPA Cascading 테스트 성공!");
    }
}


