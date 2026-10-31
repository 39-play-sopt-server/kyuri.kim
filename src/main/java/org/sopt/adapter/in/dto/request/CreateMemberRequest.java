package org.sopt.adapter.in.dto.request;

import org.sopt.application.port.in.CreateMemberCommand;

public record CreateMemberRequest(String name) {

    public CreateMemberCommand toCommand() {
        return new CreateMemberCommand(name);
    }
}