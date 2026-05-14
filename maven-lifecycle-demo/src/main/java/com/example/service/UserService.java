package com.example.service;

import com.example.entity.User;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * UserService (비즈니스 로직 계층)
 * 
 * @Service: Spring 서비스 계층 컴포넌트 등록
 * @Transactional: 메서드 레벨 트랜잭션 관리
 * @RequiredArgsConstructor: Lombok이 final 필드를 인수로 받는 생성자 자동 생성
 * @Slf4j: Lombok이 로깅 객체 자동 생성 (logger 필드 제공)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 모든 사용자 조회
     * 
     * @return 모든 사용자 리스트
     */
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        log.info("모든 사용자 조회 요청");
        List<User> users = userRepository.findAll();
        log.debug("조회된 사용자 수: {}", users.size());
        return users;
    }

    /**
     * 특정 사용자 조회
     * 
     * @param id 사용자 ID
     * @return Optional<User>
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) {
        log.info("사용자 조회: ID={}", id);
        return userRepository.findById(id)
                .map(user -> {
                    log.debug("사용자 찾음: {}", user.getName());
                    return user;
                });
    }

    /**
     * 이메일로 사용자 조회
     * 
     * @param email 이메일
     * @return Optional<User>
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        log.info("이메일로 사용자 조회: email={}", email);
        return userRepository.findByEmail(email);
    }

    /**
     * 새 사용자 생성
     * 
     * @param name 사용자 이름
     * @param email 이메일
     * @param department 부서
     * @return 생성된 사용자
     */
    @Transactional
    public User createUser(String name, String email, String department) {
        log.info("새 사용자 생성: name={}, email={}, department={}", name, email, department);

        // 비즈니스 로직: 검증
        if (name == null || name.trim().isEmpty()) {
            log.error("검증 실패: 사용자 이름 필수");
            throw new IllegalArgumentException("사용자 이름은 필수입니다");
        }
        if (email == null || email.trim().isEmpty()) {
            log.error("검증 실패: 이메일 필수");
            throw new IllegalArgumentException("이메일은 필수입니다");
        }

        // 중복 확인
        if (userRepository.findByEmail(email).isPresent()) {
            log.warn("검증 실패: 이미 존재하는 이메일 - {}", email);
            throw new IllegalArgumentException("이미 존재하는 이메일입니다");
        }

        User user = User.builder()
                .name(name)
                .email(email)
                .department(department != null ? department : "미배정")
                .build();

        User savedUser = userRepository.save(user);
        log.info("사용자 생성 완료: ID={}, name={}", savedUser.getId(), savedUser.getName());
        return savedUser;
    }

    /**
     * 부서별 사용자 조회
     * 
     * @param department 부서명
     * @return 부서별 사용자 리스트
     */
    @Transactional(readOnly = true)
    public List<User> getUsersByDepartment(String department) {
        log.info("부서별 사용자 조회: department={}", department);
        List<User> users = userRepository.findByDepartment(department);
        log.debug("{} 부서 사용자 수: {}", department, users.size());
        return users;
    }

    /**
     * 사용자 정보 업데이트
     * 
     * @param id 사용자 ID
     * @param name 새 이름
     * @return 업데이트된 사용자
     */
    @Transactional
    public Optional<User> updateUserName(Long id, String name) {
        log.info("사용자 이름 변경: ID={}, newName={}", id, name);

        return userRepository.findById(id)
                .map(user -> {
                    user.setName(name);
                    User updated = userRepository.save(user);
                    log.info("사용자 이름 변경 완료: ID={}", id);
                    return updated;
                })
                .or(() -> {
                    log.warn("사용자를 찾을 수 없음: ID={}", id);
                    return Optional.empty();
                });
    }

    /**
     * 사용자 삭제
     * 
     * @param id 사용자 ID
     */
    @Transactional
    public void deleteUser(Long id) {
        log.info("사용자 삭제: ID={}", id);
        if (!userRepository.existsById(id)) {
            log.warn("삭제할 사용자를 찾을 수 없음: ID={}", id);
            throw new IllegalArgumentException("존재하지 않는 사용자입니다");
        }
        userRepository.deleteById(id);
        log.info("사용자 삭제 완료: ID={}", id);
    }

    /**
     * 전체 사용자 수 조회
     * 
     * @return 사용자 총 수
     */
    @Transactional(readOnly = true)
    public long getTotalUserCount() {
        log.debug("전체 사용자 수 조회");
        return userRepository.count();
    }

    /**
     * 부서별 사용자 수 조회
     * 
     * @param department 부서명
     * @return 해당 부서의 사용자 수
     */
    @Transactional(readOnly = true)
    public long countByDepartment(String department) {
        log.debug("부서별 사용자 수 조회: department={}", department);
        return userRepository.countByDepartment(department);
    }
}
