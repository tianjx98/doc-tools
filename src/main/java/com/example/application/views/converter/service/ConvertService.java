package com.example.application.views.converter.service;

import com.example.application.views.converter.DataType;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;

public interface ConvertService {

    void convert(Select<DataType> leftSelect, Select<DataType> rightSelect, TextArea leftArea, TextArea rightArea);
}
