package org.jenkinsci.plugins.zap;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import hudson.model.*;
import hudson.remoting.VirtualChannel;
import hudson.slaves.NodeSpecific;
import hudson.slaves.SlaveComputer;
import hudson.tools.ToolDescriptor;
import hudson.tools.ToolInstallation;
import jenkins.model.Jenkins;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.tools.ant.BuildException;
import org.jenkinsci.remoting.RoleChecker;

import hudson.EnvVars;
import hudson.FilePath;
import hudson.Launcher;
import hudson.Proc;
import hudson.FilePath.FileCallable;

/**
 *
 * @author Goran Sarenkapa
 * @author Mostafa AbdelMoez
 * @author Tanguy de Lignières
 * @author Abdellah Azougarh
 * @author Thilina Madhusanka
 * @author Johann Ollivier-Lapeyre
 * @author Ludovic Roucoux
 *
 */

public class ZAP extends AbstractDescribableImpl<ZAP> implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String API_KEY = "ZAPROXY-PLUGIN";

    /* Command Line Options - Not exposed through the API */
    private static final String CMD_LINE_DIR = "-dir";
    private static final String CMD_LINE_HOST = "-host";
    private static final String CMD_LINE_PORT = "-port";
    private static final String CMD_LINE_DAEMON = "-daemon";
    private static final String CMD_LINE_CONFIG = "-config";
    private static final String CMD_LINE_API_KEY = "api.key";

    private int timeout;
    private String installationEnvVar;
    private String homeDir;
    private AbstractBuild<?, ?> build;
    private BuildListener listener;
    private Launcher launcher;
    private String host;
    private int port;
    private boolean autoInstall;
    private String toolUsed;
    private List<String> command = new ArrayList<String>();
    private ArrayList<ZAPCmdLine> commandLineArgs;
    private EnvVars envVars;
    
    /* ZAP executable files */
    private static final String ZAP_PROG_NAME_BAT = "zap.bat";
    private static final String ZAP_PROG_NAME_SH = "zap.sh";

    public ZAP(AbstractBuild<?, ?> build, BuildListener listener, Launcher launcher, String homeDir,
            String host, int port, boolean autoInstall, String toolUsed, String installationEnvVar, int timeout, ArrayList<ZAPCmdLine> commandLineArgs) throws IOException, InterruptedException {
        this.build = build;
        this.listener = listener;
        this.launcher = launcher;
        this.homeDir = homeDir;
        this.host = host;
        this.port = port;
        this.autoInstall = autoInstall;
        this.toolUsed = toolUsed;
        this.installationEnvVar = installationEnvVar;
        this.timeout = timeout;
        this.commandLineArgs = commandLineArgs;
        this.envVars = build.getEnvironment(listener);;
        System.out.println(this.toString());
    }

    @Override
    public String toString() {
        String s = "";
        s += "Something";
        s += "\n";
        return s;
    }

    /**
     * Get the ZAP_HOME setup by Custom Tools Plugin or already present on the build's machine.
     *
     * @param build
     * @param listener
     *            of type BuildListener: the display log listener during the Jenkins job execution.
     * @return of type String: the installed tool location, without zap.bat or zap.sh at the end.
     * @throws InterruptedException
     * @throws IOException
     * @see <a href= "https://groups.google.com/forum/#!topic/jenkinsci-dev/RludxaYjtDk"> https://groups.google.com/forum/#!topic/jenkinsci-dev/RludxaYjtDk </a>
     */
    //AbstractBuild<?, ?> build, BuildListener listener
    public String getInstallationDir() throws IOException, InterruptedException {
        String installPath = null;

        if (this.autoInstall) {
            Node node = build.getBuiltOn();
            for (ToolDescriptor<?> desc : ToolInstallation.all())
                for (ToolInstallation tool : desc.getInstallations())
                    if (tool.getName().equals(this.toolUsed)) {
                        if (tool instanceof NodeSpecific) tool = (ToolInstallation) ((NodeSpecific<?>) tool).forNode(node, listener);
                        if (tool instanceof EnvironmentSpecific) tool = (ToolInstallation) ((EnvironmentSpecific<?>) tool).forEnvironment(this.envVars);
                        installPath = tool.getHome();
                        return installPath;
                    }
        }
        else installPath = build.getEnvironment(listener).get(this.installationEnvVar);
        return installPath;
    }

    public String getAppName() throws IOException, InterruptedException {
        Node node = build.getBuiltOn();
        String appName = "";

        /* Determine if it's Master/Slave and use appropriate bracket depending on the environment (Windows/Unix) and append the application name */
        if ("".equals(node.getNodeName())) { // Master
            appName = ((File.pathSeparatorChar == ':') ? ("/" + ZAP_PROG_NAME_SH) : ("\\" + ZAP_PROG_NAME_BAT));
        } else if ("Unix".equals(((SlaveComputer) node.toComputer()).getOSDescription()))
            appName = "/" + ZAP_PROG_NAME_SH;
        else
            appName = "\\" + ZAP_PROG_NAME_BAT;
        return appName;
    }

    public FilePath getWorkspace(){
        FilePath workspace = build.getWorkspace();
        if (workspace == null) {
            Node node = build.getBuiltOn();
            if (node == null)
                throw new NullPointerException("No such build node: " + build.getBuiltOnStr());
            throw new NullPointerException("No workspace from node " + node + " which is computer " + node.toComputer() + " and has channel " + node.getChannel());
        }
        return workspace;
    }

    public FilePath getAppPath() throws IOException, InterruptedException {
        FilePath fp =  new FilePath(getWorkspace().getChannel(), getInstallationDir() + getAppName());
        Utils.loggerMessage(listener, 0, "[{0}] CONFIGURE RUN COMMANDS for [ {1} ]", Utils.ZAP, fp.getRemote());
        return fp;
    }

    // do NOT getInstallationDir since we do not want to call the method multiple times :/
    public void checkParams(String installationDir) throws IllegalArgumentException, IOException, InterruptedException {
        Utils.loggerMessage(listener, 0, "[{0}] PLUGIN VALIDATION (PLG), VARIABLE VALIDATION AND ENVIRONMENT INJECTOR EXPANSION (EXP)", Utils.ZAP);

        if (installationDir == null || installationDir.isEmpty())
            throw new IllegalArgumentException("ZAP INSTALLATION DIRECTORY IS MISSING, PROVIDED [ " + installationDir + " ]");
        else
            Utils.loggerMessage(listener, 1, "ZAP INSTALLATION DIRECTORY = [ {0} ]", installationDir);
    }

    public List<String> getCommand() {
        return this.command;
    }

    public void setCommand() throws IOException, InterruptedException {
        this.command.add(getAppPath().getRemote());
        this.command.add(CMD_LINE_DAEMON);
        this.command.add(CMD_LINE_HOST);
        this.command.add(host);
        this.command.add(CMD_LINE_PORT);
        this.command.add(Integer.toString(port));
        this.command.add(CMD_LINE_CONFIG);
        this.command.add(CMD_LINE_API_KEY + "=" + API_KEY);
        this.command.add(CMD_LINE_DIR);
        this.command.add(homeDir);
        
        System.out.println("this.command: " + this.command.size());
        System.out.println("this.commandLineArgs: " + this.commandLineArgs.size());
        /* Adds command line arguments if it's provided */
        if (!this.commandLineArgs.isEmpty()) addZapCmdLine(this.command, this.commandLineArgs);
    }

    /**
     * Add list of command line to the list in param
     *
     * @param list
     *            of type List<String>: the list to attach ZAP command line to.
     * @param cmdList
     *            of type ArrayList<ZAPCmdLine>: the list of ZAP command line options and their values.
     */
    private void addZapCmdLine(List<String> list, ArrayList<ZAPCmdLine> cmdList) {
        System.out.println("extra: " + cmdList.size());
        System.out.println("original: " + list.size());
        for (ZAPCmdLine zapCmd : cmdList) {
            if (zapCmd.getCmdLineOption() != null && !zapCmd.getCmdLineOption().isEmpty()) list.add(zapCmd.getCmdLineOption());
            if (zapCmd.getCmdLineValue() != null && !zapCmd.getCmdLineValue().isEmpty()) list.add(zapCmd.getCmdLineValue());
        }
    }

    // If the JDK was changed through the job's configurations, then update the Build's Environment Variables, can now remove the Java field to save space. Important, check if the 1.6 LTS is working with this or not.
    public void setBuildJDK() throws IOException, InterruptedException {
        EnvVars envVars = getEnvVars();

        System.out.println("setBuildJDK INSIDE: " + build.getBuildVariables().entrySet().size());

        for (Map.Entry<String, String> e : build.getBuildVariables().entrySet()) {
            envVars.put(e.getKey(), e.getValue());
            System.out.println("KEY: " + e.getKey() + "VALUE: " + e.getValue());
        }

        System.out.println("INSIDE THE SETTER");
        AbstractProject<?, ?> project = build.getProject();
        //JDK 8u131 test
        System.out.println("project.getJDK()");
        JDK jdk = project.getJDK();
        System.out.println("JDK: " + (jdk == null));

        System.out.println("Jenkins.getInstance().getJDK(\"JDK 8u131 test\")");
        jdk = Jenkins.getInstance().getJDK("JDK 8u131 test");
        System.out.println("JDK: " + (jdk == null));

        if (jdk != null) {
            // System.out.println("JDK: " + jdkToUse.getHome());
            // System.out.println("JDK: " + jdkToUse.getName());
            Computer computer = Computer.currentComputer();
            /* just in case we are not in a build */
            if (computer != null)
                jdk = jdk.forNode(computer.getNode(), listener);
            jdk.buildEnvVars(envVars);
        }
    }

    private EnvVars getEnvVars () throws IOException, InterruptedException {
        return this.envVars;
    }

    public Proc launch () throws IOException, InterruptedException {
        FilePath workDir = new FilePath(getWorkspace().getChannel(), getInstallationDir());

        Utils.loggerMessage(listener, 0, "[{0}] EXECUTE LAUNCH COMMAND", Utils.ZAP);
        Proc proc = launcher.launch().cmds(getCommand()).envs(envVars).stdout(listener).pwd(workDir).start();

        Utils.lineBreak(listener);
        Utils.loggerMessage(listener, 0, "[{0}] INITIALIZATION [ START ]", Utils.ZAP);
        build.getWorkspace().act(new WaitZAPInitCallable(listener, this));
        Utils.lineBreak(listener);
        Utils.loggerMessage(listener, 0, "[{0}] INITIALIZATION [ SUCCESSFUL ]", Utils.ZAP);
        Utils.lineBreak(listener);
        return proc;

    }

    private void waitForSuccessfulConnectionToZap(BuildListener listener) {
        int timeoutInMs = (int) TimeUnit.SECONDS.toMillis(timeout);
        int connectionTimeoutInMs = timeoutInMs;
        int pollingIntervalInMs = (int) TimeUnit.SECONDS.toMillis(1);
        boolean connectionSuccessful = false;
        long startTime = System.currentTimeMillis();
        Socket socket = null;
        do
            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress(host, port), connectionTimeoutInMs);
                connectionSuccessful = true;
            }
        catch (SocketTimeoutException ignore) {
            listener.error(ExceptionUtils.getStackTrace(ignore));
            throw new BuildException("Unable to connect to ZAP's proxy after " + timeout + " seconds.");

        }
        catch (IOException ignore) {
            /* Try again but wait some time first */
            try {
                Thread.sleep(pollingIntervalInMs);
            }
            catch (InterruptedException e) {
                listener.error(ExceptionUtils.getStackTrace(ignore));
                throw new BuildException("The task was interrupted while sleeping between connection polling.", e);
            }

            long ellapsedTime = System.currentTimeMillis() - startTime;
            if (ellapsedTime >= timeoutInMs) {
                listener.error(ExceptionUtils.getStackTrace(ignore));
                throw new BuildException("Unable to connect to ZAP's proxy after " + timeout + " seconds.");
            }
            connectionTimeoutInMs = (int) (timeoutInMs - ellapsedTime);
        }
        finally {
            if (socket != null) try {
                socket.close();
            }
            catch (IOException e) {
                listener.error(ExceptionUtils.getStackTrace(e));
            }
        }
        while (!connectionSuccessful);
    }

    private static class WaitZAPInitCallable implements FileCallable<Void> {

        private static final long serialVersionUID = 1L;

        private BuildListener listener;
        private ZAP zaproxy;

        public WaitZAPInitCallable(BuildListener listener, ZAP zaproxy) {
            this.listener = listener;
            this.zaproxy = zaproxy;
        }

        @Override
        public Void invoke(File f, VirtualChannel channel) {
            zaproxy.waitForSuccessfulConnectionToZap(listener);
            return null;
        }

        @Override
        public void checkRoles(RoleChecker checker) throws SecurityException {
            /* N/A */ }
    }
}
