package com.agenticIde.distributed_lovable.workspace_service.mapper;


import com.agenticIde.distributed_lovable.comman_lib.dto.FileNode;
import com.agenticIde.distributed_lovable.workspace_service.entity.ProjectFile;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectFileMapper {

    List<FileNode> toListOfFileNode(List<ProjectFile> projectFileList) ;
}
