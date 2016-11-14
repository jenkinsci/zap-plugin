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

import java.text.MessageFormat;

import hudson.model.BuildListener;

/**
 * A support class to prevent used to create more user friendly console output.
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
public class Utils {

    /* Global Constant: Used by Logger */
    public static final String ZAP = "ZAP Jenkins Plugin";

    /**
     * Write a empty line to the logger.
     * 
     * @param listener
     *            of type BuildListener: the display log listener during the Jenkins job execution.
     */
    public static void lineBreak(BuildListener listener) {
        String message = "";
        MessageFormat mf = new MessageFormat(message);
        listener.getLogger().println(mf.format(null));
    }

    /**
     * Write a user specified message with injected values and specified indentation to the logger.
     * 
     * @param listener
     *            of type BuildListener: the display log listener during the Jenkins job execution.
     * @param indent
     *            of type int: the indentation of the log message to be displayed.
     * @param message
     *            of type String: the message to be displayed in the log.
     * @param args
     *            of type String...: the values to be injected into the message
     */
    public static void loggerMessage(BuildListener listener, int indent, String message, String... args) {
        MessageFormat mf = new MessageFormat(indent(message, indent));
        listener.getLogger().println(mf.format(args));
    }

    /**
     * Method which adds a specified amount of tabs to the message string.
     * 
     * @param message
     *            of type String: the message which will have tabs placed in front of it.
     * @param indent
     *            of type int: the number of tabs to be placed in front of the message.
     * @return of type String: the original message with appended tabs to the front of it.
     */
    public static String indent(String message, int indent) {
        String temp = "";
        for (int i = 0; i < indent; i++)
            temp = temp + "\t";
        return temp + message;
    }
}
