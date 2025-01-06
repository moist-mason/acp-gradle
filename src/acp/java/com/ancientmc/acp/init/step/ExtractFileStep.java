package com.ancientmc.acp.init.step;

import com.ancientmc.logger.ACPLogger;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;

import java.io.File;

/**
 * Extracts a single archive file.
 */
public class ExtractFileStep extends Step {
    /**
     * The input archive file getting extracted.
     */
    private File input;
    /**
     * The output directory that the archive contents are extracted into.
     */
    private File output;
    /**
     * The Gradle project.
     */
    private Project project;

    /**
     * Main extraction method. Uses Gradle's copy task and zip-tree function.
     * @param logger The gradle logger.
     * @param condition Boolean condition that determines if the step gets executed.
     * @param acpLogger The ACP logger.
     */
    @Override
    public void exec(Logger logger, boolean condition, ACPLogger acpLogger) {
        super.exec(logger, condition, acpLogger);
        if (condition) {
            acpLogger.log("acp.init", "Input Archive: " + input.getPath());
            acpLogger.log("acp.init", "Output Directory: " + output.getPath());
            project.copy(action -> {
                action.from(project.zipTree(input));
                action.into(output);
                acpLogger.log("acp.init", "Extraction successful");
            });
        } else {
            acpLogger.log("acp.init", "Directory already exists. Skipping step");
        }
    }

    public File getOutput() {
        return output;
    }

    public ExtractFileStep setInput(File input) {
        this.input = input;
        return this;
    }

    public ExtractFileStep setOutput(File output) {
        this.output = output;
        return this;
    }

    public ExtractFileStep setProject(Project project) {
        this.project = project;
        return this;
    }
}
