package com.agenticIde.distributed_lovable.workspace_service.controller;


import com.agenticIde.distributed_lovable.comman_lib.dto.FileTreeDto;
import com.agenticIde.distributed_lovable.workspace_service.dto.project.FileContentResponse;
import com.agenticIde.distributed_lovable.workspace_service.service.ProjectFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projects/{projectId}/files")
@RequiredArgsConstructor
public class FileController {
    private final ProjectFileService fileService;

    @GetMapping
    public ResponseEntity<FileTreeDto> getFileTree(@PathVariable Long projectId){
        Long userId=1L;
        return ResponseEntity.ok(fileService.getFileTree(projectId));
    }

    @GetMapping("/content")
    public ResponseEntity<String> getFile(
            @PathVariable Long projectId,
            @RequestParam String path
    ){

        return ResponseEntity.ok(fileService.getFileContent(projectId,path));
    }

}
