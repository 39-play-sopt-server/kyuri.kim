package org.sopt.client.view;

//입력만 담당 & 숫자 파싱 검증 - 메인.java에서 가져오기

import org.sopt.domain.Category;
import java.util.Scanner;

public class InputView {
    private final Scanner scanner = new Scanner(System.in);

    public int printMenuAndGetInput() {
        System.out.println("\n--- 게시판 프로그램 ---");
        System.out.println("1. 게시글 작성");
        System.out.println("2. 게시글 목록 조회");
        System.out.println("3. 게시글 상세 조회");
        System.out.println("4. 게시글 수정");
        System.out.println("5. 게시글 삭제");
        System.out.println("0. 종료");
        System.out.print("선택: ");

        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public String inputString(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.nextLine();
    }

    public Long inputLong(String prompt) {
        System.out.print(prompt + ": ");
        try {
            return Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("올바른 숫자를 입력해주세요.");
            return null;
        }
    }

    public Category inputCategory() {
        System.out.print("카테고리 선택 (FREE, INFO, CAREER): ");
        try {
            return Category.fromString(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("유효하지 않은 카테고리입니다. 기본값 FREE로 설정됩니다.");
            return Category.FREE;
        }
    }
}