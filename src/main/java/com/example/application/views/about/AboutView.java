package com.example.application.views.about;

import java.util.List;

import com.example.application.converter.Attr;
import com.example.application.converter.Converter;
import com.example.application.views.main.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "about", layout = MainView.class)
@PageTitle("代码生成器")
@CssImport("./views/about/about-view.css")
public class AboutView extends Div {

    /**
     * 定期存款-存缴
     */
    TextField taskName;
    /**
     * 投资理财服务——推广版-高级定期存款开立接口（0.0.0.1)
     */
    TextField bankItfName;
    /**
     * QRYDZQX
     */
    TextField bankItfCode;
    /**
     * deposit
     */
    TextField itfName;
    /**
     * rq | rs
     */
    TextField itfSuffix;
    Button generate;
    TextArea bankItfRqAttrs;
    TextArea bankItfRsAttrs;
    TextArea gConstant;
    TextArea gItfMethod;
    TextArea gItfImpl;
    TextArea gRequiredAttr;
    TextArea gRequiredAttrSet;
    TextArea gTestCode;
    TextArea gRqDTO;
    TextArea gRsDTO;
    public AboutView() {
        addClassName("Generator");
        addComponents();
    }

    private void addComponents() {
        final VerticalLayout body = new VerticalLayout();
        add(body);
        addHead(body);

        bankItfRqAttrs = new TextArea("请求属性");
        bankItfRqAttrs.getStyle().set("minWidth", "700px");
        bankItfRqAttrs.getStyle().set("height", "300px");

        bankItfRsAttrs = new TextArea("响应属性");
        bankItfRsAttrs.getStyle().set("minWidth", "700px");
        bankItfRsAttrs.getStyle().set("height", "300px");
        final HorizontalLayout content = new HorizontalLayout();
        final VerticalLayout left = new VerticalLayout();
        left.add(bankItfRqAttrs, bankItfRsAttrs);
        content.add(left);

        addGenerateTextField(content);
        body.add(content);

    }

    private void addGenerateTextField(HorizontalLayout content) {
        final VerticalLayout verticalLayout = new VerticalLayout();

        gConstant = genTextArea("接口常量");
        gItfMethod = genTextArea("接口方法");
        gItfImpl = genTextArea("接口实现");
        gRequiredAttr = genTextArea("必输请求参数");
        gRequiredAttrSet = genTextArea("必输参数设值");
        gTestCode = genTextArea("测试代码");
        gRqDTO = genTextArea("RqDTO");
        gRsDTO = genTextArea("RsDTO");

        verticalLayout.add(gConstant, gItfMethod, gItfImpl, gRequiredAttr, gRequiredAttrSet, gTestCode, gRqDTO, gRsDTO);
        content.add(verticalLayout);
    }

    private TextArea genTextArea(String name) {
        TextArea textArea = new TextArea(name);
        textArea.getStyle().set("minWidth", "700px");
        textArea.getStyle().set("maxHeight", "200px");
        textArea.setReadOnly(true);
        return textArea;
    }

    private void addHead(VerticalLayout body) {
        taskName = new TextField();
        taskName.setPlaceholder("定期存款-存缴");
        taskName.setValue("定期存款-存缴");

        bankItfName = new TextField();
        bankItfName.setPlaceholder("投资理财服务——推广版-高级定期存款开立接口（0.0.0.1)");
        bankItfName.setValue("投资理财服务——推广版-高级定期存款开立接口（0.0.0.1)");

        bankItfCode = new TextField();
        bankItfCode.setPlaceholder("DQCKKL");
        bankItfCode.setValue("DQCKKL");

        itfName = new TextField();
        itfName.setPlaceholder("timeDeposit");
        itfName.setValue("timeDeposit");

        itfSuffix = new TextField();
        itfSuffix.setPlaceholder("rq | rs");
        itfSuffix.setValue("rq");

        generate = new Button("生成");
        generate.addClickListener(event -> {
            final List<Attr> attrs = Converter.loadAllAttrs(bankItfRqAttrs.getValue());
            CodeGenerator.genItfConstant(this);
            CodeGenerator.genItfMethod(this);
            CodeGenerator.genItfImpl(this);
            CodeGenerator.genItfImpl(this);
            CodeGenerator.genRequiredAttr(this, attrs);
            CodeGenerator.genRequiredAttrSet(this, attrs);
            CodeGenerator.genTestCode(this);
            CodeGenerator.genDTO(this, attrs);
        });

        final HorizontalLayout head = new HorizontalLayout();
        head.add(taskName, bankItfName, bankItfCode, itfName, itfSuffix, generate);

        body.add(head);
    }

}
