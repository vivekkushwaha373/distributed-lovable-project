package com.agenticIde.distributed_lovable.intelligence_service.security;

import com.agenticIde.distributed_lovable.comman_lib.enums.ProjectPermission;
import com.agenticIde.distributed_lovable.comman_lib.security.AuthUtil;

import com.agenticIde.distributed_lovable.intelligence_service.client.WorkspaceClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.stereotype.Component;

@Component("security")
@RequiredArgsConstructor
@Slf4j
public class SecurityExpressions {


    private final AuthUtil authUtil;
    private final WorkspaceClient workspaceClient;

    private boolean hasPermissions(Long projectId,ProjectPermission projectPermission){
        try {
            return workspaceClient.checkPermission(projectId, projectPermission);
        } catch (FeignException.Unauthorized e) {
            log.warn("Token expired or invalid during permission check for project: {}", projectId);
            throw new CredentialsExpiredException("JWT token is expired or invalid");
        } catch (FeignException e) {
            log.error("Workspace-service failed during permission check: {}", e.getMessage());
            return false;
        }

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
