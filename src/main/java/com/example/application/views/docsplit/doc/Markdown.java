package com.example.application.views.docsplit.doc;

import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import com.example.application.util.split.DocSegment;

import lombok.Builder;
import lombok.Data;

/**
 * <p>
 * description
 * </p>
 *
 * @author junxiong.tian@hand-china.com 2021/3/29 22:31
 */
@Data
@Builder
public class Markdown {
    private String fileName;
    private DocSegment summarize;
    private DocSegment callInstruction;
    private DocSegment lov;
    private DocSegment appendix;
    private DocSegment legacyAndClosedIssues;
    private List<DocSegment> interfaces = new LinkedList<>();

    public OutputStream getInterface(int index) {
        final DocSegment itf = interfaces.get(index);
            //writeDoc(originFilename, itf.getFileName(), summarize, callInstruction, itf, lov, appendix,
            //                legacyAndClosedIssues);
        return null;
    }

    //private static void writeDoc(String originFilename, String fileName, DocSegment... segments) throws IOException {
    //    final List<String> lines = new LinkedList<>();
    //    for (DocSegment segment : segments) {
    //        if (segment == null) {
    //            continue;
    //        }
    //        lines.addAll(segment.getLines());
    //    }
    //    final String prefix = originFilename.substring(0, originFilename.indexOf("接口文档"));
    //    final String dir = originFilename.substring(0, originFilename.lastIndexOf("."));
    //    final Path itfPath = Paths.get(ROOT, "split", dir, prefix + "-" + fileName);
    //    if (!itfPath.getParent().toFile().exists()) {
    //        Files.createDirectories(itfPath.getParent());
    //    }
    //    if (!itfPath.toFile().exists()) {
    //        Files.createFile(itfPath);
    //    }
    //    Files.write(itfPath, lines);
    //}
}
