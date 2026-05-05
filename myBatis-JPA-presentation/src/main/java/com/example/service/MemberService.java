package com.example.service;

import com.example.domain.Member;
import com.example.dto.MemberStatsDto;
import com.example.repository.MemberMapper;
import com.example.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final EntityManager entityManager;

    // 회원 목록 조회
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    // 3-1. Spring Boot는 JPA가 있으면 JpaTransactionManager를 기본 트랜잭션 매니저로 자동 구성, MyBatis도 이 트랜잭션에 함께 참여
    @Transactional
    public MemberStatsDto updateMemberAndGetStats(Long memberId, String newName, Boolean isDeleted, String fromDate) {
        // 간단한 쿼리 : JPA
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        member.changeName(newName); // 변경감지 -> 쓰기 지연 저장소에 update 쿼리 저장
        entityManager.flush(); // MyBatis 쿼리 사용 전 반드시 flush하기 (쓰기 지연 저장소 -> DB로 update 쿼리 전달)

        return memberMapper.getComplexMemberStats(memberId, isDeleted, fromDate); // 복잡한 쿼리는 MyBatis 사용
    }

    @Transactional
    public void softDeleteMember(Long memberId) {
        int updatedCount = memberRepository.softDeleteById(memberId, LocalDateTime.now());
        if (updatedCount == 0) {
            throw new IllegalArgumentException("사용자 없음");
        }
    }
}
