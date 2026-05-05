package com.example.repository;

import com.example.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

// crud 전용 Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // 3-2. 벌크 수정 쿼리는 엔티티 리스너를 거치지 않으므로 updatedDate를 직접 갱신하고 실행 후 컨텍스트를 비운다.
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Member m set m.isDeleted = true, m.updatedDate = :updatedDate where m.id = :memberId")
    int softDeleteById(@Param("memberId") Long memberId, @Param("updatedDate") LocalDateTime updatedDate);
}
