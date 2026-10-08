package org.sopt.client.view;

//출력 담당
public class OutputView {
    public void printMessage(String message) {
        System.out.println("[안내] " + message);
    }

    public void printError(String errorMessage) {
        System.out.println("[에러] " + errorMessage);
    }
}
