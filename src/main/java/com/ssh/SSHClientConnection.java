package com.ssh;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;
import org.apache.commons.lang3.exception.ExceptionUtils;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SSHClientConnection {

	 private final static Logger LOGGER = Logger.getLogger(SSHClientConnection.class.getName());
	
	    private static String userName = "username";

	    private static String host = "host_ip";

	    private static int port = 22;

	    private static String SSHKEY = "location of SSH private key";

	    String remoteFile = "location of remote file";

	    private JSch jSch;

	    // Getting Session and connect

	    public Session getSession() {

	        JSch jsch = new JSch();
	        Session session = null;
	        try {
	            session = jsch.getSession(userName, host, port);
	            jsch.addIdentity(SSHKEY);
	            session.setConfig("StrictHostKeyChecking", "no");
	            session.connect();
	        } catch (JSchException e) {
	            LOGGER.warning(ExceptionUtils.getStackTrace(e));
	        }

	        return session;

	    }

	    // Openning channel
	    
	    public ChannelSftp craftChannel() {


	        ChannelSftp sftpChannel = null;
	        try {

	            sftpChannel = (ChannelSftp) getSession().openChannel("sftp");
	        } catch (JSchException e) {
	            LOGGER.warning(ExceptionUtils.getStackTrace(e));
	        }


	        try {
	            sftpChannel.connect();
	        } catch (JSchException e) {
	            LOGGER.warning(ExceptionUtils.getStackTrace(e));
	        }

	        return sftpChannel;

	    }
	    		//Reading file from remote server
	    
	    public void readFile() throws IOException {


	        java.io.InputStream out = null;

	        try {
	            out = craftChannel().get(remoteFile);
	        } catch (SftpException e) {
	            LOGGER.warning(ExceptionUtils.getStackTrace(e));
	        }

	        BufferedReader br = new BufferedReader(new InputStreamReader(out));
	        String line;
	        while ((line = br.readLine()) != null)
	            System.out.println(line);
	        br.close();


	    }
	}
