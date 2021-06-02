package com.example.application.views.docsplit;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Value;
import org.vaadin.olli.FileDownloadWrapper;

import com.example.application.utils.DocDownloadUtil;
import com.example.application.utils.GitUtil;
import com.example.application.utils.MarkdownResolver;
import com.example.application.utils.dto.DocDownloadDTO;
import com.example.application.utils.split.DocSegment;
import com.example.application.views.main.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.StreamResource;

import lombok.extern.slf4j.Slf4j;

@Route(value = "split", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@PageTitle("doc-split")
@CssImport("./views/docsplit/docsplit-view.css")
@Slf4j
public class DocsplitView extends HorizontalLayout {

    private Button refreshDoc;
    private Button downloadDoc;
    private FileDownloadWrapper wrapper;
    TreeGrid<DocSegment> fileTree;

    @Value("${config.repo-path}")
    private String repoPath;

    public DocsplitView() {
        addClassName("docsplit-view");
    }

    @PostConstruct
    public void load() throws IOException {
        addButtons();
        addFileTree();
    }

    private void addFileTree() throws IOException {
        fileTree = createFileTree();
        fileTree.setSelectionMode(Grid.SelectionMode.SINGLE);
        fileTree.setHeight("800px");
        fileTree.addSelectionListener(e -> {
            final Set<DocSegment> selectedItems = fileTree.getSelectedItems();
            final DocDownloadDTO downloadFile = DocDownloadUtil.download(selectedItems);
            wrapper.setResource(new StreamResource(downloadFile.getFileName(), downloadFile.getFactory()));
        });
        add(fileTree);
    }

    private void addButtons() {
        addRefreshDocButton();
        addDocDownloadButton();
    }

    private void addDocDownloadButton() {
        downloadDoc = new Button("下载文档");
        wrapper = new FileDownloadWrapper(new StreamResource("", () -> new ByteArrayInputStream(new byte[0])));
        wrapper.wrapComponent(downloadDoc);
        add(wrapper);
    }

    private void addRefreshDocButton() {
        refreshDoc = new Button("更新文档");
        add(refreshDoc);
        setVerticalComponentAlignment(Alignment.END, refreshDoc);
        refreshDoc.addClickListener(e -> {
            try {
                if (!pullDoc()) {
                    throw new RuntimeException("未知错误");
                }
                Notification.show("文档更新成功");
                remove(fileTree);
                addFileTree();
            } catch (GitAPIException | IOException | InterruptedException exception) {
                exception.printStackTrace();
                Notification.show("更新文档失败: " + exception.getMessage());
            }
        });
    }

    private boolean pullDoc() throws IOException, InterruptedException, GitAPIException {
        return GitUtil.pull(repoPath);
    }

    private TreeGrid<DocSegment> createFileTree() throws IOException {
        TreeGrid<DocSegment> grid = new TreeGrid<>();
        grid.addHierarchyColumn(DocSegment::getSegmentName).setHeader("文档名");
        final List<DocSegment> segments = Files.list(Path.of(repoPath, "document"))
                        .map(path -> path.getFileName().toString()).map(fileName -> {
                            try {
                                return MarkdownResolver.resolve(repoPath, fileName);
                            } catch (Exception e) {
                                return null;
                            }
                        }).filter(Objects::nonNull).collect(Collectors.toList());
        grid.setItems(segments, DocSegment::getInterfaces);
        return grid;
    }

}
