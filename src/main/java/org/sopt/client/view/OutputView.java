package org.sopt.client.view;

import org.sopt.adapter.in.dto.PostResponse;

import java.util.List;

//출력 담당
public class OutputView {

    // 1. 메뉴 출력
    public void printMenu() {
        System.out.println("\n--- 게시판 프로그램 ---");
        System.out.println("1. 게시글 작성");
        System.out.println("2. 게시글 목록 조회");
        System.out.println("3. 게시글 상세 조회");
        System.out.println("4. 게시글 수정");
        System.out.println("5. 게시글 삭제");
        System.out.print("선택: ");
    }

    // 2. 목록
    public void printPosts(List<PostResponse> posts) {
        if (posts == null || posts.isEmpty()) {
            System.out.println("[알림] 등록된 게시글이 없습니다.");
            return;
        }
        System.out.println("\n--- 게시글 목록 ---");
        for (PostResponse p : posts) {
            System.out.println("[" + p.id() + "] " + p.title() + " (작성자: " + p.writer() + ")");
        }
    }

    // 3. 상세
    public void printPostDetail(PostResponse p) {
        System.out.println("\n--- 게시글 상세 ---");
        System.out.println("제목: " + p.title());
        System.out.println("내용: " + p.content());
        System.out.println("작성자: " + p.writer());
        System.out.println("카테고리: " + p.category());
        System.out.println("작성일: " + p.createdAt());
    }

    public void printMessage(String message) {
        System.out.println("[알림] " + message);
    }

    public void printError(String errorMessage) {
        System.out.println("[에러] " + errorMessage);
    }
}