package org.example.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author zhenwu
 * @date 2025/7/5 15:59
 */
public final class FileUtil {
    private FileUtil() {
    }

    public static void copyDirectory(String sourcePath, String targetPath) {
        Path source = Paths.get(sourcePath), target = Paths.get(targetPath);
        try {
            if (!Files.exists(target)) {
                Files.createDirectory(target);
            }
            Files.walkFileTree(source, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    Files.createDirectory(Paths.get(dir.toString().replace(sourcePath, targetPath)));
                    return super.preVisitDirectory(dir, attrs);
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    copy(file, Paths.get(file.toString().replace(sourcePath, targetPath)));
                    return super.visitFile(file, attrs);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteDirectory(String path) {
        try {
            Files.walkFileTree(Paths.get(path), new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return super.visitFile(file, attrs);
                }
                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return super.postVisitDirectory(dir, exc);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static long dirCount(String path) {
        AtomicLong count = new AtomicLong();
        try {
            Files.walkFileTree(Paths.get(path), new SimpleFileVisitor<>(){
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    count.incrementAndGet();
                    return super.preVisitDirectory(dir, attrs);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count.get();
    }
    public static long fileCount(String path, String fileSuffix) {

        if (fileSuffix == null || fileSuffix.trim().isEmpty()) {
            return fileCount(path);
        }
        AtomicLong count = new AtomicLong();
        try {
            Files.walkFileTree(Paths.get(path), new SimpleFileVisitor<>(){
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (file.endsWith(fileSuffix)) {
                        count.incrementAndGet();
                    }
                    return super.visitFile(file, attrs);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count.get();
    }

    public static long fileCount(String path) {
        AtomicLong count = new AtomicLong();
        try {
            Files.walkFileTree(Paths.get(path), new SimpleFileVisitor<>(){
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    count.incrementAndGet();
                    return super.visitFile(file, attrs);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count.get();
    }

    public static void copy(Path originalPath, Path targetPath) {
        try (FileChannel originalChannel = new FileInputStream(originalPath.toFile()).getChannel();
             FileChannel targetChannel = new FileInputStream(targetPath.toFile()).getChannel()) {
            long remain = originalChannel.size(), pos = 0;
            do {
                long transferLen = originalChannel.transferTo(pos, remain, targetChannel);
                pos += transferLen;
                remain -= transferLen;
            } while (remain > 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void copy(String originalPath, String targetPath) {
        copy(Paths.get(originalPath), Paths.get(targetPath));
    }
}
