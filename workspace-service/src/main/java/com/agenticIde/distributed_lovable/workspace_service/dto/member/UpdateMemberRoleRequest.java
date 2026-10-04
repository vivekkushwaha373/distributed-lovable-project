package com.agenticIde.distributed_lovable.workspace_service.dto.member;

import com.agenticIde.distributed_lovable.comman_lib.enums.ProjectRole;
import jakarta.validation.constraints.NotNull;

public record UpdateMemberRoleRequest(
        @NotNull
        ProjectRole role
) {
}
