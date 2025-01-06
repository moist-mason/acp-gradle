package com.ancientmc.acp.init.step;

import com.ancientmc.acp.utils.Json;
import com.ancientmc.logger.ACPLogger;
import org.apache.commons.io.FileUtils;
import org.gradle.api.logging.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Extension of the DownloadFile step that allows for extra configuration for jar downloading.
 */
public class DownloadJarStep extends DownloadFileStep {
    /**
     * The Minecraft version, as determined in the ACP end-gradle workspace.
     */
    private String version;

    /**
     * This method parses through the JSON file to find the jar URL. The URL is retrieved via a method in the Json utilities class.
     * @param logger The gradle logger.
     * @param condition Boolean condition that determines if the step gets executed.
     * @param acpLogger The ACP logger.
     * @see Json#getJarUrl(File, String)
     */
    @Override
    public void exec(Logger logger, boolean condition, ACPLogger acpLogger) {
        printMessage(logger, message, condition);
        if (condition) {
            try {
                File jar = new File(output, version + (input.getPath().contains("client") ? ".jar" : "-server.jar"));
                acpLogger.log("acp.init", "Input URL: " + input);
                acpLogger.log("acp.init", "Output File: " + jar.getAbsolutePath());
                FileUtils.copyURLToFile(input, jar);
                acpLogger.log("acp.init", "Download successful");
            } catch (IOException e) {
                acpLogger.log("acp.init.step", "WARNING: Download of file at " + input.toString() + " went wrong!");
                e.printStackTrace();
            }
        } else {
            acpLogger.log("acp.init", "File already exists. Skipping step");
        }
    }

    public DownloadJarStep setVersion(String version) {
        this.version = version;
        return this;
    }

    public DownloadJarStep setInput(URL input) {
        super.setInput(input);
        return this;
    }

    public DownloadJarStep setOutput(File output) {
        super.setOutput(output);
        return this;
    }
}
