package com.ancientmc.acp.init.step;

import com.ancientmc.acp.utils.Json;
import com.ancientmc.logger.ACPLogger;
import org.apache.commons.io.FileUtils;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Extracts native libraries required for Minecraft to run into the designated folder.
 */
public class ExtractNativesStep extends Step {
    /**
     * The list of URLs for native libraries from Minecraft's website. The URLs are retrieved via a method in utils.Json
     * @see Json#getNativeUrls(File)
     */
    private List<URL> urls;
    /**
     * The output directory that will contain the native files.
     */
    private File output;
    /**
     * The gradle project.
     */
    private Project project;

    /**
     * To extract the natives, we first download the JAR files in the URLS. Then for each file, we extract the native libraries
     * from their JAR files into the output folder.
     * @param logger The gradle logger.
     * @param condition Boolean condition that determines if the step gets executed.
     * @param acpLogger The ACP logger.
     */
    @Override
    public void exec(Logger logger, boolean condition, ACPLogger acpLogger) {
        super.exec(logger, condition, acpLogger);
        if (condition) {
            try {
                acpLogger.log("acp.init", "Output Directory: " + output.getAbsolutePath());
                List<File> jars = new ArrayList<>();
                for(URL url : urls) {
                    String path = url.getPath().substring(url.getPath().lastIndexOf('/') + 1);
                    FileUtils.copyURLToFile(url, new File(output, path));
                    jars.add(new File(output, path));
                }

                jars.forEach(jar -> {
                    acpLogger.log("acp.init", "Extracting native archive " + jar.getName() + " into " + output.getAbsolutePath());
                    project.copy(action -> {
                        action.from(project.zipTree(jar));
                        action.into(project.file(output));
                    });
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public File getOutput() {
        return output;
    }

    public ExtractNativesStep setUrls(List<URL> urls) {
        this.urls = urls;
        return this;
    }

    public ExtractNativesStep setOutput(File output) {
        this.output = output;
        return this;
    }

    public ExtractNativesStep setProject(Project project) {
        this.project = project;
        return this;
    }
}
