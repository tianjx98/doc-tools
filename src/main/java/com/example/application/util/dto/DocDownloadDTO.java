package com.example.application.util.dto;

import java.io.ByteArrayInputStream;

import com.vaadin.flow.server.InputStreamFactory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * description
 * </p>
 *
 * @author junxiong.tian@hand-china.com 2021/3/30 16:03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocDownloadDTO {
    private String fileName;
    private InputStreamFactory factory;

    public static DocDownloadDTO empty() {
        final DocDownloadDTO docDownloadDTO = new DocDownloadDTO();
        docDownloadDTO.setFileName("empty");
        docDownloadDTO.setFactory(()->new ByteArrayInputStream(new byte[0]));
        return docDownloadDTO;
    }
}
