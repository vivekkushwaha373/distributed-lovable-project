package com.agenticIde.distributed_lovable.workspace_service.entity;


import com.agenticIde.distributed_lovable.comman_lib.enums.ProjectRole;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="project_members")
public class ProjectMember {
    // this is a composite Id
    @EmbeddedId
    ProjectMemberId id;

    @ManyToOne
    @MapsId("projectId")
    Project project;


    @Enumerated(EnumType.STRING)
    ProjectRole projectRole;

    Instant invitedAt;
    Instant acceptedAt;
}
