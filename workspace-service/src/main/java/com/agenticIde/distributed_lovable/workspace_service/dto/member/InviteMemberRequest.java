package com.agenticIde.distributed_lovable.workspace_service.dto.member;

//import com.personal.project.agenticIde.enums.ProjectRole;
import com.agenticIde.distributed_lovable.comman_lib.enums.ProjectRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InviteMemberRequest(
        @Email
        @NotBlank
        String username,
        @NotNull
        ProjectRole role
) {
}
