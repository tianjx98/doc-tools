package com.example.application.views.converter.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.application.utils.StringUtil;
import com.example.application.views.converter.DataType;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ConvertServiceImpl implements ConvertService {
    @Override
    public void convert(Select<DataType> leftSelect, Select<DataType> rightSelect, TextArea leftArea, TextArea rightArea) {
        // 去掉双引号前的转义字符
        final String sourceText = leftArea.getValue().replaceAll("\\\\\"", "\\\"");
        System.out.println(sourceText);
        final JSONObject jsonObject = JSON.parseObject(sourceText);
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            final String fieldName = entry.getKey();
            System.out.println("private String " + StringUtil.toLowerCamelCase(fieldName) + ";");
        }
    }
}
