package com.bit4mation.web.controller;

import com.bit4mation.core.tree.NodeDTO;
import com.bit4mation.core.tree.NodeEntity;
import com.bit4mation.core.tree.NodeMapper;
import com.bit4mation.core.tree.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

/**
 * Node controller with all endpoints to manage tree
 */
@RestController
@RequestMapping("/node")
public class NodeController {

    @Autowired
    private NodeService nodeService;

    @Autowired
    private NodeMapper nodeMapper;

    /**
     * Endpoint which get all root nodes
     *
     * @return root nodes
     */
    @ResponseStatus(OK)
    @RequestMapping(method = RequestMethod.GET, value = "/root")
    public NodeDTO getRootNode() {
        NodeEntity rootNode = nodeService.getRootNode();
        return nodeMapper.toDTO(rootNode);
    }

    /**
     * Endpoint which get all node children by node id
     *
     * @param id - node id
     * @return node children
     */
    @ResponseStatus(OK)
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/children")
    public Set<NodeDTO> getNodeChildren(@PathVariable("id") Long id) {
        Set<NodeEntity> nodeChildren = nodeService.getChildren(id);
        return nodeMapper.toDTOs(nodeChildren);
    }

    /**
     * Endpoint which allow to add new node
     *
     * @param nodeDTO - node object with filled fields
     * @return persisted node
     */
    @ResponseStatus(CREATED)
    @RequestMapping(method = RequestMethod.POST)
    public NodeDTO addNode(@RequestBody NodeDTO nodeDTO) {
        NodeEntity nodeEntity = nodeMapper.toEntity(nodeDTO);
        nodeEntity = nodeService.add(nodeEntity);
        return nodeMapper.toDTO(nodeEntity);
    }

    /**
     * Endpoint which allow to delete node with his children
     *
     * @param id - node id
     */
    @ResponseStatus(OK)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void deleteNode(@PathVariable("id") Long id) {
        nodeService.delete(id);
    }

    /**
     * Endpoint which allow to update node without children
     *
     * @param id      - node id
     * @param nodeDTO - node object with changes fields
     * @return update node
     */
    @ResponseStatus(OK)
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public NodeDTO updateNode(@PathVariable("id") Long id, @RequestBody NodeDTO nodeDTO) {
        nodeDTO.setId(id);
        NodeEntity nodeEntity = nodeMapper.toEntity(nodeDTO);
        nodeEntity = nodeService.update(nodeEntity);
        return nodeMapper.toDTO(nodeEntity);
    }
}
