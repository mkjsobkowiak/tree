package com.bit4mation.core.tree;

import java.util.Set;

public interface NodeMapper {

    NodeDTO toDTO(NodeEntity nodeEntity);

    Set<NodeDTO> toDTOs(Set<NodeEntity> nodeEntitySet);

    NodeEntity toEntity(NodeDTO nodeDTO);
}
