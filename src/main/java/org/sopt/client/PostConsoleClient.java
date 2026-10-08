package org.sopt.client;

//메뉴 루프, 입력 -> 서버 호출 -> 응답 출력하는 파일 (ApiResponse 보고..)

import org.sopt.adapter.in.controller.PostController;
import org.sopt.adapter.in.dto.CreatePostRequest;
import org.sopt.adapter.in.dto.PostResponse;
import org.sopt.client.view.InputView;
import org.sopt.client.view.OutputView;
import org.sopt.common.response.ApiResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostConsoleClient {
    private final PostController postController;
    private final InputView inputView = new InputView();
    private final OutputView outputView = new OutputView();

    public PostConsoleClient(PostController postController) {
        this.postController = postController;
    }

    public void run() {
        while (true) {
            //출력에서 입력하는 순서로 분리하기
            outputView.printMenu();
            int choice = inputView.inputInt();

            if (choice == 0) {
                outputView.printMessage("프로그램을 종료합니다.");
                break;
            }

            try {
                switch (choice) {
                    case 1 -> createPost();
                    case 2 -> getAllPosts();
                    case 3 -> getPostById();
                    case 4 -> updatePost();
                    case 5 -> deletePost();
                    default -> outputView.printError("잘못된 메뉴 선택입니다.");
                }
            } catch (Exception e) {
                outputView.printError("요청 처리 중 오류가 발생했습니다: " + e.getMessage());
            }
        }
    }

    private void createPost() {
        String title = inputView.inputString("제목");
        String content = inputView.inputString("내용");
        String writer = inputView.inputString("작성자");

        // Category 객체 대신 순수 String으로 카테고리 입력받기로 변경 (도메인 의존성 제거해야 하니까)
        String category = inputView.inputString("카테고리 (FREE, INFO, CAREER)");

        CreatePostRequest request = new CreatePostRequest(title, content, writer, category);
        ApiResponse<PostResponse> response = postController.createPost(request);

        if (response.isSuccess()) {
            outputView.printMessage("게시글이 성공적으로 등록되었습니다. (ID: " + response.getData().id() + ")");
        } else {
            outputView.printError(response.getMessage());
        }
    }

    private void getAllPosts() {
        ApiResponse<List<PostResponse>> response = postController.getAllPosts();
        if (response.isSuccess()) {
            // 목록 출력 output으로 옮기는 걸로 리팩토링함
            outputView.printPosts(response.getData());
        } else {
            outputView.printError(response.getMessage());
        }
    }

    private void getPostById() {
        Long id = inputView.inputLong("조회할 게시글 ID");
        if (id == null) {
            outputView.printError("올바른 숫자를 입력해주세요.");
            return;
        }

        ApiResponse<PostResponse> response = postController.getPostById(id);
        if (response.isSuccess()) {
            outputView.printPostDetail(response.getData());
        } else {
            outputView.printError(response.getMessage());
        }
    }

    private void updatePost() {
        Long id = inputView.inputLong("수정할 게시글 ID");
        if (id == null) {
            outputView.printError("올바른 숫자를 입력해주세요.");
            return;
        }

        String title = inputView.inputString("새 제목");
        String content = inputView.inputString("새 내용");

        CreatePostRequest request = new CreatePostRequest(title, content, null, null);
        ApiResponse<Void> response = postController.updatePost(id, request);

        if (response.isSuccess()) {
            outputView.printMessage("게시글이 성공적으로 수정되었습니다.");
        } else {
            outputView.printError("[" + response.getCode() + "] " + response.getMessage());
        }
    }

    private void deletePost() {
        Long id = inputView.inputLong("삭제할 게시글 ID");
        if (id == null) {
            outputView.printError("올바른 숫자를 입력해주세요.");
            return;
        }

        ApiResponse<Void> response = postController.deletePost(id);
        if (response.isSuccess()) {
            outputView.printMessage("게시글이 삭제되었습니다.");
        } else {
            outputView.printError(response.getMessage());
        }
    }
}
