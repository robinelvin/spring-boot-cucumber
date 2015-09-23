package com.robinelvin.sbc.models;

import org.neo4j.ogm.annotation.*;

/**
 * @author Robin Elvin
 */
@RelationshipEntity(type = User.MEMBER_OF_INSTITUTION)
public class HasAccountsWith {
    @GraphId Long relationshipId;
    @StartNode private User user;
    @EndNode private Institution institution;
    @Property private Long memSiteAccId;
    @Property private String refreshStatus;

    public Long getRelationshipId() {
        return relationshipId;
    }

    public void setRelationshipId(Long relationshipId) {
        this.relationshipId = relationshipId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public Long getMemSiteAccId() {
        return memSiteAccId;
    }

    public void setMemSiteAccId(Long memSiteAccId) {
        this.memSiteAccId = memSiteAccId;
    }

    public String getRefreshStatus() {
        return refreshStatus;
    }

    public void setRefreshStatus(String refreshStatus) {
        this.refreshStatus = refreshStatus;
    }

    public HasAccountsWith(User user, Institution institution, Long memSiteAccId, String refreshStatus) {
        this.user = user;
        this.institution = institution;
        this.memSiteAccId = memSiteAccId;
        this.refreshStatus = refreshStatus;
    }

    public HasAccountsWith() {
    }

    @Override
    public boolean equals(Object o) {
        HasAccountsWith other = (HasAccountsWith)o;
        return this.user.getNodeId().equals(other.user.getNodeId()) &&
                this.institution.getNodeId().equals(other.getInstitution().getNodeId()) &&
                this.memSiteAccId.equals(other.getMemSiteAccId());
    }
}
