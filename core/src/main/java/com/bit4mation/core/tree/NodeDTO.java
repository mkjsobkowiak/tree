package com.bit4mation.core.tree;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NodeDTO {

    private Long id;
    private Long parentId;
    private String text;
    private boolean children;
}
