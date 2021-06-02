package com.example.application.views.about;

import java.util.List;
import java.util.Map;

import com.example.application.converter.Attr;
import com.example.application.converter.Converter;
import com.example.application.utils.StringUtil;

import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * description
 * </p>
 *
 * @author junxiong.tian@hand-china.com 2021/4/29 10:55
 */
public class CodeGenerator {
    public static void genItfConstant(AboutView view) {
        final String template =
                        "    /**\n" + "     * %s\n" + "     */\n" + "    public static final String %s = \"%s\";";
        final String constant = String.format(template, view.bankItfName.getValue(), view.itfName.getValue(),
                        view.bankItfCode.getValue());
        view.gConstant.setValue(constant);
    }

    public static void genItfMethod(AboutView view) {
        final String template = "    /**\n" + "     * %s<br>\n" + "     * %s<br>\n" + "     *\n"
                        + "     * @param itfRegister 银企接口注册信息\n" + "     * @param %sRqDTOList %s 请求DTO\n"
                        + "     * @return\n" + "     */\n"
                        + "    List<%sRsDTO> %s(ItfRegister itfRegister, List<%sRqDTO> %sRqDTOList);";
        final String itfName = view.itfName.getValue();
        final String itfMethod = String.format(template, view.taskName.getValue(), view.bankItfName.getValue(), itfName,
                        view.taskName.getValue(), StringUtil.firstCharToUpperCase(itfName), itfName,
                        StringUtil.firstCharToUpperCase(itfName), itfName);
        view.gItfMethod.setValue(itfMethod);
    }

    public static void genItfImpl(AboutView view) {
        final String itfName = view.itfName.getValue();
        final String itfNameUpperCase = StringUtil.firstCharToUpperCase(itfName);
        final Map<String, String> paramMap = Map.of("itfName", itfName, "itfNameUpperCase", itfNameUpperCase,
                        "rqClassName", getRqClassName(view), "rsClassName", getRsClassName(view));
        final String itfImpl = genItfMethod(paramMap) + "\n\n" + getReqAssembleMethod(paramMap) + "\n\n"
                        + getParseResponseMethod(paramMap);
        view.gItfImpl.setValue(itfImpl);
    }

    private static String getParseResponseMethod(Map<String, String> paramMap) {
        String template =
                        "private {itfNameUpperCase}RsDTO parse{itfNameUpperCase}Response({rsClassName} rspDTO) {\n"
                                        + "        // TODO: 返回统一响应报文格式待确定 [junxiong.tian@hand-china.com, 2021/4/25 20:35]\n"
                                        + "        return null;\n" + "    }";
        return StrUtil.format(template, paramMap);
    }

    private static String getReqAssembleMethod(Map<String, String> paramMap) {
        String template =
                        "private {rqClassName} assemble{itfNameUpperCase}RequestDTO(ItfRegister itfRegister, List<{itfNameUpperCase}RqDTO> {itfName}RqDTOList) {\n"
                                        + "        final {rqClassName} reqDTO = new {rqClassName}();\n"
                                        + "        final {rqClassName}.Eb eb = reqDTO.new Eb();\n"
                                        + "        reqDTO.setEb(eb);\n"
                                        + "        final Date date = new Date();\n"
                                        + "        RequestPubDTO requestPubDTO = new RequestPubDTO(IcbcConstants.{itfName}, itfRegister.getCorpno(),\n"
                                        + "                        Constants.BankCode.ICBC, itfRegister.getCertificateId(),\n"
                                        + "                        DateUtils.dateToString(date, Constants.DATE_FORMAT),\n"
                                        + "                        DateUtils.dateToString(date, Constants.TIME_FORMAT),\n"
                                        + "                        accBalanceQueryDTOList.get(0).getBachNumber());\n"
                                        + "        eb.setPub(requestPubDTO);\n" + "\n"
                                        + "        final {rqClassName}.Eb.DataBody dataBody = eb.new DataBody();\n"
                                        + "        eb.setDataBody(dataBody);\n"
                                        + "        dataBody.setTotalNum(String.valueOf({itfName}RqDTOList.size()));\n"
                                        + "        final BigDecimal totalAmount =\n"
                                        + "                        {itfName}RqDTOList.stream().map({itfNameUpperCase}RqDTO::getPayAmt).reduce(BigDecimal.ZERO, BigDecimal::add);\n"
                                        + "        dataBody.setTotalAmt(IcbcAmountUtils.toString(totalAmount));\n"
                                        + "        dataBody.setSignTime(DateUtils.dateToString(new Date(),\n"
                                        + "                        Constants.DATEMSEL_FORMAT));\n" + "\n"
                                        + "        final List<{rqClassName}.Eb.DataBody.Rd> rds = {itfName}RqDTOList.stream().map(req -> {\n"
                                        + "            final {rqClassName}.Eb.DataBody.Rd rd = dataBody.new Rd();\n"
                                        + "            rd.setSeqNo(req.getISeqno());\n"
                                        + "            rd.setPayAccNo(rd.getPayAccNo());\n"
                                        + "            rd.setPayAccNameCN(rd.getPayAccNameCN());\n"
                                        + "            rd.setNotifyAccNo(rd.getNotifyAccNo());\n"
                                        + "            rd.setNotifyAccNameCN(req.getNotifyAccNameCN());\n"
                                        + "            rd.setNotifyType(rd.getNotifyType());\n"
                                        + "            rd.setCurrType(rd.getCurrType());\n"
                                        + "            rd.setPayAmt(rd.getPayAmt());\n" + "            return rd;\n"
                                        + "        }).collect(Collectors.toList());\n"
                                        + "        dataBody.setRd(rds);\n" + "        return reqDTO;\n" + "    }";
        final String format = StrUtil.format(template, paramMap);
        return format;
    }

