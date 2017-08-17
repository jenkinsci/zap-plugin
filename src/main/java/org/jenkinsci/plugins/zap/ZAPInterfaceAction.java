package org.jenkinsci.plugins.zap;

import java.util.ArrayList;
import hudson.model.Action;

/**
 * @author Lenaic Tchokogoue
 * @author Goran Sarenkapa
 *
 */

public class ZAPInterfaceAction implements Action {

    private boolean buildStatus;
    private int timeout;
    private String installationEnvVar;
    private String homeDir;
    private String host;
    private int port;
    private boolean autoInstall;
    private String toolUsed;
    public String sessionFilePath;
    ArrayList<ZAPCmdLine> commandLineArgs;

    public ZAPInterfaceAction() {
        this.buildStatus = false;

        this.timeout = -1;
        this.homeDir = "";
        this.installationEnvVar = "";
        this.host = "";
        this.port = 0;
        this.commandLineArgs = null;
        this.autoInstall = false;
        this.toolUsed= null;
        this.sessionFilePath=null;
    }

    public ZAPInterfaceAction(boolean buildStatus, String toolUsed, boolean autoInstall, String sessionFilePath, int timeout, String installationEnvVar, String homeDir, String host, int port, ArrayList<ZAPCmdLine> commandLineArgs) {
        this.buildStatus = buildStatus;
        this.timeout = timeout;
        this.installationEnvVar = installationEnvVar;
        this.homeDir = homeDir;
        this.host = host;
        this.port = port;
        this.commandLineArgs = commandLineArgs;
        this.autoInstall = autoInstall;
        this.toolUsed= toolUsed;
        this.sessionFilePath = sessionFilePath;

    }
    public boolean getBuildStatus() {
        return this.buildStatus;
    }

    public void setBuildStatus(boolean buildStatus) {
        this.buildStatus = buildStatus;
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

    public ArrayList<ZAPCmdLine> getCommandLineArgs(){
        return commandLineArgs;
    }

    public void setCommandLineArgs(ArrayList<ZAPCmdLine> commandLineArgs){
        this.commandLineArgs = commandLineArgs;
    }

    public String getSessionFilePath(){return this.sessionFilePath;}

    public void setSessionFilePath(String sessionFilePath){this.sessionFilePath = sessionFilePath;}

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
