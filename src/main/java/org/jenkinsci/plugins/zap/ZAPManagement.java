/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Goran Sarenkapa (JordanGS), and a number of other of contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jenkinsci.plugins.zap;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import hudson.model.*;
import hudson.remoting.VirtualChannel;
import hudson.slaves.SlaveComputer;
import jenkins.model.Jenkins;

import org.kohsuke.stapler.DataBoundConstructor;
import org.zaproxy.clientapi.core.ApiResponseList;
import org.zaproxy.clientapi.core.ApiResponseSet;
import org.zaproxy.clientapi.core.ClientApi;
import org.zaproxy.clientapi.core.ClientApiException;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.tools.ant.BuildException;
import org.jenkinsci.remoting.RoleChecker;

import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.Proc;
import hudson.FilePath.FileCallable;

/**
 * Contains methods to start and execute ZAPZAPManagement. Members variables are
 * bound to the config.jelly placed to
 * {@link "com/github/jenkinsci/zaproxyplugin/ZAPZAPManagement/config.jelly"}
 *
 * @author Goran Sarenkapa
 * @author Mostafa AbdelMoez
 * @author Tanguy de Lignières
 * @author Abdellah Azougarh
 * @author Thilina Madhusanka
 * @author Johann Ollivier-Lapeyre
 * @author Ludovic Roucoux
 *
 * @see <a href=
 *      "https://github.com/zaproxy/zap-api-java/tree/master/subprojects/zap-clientapi">
 *      [JAVA] Client API</a> The pom should show the artifact being from maven
 *      central.
 */
