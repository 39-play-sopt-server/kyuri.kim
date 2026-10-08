package org.sopt.client.view;

//입력만 담당 & 숫자 파싱 검증 - 메인.java에서 가져오기

import org.sopt.domain.Category;
import java.util.Scanner;

public class InputView {
    private final Scanner scanner = new Scanner(System.in);

//메뉴 프린트 하는 거 outputview로 이동시킴

    //얘는 읽기만 하기
    public int inputInt() {
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
            return null; // 에러 메시지 출력 책임을 없애고 null 반환하는 걸로
        }
    }
}