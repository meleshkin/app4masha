package com.example.app4masha.app4masha.data;

import java.util.Objects;

public class OrganizationAndPunishment {
    private final String organizationName;
    private final String punishment;

    public OrganizationAndPunishment(String organizationName, String punishment) {
        this.organizationName = organizationName;
        this.punishment = punishment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganizationAndPunishment that = (OrganizationAndPunishment) o;
        return organizationName.equals(that.organizationName) &&
                punishment.equals(that.punishment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(organizationName, punishment);
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public String getPunishment() {
        return punishment;
    }
}

