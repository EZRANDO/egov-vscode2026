package com.example.repository;

import com.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * UserRepository (Spring Data JPA)
 * 
 * JpaRepository를 상속받아 기본 CRUD 기능을 자동으로 제공합니다.
 * - save(), findById(), findAll(), delete() 등 자동 제공
 * - 커스텀 쿼리는 @Query를 사용하여 정의
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 이메일로 사용자 조회
     */
    Optional<User> findByEmail(String email);

    /**
     * 이름으로 사용자 조회
     */
    Optional<User> findByName(String name);

    /**
     * 부서별 사용자 조회
     */
    List<User> findByDepartment(String department);

    /**
     * 커스텀 쿼리: 부서별 사용자 수
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.department = :department")
    long countByDepartment(@Param("department") String department);

    /**
     * 커스텀 쿼리: 특정 이메일 도메인의 사용자 조회
     */
    @Query("SELECT u FROM User u WHERE u.email LIKE :emailPattern")
    List<User> findByEmailPattern(@Param("emailPattern") String emailPattern);

    /**
     * 커스텀 쿼리: 최근 생성된 사용자 조회
     */
    @Query(value = "SELECT * FROM users ORDER BY created_at DESC LIMIT :limit", nativeQuery = true)
    List<User> findRecentUsers(@Param("limit") int limit);
}

