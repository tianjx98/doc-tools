package com.example.application.converter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import com.example.application.util.StringUtil;

import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * description
 * </p>
 *
 * @author junxiong.tian@hand-china.com 2021/4/27 13:24
 */
public class Converter {
    public static void main(String[] args) throws Exception {
        // String itfName = "qrydzqx";
        // convert(itfName, "rq");
        // convert(itfName, "rs");
        String attrs = "/CMS/eb/in/AccNo\t账号\t必输项\t数字\t34\t\"对于本行FT类账户，支持账号前加FT类标志的上送；对于本行H、HNRA账户，支持账号前加H、HNRA标志的上送；\"\n"
                        + "/CMS/eb/in/CurrType\t币种\t必输项\t字符\t3\n"
                        + "/CMS/eb/in/BeginDate\t起始日期\t必输项\t字符\t8\t起始日期不能大于截止日期,格式YYYYMMDD\n"
                        + "/CMS/eb/in/EndDate\t截止日期\t必输项\t字符\t8\n"
                        + "/CMS/eb/in/ReqReserved1\t请求备用字段1\t可选项\t字符\t100\t\"账号序号（9位数字）,对于注册了电子银行渠道的账户管家，该字段若上送，则长度必须为9；对于本行FT类账户,忽略该字段上送值\"\n"
                        + "/CMS/eb/in/ReqReserved2\t请求备用字段2\t可选项\t字符\t100\t启用为下页标识，第一次查询送空,查询下页时上送上次查询时返回的值\n";
        final List<Attr> attrs1 = loadAllAttrs(attrs);
    }

    // private static void convert(String itfName, String suffix) throws Exception {
    // final String path = "./icbc/" + itfName + "_" + suffix;
    // final Path filePath = Paths.get(path);
    // if (!filePath.toFile().exists()) {
    // return;
    // }
    // final List<Attr> attrs = loadAllAttrs(filePath);
    // // attrs.forEach(System.out::println);
    // convertAttrs(itfName, suffix, attrs);
    // }

    public static String convertAttrs(String itfCode, String suffix, String bankItfName, List<Attr> attrs) {
        final ClassInfo classInfo = toClass(attrs);
        final String upperCaseSuffix = StringUtil.firstCharToUpperCase(suffix);
        final String className = "B2e" + StringUtil.firstCharToUpperCase(itfCode) + upperCaseSuffix + "DTO";
        classInfo.setClassName(className);

        classInfo.setImportLines(List.of("import java.util.List;",
                        "import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;",
                        "import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;",
                        "import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;",
                        "import io.swagger.annotations.ApiModelProperty;", "import lombok.AllArgsConstructor;",
                        "import lombok.Data;", "import lombok.NoArgsConstructor;",
                        "import javax.validation.constraints.NotNull;"));

        String version = bankItfName.contains("0.0.0.1") ? "0.0.0.1" : "0.0.1.0";
        classInfo.setDescList(List.of(bankItfName + " " + ("rq".equals(suffix) ? "请求DTO" : "响应DTO"), "",
                        "@version " + version, "@author junxiong.tian@hand-china.com 2021/4/29 15:18"));

        classInfo.setAnnotationList(List.of("@JacksonXmlRootElement(localName = \"CMS\")"));


        final String classStr = classInfo.toClassString();
        return classStr;
        // Files.write(Paths.get("./icbc/" + className + ".java"),
        // classStr.getBytes(StandardCharsets.UTF_8));
    }

    private static ClassInfo toClass(List<Attr> attrs) {
        final Map<String, ClassInfo> classTree = buildClassTree(attrs);
        final LinkedList<ClassInfo> classInfos = new LinkedList<>();
        classTree.forEach((k, v) -> {
            classInfos.add(v);
        });
        return classInfos.get(0);
    }

    private static Map<String, ClassInfo> buildClassTree(List<Attr> attrs) {
        final HashMap<String, ClassInfo> classTree = new HashMap<>();
        for (Attr attr : attrs) {
            addToTree(classTree, attr);
        }
        return classTree;
    }

    private static void addToTree(Map<String, ClassInfo> classTree, Attr attr) {
        Map<String, ClassInfo> map = classTree;
        Map<String, ClassInfo> prev = null;
        String className = null;
        while (attr.hasNextLevel()) {
            className = attr.nextLevel();
            final ClassInfo classInfo = map.get(className);
            if (classInfo == null) {
                final ClassInfo newClass = new ClassInfo(className);
                map.put(className, newClass);
            }
            prev = map;
            map = map.get(className).getClassInfos();
        }
        prev.get(className).getAttrs().add(attr);
    }

    public static List<Attr> loadAllAttrs(String attrLines) {
        return Arrays.stream(attrLines.split("\n")).filter(StrUtil::isNotBlank).map(line -> {
            final String[] cols = line.split("\t");
            return cols.length > 5 ? new Attr(cols[0], cols[1], cols[2], cols[3], cols[4], cols[5])
                            : new Attr(cols[0], cols[1], cols[2], cols[3], cols[4]);
        }).collect(Collectors.toList());
    }

    private static List<Attr> loadAllAttrs(Path filePath) throws Exception {
        final List<String> lines = Files.readAllLines(filePath);
        return lines.stream().map(line -> {
            final String[] cols = line.split("\t");
            return cols.length > 5 ? new Attr(cols[0], cols[1], cols[2], cols[3], cols[4], cols[5])
                            : new Attr(cols[0], cols[1], cols[2], cols[3], cols[4]);
        }).collect(Collectors.toList());
    }
}
