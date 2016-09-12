package com.bit4mation.web.controller;

import com.bit4mation.core.tree.NodeDTO;
import com.bit4mation.core.tree.NodeEntity;
import com.bit4mation.core.tree.NodeMapper;
import com.bit4mation.core.tree.NodeService;
import com.google.common.collect.Sets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NodeControllerTest {

    private static final long NODE_ID = 3123L;
    @Mock
    private NodeService nodeService;

    @Mock
    private NodeMapper nodeMapper;

    @InjectMocks
    private NodeController nodeController;

    @Test
    public void testGetRootNodes() throws Exception {
        // given
        NodeEntity rootNodeEntity = new NodeEntity();
        NodeDTO rootNodeDTO = new NodeDTO();
        when(nodeService.getRootNode()).thenReturn(rootNodeEntity);
        when(nodeMapper.toDTO(rootNodeEntity)).thenReturn(rootNodeDTO);

        // when
        NodeDTO nodeDTOResponse = nodeController.getRootNode();

        // then
        assertThat(nodeDTOResponse).isEqualTo(rootNodeDTO);
        verify(nodeService, times(1)).getRootNode();
        verify(nodeMapper, times(1)).toDTO(rootNodeEntity);
    }

    @Test
    public void testGetNodeChildren() throws Exception {
        // given
        Set<NodeEntity> childrenNodeEntities = Sets.newHashSet();
        Set<NodeDTO> childrenNodeDTOs = Sets.newHashSet();
        when(nodeService.getChildren(NODE_ID)).thenReturn(childrenNodeEntities);
        when(nodeMapper.toDTOs(childrenNodeEntities)).thenReturn(childrenNodeDTOs);

        // when
        Set<NodeDTO> nodeDTOSetResponse = nodeController.getNodeChildren(NODE_ID);

        // then
        assertThat(nodeDTOSetResponse).isEqualTo(childrenNodeDTOs);
        verify(nodeService, times(1)).getChildren(NODE_ID);
        verify(nodeMapper, times(1)).toDTOs(childrenNodeEntities);
    }

    @Test
    public void testAddNode() throws Exception {
        // given
        NodeEntity nodeEntity = NodeEntity.builder().id(NODE_ID).build();
        NodeDTO nodeDTO = NodeDTO.builder().id(NODE_ID).build();
        when(nodeService.add(nodeEntity)).thenReturn(nodeEntity);
        when(nodeMapper.toDTO(nodeEntity)).thenReturn(nodeDTO);
        when(nodeMapper.toEntity(nodeDTO)).thenReturn(nodeEntity);

        // when
        NodeDTO nodeDTOResponse = nodeController.addNode(nodeDTO);

        // then
        assertThat(nodeDTOResponse).isEqualTo(nodeDTO);
        verify(nodeService, times(1)).add(nodeEntity);
        verify(nodeMapper, times(1)).toDTO(nodeEntity);
        verify(nodeMapper, times(1)).toEntity(nodeDTO);
    }

    @Test
    public void testDeleteNode() throws Exception {
        // when
        nodeController.deleteNode(NODE_ID);

        // then
        verify(nodeService, times(1)).delete(NODE_ID);
    }

    @Test
    public void testUpdateNode() throws Exception {
        // given
        NodeEntity nodeEntity = NodeEntity.builder().build();
        NodeDTO nodeDTO = NodeDTO.builder().build();
        when(nodeService.update(nodeEntity)).thenReturn(nodeEntity);
        when(nodeMapper.toDTO(nodeEntity)).thenReturn(nodeDTO);
        when(nodeMapper.toEntity(nodeDTO)).thenReturn(nodeEntity);

        // when
        NodeDTO nodeDTOResponse = nodeController.updateNode(NODE_ID, nodeDTO);

        // then
        assertThat(nodeDTOResponse).isEqualTo(nodeDTO);
        verify(nodeService, times(1)).update(nodeEntity);
        verify(nodeMapper, times(1)).toDTO(nodeEntity);
        verify(nodeMapper, times(1)).toEntity(nodeDTO);
    }
}