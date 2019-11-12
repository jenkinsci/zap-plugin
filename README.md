[![Release](https://img.shields.io/github/release/jenkinsci/zap-plugin.svg)](https://github.com/jenkinsci/zap-plugin/releases)
[![Jenkins](https://ci.jenkins.io/job/Plugins/job/zap-plugin/job/master/badge/icon)](https://ci.jenkins.io/job/Plugins/job/zap-plugin/job/master/)
[![Coverity](https://scan.coverity.com/projects/10817/badge.svg)](https://scan.coverity.com/projects/jenkinsci-zap-plugin)
[![Best Practices](https://bestpractices.coreinfrastructure.org/projects/490/badge)](https://bestpractices.coreinfrastructure.org/projects/490)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](https://github.com/jenkinsci/zap-plugin/blob/master/LICENSE)

<br />

<a href='https://www.owasp.org/index.php/OWASP_Zed_Attack_Proxy_Project' align="top"><img align="left" src='https://www.owasp.org/images/1/11/Zap128x128.png'></a>

<br />

[OWASP Zed Attack Proxy Jenkins Plugin](https://wiki.jenkins-ci.org/display/JENKINS/zap+plugin)
==============================================

<br />

The OWASP Zed Attack Proxy (<b>[ZAP](https://www.owasp.org/index.php/OWASP_Zed_Attack_Proxy_Project)</b>) is one of the world’s most popular free security tools and is actively maintained by hundreds of international volunteers. It can help you automatically find security vulnerabilities in your web applications while you are developing and testing your applications. Its also a great tool for experienced pentesters to use for manual security testing.

The <b>[OWASP ZAP Jenkins Plugin](https://wiki.jenkins-ci.org/display/JENKINS/zap+plugin)</b> extends the functionality of the <b>[ZAP](https://www.owasp.org/index.php/OWASP_Zed_Attack_Proxy_Project)</b> security tool into a CI Environment.

  - Manage Sessions (Load or Persist)
  - Define Context (Name, Include URLs and Exclude URLs)
  - Attack Contexts (Spider Scan, AJAX Spider, Active Scan) 

You can also:
  - Setup Authentication (Form Based or Script Based)
  - Run as Pre-Build as part of a <b>[Selenium](http://docs.seleniumhq.org/)</b> Build
  - Generate Reports (**.xhtml** [![XHTML](https://wiki.jenkins-ci.org/download/attachments/102662470/html.png)](http://www.w3schools.com/html/html_xhtml.asp), **.xml** [![XML](https://wiki.jenkins-ci.org/download/attachments/102662470/xml.png)](http://www.w3schools.com/xml/default.asp), **.json** [![JSON](https://wiki.jenkins-ci.org/download/attachments/102662470/json.png)](http://www.w3schools.com/js/js_json_intro.asp))

### Questions/Troubleshooting

* Please use the <b>[Google Group](https://groups.google.com/forum/#!forum/zaproxy-jenkins)</b> for any questions about using the plugin.
* <b>Notice</b>:
  * Include the following:
    * Java version
    * Jenkins version
    * ZAP version
    * ZAP Jenkins Plugin version
    * Firefox version (if running AJAX Spider or a Selenium build)
    * Selenium (if applicable)
  * Upload copies of the <i>zap.log</i> files and a copy of the <i>console output</i> of the jenkins log to <b>[pastebin](https://pastebin.mozilla.org/)</b>.
  * Jenkins is always running on a master, is ZAP running on the master as well or on a separate slave machine?
  * Relevant Jenkins Job Configurations sanitized screenshots.

### Issue Tracking

* Issues can be created on the <b>Jenkins JIRA</b> for the component <b>[zap-plugin](https://issues.jenkins-ci.org/issues/?jql=project%20%3D%20JENKINS%20AND%20component%20%3D%20zap-plugin)</b>.
  * <b>[All](https://issues.jenkins-ci.org/issues/?jql=project%20%3D%20JENKINS%20AND%20component%20%3D%20zap-plugin)</b> Issues
  * Want to see what we need help with? See <b>[Open, Reopened and Verified](https://issues.jenkins-ci.org/browse/JENKINS-43384?jql=project%20%3D%20JENKINS%20AND%20status%20in%20%28Open%2C%20Reopened%2C%20Verified%29%20AND%20component%20%3D%20zap-plugin)</b> Issues
  * Want to see what contributors are currently working on? See <b>[In Progress and In Review](https://issues.jenkins-ci.org/browse/JENKINS-43483?jql=project%20%3D%20JENKINS%20AND%20status%20in%20%28%22In%20Progress%22%2C%20%22In%20Review%22%29%20AND%20component%20%3D%20zap-plugin)</b> Issues
  * Want to see what we've done so far? See <b>[Closed and Resolved](https://issues.jenkins-ci.org/browse/JENKINS-41069?jql=project%20%3D%20JENKINS%20AND%20status%20in%20%28Resolved%2C%20Closed%29%20AND%20component%20%3D%20zap-plugin)</b> Issues
* <b>Before</b> creating an Issue please read the <b>[JIRA guidelines](https://wiki.jenkins-ci.org/display/JENKINS/How+to+report+an+issue)</b>.
* <b>Notice</b>: GitHub Issues have been disabled.

### Security Vulnerabilities

* If you find any security vulnerabilities or exploits caused by the plugin, please send a private email to one of the <b>[maintainer(s)](https://wiki.jenkins-ci.org/display/JENKINS/zap+plugin#zapplugin-PluginInformation)</b>.
* <b>Notice</b>: These should be kept private until a fix is issued.

### Latest Release - Version 1.1.0 (July 10, 2017)

* <img src="https://wiki.jenkins.io/s/en_GB/6441/82994790ee2f720a5ec8daf4850ac5b7b34d2194/_/images/icons/emoticons/add.png" alt="add"> <b>[ <a href="https://issues.jenkins-ci.org/browse/JENKINS-39985">JENKINS-39985</a> ]</b> Added support for context Alert Filters.
* <img src="https://wiki.jenkins.io/s/en_GB/6441/82994790ee2f720a5ec8daf4850ac5b7b34d2194/_/images/icons/emoticons/add.png" alt="add"> <b>[ <a href="https://issues.jenkins-ci.org/browse/JENKINS-43554">JENKINS-43554</a> ]</b> Added support for the internationalization of:
  1. UI Elements
  1. Associated Help Doc's
* <img src="https://wiki.jenkins.io/s/en_GB/6441/82994790ee2f720a5ec8daf4850ac5b7b34d2194/_/images/icons/emoticons/warning.png" alt="warning"> <b>[ <a href="https://issues.jenkins-ci.org/browse/JENKINS-43483">JENKINS-43483</a> ]</b> Added support to the authentication module to be able to utilize Logged Out Indicators.
* <img src="https://wiki.jenkins.io/s/en_GB/6441/82994790ee2f720a5ec8daf4850ac5b7b34d2194/_/images/icons/emoticons/forbidden.png" alt="remove"> <b>[ <a href="https://issues.jenkins-ci.org/browse/JENKINS-43384">JENKINS-43384</a> ]</b> Removed support from the ZAP Settings Variable for System Environment Variables, Build Variables as well as Environment Inject Plugin Variables since the Job Configuration page is ONLY run on the master and has no access to the slave.
* <img src="https://wiki.jenkins.io/s/en_GB/6441/82994790ee2f720a5ec8daf4850ac5b7b34d2194/_/images/icons/emoticons/error.png" alt="bugfix"> Bug-fixes:
  1. Fixed Broken links in Help Files
  1. Fixed misused variables (evaluatedSessionFilename instead of evaluatedZapSettingsDir) of <i>ZAPDriver.java</i>
  1. Correct improper use of JIRA Extension parameters
* <img src="https://wiki.jenkins.io/s/en_GB/6441/82994790ee2f720a5ec8daf4850ac5b7b34d2194/_/images/icons/emoticons/information.png" alt="info"> Usability Improvements:
  * Updated all affected help files
  * Created the webapp folder
  * Established clearer distinction between the ZAP Installation Directory and ZAP Home Directory
* <img src="https://wiki.jenkins.io/s/en_GB/6441/82994790ee2f720a5ec8daf4850ac5b7b34d2194/_/images/icons/emoticons/information.png" alt="info"> Updated LICENSE and README information.

See <b>[Full Version History](https://wiki.jenkins.io/display/JENKINS/Version+History)</b>

### License

	The MIT License (MIT)
	
	Copyright (c) 2016 Goran Sarenkapa (JordanGS), and a number of other of contributors
	
	Permission is hereby granted, free of charge, to any person obtaining a copy
	of this software and associated documentation files (the "Software"), to deal
	in the Software without restriction, including without limitation the rights
	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
	copies of the Software, and to permit persons to whom the Software is
	furnished to do so, subject to the following conditions:
	
	The above copyright notice and this permission notice shall be included in all
	copies or substantial portions of the Software.
	
	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
	SOFTWARE.


See <b>[License](LICENSE)</b>
