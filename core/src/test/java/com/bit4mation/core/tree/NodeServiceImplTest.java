package com.bit4mation.core.tree;

import com.bit4mation.core.exception.IllegalDataException;
import com.bit4mation.core.exception.ResourceNotFoundException;
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
public class NodeServiceImplTest {
    private static final long NODE_ID = 312L;
    private static final long PARENT_ID = 312321L;
    private static final long PARENT_ID_2 = 444L;
    private static final double VALUE = 31.0;
    private static final long ROOT_NODE_ID = 43L;
    private static final double NODE_VALUE_SUM = 62.0;

    @Mock
    private NodeRepository nodeRepository;

    @InjectMocks
    private NodeServiceImpl nodeService;

    @Test
    public void testDelete() throws Exception {
        // given
        NodeEntity nodeEntity = NodeEntity.builder().id(NODE_ID).parentId(ROOT_NODE_ID).build();
        when(nodeRepository.findOne(NODE_ID)).thenReturn(nodeEntity);
        when(nodeRepository.findOne(ROOT_NODE_ID)).thenReturn(NodeEntity.builder().id(NODE_ID).build());

        // when
        nodeService.delete(NODE_ID);

        // then
        verify(nodeRepository, times(1)).delete(nodeEntity);
        verify(nodeRepository, times(2)).findOne(NODE_ID);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testDeleteThrowExceptionOnBadId() throws Exception {
        // given
        when(nodeRepository.findOne(NODE_ID)).thenReturn(null);

        // when
        nodeService.delete(NODE_ID);
    }

    @Test(expected = IllegalDataException.class)
    public void testDeleteThrowExceptionDeleteRootNode() throws Exception {
        // given
        NodeEntity nodeEntity = NodeEntity.builder().id(NODE_ID).parentId(null).build();
        when(nodeRepository.findOne(NODE_ID)).thenReturn(nodeEntity);

        // when
        nodeService.delete(NODE_ID);
    }

    @Test
    public void testAdd() throws Exception {
        // given
        NodeEntity nodeEntity = NodeEntity.builder().id(NODE_ID).parentId(ROOT_NODE_ID).build();
        when(nodeRepository.save(nodeEntity)).thenReturn(nodeEntity);
        when(nodeRepository.findOne(ROOT_NODE_ID)).thenReturn(NodeEntity.builder().id(NODE_ID).build());

        // when
        NodeEntity persistedNodeEntity = nodeService.add(nodeEntity);

        // then
        assertThat(persistedNodeEntity).isEqualTo(nodeEntity);
        verify(nodeRepository, times(1)).save(nodeEntity);
    }

    @Test
    public void testUpdateNodeWithChildren() throws Exception {
        // given
        NodeEntity nodeEntity = NodeEntity.builder().id(NODE_ID).parentId(ROOT_NODE_ID).value(VALUE).build();
        NodeEntity nodeEntityDB = NodeEntity.builder().id(NODE_ID).parentId(ROOT_NODE_ID).hasChildren(true).build();
        when(nodeRepository.findOne(NODE_ID)).thenReturn(nodeEntityDB);
        when(nodeRepository.findOne(ROOT_NODE_ID)).thenReturn(NodeEntity.builder().id(NODE_ID).build());

        // when
        NodeEntity updatedNodeEntity = nodeService.update(nodeEntity);

        // then
        assertThat(updatedNodeEntity).isEqualTo(nodeEntityDB);
        assertThat(updatedNodeEntity.getParentId()).isEqualTo(ROOT_NODE_ID);
        assertThat(updatedNodeEntity.getValue()).isEqualTo(VALUE);
        verify(nodeRepository, times(1)).findOne(NODE_ID);
    }

    @Test
    public void testUpdateLeaf() throws Exception {
        // given
        NodeEntity nodeEntityToUpdate = NodeEntity.builder().id(NODE_ID).value(VALUE).build();
        NodeEntity nodeEntity1 = NodeEntity.builder().id(PARENT_ID_2).parentId(ROOT_NODE_ID).value(VALUE).build();
        NodeEntity nodeEntity2 = NodeEntity.builder().id(PARENT_ID).parentId(PARENT_ID_2).value(VALUE).build();
        NodeEntity nodeEntityDB = NodeEntity.builder().id(NODE_ID).parentId(PARENT_ID).hasChildren(false).build();
        when(nodeRepository.findOne(NODE_ID)).thenReturn(nodeEntityDB);
        when(nodeRepository.findOne(PARENT_ID)).thenReturn(nodeEntity2);
        when(nodeRepository.findOne(PARENT_ID_2)).thenReturn(nodeEntity1);
        when(nodeRepository.findOne(ROOT_NODE_ID)).thenReturn(NodeEntity.builder().id(NODE_ID).build());

        // when
        NodeEntity updatedNodeEntity = nodeService.update(nodeEntityToUpdate);

        // then
        assertThat(updatedNodeEntity).isNotNull();
        assertThat(updatedNodeEntity.getValue()).isEqualTo(NODE_VALUE_SUM);
        assertThat(updatedNodeEntity.getParentId()).isEqualTo(PARENT_ID);
        verify(nodeRepository, times(1)).findOne(NODE_ID);
        verify(nodeRepository, times(1)).findOne(PARENT_ID);
        verify(nodeRepository, times(1)).findOne(PARENT_ID_2);
        verify(nodeRepository, times(1)).findOne(ROOT_NODE_ID);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testUpdateThrowResourceNotFoundException() throws Exception {
        // given
        NodeEntity nodeEntity = NodeEntity.builder().id(NODE_ID).parentId(PARENT_ID).value(VALUE).build();
        when(nodeRepository.findOne(NODE_ID)).thenReturn(null);

        // when
        nodeService.update(nodeEntity);
    }

    @Test(expected = IllegalDataException.class)
    public void testUpdateThrowIllegalDataExceptionOnUpdateValueOfRootNode() throws Exception {
        // given
        NodeEntity nodeEntity = NodeEntity.builder().id(NODE_ID).parentId(null).value(VALUE).build();
        when(nodeRepository.findOne(NODE_ID)).thenReturn(nodeEntity);

        // when
        nodeService.update(nodeEntity);
    }

    @Test
    public void testGetChildren() throws Exception {
        // given
        NodeEntity nodeEntity = NodeEntity.builder().id(NODE_ID).build();
        Set<NodeEntity> nodeEntities = Sets.newHashSet();
        when(nodeRepository.findOne(NODE_ID)).thenReturn(nodeEntity);
        when(nodeRepository.findNodeChildren(NODE_ID)).thenReturn(nodeEntities);

        // when
        Set<NodeEntity> childrenNodeEntities = nodeService.getChildren(NODE_ID);

        // then
        assertThat(childrenNodeEntities).isEqualTo(nodeEntities);
        verify(nodeRepository, times(1)).findOne(NODE_ID);
        verify(nodeRepository, times(1)).findNodeChildren(NODE_ID);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetChildrenThrowException() throws Exception {
        // given
        when(nodeRepository.findOne(NODE_ID)).thenReturn(null);

        // when
        nodeService.getChildren(NODE_ID);
    }

    @Test
    public void testGetRootNodes() throws Exception {
        // given
        NodeEntity nodeEntity = new NodeEntity();
        when(nodeRepository.findByParentIdIsNull()).thenReturn(nodeEntity);

        // when
        NodeEntity nodeEntityDB = nodeService.getRootNode();

        // then
        assertThat(nodeEntityDB).isEqualTo(nodeEntity);
    }
}