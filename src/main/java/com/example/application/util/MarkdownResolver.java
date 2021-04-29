package com.example.application.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import com.example.application.util.split.DocSegment;
import com.example.application.util.split.SegmentType;

import lombok.extern.slf4j.Slf4j;


/**
 * <p>
 * Markdown文档拆分工具
 * </p>
 *
 * @author junxiong.tian@hand-china.com 2021/3/16 9:32
 */
// @Component
@Slf4j
public class MarkdownResolver {
    /**
     * 项目绝对路径
     */
    private static final String ROOT = "C:/Users/tian/IdeaProjects/htc-document/";
    private static final String[] TITLES = {"# ", "## ", "### ", "#### ", "##### ", "###### "};

    public static void main(String[] args) throws IOException {
        final String[] filenames = {"汉得汇税通-OFD电子发票识别接口文档_UAT_V1.0.md", "汉得汇税通-OFD电子发票识别接口文档_V1.1.md",
                "汉得汇税通-进项通道服务接口文档_UAT_V1.1.md", "汉得汇税通-进项通道服务接口文档_V2.9.md", "汉得汇税通-销项通道服务接口文档_UAT_V1.0.md",
                "汉得汇税通-销项通道服务接口文档_V1.5.md"};
        for (String filename : filenames) {
            resolve(ROOT, filename);
        }
    }

    public static DocSegment resolve(String repoPath, String originFilename) throws IOException {
        final Map<SegmentType, List<DocSegment>> segmentsMap = extractAllSegments(repoPath, originFilename).stream()
                        .collect(Collectors.groupingBy(DocSegment::getSegmentType));
        final List<DocSegment> interfaces = segmentsMap.get(SegmentType.INTERFACE);
        final DocSegment summarize = Optional.ofNullable(segmentsMap.get(SegmentType.DOC_SUMMARIZE))
                        .orElse(Collections.singletonList(null)).get(0);
        final DocSegment callInstruction = Optional.ofNullable(segmentsMap.get(SegmentType.CALL_INSTRUCTION))
                        .orElse(Collections.singletonList(null)).get(0);
        final DocSegment lov = Optional.ofNullable(segmentsMap.get(SegmentType.LOV))
                        .orElse(Collections.singletonList(null)).get(0);
        final DocSegment appendix = Optional.ofNullable(segmentsMap.get(SegmentType.APPENDIX))
                        .orElse(Collections.singletonList(null)).get(0);
        final DocSegment legacyAndClosedIssues =
                        Optional.ofNullable(segmentsMap.get(SegmentType.LEGACY_AND_CLOSED_ISSUES))
                                        .orElse(Collections.singletonList(null)).get(0);

        final DocSegment parent = DocSegment.builder().segmentName(originFilename).interfaces(interfaces)
                        .summarize(summarize).callInstruction(callInstruction).lov(lov).appendix(appendix)
                        .legacyAndClosedIssues(legacyAndClosedIssues).build();
        if (!CollectionUtils.isEmpty(interfaces)) {
            for (DocSegment itf : interfaces) {
                itf.setParent(parent);
            }
        }
        return parent;
    }

    private static void writeDoc(String originFilename, String fileName, DocSegment... segments) throws IOException {
        final List<String> lines = new LinkedList<>();
        for (DocSegment segment : segments) {
            if (segment == null) {
                continue;
            }
            lines.addAll(segment.getLines());
        }
        final String prefix = originFilename.substring(0, originFilename.indexOf("接口文档"));
        final String dir = originFilename.substring(0, originFilename.lastIndexOf("."));
        final Path itfPath = Paths.get(ROOT, "split", dir, prefix + "-" + fileName);
        if (!itfPath.getParent().toFile().exists()) {
            Files.createDirectories(itfPath.getParent());
        }
        if (!itfPath.toFile().exists()) {
            Files.createFile(itfPath);
        }
        Files.write(itfPath, lines);
    }

    private static List<DocSegment> extractAllSegments(String repoPath, String filename) throws IOException {
        final Path path = Paths.get(repoPath, "document/", filename);
        final List<String> lines = Files.readAllLines(path);
        final String name = filename.substring(0, filename.lastIndexOf("."));
        final List<DocSegment> segments = new LinkedList<>();
        for (int startLevel = 0; startLevel < 6; startLevel++) {
            segments.addAll(extractSegments(lines, name, TITLES[startLevel]));
        }
        return segments;
    }

    /**
     * 获取指定等级的标题片段
     * 
     * @param lines 文档所有行
     * @param originFilename 原文档名称
     * @param titleFlag 文档等级标志(#)
     * @return 返回指定级别的文档片段
     */
    private static List<DocSegment> extractSegments(List<String> lines, String originFilename, String titleFlag) {
        String title = null;
        int from = 0;
        final List<DocSegment> docSegments = new LinkedList<>();
        for (int i = 0; i < lines.size(); i++) {
            final String line = lines.get(i);
            boolean isEnd = i + 1 == lines.size();
            if (isEnd || line.startsWith(titleFlag)) {
                if (title != null) {
                    final DocSegment docSegment = new DocSegment(lines.subList(from, isEnd ? i + 1 : i), title,
                                    originFilename, titleFlag);
                    docSegments.add(docSegment);
                }
                if (!isEnd) {
                    title = line.substring(line.indexOf(" "));
                    from = i;
                }
            }
        }
        return docSegments;
    }
}
