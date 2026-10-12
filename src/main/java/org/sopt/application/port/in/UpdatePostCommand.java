package org.sopt.application.port.in;

public record UpdatePostCommand (
        String title,
        String content
){
}
