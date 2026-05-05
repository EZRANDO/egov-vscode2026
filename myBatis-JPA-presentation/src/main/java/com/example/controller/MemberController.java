package com.example.controller;

import com.example.dto.MemberStatsDto;
import com.example.domain.Member;
import com.example.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 회원 목록 조회 api
    @GetMapping("/members")
    public ResponseEntity<List<Member>> getAllMembers() {
        List<Member> members = memberService.getAllMembers();
        return ResponseEntity.ok(members);
    }

    // 회원 이름 변경 api
    @PatchMapping("/members/{memberId}/name")
    public ResponseEntity<MemberStatsDto> updateMemberName(
            @PathVariable Long memberId,
            @RequestParam String newName,
            @RequestParam(required = false) Boolean isDeleted,
            @RequestParam(required = false) String fromDate
    ) {
        MemberStatsDto stats = memberService.updateMemberAndGetStats(memberId, newName, isDeleted, fromDate);
        return ResponseEntity.ok(stats);
    }

    // 회원 탈퇴 api
    @DeleteMapping("}/members/{memberId")
    public ResponseEntity<Void> softDeleteMember(@PathVariable Long memberId) {
        memberService.softDeleteMember(memberId);
        return ResponseEntity.noContent().build();
    }
}
