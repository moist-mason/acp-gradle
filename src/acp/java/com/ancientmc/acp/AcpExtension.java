package com.ancientmc.acp;

import org.gradle.api.Project;
import org.gradle.api.provider.Property;

public class AcpExtension {
    /**
     * The gradle project.
     */
    protected final Project project;

    /**
     * The data property.
     */
    private final Property<String> data;

    public AcpExtension(final Project project) {
        this.project = project;
        this.data = project.getObjects().property(String.class);
    }

    public Property<String> getData() {
        return this.data;
    }
}
