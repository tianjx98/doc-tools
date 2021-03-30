package com.example.application.views.util;

import java.io.IOException;
import java.nio.charset.Charset;

import org.springframework.util.StreamUtils;

/**
 * <p>
 * description
 * </p>
 *
 * @author junxiong.tian@hand-china.com 2021/3/29 11:03
 */
public class CmdUtil {
    public static String execCmd(String cmd) throws IOException, InterruptedException {
        final Process process = Runtime.getRuntime().exec(cmd);
        process.waitFor();
        process.destroy();
        final String s = StreamUtils.copyToString(process.getInputStream(), Charset.defaultCharset())
                        + StreamUtils.copyToString(process.getErrorStream(), Charset.defaultCharset());
        return s;
    }
}
