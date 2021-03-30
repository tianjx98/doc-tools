package com.example.application.views.util.split;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.example.application.views.docsplit.doc.Markdown;

/**
 * <p>
 * description
 * </p>
 *
 * @author junxiong.tian@hand-china.com 2021/3/30 10:04
 */
public class MarkdownResolver1 {
    public static Markdown resolve(Path path) throws IOException {
        final String fileName = getFileName(path);
        final List<String> lines = Files.readAllLines(path);

        System.out.println(fileName);

        return null;
    }

    private static String getFileName(Path path) {
        final String fileName = path.getFileName().toString();
        return fileName.substring(0, fileName.lastIndexOf('.'));
    }

    public static final String path = "D:/Code/vaddin/htc-document/document/汉得汇税通-销项通道服务接口文档_V1.5.md";
    public static void main(String[] args) throws IOException {
        resolve(Path.of(path));
    }
}