    private static String genItfMethod(Map<String, String> paramMap) {
        String template = "@Override\n"
                        + "    public List<{itfNameUpperCase}RsDTO> {itfName}(ItfRegister itfRegister,\n"
                        + "                    List<{itfNameUpperCase}RqDTO> {itfName}RqDTOList) {\n"
                        + "        try {\n"
                        + "            final List<{itfNameUpperCase}RsDTO> rspDTOs = new ArrayList<>({itfName}RqDTOList.size());\n"
                        + "            final {rqClassName} reqDTO = assemble{itfNameUpperCase}RequestDTO(itfRegister, {itfName}RqDTOList);\n"
                        + "            final {rsClassName} rspDTO = sendRequest(reqDTO, {rsClassName}.class);\n"
                        + "            rspDTOs.add(parse{itfNameUpperCase}Response(rspDTO));\n"
                        + "            return rspDTOs;\n" + "        } catch (IOException e) {\n"
                        + "            log.info(LogUtils.getErrorMsg(e));\n"
                        + "            throw new CommonException(\"hfins.ebank.error.m001\");\n" + "        }\n"
                        + "    }";
        return StrUtil.format(template, paramMap);
    }

    private static String getRqClassName(AboutView view) {
        final String bankItfCode = StringUtil.firstCharToUpperCase(view.bankItfCode.getValue().toLowerCase());
        return String.format("B2e%sRqDTO", bankItfCode);
    }

    private static String getRsClassName(AboutView view) {
        final String bankItfCode = StringUtil.firstCharToUpperCase(view.bankItfCode.getValue().toLowerCase());
        return String.format("B2e%sRsDTO", bankItfCode);
    }

    public static void genRequiredAttr(AboutView view, List<Attr> attrs) {
        final StringBuilder sb = new StringBuilder();
        sb.append("// -------------------------工商银行参数开始---------------------------\n\n");
        for (Attr attr : attrs) {
            if (attr.getIsNotNull()) {
                sb.append(attr.toJavaCode());
                sb.append("\n");
            }
        }
        sb.append("// -------------------------工商银行参数结束---------------------------\n");
        view.gRequiredAttr.setValue(sb.toString());
    }

    public static void genRequiredAttrSet(AboutView view, List<Attr> attrs) {
        final StringBuilder sb = new StringBuilder();
        for (Attr attr : attrs) {
            if (attr.getIsNotNull()) {
                sb.append(StrUtil.format("rd.set{attrName}(req.get{attrName}());",
                                Map.of("attrName", StringUtil.firstCharToUpperCase(attr.getFieldName()))));
                sb.append("\n");
            }
        }
        view.gRequiredAttrSet.setValue(sb.toString());
    }

