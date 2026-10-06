package org.sopt;

/*여기서는 게시판이 동작하는 데에 필요한 정보를 출력하는 역할을 해야 합니다.
게시판 데이터의 조회 뿐 아니라 각종 입력 메시지와 결과 출력도 포함되어야 하죠
*/

import javax.sound.sampled.Port;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PostView {
    private final Scanner scanner = new Scanner(System.in);

    public int showMenu() {
        System.out.println("\n=== 게시판 ===");
        System.out.println("1. 게시글 작성");
        System.out.println("2. 게시글 목록 조회");
        System.out.println("3. 게시글 단건 조회");
        System.out.println("4. 게시글 수정");
        System.out.println("5. 게시글 삭제");
        System.out.println("6. 종료");
        System.out.print("선택: ");

        return Integer.parseInt(scanner.nextLine());
    }

    public String inputTitle() {
        System.out.print("제목: ");
        return scanner.nextLine();
    }

    public String inputContent() {
        System.out.print("내용: ");
        return scanner.nextLine();
    }

    /*일단은 조회 (showMenu()와 비슷하게 작성하면 되겠죠?

    그 전에!!

    근데 '조회할 게시굴 번호', '수정할 게시글 번호', '삭제할 게시글 번호'를 다 따로따로 메서드를 만들기는 비효율적이잖아요?
    그래서 저는 actionMessage로 action을 묶어내봤어요
    그리고 인덱스니까 사용자가 입력한 게시글 번호에서 -1을 하는 것도 잊지 말기!
    */

    public int inputPostIndex(String actionMessage) {
        System.out.print(actionMessage + "을 행할 게시글 번호: ");
        return Integer.parseInt(scanner.nextLine()) -1;
    }

    /*이제 조회하는 걸 만들어봅시다(Main에 있었던 거 뽑아오기)*/
    /*1. 게시글 목록 조회*/
    public void showPostList(List<Post> posts) {
        System.out.println("\n=== 게시글 목록===");

        //main에 있던 '게시글이 없습니다'는 여기로 와야겠죠?
        if(posts.isEmpty()){
            System.out.println("게시글이 없습니다.");
            return;
        }

        //그리고 조회 기능
        for(int i = 0; i<posts.size(); i++){
            System.out.println((i + 1) + ". " + posts.get(i).getTitle());
        }
    }

    /*2. 게시글 단건 조회*/
    public void showPostDetail(Post post) {
        System.out.println("\n=== 게시글 ===");
        System.out.println("제목: " + post.getTitle());
        System.out.println("내용: " + post.getContent());
    }

    /*3. 게시글 상세 조회*/
    public void showMessage(String message) {
        System.out.println(message);
    }
}