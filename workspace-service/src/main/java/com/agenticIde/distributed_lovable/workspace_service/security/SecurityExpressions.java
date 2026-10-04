package com.agenticIde.distributed_lovable.workspace_service.security;

import com.agenticIde.distributed_lovable.comman_lib.enums.ProjectPermission;
import com.agenticIde.distributed_lovable.comman_lib.security.AuthUtil;

import com.agenticIde.distributed_lovable.workspace_service.repository.ProjectMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("security")
@RequiredArgsConstructor
public class SecurityExpressions {

    private final ProjectMemberRepository projectMemberRepository;
    private final AuthUtil authUtil;

    public boolean hasPermissions(Long projectId,ProjectPermission projectPermission){
        Long userId = authUtil.getCurrentUserId();
        return projectMemberRepository.findRoleByProjectIdAndUserId(projectId,userId)
                .map(role-> role.getPermissions().contains(projectPermission)).orElse(false);
    }

    public boolean canViewProject(Long projectId){
      return hasPermissions(projectId,ProjectPermission.VIEW);
    }

    public boolean canEditProject(Long projectId){
      return hasPermissions(projectId,ProjectPermission.EDIT);
    }

    public boolean canDeleteProject(Long projectId){
        return hasPermissions(projectId, ProjectPermission.DELETE);
    }

    public boolean canViewMembers(Long projectId){
        return hasPermissions(projectId,ProjectPermission.VIEW_MEMBERS);
    }

    public boolean canManageMembers(Long projectId){
        return hasPermissions(projectId,ProjectPermission.MANAGE_MEMBERS);
    }

}
