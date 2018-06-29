package com.inwebo.integrations.forgerock.am.push;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.security.Principal;

/**
 * Authorization Principal for InWebo Security.
 * This is required by the authorization interface.
 */
public class InWeboPushAuthPrincipal implements Principal, Serializable {

    private static final long serialVersionUID = -4375241626363474200L;

    private final String name;

    public InWeboPushAuthPrincipal(final String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public boolean equals(final Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