    public static void genTestCode(AboutView view) {
        final String itfName = view.itfName.getValue();
        final Map<String, String> paramMap =
                        Map.of("itfName", itfName, "itfNameUpperCase", StringUtil.firstCharToUpperCase(itfName),
                                        "rqClassName", getRqClassName(view), "rsClassName", getRsClassName(view));
        String testCode = genTestMethod(paramMap) + "\n\n" + genBuildTestDTOMethod(paramMap);
        view.gTestCode.setValue(testCode);
    }

    private static String genBuildTestDTOMethod(Map<String, String> paramMap) {
        String template = "private static {rqClassName} build{itfNameUpperCase}RqDTO() {\n"
                        + "        final {rqClassName} reqDTO = new {rqClassName}();\n"
                        + "        final {rqClassName}.Eb eb = reqDTO.new Eb();\n" + "        reqDTO.setEb(eb);\n"
                        + "        final Date date = new Date();\n"
                        + "        RequestPubDTO requestPubDTO = new RequestPubDTO(IcbcConstants.{itfName}, \"itfRegister.getCorpno()\",\n"
                        + "                        Constants.BankCode.ICBC, \"itfRegister.getCertificateId()\",\n"
                        + "                        DateUtils.dateToString(date, Constants.DATE_FORMAT),\n"
                        + "                        DateUtils.dateToString(date, Constants.TIME_FORMAT),\n"
                        + "                        \"accBalanceQueryDTOList.get(0).getBachNumber()\");\n"
                        + "        eb.setPub(requestPubDTO);\n" + "\n"
                        + "        final {rqClassName}.Eb.DataBody dataBody = eb.new DataBody();\n"
                        + "        eb.setDataBody(dataBody);\n"
                        + "        dataBody.setTotalNum({itfName}RqDTOList.size());\n"
                        + "        dataBody.setTotalAmt({itfName}RqDTOList.stream().map({itfNameUpperCase}RqDTO::getPayAmt).reduce(0L, Long::sum));\n"
                        + "        dataBody.setSignTime(DateUtils.dateToString(date,\n"
                        + "                        Constants.DATEMSEL_FORMAT));\n" + "\n"
                        + "        final List<{rqClassName}.Eb.DataBody.Rd> rds = {itfName}RqDTOList.stream().map(req -> {\n"
                        + "            final {rqClassName}.Eb.DataBody.Rd rd = dataBody.new Rd();\n"
                        + "            rd.setSeqNo(req.getISeqno());\n"
                        + "            rd.setPayAccNo(req.getPayAccNo());\n"
                        + "            rd.setPayAccNameCN(req.getPayAccNameCN());\n"
                        + "            rd.setNotifyAccNo(req.getNotifyAccNo());\n"
                        + "            rd.setNotifyAccNameCN(req.getNotifyAccNameCN());\n"
                        + "            rd.setNotifyType(req.getNotifyType());\n"
                        + "            rd.setCurrType(req.getCurrType());\n"
                        + "            rd.setPayAmt(req.getPayAmt());\n" + "            return rd;\n"
                        + "        }).collect(Collectors.toList());\n" + "        dataBody.setRd(rds);\n"
                        + "        return reqDTO;\n" + "    }";
        final String format = StrUtil.format(template, paramMap);
        return format;
    }

    private static String genTestMethod(Map<String, String> paramMap) {
        String template = "private static void test{itfNameUpperCase}()throws Exception {\n"
                        + "        final {rqClassName} reqDTO = build{itfNameUpperCase}RqDTO();\n"
                        + "        convertReqDTO(reqDTO);\n" + "        final String rspXml = \"\";\n"
                        + "        convertRspDTO(rspXml, {rsClassName}.class);\n" + "    }";
        return StrUtil.format(template, paramMap);
    }

    public static void genDTO(AboutView view, List<Attr> attrs) {
        final String rqDTOStr = Converter.convertAttrs(view.bankItfCode.getValue().toLowerCase(), "rq",
                        view.bankItfName.getValue(), attrs);
        final List<Attr> rsAttrs = Converter.loadAllAttrs(view.bankItfRsAttrs.getValue());
        final String rsDTOStr = Converter.convertAttrs(view.bankItfCode.getValue().toLowerCase(), "rs",
                        view.bankItfName.getValue(), rsAttrs);
        view.gRqDTO.setValue(rqDTOStr);
        view.gRsDTO.setValue(rsDTOStr);
    }
}
