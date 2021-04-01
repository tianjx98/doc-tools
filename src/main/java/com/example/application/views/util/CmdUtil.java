package com.example.application.views.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.springframework.util.StreamUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * description
 * </p>
 *
 * @author junxiong.tian@hand-china.com 2021/3/29 11:03
 */
@Slf4j
public class CmdUtil {
    public static String execCmd(String cmd) throws IOException, InterruptedException {
        final Process process = Runtime.getRuntime().exec(cmd);
        process.waitFor();
        process.destroy();
        log.debug("exitValue: {}", process.exitValue());
        final InputStream inputStream = process.getInputStream();
        log.debug(inputStream.toString());
        final String s = StreamUtils.copyToString(inputStream, Charset.defaultCharset());
                        //+ StreamUtils.copyToString(process.getErrorStream(), Charset.defaultCharset());
        return s;
    }
}
