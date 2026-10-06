package org.sopt;

/* 여기서는 게시판의 기능 흐름을 제어하는 역할을 합니다
사용자의 메뉴 선택에 맞는 기능을 제공하면서 model과 view에 결과를 반영하면 됩니다
*/

import java.util.ArrayList;
import java.util.List;

public class PostController {
    private final List<Post> posts = new ArrayList<>();
    private final PostView view = new PostView();

    public void run(){
        while (true) {
            int command = view.showMenu();

            switch (command) {
                case 1:
                    createPost();
                    break;
                case 2:
                    showPosts();
                    break;
                case 3:
                    showPostDetail();
                    break;
                case 4:
                    updatePost();
                    break;
                case 5:
                    deletePost();
                    break;
                case 6:
                    view.showMessage("프로그램을 종료합니다.");
                    return;
                default:
                    view.showMessage("잘못된 입력입니다.");
            }
        }
    }

    private void createPost(){
        String title = view.inputTitle();
        String content = view.inputContent();

        Post post = new Post(title, content);
        posts.add(post);

        view.showMessage("게시글이 작성되었습니다.");
    }

    private void showPosts(){
        view.showPostList(posts);
    }

    private void showPostDetail(){
        if (checkEmpty()) return;

        int index = view.inputPostIndex("조회");
        if (isInvalidIndex(index)) return;

        Post post = posts.get(index);
        view.showPostDetail(post);
    }

    private void updatePost(){
        if (checkEmpty()) return;

        int index = view.inputPostIndex("수정");
        if (isInvalidIndex(index)) return;

        String newTitle = view.inputTitle();
        String newContent = view.inputContent();

        Post post = posts.get(index);
        post.update(newTitle, newContent);

        view.showMessage("게시글이 수정되었습니다.");
    }

    private void deletePost() {
        if (checkEmpty())
        {
            view.showMessage("게시글이 없습니다.");
            return;
        }

        int index = view.inputPostIndex("삭제");
        if (isInvalidIndex(index)) return;

        posts.remove(index);
        view.showMessage("게시글이 삭제되었습니다.");
    }

    private boolean checkEmpty(){
        if (posts.isEmpty()) {
            view.showMessage("게시글이 없습니다.");
            return true;
        }
        return false;
    }

    private boolean isInvalidIndex(int index){
        if (index < 0 || index >= posts.size()) {
            view.showMessage("존재하지 않는 게시글입니다.");
            return true;
        }
        return false;
    }
}