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
    ArrayList<ZAPCmdLine> commandLineArgs;
    
    public ZAPInterfaceAction() {
        this.buildStatus = false;

        this.timeout = -1;
        this.homeDir = "";
        this.installationEnvVar = "";
        this.host = "";
        this.port = 0;
        this.commandLineArgs = null;
        System.out.println();
        System.out.println("timeout: " + timeout);
        System.out.println("homeDir: " + homeDir);
        System.out.println("installationEnv: " + installationEnvVar);
    }

    public ZAPInterfaceAction(boolean buildStatus, String hello, int low, ClientApi i, int timeout, String installationEnvVar, String homeDir, String host, int port, ArrayList<ZAPCmdLine> commandLineArgs) {
        this.buildStatus = buildStatus;
        this.timeout = timeout;
        this.installationEnvVar = installationEnvVar;
        this.homeDir = homeDir;
        this.host = host;
        this.port = port;
        this.commandLineArgs = commandLineArgs;
        System.out.println();
        System.out.println("timeout: " + timeout);
        System.out.println("homeDir: " + homeDir);
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
