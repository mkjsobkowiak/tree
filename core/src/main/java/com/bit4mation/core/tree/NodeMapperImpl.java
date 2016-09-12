package com.bit4mation.core.tree;

import org.hibernate.annotations.common.util.StringHelper;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

/**
 * Node mapper which map NodeDTO from/to NodeEntity class
 */
class NodeMapperImpl implements NodeMapper {

    /**
     * Mapper from nodeEntity to nodeDTO class
     *
     * @param nodeEntity
     * @return mapped NodeDTO class
     */
    @Override
    public NodeDTO toDTO(NodeEntity nodeEntity) {
        NodeDTO nodeDTO = NodeDTO.builder()
                .id(nodeEntity.getId())
                .parentId(nodeEntity.getParentId())
                .children(nodeEntity.isHasChildren())
                .build();
        if (nodeEntity.getValue() != null) {
            nodeDTO.setText(nodeEntity.getValue().toString());
        }
        return nodeDTO;
    }

    /**
     * Mapper from set of nodeEntities to set of nodeDTOs classes
     *
     * @param nodeEntitySet
     * @return mapped set of nodeDTO classes
     */
    @Override
    public Set<NodeDTO> toDTOs(Set<NodeEntity> nodeEntitySet) {
        Set<NodeDTO> nodeDTOs = new HashSet<>(nodeEntitySet.size());
        if (isNotEmpty(nodeEntitySet)) {
            nodeDTOs.addAll(nodeEntitySet.stream()
                            .map(this::toDTO)
                            .collect(Collectors.toSet())
            );
        }
        return nodeDTOs;
    }

    /**
     * Mapper from nodeDTO to nodeEntity class
     *
     * @param nodeDTO
     * @return mapped NodeDTO class
     */
    @Override
    public NodeEntity toEntity(NodeDTO nodeDTO) {
        NodeEntity nodeEntity = NodeEntity.builder()
                .id(nodeDTO.getId())
                .parentId(nodeDTO.getParentId())
                .build();
        if (StringHelper.isNotEmpty(nodeDTO.getText())) {
            nodeEntity.setValue(Double.parseDouble(nodeDTO.getText()));
        }
        return nodeEntity;
    }
}
