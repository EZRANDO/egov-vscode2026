package com.example.entity;

import lombok.*;

import javax.persistence.*;

/**
 * User Entity (JPA 엔티티)
 * 
 * Lombok을 사용하여 보일러플레이트 코드 제거
 * - @Data: getter, setter, equals, hashCode, toString 자동 생성
 * - @NoArgsConstructor: 기본 생성자 자동 생성
 * - @AllArgsConstructor: 모든 필드를 인수로 받는 생성자 자동 생성
 * - @Builder: Builder 패턴 자동 생성
 */
@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_email", columnList = "email", unique = true),
    @Index(name = "idx_department", columnList = "department")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 50)
    private String department;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Long createdAt;

    @Column(name = "updated_at")
    private Long updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = System.currentTimeMillis();
        updatedAt = System.currentTimeMillis();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = System.currentTimeMillis();
    }
}

