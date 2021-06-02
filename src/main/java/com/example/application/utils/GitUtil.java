package com.example.application.utils;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.*;

import com.jcraft.jsch.Session;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * description
 * </p>
 *
 * @author junxiong.tian@hand-china.com 2021/3/29 11:03
 */
@Slf4j
public class GitUtil {

    /**
     * 执行git pull命令
     * 
     * @param repositoryPath git仓库绝对路径
     * @return pull是否成功
     * @throws IOException
     * @throws GitAPIException
     */
    public static boolean pull(String repositoryPath) throws IOException, GitAPIException {
        final Git git = Git.open(new File(repositoryPath));
        SshSessionFactory sshSessionFactory = new JschConfigSessionFactory() {
            @Override
            protected void configure(OpenSshConfig.Host hc, Session session) {}
        };
        return git.pull().setRebase(true).setTransportConfigCallback(new TransportConfigCallback() {
            @Override
            public void configure(Transport transport) {
                SshTransport sshTransport = (SshTransport) transport;
                sshTransport.setSshSessionFactory(sshSessionFactory);
            }
        }).call().isSuccessful();
    }

}
