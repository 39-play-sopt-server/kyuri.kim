package org.sopt.adapter.out.persistence;

import jakarta.persistence.criteria.Predicate;
import org.sopt.domain.Category;
import org.sopt.domain.Post;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class PostSpecifications {

    private PostSpecifications() {
    }

    public static Specification<Post> search(Long memberId, Category category, String title) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (memberId != null) {
                predicates.add(cb.equal(root.get("member").get("id"), memberId));
            }
            if (category != null) {
                predicates.add(cb.equal(root.get("category"), category));
            }
            if (title != null && !title.isBlank()) {
                String pattern = escapeLike(title.trim()) + "%";
                predicates.add(cb.like(root.get("title"), pattern, '!'));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    // 사용자가 입력한 %, _ 가 와일드카드로 해석하도록 허용x -> escape!
    private static String escapeLike(String value) {
        return value.replace("!", "!!")
                .replace("%", "!%")
                .replace("_", "!_");
    }
}