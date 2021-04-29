package com.example.application.converter;

import java.util.Arrays;

import org.springframework.util.StringUtils;

import com.example.application.util.StringUtil;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * description
 * </p>
 *
 * @author junxiong.tian@hand-china.com 2021/4/27 13:25
 */
@Data
@NoArgsConstructor
public class Attr {
    String path;
    int levelIndex = 0;
    String[] classLevel;
    String fieldName;
    String name;
    String isNotNull;
    String type;
    String length;
    String desc;
    private StringBuilder sb;

    public Attr(String path, String name, String isNotNull, String type, String length, String desc) {
        resolvePath(path);
        this.path = path.trim();
        this.name = name.trim();
        this.isNotNull = isNotNull.trim();
        this.type = type.trim();
        this.length = length.trim();
        this.desc = desc == null ? null : desc.trim();
    }

    public Attr(String path, String name, String isNotNull, String type, String length) {
        this(path, name, isNotNull, type, length, null);
    }

    private void resolvePath(String path) {
        final String[] segments = path.split("/");
        classLevel = Arrays.copyOfRange(segments, 1, segments.length - 1);
        fieldName = segments[segments.length - 1].trim();
    }

    public boolean getIsNotNull() {
        return "必输项".equals(isNotNull);
    }

    public boolean hasNextLevel() {
        return levelIndex < classLevel.length;
    }

    public String nextLevel() {
        return classLevel[levelIndex++];
    }

    public String getClassName() {
        return classLevel[classLevel.length - 1];
    }

    public StringBuilder toJavaCode() {
        if (sb != null) {
            return sb;
        }
        sb = new StringBuilder();
        appendDesc();
        appendAnnotation();
        appendAttr();
        return sb;
    }

    private void appendDesc() {
        if (StringUtils.isEmpty(desc)) {
            return;
        }
        sb.append("/**\n");
        sb.append(" * ");
        sb.append(desc);
        sb.append("\n");
        sb.append("*/\n");
    }

    private void appendAnnotation() {
        sb.append("@ApiModelProperty(\"");
        sb.append(name);
        sb.append(",");
        sb.append(length);
        sb.append("\")\n");

        sb.append("@JacksonXmlProperty(localName = \"");
        sb.append(fieldName);
        sb.append("\")\n");
        if (getIsNotNull()) {
            sb.append("@NotNull\n");
        }
    }

    private void appendAttr() {
        sb.append("private ");
        sb.append(getType());
        sb.append(" ");
        sb.append(getFieldName());
        sb.append(";\n");
    }

    public String getFieldName() {
        if (fieldName.startsWith("ERP")) {
            return "erp" + StringUtil.firstCharToUpperCase(fieldName.replace("ERP", ""));
        } else {
            if (Character.isUpperCase(fieldName.charAt(1))) {
                return fieldName.toLowerCase();
            } else {
                return StringUtil.firstCharToLowerCase(fieldName);
            }
        }
    }

    public String getType() {
        switch (type) {
            case "数字":
                return "Integer";
            default:
                return "String";
        }
    }
}
