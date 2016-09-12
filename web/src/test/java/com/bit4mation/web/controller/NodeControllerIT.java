package com.bit4mation.web.controller;

import com.bit4mation.core.tree.NodeDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NodeControllerIT {

    private static final String NODE_NUMBER = "33.0";
    private static final String LEAF_SUM = "66.0";
    private static final String DEFAULT_STRING_NUMBER = "0.0";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void test() {
        Long rootId = getRootId();
        Long firstNodeId = addNode(rootId);
        Long secondNodeId = addNode(firstNodeId);
        Long thirdNodeId = addNode(secondNodeId);
        renameNode(rootId, firstNodeId);
        assertNodeRename(rootId);
        renameNode(firstNodeId, secondNodeId);
        assertNodeRename(firstNodeId);
        assertLeafValue(secondNodeId);
        deleteNode(rootId, firstNodeId, secondNodeId, thirdNodeId);
    }

    private void deleteNode(Long rootId, Long firstNodeId, Long secondNodeId, Long thirdNodeId) {
        // when
        this.restTemplate.delete("/node/" + firstNodeId);

        // then
        assertNodeHttpStatus(firstNodeId, HttpStatus.BAD_REQUEST);
        assertNodeHttpStatus(secondNodeId, HttpStatus.BAD_REQUEST);
        assertNodeHttpStatus(thirdNodeId, HttpStatus.BAD_REQUEST);
        assertNodeHttpStatus(rootId, HttpStatus.OK);
    }

    private void assertNodeHttpStatus(Long nodeId, HttpStatus httpStatus) {
        // when
        ResponseEntity<String> responseEntity = this.restTemplate.getForEntity("/node/" + nodeId + "/children",
                String.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(httpStatus);
    }

    private void assertLeafValue(Long nodeId) {
        // when
        ResponseEntity<NodeDTO[]> responseEntity = this.restTemplate.getForEntity("/node/" + nodeId + "/children",
                NodeDTO[].class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).hasSize(1);
        assertThat(responseEntity.getBody()[0].getText()).isEqualTo(LEAF_SUM);
    }

    private void assertNodeRename(Long rootId) {
        // when
        ResponseEntity<NodeDTO[]> responseEntity = this.restTemplate.getForEntity("/node/" + rootId + "/children",
                NodeDTO[].class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).hasSize(1);
        assertThat(responseEntity.getBody()[0].getText()).isEqualTo(NODE_NUMBER);
    }

    private void renameNode(Long parentId, Long nodeId) {
        // given
        NodeDTO nodeDTO = NodeDTO.builder().text(NODE_NUMBER).parentId(parentId).build();

        // when
        this.restTemplate.put("/node/" + nodeId, nodeDTO);
    }

    private Long addNode(Long rootId) {
        // given
        NodeDTO nodeDTO = NodeDTO.builder().text(NODE_NUMBER).parentId(rootId).build();

        // when
        ResponseEntity<NodeDTO> responseEntity = this.restTemplate.postForEntity("/node", nodeDTO, NodeDTO.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody().getId()).isNotNull();
        assertThat(responseEntity.getBody().getParentId()).isEqualTo(rootId);
        assertThat(responseEntity.getBody().getText()).isEqualTo(DEFAULT_STRING_NUMBER);
        return responseEntity.getBody().getId();
    }

    private Long getRootId() {
        // when
        ResponseEntity<NodeDTO> responseEntity = this.restTemplate.getForEntity("/node/root", NodeDTO.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getId()).isNotNull();
        return responseEntity.getBody().getId();
    }
}
