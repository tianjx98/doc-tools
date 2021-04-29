package com.example.application.util.split;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocSegment {
    /**
     * 文档的行
     */
    private List<String> lines;
    /**
     * 文档名称
     */
    private String segmentName;
    /**
     * 所在父文档的名称
     */
    private String parentName;
    /**
     * 标题等级
     */
    private String titleLevel;

    private DocSegment parent;
    private List<DocSegment> subs;
    private DocSegment summarize;
    private DocSegment callInstruction;
    private DocSegment lov;
    private DocSegment appendix;
    private DocSegment legacyAndClosedIssues;
    private List<DocSegment> interfaces = new ArrayList<>();

    public DocSegment(List<String> lines, String segmentName, String parentName, String titleLevel) {
        this.lines = lines;
        this.segmentName = segmentName.replace("\\*", "").trim();
        this.parentName = parentName.trim();
        this.titleLevel = titleLevel.trim();
    }

    /**
     * 获取文档片段的类型
     *
     * @return 文档类型枚举
     */
    public SegmentType getSegmentType() {
        if (isSummarize()) {
            return SegmentType.DOC_SUMMARIZE;
        } else if (isCallInstructions()) {
            return SegmentType.CALL_INSTRUCTION;
        } else if (isLovDesc()) {
            return SegmentType.LOV;
        } else if (isAppendix()) {
            return SegmentType.APPENDIX;
        }
        if (isLegacyAndClosedIssues()) {
            return SegmentType.LEGACY_AND_CLOSED_ISSUES;
        } else if (isInterface()) {
            return SegmentType.INTERFACE;
        } else {
            return SegmentType.OTHER;
        }
    }

    private boolean isLegacyAndClosedIssues() {
        return segmentName.contains("遗留和已结问题");
    }

    private boolean isAppendix() {
        return segmentName.contains("附录");
    }

    private boolean isLovDesc() {
        return segmentName.contains("值集");
    }

    private boolean isCallInstructions() {
        return segmentName.contains("接口调用说明");
    }

    private boolean isSummarize() {
        return segmentName.contains("文档概述");
    }

    private boolean isInterface() {
        for (String line : lines) {
            if (line.startsWith("#")) {
                final int blankIndex = line.indexOf(" ");
                if ("简要描述".equals(line.substring(blankIndex).trim())) {
                    if (!line.substring(0, blankIndex).equals(titleLevel + "#")) {
                        return false;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    void write(String basePath) throws IOException {
        final Path path = Paths.get(basePath, getFileName());
        if (!path.getParent().toFile().exists()) {
            Files.createDirectories(path.getParent());
        }
        if (!path.toFile().exists()) {
            // path.toFile().createNewFile();
            Files.createFile(path);
        }
        Files.write(path, lines);
    }

    public String getFileName() {
        final String originFilename = parent.getSegmentName();
        return originFilename.substring(0, originFilename.indexOf("接口文档")) + "-" + this.segmentName + ".md";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DocSegment that = (DocSegment) o;

        if (segmentName != null ? !segmentName.equals(that.segmentName) : that.segmentName != null) {
            return false;
        }
        return parentName != null ? parentName.equals(that.parentName) : that.parentName == null;
    }

    @Override
    public int hashCode() {
        int result = segmentName != null ? segmentName.hashCode() : 0;
        result = 31 * result + (parentName != null ? parentName.hashCode() : 0);
        return result;
    }
}
