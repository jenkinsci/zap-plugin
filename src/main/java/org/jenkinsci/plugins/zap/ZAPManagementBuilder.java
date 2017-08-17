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
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.jenkinsci.remoting.RoleChecker;
import org.kohsuke.stapler.DataBoundConstructor;

import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.FilePath.FileCallable;
import hudson.Launcher;
import hudson.Proc;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.remoting.VirtualChannel;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;

/**
 * @author Lenaic Tchokogoue
 * @author Goran Sarenkapa
 * 
 */
public class ZAPManagementBuilder extends Recorder {


    @DataBoundConstructor
    public ZAPManagementBuilder(ZAPManagement management) {
        this.management = management;

        /* Call the set methods of ZAPDriver to set the values */
        // this.zaproxy.setJiraUsername(ZAPBuilder.DESCRIPTOR.getJiraUsername());
        // this.zaproxy.setJiraPassword(ZAPBuilder.DESCRIPTOR.getJiraPassword());
        // ZAPInterfaceAction zapInterface =
        // build.getAction(ZAPInterfaceAction.class);
        //
        // System.out.println("my action timeout: " +
        // zapInterface.getTimeout());
        // System.out.println("my action install dir: " +
        // zapInterface.getInstallationDir());
        // System.out.println("my action home dir: " +
        // zapInterface.getHomeDir());
    }

    private ZAPManagement management;

    public ZAPManagement getManagement() { return management; }

    private Proc proc = null;

    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    @Override /* @Override for better type safety, not needed if plugin doesn't define any property on Descriptor */
    public ZAPManagementBuilderDescriptorImpl getDescriptor() { return (ZAPManagementBuilderDescriptorImpl) super.getDescriptor(); }

    /** Method called when the build is launching 
     * @throws InterruptedException 
     * @throws IOException */
    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws IOException, InterruptedException {
        listener.getLogger().println("[ZAP Jenkins Plugin] POST-BUILD MANAGEMENT");
        ZAPInterfaceAction zapInterface = build.getAction(ZAPInterfaceAction.class);
        if (zapInterface != null) {
            if (zapInterface.getBuildStatus()) {
                System.out.println("----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ");
                System.out.println("my action timeout: " + zapInterface.getTimeout());
                System.out.println("my action install dir: " + zapInterface.getInstallationEnvVar());
                System.out.println("my action home dir: " + zapInterface.getHomeDir());
                System.out.println("my action host: " + zapInterface.getHost());
                System.out.println("my action port: " + zapInterface.getPort());
                System.out.println("my action command line args extra: " + zapInterface.getCommandLineArgs().size());
                System.out.println("my action autoinstall: " + zapInterface.getAutoInstall());
                System.out.println("my action tool used: " + zapInterface.getToolUsed());
                System.out.println("my action session path: " + zapInterface.getSessionFilePath());
                System.out.println("----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ");
                for (int i = 0; i < zapInterface.getCommandLineArgs().size(); i++) {
                    System.out.println(zapInterface.getCommandLineArgs().get(i));
                }
                this.management.setBuildStatus(zapInterface.getBuildStatus());
                this.management.setTimeout(zapInterface.getTimeout());
                this.management.setInstallationEnvVar(zapInterface.getInstallationEnvVar());
                this.management.setHomeDir(zapInterface.getHomeDir());
                this.management.setHost(zapInterface.getHost());
                this.management.setPort(zapInterface.getPort());
                this.management.setCommandLineArgs(zapInterface.getCommandLineArgs());
                this.management.setAutoInstall(zapInterface.getAutoInstall());
                this.management.setToolUsed(zapInterface.getToolUsed());
                this.management.setSessionFilePath(zapInterface.getSessionFilePath());
            }
        }
        else {
            /* THE DEFAULT CONSTRUCTOR IS NEVER TRIGGER, DERP DERP */
            Utils.lineBreak(listener);
            Utils.loggerMessage(listener, 0, "[{0}] THERE IS NO BUILD STEP, MARKED AS FAILURE", Utils.ZAP);
            Utils.lineBreak(listener);
            return false;
        }
        try {
            Utils.lineBreak(listener);
            Utils.loggerMessage(listener, 0, "[{0}] START BUILD STEP", Utils.ZAP);
            Utils.lineBreak(listener);
            listener.getLogger().println("management: " + management);
            listener.getLogger().println("build: " + build);
            listener.getLogger().println("listener: " + listener);
            listener.getLogger().println("launcher: " + launcher);
            
            proc = management.startZAP(build, listener, launcher);
            listener.getLogger().println("after proc assignment");
        }
        catch (Exception e) {
            listener.getLogger().println("we failed");
            e.printStackTrace();
            listener.error(ExceptionUtils.getStackTrace(e));
            return false;
        }
        
        Result res;
        try {
            res = build.getWorkspace().act(new ZAPManagementCallable(listener, this.management));
            build.setResult(res);
            proc.joinWithTimeout(60L, TimeUnit.MINUTES, listener);
            Utils.lineBreak(listener);
            Utils.lineBreak(listener);
            Utils.loggerMessage(listener, 0, "[{0}] SHUTDOWN [ SUCCESSFUL ]", Utils.ZAP);
            Utils.lineBreak(listener);
        }
        catch (Exception e) {
            e.printStackTrace();
            listener.error(ExceptionUtils.getStackTrace(e));
            return false;
        }
        // EnvVars abc = build.getEnvironment(listener);
        // printMap(abc);
        return res.completeBuild;
    }
    private void printMap(Map mp) {
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }
    }
    @Extension
    public static final class ZAPManagementBuilderDescriptorImpl extends BuildStepDescriptor<Publisher> {

        /* In order to load the persisted global configuration, you have to call load() in the constructor. */
        public ZAPManagementBuilderDescriptorImpl() { load(); }

        /* Indicates that this builder can be used with all kinds of project types */
        @SuppressWarnings("rawtypes")
        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) { return true; }

        /* This human readable name is used in the configuration screen. */
        @Override
        public String getDisplayName() { return Messages.jenkins_jobconfig_addpostbuild_zap(); }
    }

    /**
     * Used to execute ZAP remotely.
     */
    private static class ZAPManagementCallable implements FileCallable<Result> {

        private static final long serialVersionUID = 1L;
        private BuildListener listener;
        private ZAPManagement management;

        public ZAPManagementCallable(BuildListener listener, ZAPManagement management) {
            this.listener = listener;
            this.management = management;
        }

        @Override
        public Result invoke(File f, VirtualChannel channel) { return management.executeZAP(listener, new FilePath(f)); }

        @Override
        public void checkRoles(RoleChecker checker) throws SecurityException { /* N/A */ }
    }
}
