package org.sopt.adapter.in.controller;

import org.sopt.adapter.in.dto.response.MemberResponse;
import org.sopt.application.port.out.MemberRepository;
import org.sopt.common.response.ApiResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
public class AdminMemberController {
    private final MemberRepository members;
    public AdminMemberController(MemberRepository members) { this.members=members; }
    @GetMapping
    public ApiResponse<org.springframework.data.domain.Page<MemberResponse>> members(@RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="20") int size) {
        if(page<0 || size<1 || size>100) throw new IllegalArgumentException("invalid page request");
        return ApiResponse.success(members.findAll(PageRequest.of(page,size)).map(MemberResponse::from));
    }
}
