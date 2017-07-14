package org.jenkinsci.plugins.zap;

import java.util.ArrayList;
import hudson.model.Action;

public class ZAPInterfaceAction implements Action {

    private boolean buildStatus;
    private String installationEnvVar;
    private String homeDir;
    private String host;
    private int port;
    private boolean autoInstall;
    private String toolUsed;
    private String sessionFilePath;
    private int timeout;
    private boolean autoLoadSession;
    ArrayList<ZAPCmdLine> commandLineArgs;

    public ZAPInterfaceAction() {
        this.buildStatus = false;

        this.homeDir = "";
        this.installationEnvVar = "";
        this.host = "";
        this.port = 0;

        this.autoInstall = false;
        this.toolUsed = null;
        this.sessionFilePath = null;
        this.timeout = -1;

        this.commandLineArgs = null;
    }

    public ZAPInterfaceAction(boolean buildStatus, String homeDir, String host, int port, boolean autoInstall,
            String toolUsed, String installationEnvVar, int timeout, String sessionFilePath,
            ArrayList<ZAPCmdLine> commandLineArgs) {
        this.buildStatus = buildStatus;
        this.homeDir = homeDir;
        this.host = host;
        this.port = port;
        this.autoInstall = autoInstall;
        this.toolUsed = toolUsed;
        this.installationEnvVar = installationEnvVar;
        this.timeout = timeout;
        this.sessionFilePath = sessionFilePath;
        this.commandLineArgs = commandLineArgs;

    }

    public boolean getBuildStatus() {
        return this.buildStatus;
    }

    public void setBuildStatus(boolean buildStatus) {
        this.buildStatus = buildStatus;
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

    public String getInstallationEnvVar() {
        return this.installationEnvVar;
    }

    public void setInstallationEnvVar(String installationEnvVar) {
        this.installationEnvVar = installationEnvVar;
    }

    public int getTimeout() {
        return this.timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
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

    public ArrayList<ZAPCmdLine> getCommandLineArgs() {
        return commandLineArgs;
    }

    public void setCommandLineArgs(ArrayList<ZAPCmdLine> commandLineArgs) {
        this.commandLineArgs = commandLineArgs;
    }

    @Override
    public String getDisplayName() {
        return "ZAP Post-build Action";
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
