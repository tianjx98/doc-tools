package com.example.application.views.docsplit;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;

import com.example.application.views.docsplit.doc.Markdown;
import com.example.application.views.main.MainView;
import com.example.application.views.util.CmdUtil;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@Route(value = "split", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@PageTitle("doc-split")
@CssImport("./views/docsplit/docsplit-view.css")
public class DocsplitView extends HorizontalLayout {

    //private TextField name;
    private Button refreshDoc;

    @Value("${config.repo-path}")
    private String repoPath;

    public DocsplitView() {
        addClassName("docsplit-view");
        refreshDoc = new Button("更新文档");
        add(refreshDoc);
        setVerticalComponentAlignment(Alignment.END, refreshDoc);
        refreshDoc.addClickListener(e -> {
            try {
                Notification.show(pullDoc());
            } catch (IOException | InterruptedException exception) {
                Notification.show("更新文档失败: " + exception.getMessage());
            }
        });

        add(createFileTree());
    }

    private String pullDoc() throws IOException, InterruptedException {
        return CmdUtil.execCmd(String.format("git --git-dir=%s/.git --work-tree=%s pull", repoPath, repoPath));
    }

    private TreeGrid<Markdown> createFileTree() {
        TreeGrid<Markdown> grid = new TreeGrid<>();
        grid.addHierarchyColumn(Markdown::getTitle).setHeader("Title");
        final Markdown markdown = new Markdown("123");
        markdown.setSubs(List.of(new Markdown("456")));
        grid.setItems(List.of(markdown), Markdown::getSubs);
        return grid;
    }

}
