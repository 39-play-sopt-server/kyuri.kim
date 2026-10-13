package org.sopt.adapter.in.dto;

import org.sopt.application.port.in.UpdatePostCommand;

public record UpdatePostRequest (
    String title,
    String content
){
    public UpdatePostCommand toCommand() {
        return new UpdatePostCommand(this.title, this.content);
    }
}
