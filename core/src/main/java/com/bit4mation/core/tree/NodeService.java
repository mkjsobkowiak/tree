package com.bit4mation.core.tree;

import java.util.Set;

public interface NodeService {

    void delete(Long id);

    NodeEntity add(NodeEntity nodeEntity);

    NodeEntity update(NodeEntity nodeEntity);

    Set<NodeEntity> getChildren(Long id);

    NodeEntity getRootNode();
}
