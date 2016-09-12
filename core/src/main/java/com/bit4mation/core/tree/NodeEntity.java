package com.bit4mation.core.tree;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "NODE")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NodeEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "VALUE")
    private Double value;

    @Column(name = "PARENT_ID")
    private Long parentId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    @LazyCollection(LazyCollectionOption.EXTRA)
    private Set<NodeEntity> children = new HashSet<>();

    @Transient
    private boolean hasChildren;

    /**
     * Method which set hasChildren field always after load from database - if children collection has LazyOption
     * .Extra then collection does not fetch all objects after size() method invocation
     */
    @PostLoad
    @PostUpdate
    private void setVariableAfterLoad() {
        if (children.size() > 0) {
            hasChildren = true;
        }
    }
}
