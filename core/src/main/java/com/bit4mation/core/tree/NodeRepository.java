package com.bit4mation.core.tree;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

interface NodeRepository extends CrudRepository<NodeEntity, Long> {

    @Query("SELECT ne.children FROM NodeEntity ne WHERE ne.id=?1")
    Set<NodeEntity> findNodeChildren(Long id);

    NodeEntity findByParentIdIsNull();
}
