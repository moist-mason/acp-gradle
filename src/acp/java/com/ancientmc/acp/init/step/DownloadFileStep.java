package com.ancientmc.acp.init.step;

import com.ancientmc.logger.ACPLogger;
import org.apache.commons.io.FileUtils;
import org.gradle.api.logging.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Downloads a file from a URL link.
 */
public class DownloadFileStep extends Step {
    /**
     * The input URL.
     */
    protected URL input;
    /**
     * The downloaded file.
     */
    protected File output;

    /**
     * This method uses a function from Apache Commons-IO to download a file from a URL.
     * @param logger The gradle logger.
     * @param condition Boolean condition that determines if the step gets executed.
     * @param acpLogger The ACP logger.
     */
    @Override
    public void exec(Logger logger, boolean condition, ACPLogger acpLogger) {
        super.exec(logger, condition, acpLogger);
        if (condition) {
            try {
                acpLogger.log("acp.init", "Input URL: " + input.toString());
                acpLogger.log("acp.init", "Output File: " + output.getAbsolutePath());
                FileUtils.copyURLToFile(input, output);
                acpLogger.log("acp.init", "Download successful");
            } catch (IOException e) {
                acpLogger.log("acp.init.step", "WARNING: Download of file at " + input.toString() + " went wrong!");
                e.printStackTrace();
            }
        } else {
            acpLogger.log("acp.init", "File already exists. Skipping step");
        }
    }

    public File getOutput() {
        return output;
    }

    public DownloadFileStep setInput(URL input) {
        this.input = input;
        return this;
    }

    public DownloadFileStep setOutput(File output) {
        this.output = output;
        return this;
    }
}
