package com.robinelvin.sbc.models;

import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

@NodeEntity
public class Institution extends BaseEntity {
    @Property(name="name")
    @Index(unique = true)
    public String name;

    public Long siteId;

    public Institution() { }

    public Institution(String name, Long siteId) {
        this.name = name;
        this.siteId = siteId;
    }
}
