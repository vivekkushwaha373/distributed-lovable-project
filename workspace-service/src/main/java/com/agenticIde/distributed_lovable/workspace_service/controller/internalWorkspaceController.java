package com.agenticIde.distributed_lovable.workspace_service.controller;


import com.agenticIde.distributed_lovable.comman_lib.dto.FileTreeDto;
import com.agenticIde.distributed_lovable.comman_lib.enums.ProjectPermission;
import com.agenticIde.distributed_lovable.workspace_service.service.ProjectFileService;
import com.agenticIde.distributed_lovable.workspace_service.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/internal/v1/")
public class internalWorkspaceController {

    private final ProjectService projectService;
    private final ProjectFileService projectFileService;

    @GetMapping("/projects/{projectId}/files/tree")
    public FileTreeDto getFileTree(@PathVariable Long projectId){
        return projectFileService.getFileTree(projectId);
    }


    @GetMapping("/projects/{projectId}/files/content")
    public String getFileContent(@PathVariable Long projectId, @RequestParam String path){
        return projectFileService.getFileContent(projectId,path);
    }

    @GetMapping("/projects/{projectId}/permissions/check")
    public boolean checkProjectPermission(
            @PathVariable Long projectId,
            @RequestParam ProjectPermission permission) {
        return projectService.hasPermission(projectId, permission);
    }

}
