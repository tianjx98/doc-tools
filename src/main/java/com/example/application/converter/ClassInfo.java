package com.example.application.converter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * <p>
 * description
 * </p>
 *
 * @author junxiong.tian@hand-china.com 2021/4/27 13:40
 */
@Data
public class ClassInfo {
    private String className;
    private Map<String, ClassInfo> classInfos = new HashMap<>();
    private List<Attr> attrs = new LinkedList<>();
    private List<String> descList;
    private List<String> annotationList;
    private List<String> importLines;
    private StringBuilder sb;

    public ClassInfo(String className) {
        this.className = className;
    }

    public String toClassString() {
        if (sb != null) {
            return sb.toString();
        }
        sb = new StringBuilder();
        start();
        appendAttr();
        appendClass();
        end();
        return sb.toString();
    }

    private void appendClass() {
        classInfos.forEach((key, value) -> {
            appendClassAttr(key, value);
            sb.append("\n");
            sb.append(value.toClassString());
        });
    }

    private void appendClassAttr(String key, ClassInfo value) {
        sb.append("@JacksonXmlProperty(localName = \"");
        sb.append(key);
        sb.append("\")\n");

        sb.append("private ");
        sb.append(Character.toUpperCase(key.charAt(0)));
        sb.append(key.substring(1));
        sb.append(" ");
        sb.append(Character.toLowerCase(key.charAt(0)));
        sb.append(key.substring(1));
        sb.append(";\n");
    }

    private void appendAttr() {
        for (Attr attr : attrs) {
            sb.append(attr.toJavaCode());
            sb.append("\n");
        }
    }

    private void end() {
        sb.append("\n}");
    }

    private void start() {
        if (importLines != null) {
            for (String importLine : importLines) {
                sb.append(importLine);
                sb.append("\n");
            }
            sb.append("\n");
            sb.append("/**\n");
        }

        if (descList != null) {
            for (String desc : descList) {
                sb.append(" * ");
                sb.append(desc);
                if (!desc.startsWith("@")) {
                    sb.append("<br>");
                }
                sb.append("\n");
            }
            sb.append(" */\n");
        }

        sb.append("@Data\n");
        sb.append("@NoArgsConstructor\n");
        sb.append("@AllArgsConstructor\n");
        if (annotationList != null) {
            for (String annotation : annotationList) {
                sb.append(annotation);
                sb.append("\n");
            }
        }
        // sb.append("@JacksonXmlRootElement(localName = \"CMS\")");
        sb.append("public class ");
        sb.append(Character.toUpperCase(className.charAt(0)));
        sb.append(className.substring(1));
        sb.append(" {\n");
    }
}
