package com.example.application.views.util;

import java.io.File;
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
        log.debug("执行命令: {}", cmd);
        final Process process = Runtime.getRuntime().exec(cmd);
        return getString(process);
    }

    public static String execCmd(String cmd, File file) throws IOException, InterruptedException {
        log.debug("执行命令: {}", cmd);
        final Process process = Runtime.getRuntime().exec(cmd, null, file);
        return getString(process);
    }

    private static String getString(Process process) throws InterruptedException, IOException {
        process.waitFor();
        process.destroy();
        log.debug("exitValue: {}", process.exitValue());
        final InputStream inputStream = process.getInputStream();
        final String s = StreamUtils.copyToString(inputStream, Charset.defaultCharset());
        //+ StreamUtils.copyToString(process.getErrorStream(), Charset.defaultCharset());
        log.debug("执行结果: {}", s);
        return s;
    }
}
