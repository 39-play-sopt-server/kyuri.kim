package org.sopt.application.port.in;

import org.sopt.domain.Post;

import java.util.List;

public record PostPage (
    List<Post> posts,
    int page,
    int size,
    long totalElements,
    int totalPages
){}
