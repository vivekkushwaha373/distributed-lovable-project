package com.agenticIde.distributed_lovable.comman_lib.dto;

public record FileNode(
        String path
) {
    @Override
    public String toString(){
        return path;
    }
}
