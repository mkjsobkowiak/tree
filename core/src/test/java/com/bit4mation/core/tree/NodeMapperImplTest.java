package com.bit4mation.core.tree;

import com.google.common.collect.Sets;
import org.junit.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class NodeMapperImplTest {

    private static final long NODE_ID = 32L;
    private static final long PARENT_ID = 111222L;
    private static final double VALUE = 312.0;
    private static final boolean HAS_CHILDREN = true;

    private NodeMapper nodeMapper = new NodeMapperImpl();

    @Test
    public void testToDTO() throws Exception {
        // when
        NodeDTO nodeDTO = nodeMapper.toDTO(getFullNodeEntity());

        // then
        assertNodeDTO(nodeDTO);
    }

    @Test
    public void testToDTOs() throws Exception {
        // when
        Set<NodeDTO> nodeDTOs = nodeMapper.toDTOs(Sets.newHashSet(getFullNodeEntity()));

        // then
        nodeDTOs.forEach(this::assertNodeDTO);
    }

    @Test
    public void testToEntity() throws Exception {
        // when
        NodeEntity nodeEntity = nodeMapper.toEntity(getFullNodeDTO());

        // then
        assertNodeEntity(nodeEntity);
    }

    private void assertNodeDTO(NodeDTO nodeDTO) {
        assertThat(nodeDTO.getId()).isEqualTo(NODE_ID);
        assertThat(nodeDTO.getParentId()).isEqualTo(PARENT_ID);
        assertThat(nodeDTO.getText()).isEqualTo(String.valueOf(VALUE));
    }

    private void assertNodeEntity(NodeEntity nodeEntity) {
        assertThat(nodeEntity.getId()).isEqualTo(NODE_ID);
        assertThat(nodeEntity.getParentId()).isEqualTo(PARENT_ID);
        assertThat(nodeEntity.getValue()).isEqualTo(VALUE);
        assertThat(nodeEntity.getChildren()).isNull();
    }

    private NodeDTO getFullNodeDTO() {
        return NodeDTO.builder()
                .id(NODE_ID)
                .parentId(PARENT_ID)
                .text(String.valueOf(VALUE))
                .children(HAS_CHILDREN)
                .build();
    }

    private NodeEntity getFullNodeEntity() {
        return NodeEntity.builder()
                .id(NODE_ID)
                .parentId(PARENT_ID)
                .value(VALUE)
                .children(Sets.newHashSet(NodeEntity.builder().build()))
                .hasChildren(HAS_CHILDREN)
                .build();
    }
}