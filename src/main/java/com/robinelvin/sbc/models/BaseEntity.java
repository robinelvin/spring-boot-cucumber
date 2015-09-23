package com.robinelvin.sbc.models;

import org.neo4j.ogm.annotation.GraphId;

/**
 * @author Robin Elvin
 */
abstract class BaseEntity {
    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    @GraphId
    public Long nodeId;
}

