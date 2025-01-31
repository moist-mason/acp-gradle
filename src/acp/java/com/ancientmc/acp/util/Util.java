package com.ancientmc.acp.util;

import net.neoforged.srgutils.IMappingFile;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.io.IOUtils;
import org.gradle.internal.os.OperatingSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Class full of miscellaneous utilities.
 */
public class Util {

    /**
     * Gets a map of class names from the SRG file. The key is the obfuscated name, while the value is the mapped name.
     * @param srg The SRG file.
     * @return The class map.
     * @throws IOException
     */
    public static Map<String, String> getClassMap(File srg) throws IOException {
        Map<String, String> map = new HashMap<>();
        IMappingFile mapping = IMappingFile.load(srg);
        mapping.getClasses().forEach(cls -> map.put(cls.getOriginal(), cls.getMapped()));
        return map;
    }

    /**
     * Converts a maven path into a URL whose contents can be downloaded.
     * @param repo The repository URL.
     * @param path The maven path (group.sub:name:version).
     * @param ext The file extension.
     * @return The maven URL.
     */
    public static URL toMavenUrl(String repo, String path, String ext) {
        try {
            String[] split = path.split(":");
            String file = split[1] + "-" + split[2] + (split.length > 3 ? "-" + split[3] : "") + "." + ext;
            String newPath = split[0].replace('.', '/') + "/" + split[1] + "/" + split[2] + "/" + file;
            return new URL(repo + newPath);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Quick method that compresses multiple files into both a ZIP and TAR
     * @param files The files.
     * @param directory The output directory.
     * @throws IOException
     */
    public static void compress(Collection<File> files, File directory) throws IOException {
        String archive = directory.getName();
        Util.compressZip(files, new File(directory, archive + ".zip"));
        Util.compressTar(files, new File(directory, archive + ".tar.gz"));
    }

    /**
     * Simple compression function for multiple files.
     * @param files The files.
     * @param zip The output ZIP.
     * @throws IOException
     */
    public static void compressZip(Collection<File> files, File zip) throws IOException {
        ZipOutputStream zipOut = new ZipOutputStream(Files.newOutputStream(zip.toPath()));

        for (File file : files) {
            zipOut.putNextEntry(new ZipEntry(file.getName()));
            FileInputStream in = new FileInputStream(file);
            IOUtils.copy(in, zipOut);
            zipOut.closeEntry();
        }
        zipOut.close();
    }

    /**
     * Simple compression function for multiple files.
     * @param files The files.
     * @param tar The output TAR GZIP.
     * @throws IOException
     */
    public static void compressTar(Collection<File> files, File tar) throws IOException {
        GzipCompressorOutputStream gzipOut = new GzipCompressorOutputStream(Files.newOutputStream(tar.toPath()));
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(gzipOut);

        for (File file : files) {
            tarOut.putArchiveEntry(new TarArchiveEntry(file, file.getName()));
            FileInputStream in = new FileInputStream(file);
            IOUtils.copy(in, tarOut);
            tarOut.closeArchiveEntry();
        }
        tarOut.finish();
        tarOut.close();
        gzipOut.close();
    }

    /**
     * Gets a shortened version of the operating system's name. This class is used in getting the native URLs,
     * as different versions for LWJGL's natives are needed depending on the operating system.
     * @see Json#getNativeUrls(File)
     */
    public static String getOSName() {
        OperatingSystem os = OperatingSystem.current();
        if(os.isWindows()) {
            return "windows";
        } else if (os.isMacOsX()) {
            return "osx";
        } else if (os.isLinux() || os.isUnix()) {
            return "linux";
        }
        return "unknown";
    }

    public static String getAncientMCMaven() {
        return "https://github.com/ancientmc/ancientmc-maven/raw/maven/";
    }
}