public class ZAPManagement extends AbstractDescribableImpl<ZAPManagement> implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String API_KEY = "ZAPROXY-PLUGIN";

    /* ZAP executable files */
    private static final String ZAP_PROG_NAME_BAT = "zap.bat";
    private static final String ZAP_PROG_NAME_SH = "zap.sh";

    @DataBoundConstructor
    public ZAPManagement(boolean buildThresholds, int hThresholdValue, int hSoftValue, int mThresholdValue,
            int mSoftValue, int lThresholdValue, int lSoftValue, int iThresholdValue, int iSoftValue, int cumulValue) {
        /* Post Build Step */
        this.buildThresholds = buildThresholds;
        this.hThresholdValue = hThresholdValue;
        this.hSoftValue = hSoftValue;
        this.mThresholdValue = mThresholdValue;
        this.mSoftValue = mSoftValue;
        this.lThresholdValue = lThresholdValue;
        this.lSoftValue = lSoftValue;
        this.iThresholdValue = iThresholdValue;
        this.iSoftValue = iSoftValue;
        this.cumulValue = cumulValue;
        System.out.println(this.toString());
    }

    /**
     * Evaluated values will return null, this is printed to console on save
     *
     * @return
     */
    @Override
    public String toString() {
        String s = "\n\npost build to string\n\n";
        return s;
    }

    public Proc startZAP(AbstractBuild<?, ?> build, BuildListener listener, Launcher launcher) throws IllegalArgumentException, IOException, InterruptedException {
        System.out.println("");
        System.out.println("INITIATING POST BUILD");
        System.out.println("");
        System.out.println("");

        if (this.buildStatus == false){
            Utils.loggerMessage(listener, 0, "[{0}] WE GOT FALSE SO WE BREAK", Utils.ZAP);
            System.out.println("WE GOT FALSE SO WE BREAK");
            return null;
        }
        System.out.println("WE GOT TRUE SO WE INIT");
        ZAP zap = new ZAP(build, listener, launcher, this.getTimeout(), this.getInstallationEnvVar(), this.getHomeDir(), this.getHost(), this.getPort(), this.getCommandLineArgs());

        this.zapInstallationDir = zap.getInstallationDir();
        zap.checkParams(this.zapInstallationDir);

        System.out.println("startZAP list -------------");
        for(int i = 0; i < this.getCommandLineArgs().size(); i++) {
            System.out.println(this.getCommandLineArgs().get(i));
        }
        
        /* Command to start ZAProxy with parameters */
        zap.setCommand();

        System.out.println("new list -------------");
        for(int i = 0; i < zap.getCommand().size(); i++) {
            //System.out.println(zap.getCommand().get(i));
        }

        EnvVars envVars = build.getEnvironment(listener);
        
        System.out.println("");
        System.out.println("");
        System.out.println("BEFORE SETTING JDK");
        zap.setBuildJDK();

        /*
         * Launch ZAP process on remote machine (on master if no remote machine)
         */
//        Utils.loggerMessage(listener, 0, "[{0}] EXECUTE LAUNCH COMMAND", Utils.ZAP);
//        Proc proc = launcher.launch().cmds(zap.getCommand()).envs(envVars).stdout(listener).pwd(workDir).start();
//
//        /* Call waitForSuccessfulConnectionToZap(int, BuildListener) remotely */
//        Utils.lineBreak(listener);
//        Utils.loggerMessage(listener, 0, "[{0}] INITIALIZATION [ START ]", Utils.ZAP);
//        build.getWorkspace().act(new WaitZAPManagementInitCallable(listener, this));
//        Utils.lineBreak(listener);
//        Utils.loggerMessage(listener, 0, "[{0}] INITIALIZATION [ SUCCESSFUL ]", Utils.ZAP);
//        Utils.lineBreak(listener);
//        return proc;
        return zap.launch();
    }
 
    /**
     * Wait for ZAP's initialization such that it is ready to use at the end of
     * the method, otherwise catch the exception. If there is a remote machine,
     * then this method will be launched there.
     *
     * @param listener
     *            of type BuildListener: the display log listener during the
     *            Jenkins job execution.
     * @param timeout
     *            of type int: the time in seconds to try to connect to ZAP.
     * @see <a href=
     *      "https://groups.google.com/forum/#!topic/zaproxy-develop/gZxYp8Og960">
     *      [JAVA] Avoid sleep to wait ZAProxy initialization</a>
     */


    /**
     * Execute ZAPDriver method following build's setup and stop ZAP at the end.
     * Note: No param's to executeZAP method since they would also need to be
     * accessible in Builder, somewhat redundant.
     *
     * @param listener
     *            of type BuildListener: the display log listener during the
     *            Jenkins job execution.
     * @param workspace
     *            of type FilePath: a {@link FilePath} representing the build's
     *            workspace.
     * @return of type: boolean DESC: true if no exception is caught, false
     *         otherwise.
     */
    public boolean executeZAP(BuildListener listener, FilePath workspace) {
        listener.getLogger().println("executeZAP");
        boolean buildSuccess = true;

        /*
         * Check to make sure that plugin's are installed with ZAP if they are
         * selected in the UI.
         */
        listener.getLogger().println("host: " + host);
        listener.getLogger().println("port: " + port);
        ClientApi clientApi = new ClientApi(host, port, API_KEY);

        try {
            if (buildSuccess) {
            }
        } catch (Exception e) {
            listener.error(ExceptionUtils.getStackTrace(e));
            buildSuccess = false;
        } finally {
            try {
                stopZAP(listener, clientApi);
            } catch (ClientApiException e) {
                listener.error(ExceptionUtils.getStackTrace(e));
                buildSuccess = false;
            }
        }
        Utils.lineBreak(listener);
        return buildSuccess;
    }

    /**
     * ManageThreshold define build value failed, pass , unstable.
     *
     * @param listener
     *            of type BuildListener: the display log listener during the
     *            Jenkins job execution.
     * @param clientApi
     *            of type ClientApi: the ZAP client API to call method.
     * @param hThresholdValue
     *            of type int: the Weight of the alert severity high.
     * @param hSoftValue
     *            of type int: the threshold of the alert severity high.
     * @param mThresholdValue
     *            of type int: the Weight of the alert severity meduim.
     * @param mSoftValue
     *            of type int: the threshold of the alert severity meduim.
     * @param lThresholdValue
     *            of type int: the Weight of the alert severity low.
     * @param lSoftValue
     *            of type int: the threshold of the alert severity low.
     * @param iThresholdValue
     *            of type int: the Weight of the alert severity informational.
     * @param iSoftValue
     *            of type int: the threshold of the alert severity
     *            informational.
     * @param cumulValue
     *            of type int: the cumulative threshold of the alerts.
     *
     */

    private Result ManageThreshold(BuildListener listener, ClientApi clientApi, int hThresholdValue, int hSoftValue,
            int mThresholdValue, int mSoftValue, int lThresholdValue, int lSoftValue, int iThresholdValue,
            int iSoftValue, int cumulValue) throws ClientApiException, IOException {

        Utils.lineBreak(listener);
        Utils.loggerMessage(listener, 0, "START : COMPUTE THRESHOLD", Utils.ZAP);
        Result buildStatus = Result.SUCCESS;

        Utils.lineBreak(listener);
        int nbAlertHigh = countAlertbySeverity(clientApi, "High");
        Utils.loggerMessage(listener, 1, "ALERTS High COUNT [ {1} ]", Utils.ZAP, Integer.toString(nbAlertHigh));

        int nbAlertMedium = countAlertbySeverity(clientApi, "Medium");
        Utils.loggerMessage(listener, 1, "ALERTS Medium COUNT [ {1} ]", Utils.ZAP, Integer.toString(nbAlertMedium));

        int nbAlertLow = countAlertbySeverity(clientApi, "Low");
        // setLowAlert(nbAlertLow);
        Utils.loggerMessage(listener, 1, "ALERTS Low COUNT [ {1} ]", Utils.ZAP, Integer.toString(nbAlertLow));

        int nbAlertInfo = countAlertbySeverity(clientApi, "Informational");
        Utils.loggerMessage(listener, 1, "ALERTS Informational COUNT [ {1} ]", Utils.ZAP,
                Integer.toString(nbAlertInfo));
        int count = 0;

        int hScale = computeProduct(hThresholdValue, nbAlertHigh);
        int mScale = computeProduct(mThresholdValue, nbAlertMedium);
        int lScale = computeProduct(lThresholdValue, nbAlertLow);
        int iScale = computeProduct(iThresholdValue, nbAlertInfo);

        if ((hScale > hSoftValue) || (mScale > mSoftValue) || (lScale > lSoftValue) || (iScale > iSoftValue)) {
            count++;
        }
        if ((hScale + mScale + lScale + iScale) > cumulValue) {
            count++;
        }

        if (count == 1) {
            buildStatus = Result.UNSTABLE;
        }
        if (count == 2) {
            buildStatus = Result.FAILURE;
        }

        Utils.loggerMessage(listener, 0, "END : COMPUTING THRESHOLD", Utils.ZAP);

        return buildStatus;

    }

    /**
     * computeProduct do the product of two Integer.
     *
     * @param a
     *            of type Integer.
     * @param b
     *            of type Integer.
     */
    public int computeProduct(int a, int b) {
        int res;
        res = a * b;
        return res;
    }

    /**
     * countAlertbySeverity count the number of alert by severity.
     *
     * @param clientApi
     *            of type ClientApi: the ZAP client API to call method.
     * @param risk
     *            of type string : it's the alert severity.
     */
    public int countAlertbySeverity(ClientApi clientApi, String risk) throws ClientApiException {
        int nbAlert = 0;
        List<String> tempid = new ArrayList<String>();
        tempid.add("begin");

        List allAlerts1 = ((ApiResponseList) clientApi.core.alerts("", "", "")).getItems();
        for (int i = 0; i < allAlerts1.size(); i++) {
            ApiResponseSet temp = ((ApiResponseSet) allAlerts1.get(i));
            if (!tempid.contains(temp.getValue("alert").toString())) {
                if (risk.equals(temp.getValue("risk").toString())) {
                    nbAlert++;
                }
            }
            tempid.add(temp.getValue("alert").toString());
        }

        return nbAlert;
    }

    /**
     * Stop ZAP if it has been previously started.
     * 
     * @param listener
     *            of type BuildListener: the display log listener during the
     *            Jenkins job execution.
     * @param clientApi
     *            of type ClientApi: the ZAP client API to call method.
     * @throws ClientApiException
     */
    private void stopZAP(BuildListener listener, ClientApi clientApi) throws ClientApiException {
        if (clientApi != null) {
            Utils.lineBreak(listener);
            Utils.loggerMessage(listener, 0, "[{0}] SHUTDOWN [ START ]", Utils.ZAP);
            Utils.lineBreak(listener);
            /**
             * @class ApiResponse org.zaproxy.clientapi.gen.Core
             *
             * @method shutdown
             *
             * @param String
             *            apikey
             *
             * @throws ClientApiException
             */
            clientApi.core.shutdown();
        } else
            Utils.loggerMessage(listener, 0, "[{0}] SHUTDOWN [ ERROR ]", Utils.ZAP);
    }

    @Extension
    public static class ZAPManagementDescriptorImpl extends Descriptor<ZAPManagement> implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * To persist global configuration information, simply store it in a
         * field and call save().
         *
         * <p>
         * If you don't want fields to be persisted, use <tt>transient</tt>.
         */

        /** Represents the build's workspace */
        private FilePath workspace;

        public void setWorkspace(FilePath ws) {
            this.workspace = ws;
        }

        @Override
        public String getDisplayName() {
            return null;
        }

        /**
         * In order to load the persisted global configuration, you have to call
         * load() in the constructor.
         */
        public ZAPManagementDescriptorImpl() {
            load();
        }
    }

    /*
     * Variable Declaration Getters allows to load members variables into UI.
     * Setters
     */
    @Override
    public ZAPManagementDescriptorImpl getDescriptor() {
        return (ZAPManagementDescriptorImpl) super.getDescriptor();
    }

    /*
     * Overridden for better type safety. If your plugin doesn't really define
     * any property on Descriptor, you don't have to do this.
     */

    private String contextId; /* ID of the created context. */

    private String userId; /* ID of the created user. */

    /* Post Build Step >> Manage Threshold */

    private int nbAlertLow;

    private void setLowAlert(int a) {
        nbAlertLow = a;
    }

    public int getLowAlert() {
        return nbAlertLow;
    }

    // private final String jdk; /* The IDK to use to start ZAP. */
    //
    // public String getJdk() { return jdk; }
    //
    // /* Gets the JDK that this Sonar builder is configured with, or null. */
    // public JDK getJDK() { return Jenkins.getInstance().getJDK(jdk); }

    // private String zapProgram; /* Path to the ZAP security tool. */

    private String zapInstallationDir;

    private final boolean buildThresholds;

    public boolean isbuildThresholds() {
        return buildThresholds;
    }

    private final int hThresholdValue;

    public int gethThresholdValue() {
        return hThresholdValue;
    }

    private final int hSoftValue;

    public int gethSoftValue() {
        return hSoftValue;
    }

    private final int mThresholdValue;

    public int getmThresholdValue() {
        return mThresholdValue;
    }

    private final int mSoftValue;

    public int getmSoftValue() {
        return mSoftValue;
    }

    private final int lThresholdValue;

    public int getlThresholdValue() {
        return lThresholdValue;
    }

    private final int lSoftValue;

    public int getlSoftValue() {
        return lSoftValue;
    }

    private final int iThresholdValue;

    public int getiThresholdValue() {
        return iThresholdValue;
    }

    private final int iSoftValue;

    public int getiSoftValue() {
        return iSoftValue;
    }

    private final int cumulValue;

    public Boolean execute;

    public int getcumulValue() {
        return cumulValue;
    }

    /*****************************/

    private boolean buildStatus;
    private int timeout;
    private String installationEnvVar;
    private String homeDir;
    private String host;
    private int port;

    public void setBuildStatus(boolean buildStatus) {
        this.buildStatus = buildStatus;
    }

    private int getTimeout() {
        return this.timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    private String getInstallationEnvVar() {
        return this.installationEnvVar;
    }

    public void setInstallationEnvVar(String installationEnvVar) {
        this.installationEnvVar = installationEnvVar;
    }

    private String getHomeDir() {
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

    private ArrayList<ZAPCmdLine> commandLineArgs; /* List of all ZAP command lines specified by the user ArrayList because it needs to be Serializable (whereas List is not Serializable). */

    public ArrayList<ZAPCmdLine> getCommandLineArgs() { return commandLineArgs; }

    public void setCommandLineArgs(ArrayList<ZAPCmdLine> commandLineArgs) { this.commandLineArgs = commandLineArgs; }
}
