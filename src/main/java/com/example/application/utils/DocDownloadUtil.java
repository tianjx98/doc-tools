package com.example.application.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.logging.log4j.util.Strings;
import org.springframework.util.CollectionUtils;

import com.example.application.utils.dto.DocDownloadDTO;
import com.example.application.utils.split.DocSegment;

/**
 * <p>
 * description
 * </p>
 *
 * @author junxiong.tian@hand-china.com 2021/3/30 16:04
 */
public class DocDownloadUtil {
    public static DocDownloadDTO download(Set<DocSegment> segments) {
        if (CollectionUtils.isEmpty(segments)) {
            return DocDownloadDTO.empty();
        }
        if (segments.size() == 1) {
            return downloadSingleDoc(segments.stream().findFirst());
        }
        return DocDownloadDTO.builder().fileName(getZipFileName()).factory(() -> zipSegments(segments)).build();
    }

    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

    private static String getZipFileName() {
        return format.format(new Date()) + ".zip";
    }

    private static InputStream zipSegments(Set<DocSegment> segments) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        ZipOutputStream zos = new ZipOutputStream(outputStream)) {
            for (DocSegment segment : segments) {
                zos.putNextEntry(new ZipEntry(segment.getFileName()));
                zos.write(getFullDoc(segment).getBytes(StandardCharsets.UTF_8));
                zos.closeEntry();
            }
            zos.flush();
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (Exception e) {
            return null;
        }
    }

    private static String concatDoc(DocSegment... segments) {
        final StringBuilder sb = new StringBuilder();
        for (DocSegment segment : segments) {
            if (segment == null) {
                continue;
            }
            for (String line : segment.getLines()) {
                sb.append(line);
                sb.append(lineSeparator);
            }
        }
        return sb.toString();
    }

    private static DocDownloadDTO downloadSingleDoc(Optional<DocSegment> segmentOptional) {
        if (segmentOptional.isEmpty()) {
            throw new RuntimeException("下载的文件为空");
        }
        final DocSegment segment = segmentOptional.get();
        // 如果选中的父节点, 则下载父节点下所有的子节点
        if (segment.getParent() == null) {
            //return download(new HashSet<>(segment.getInterfaces()));
            return DocDownloadDTO.empty();
        }
        return DocDownloadDTO.builder().fileName(segment.getFileName()).factory(() -> getDoc(segment)).build();
    }

    private static InputStream getDoc(DocSegment segment) {
        try {
            return new ByteArrayInputStream(getFullDoc(segment).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            return null;
        }
    }

    private static String getFullDoc(DocSegment segment) throws IOException {
        final DocSegment parent = segment.getParent();
        return concatDoc(parent.getSummarize(), parent.getCallInstruction(), segment, parent.getLov(),
                        parent.getAppendix(), parent.getLegacyAndClosedIssues());
    }

    private static final String lineSeparator = Strings.LINE_SEPARATOR;

}
