package com.agenticIde.distributed_lovable.workspace_service.dto.project;



import com.agenticIde.distributed_lovable.comman_lib.enums.ProjectRole;

import java.time.Instant;

public record ProjectSummaryResponse(
        Long id,
        String name,
        Instant createdAt,
        Instant updatedAt,
        ProjectRole role
) {
}
