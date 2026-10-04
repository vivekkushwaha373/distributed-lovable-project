package com.agenticIde.distributed_lovable.workspace_service.dto.member;

//import com.personal.project.agenticIde.enums.ProjectRole;

import com.agenticIde.distributed_lovable.comman_lib.enums.ProjectRole;

import java.time.Instant;

public record MemberResponse(
        Long userId,
        String username,
        String name,
        String avatarUrl,
        ProjectRole projectRole,
        Instant invitedAt
) {
}
