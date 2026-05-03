package com.example.repository;

import com.example.dto.MemberStatsDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper {
    // 3-3. MyBatis는 Entity 대신 조회 목적에 맞춘 DTO로 결과를 매핑해 JPA 도메인과 결합도를 낮춘다. 
    MemberStatsDto getComplexMemberStats(
            @Param("memberId") Long memberId,
            @Param("isDeleted") Boolean isDeleted,
            @Param("fromDate") String fromDate
    );
}
