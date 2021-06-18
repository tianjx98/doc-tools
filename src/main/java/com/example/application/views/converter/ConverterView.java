package com.example.application.views.converter;

import com.example.application.views.converter.service.ConvertService;
import com.example.application.views.main.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Route(value = "converter", layout = MainView.class)
@PageTitle("converter")
@Slf4j
public class ConverterView extends HorizontalLayout {

    TextArea leftArea;
    TextArea rightArea;

    Select<DataType> leftSelect;
    Select<DataType> rightSelect;

    Button convertButton;
    @Autowired
    ConvertService convertService;

    public ConverterView() {
        addClassName("converter-view");
    }

    @PostConstruct
    public void load() throws IOException {
        final HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setHeight("800px");
        horizontalLayout.setWidthFull();
        horizontalLayout.setAlignItems(Alignment.CENTER);
        horizontalLayout.getStyle().set("padding", "30px 30px 30px 30px");

        add(horizontalLayout);
        horizontalLayout.add(leftArea = new TextArea(""));
        leftArea.setWidthFull();
        leftArea.setHeight("85%");

        final VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setAlignItems(Alignment.CENTER);
        horizontalLayout.add(verticalLayout);
        verticalLayout.add(convertButton = new Button("转换"));
        addSelection(verticalLayout);
        verticalLayout.setWidth("20%");


        horizontalLayout.add(rightArea = new TextArea(""));
        rightArea.setWidthFull();
        rightArea.setHeight("85%");

        addEventListener();
    }

    private void addSelection(VerticalLayout verticalLayout) {
        final HorizontalLayout horizontalLayout = new HorizontalLayout();
        verticalLayout.add(horizontalLayout);

        horizontalLayout.setAlignItems(Alignment.CENTER);
        horizontalLayout.setWidthFull();

        leftSelect = new Select<>(DataType.values());
        horizontalLayout.add(leftSelect);
        leftSelect.setWidth("40%");

        Icon arrow = new Icon(VaadinIcon.ARROWS_LONG_RIGHT);
        horizontalLayout.add(arrow);

        rightSelect = new Select<>(DataType.values());
        horizontalLayout.add(rightSelect);
        rightSelect.setWidth("40%");
    }

    private void addEventListener() {
        convertButton.addClickListener(event -> {
            convertService.convert(leftSelect, rightSelect, leftArea, rightArea);
        });
    }


}
