package com.bit4mation.core.tree;

import com.bit4mation.core.exception.IllegalDataException;
import com.bit4mation.core.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static com.bit4mation.core.exception.IllegalDataException.CHANGE_ROOT_NOTE;
import static com.bit4mation.core.exception.IllegalDataException.ROOT_NODE_DELETE;
import static com.bit4mation.core.exception.ResourceNotFoundException.ENTITY_NOT_FOUND;

/**
 * Service to node management
 */
public class NodeServiceImpl implements NodeService {

    private static final double DEFAULT_NODE_VALUE = 0.0;

    @Autowired
    private NodeRepository nodeRepository;

    /**
     * Method which delete node with their children. If it's root node id then exception in thrown.
     *
     * @param id - node id
     */
    @Transactional
    @Override
    public void delete(Long id) {
        NodeEntity nodeEntity = findOneOrThrowException(id);
        if (nodeEntity.getParentId() == null) {
            throw new IllegalDataException(ROOT_NODE_DELETE);
        }
        NodeEntity parentNodeEntity = nodeRepository.findOne(nodeEntity.getParentId());
        Double sum = getLeafSum(parentNodeEntity.getId(), DEFAULT_NODE_VALUE);
        nodeEntity.setValue(sum);
        nodeRepository.delete(nodeEntity);
    }

    /**
     * Method which add new node (always set null id before persist to ensure that is new node)
     *
     * @param nodeEntity
     * @return persisted node
     */
    @Override
    public NodeEntity add(NodeEntity nodeEntity) {
        nodeEntity.setId(null);
        setParentNodeDefaultValueIfNeeded(nodeEntity);
        Double sum = getLeafSum(nodeEntity.getParentId(), DEFAULT_NODE_VALUE);
        nodeEntity.setValue(sum);
        return nodeRepository.save(nodeEntity);
    }

    /**
     * Method which update only node value. If current node is leaf then sum from root to leaf is set
     *
     * @param nodeEntity
     * @return updated node
     */
    @Transactional
    @Override
    public NodeEntity update(NodeEntity nodeEntity) {
        NodeEntity nodeEntityFromDB = findOneOrThrowException(nodeEntity.getId());
        if (nodeEntityFromDB.getParentId() == null && nodeEntity.getValue() != null) {
            throw new IllegalDataException(CHANGE_ROOT_NOTE);
        }
        if (nodeEntityFromDB.isHasChildren()) {
            nodeEntityFromDB.setValue(nodeEntity.getValue());
        } else {
            Double sum = getLeafSum(nodeEntityFromDB.getParentId(), DEFAULT_NODE_VALUE);
            nodeEntityFromDB.setValue(sum);
        }
        return nodeEntityFromDB;
    }

    /**
     * Method which get node children. If child is leaf then sum from root to leaf is set
     *
     * @param id - node id
     * @return node children
     */
    @Override
    public Set<NodeEntity> getChildren(Long id) {
        findOneOrThrowException(id);
        Set<NodeEntity> nodeEntities = nodeRepository.findNodeChildren(id);
        nodeEntities.forEach(nodeEntity -> {
            if (nodeEntity.isHasChildren() == false) {
                Double nodeSum = getLeafSum(nodeEntity.getParentId(), DEFAULT_NODE_VALUE);
                nodeEntity.setValue(nodeSum);
            }
        });
        return nodeEntities;
    }

    /**
     * Method which get all root nodes
     *
     * @return nodes
     */
    @Override
    public NodeEntity getRootNode() {
        return nodeRepository.findByParentIdIsNull();
    }

    /**
     * Method which set parent node default value when it's no longer leaf
     *
     * @param nodeEntity
     */
    private void setParentNodeDefaultValueIfNeeded(NodeEntity nodeEntity) {
        if (nodeEntity.getParentId() != null) {
            NodeEntity nodeEntityToChange = nodeRepository.findOne(nodeEntity.getParentId());
            if (nodeEntityToChange.getParentId() != null) {
                nodeEntityToChange.setValue(DEFAULT_NODE_VALUE);
            }
        }
    }

    /**
     * Method which return exists node or throw exception
     *
     * @param id - node id
     * @return node
     */
    private NodeEntity findOneOrThrowException(Long id) {
        NodeEntity nodeEntity = nodeRepository.findOne(id);
        if (nodeEntity == null) {
            throw new ResourceNotFoundException(String.format(ENTITY_NOT_FOUND, id));
        }
        return nodeEntity;
    }

    /**
     * Method which uses tail recursion to get sum from root node to leaf
     *
     * @param nodeId - node id, sum - number
     * @return sum
     */
    private Double getLeafSum(Long nodeId, Double sum) {
        NodeEntity parentNode = nodeRepository.findOne(nodeId);
        if (parentNode.getParentId() != null) {
            return getLeafSum(parentNode.getParentId(),
                    parentNode.getValue() != null ? (sum += parentNode.getValue()) : sum);
        } else {
            return sum;
        }
    }
}
