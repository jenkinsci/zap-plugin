package org.jenkinsci.plugins.zap;

import java.util.ArrayList;

import org.zaproxy.clientapi.core.ClientApi;

import hudson.model.Action;

public class ZAPInterfaceAction implements Action {

    private boolean buildStatus;
    private int timeout;
    private String installationEnvVar;
    private String homeDir;
    private String host;
    private int port;
    // New addition
    private boolean autoInstall; /* True/False to determine if we use custom tool installation or if we use zap home, maybe not needed here since the tool is already installed and could be referenced? */
    private String toolUsed;     /* refers to the custom tools */
    private String sessionFilePath;  /* full path to the session? */
    private boolean autoLoadSession; /* True/False to determine if a session will be loaded or persisted, this should not be here since a session should always be loaded in the post build action */
    //end new addition
    ArrayList<ZAPCmdLine> commandLineArgs;
    
    public ZAPInterfaceAction() {
        this.buildStatus = false;

        this.timeout = -1;
        this.homeDir = "";
        this.installationEnvVar = "";
        this.host = "";
        this.port = 0;

        this.autoInstall = false;
        this.toolUsed = null;
        this.sessionFilePath = null;

        this.commandLineArgs = null;
        System.out.println();
        System.out.println("timeout: " + timeout);
        System.out.println("homeDir: " + homeDir);
        System.out.println("installationEnv: " + installationEnvVar);
    }

    //String hello, int low, ClientApi i, 
    public ZAPInterfaceAction(boolean buildStatus, int timeout, String installationEnvVar, String homeDir, String host, int port, boolean autoInstall, String toolUsed, String sessionFilePath, ArrayList<ZAPCmdLine> commandLineArgs) {
        this.buildStatus = buildStatus;
        this.timeout = timeout;
        this.homeDir = homeDir;
        this.host = host;
        this.port = port;
        this.autoInstall = autoInstall;
        this.installationEnvVar = installationEnvVar;
        this.toolUsed = toolUsed;
        this.sessionFilePath = sessionFilePath;
        this.sessionFilePath = "";
        this.commandLineArgs = commandLineArgs;
        System.out.println();
        System.out.println("timeout: " + timeout);
        System.out.println("homeDir: " + homeDir);
        System.out.println("installationEnvVar: " + installationEnvVar);
        System.out.println("toolUsed: " + toolUsed);
        System.out.println("commandLineArgs: " + commandLineArgs.size());
        
    }
    public boolean getBuildStatus() {
        return this.buildStatus;
    }

    public void setBuildStatus(boolean buildStatus) {
        this.buildStatus = buildStatus;
    }

    public int getTimeout() {
        return this.timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getInstallationEnvVar() {
        return this.installationEnvVar;
    }

    public void setInstallationEnvVar(String installationEnvVar) {
        this.installationEnvVar = installationEnvVar;
    }

    public String getHomeDir() {
        return this.homeDir;
    }

    public void setHomeDir(String homeDir) {
        this.homeDir = homeDir;
    }
    
    public String getHost() {
        return this.host;
    }
    
    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return this.port;
    }
    
    public void setPort(int port) {
        this.port = port;
    }

    public boolean getAutoInstall() {
        return this.autoInstall;
    }

    public void setAutoInstall(boolean autoInstall) {
        this.autoInstall = autoInstall;
    }

    public String getToolUsed() {
        return this.toolUsed;
    }

    public void setToolUsed(String toolUsed) {
        this.toolUsed = toolUsed;
    }
    
    public boolean getAutoLoadSession() {
        return this.autoLoadSession;
    }

    public void setAutoLoadSession(boolean autoLoadSession) {
        this.autoLoadSession = autoLoadSession;
    }

    public String getSessionFilePath() {
        return this.sessionFilePath;
    }

    public void setSessionFilePath(String sessionFilePath) {
        this.sessionFilePath = sessionFilePath;
    }

    public ArrayList<ZAPCmdLine> getCommandLineArgs(){
        return commandLineArgs;
    }
    
    public void setCommandLineArgs(ArrayList<ZAPCmdLine> commandLineArgs){
        this.commandLineArgs = commandLineArgs;
    }

    @Override
    public String getDisplayName() {
        return "My Action";
    }

    @Override
    public String getIconFileName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getUrlName() {
        // TODO Auto-generated method stub
        return null;
    }

}
