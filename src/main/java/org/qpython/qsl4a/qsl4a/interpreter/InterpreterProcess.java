/*
 * Copyright (C) 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.qpython.qsl4a.qsl4a.interpreter;

import android.content.Context;

import org.qpython.qsl4a.qsl4a.Analytics;
import org.qpython.qsl4a.qsl4a.AndroidProxy;
import org.qpython.qsl4a.qsl4a.FileUtils;
import org.qpython.qsl4a.qsl4a.LogUtil;
import org.qpython.qsl4a.qsl4a.Process;
import org.qpython.qsl4a.qsl4a.SimpleServer;
import org.qpython.qsl4a.qsl4a.jsonrpc.RpcReceiverManagerFactory;

import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * This is a skeletal implementation of an interpreter process.
 * 
 * @author Damon Kohler (damonkohler@gmail.com)
 */
public class InterpreterProcess extends Process {

  private final AndroidProxy mProxy;
  private final Interpreter mInterpreter;
  private String mCommand;

  /**
   * Creates a new {@link InterpreterProcess}.
   * 
   * @param launchScript
   *          the absolute path to a script that should be launched with the interpreter
   * @param port
   *          the port that the AndroidProxy is listening on
   */
  public InterpreterProcess(Interpreter interpreter, AndroidProxy proxy) {
    mProxy = proxy;
    mInterpreter = interpreter;

    setBinary(interpreter.getBinary());
    setName(interpreter.getNiceName());
    setCommand(interpreter.getInteractiveCommand());
    addAllArguments(interpreter.getArguments());
    putAllEnvironmentVariables(System.getenv());
    putEnvironmentVariable("AP_HOST", getHost());
    putEnvironmentVariable("AP_PORT", Integer.toString(getPort()));
    if (proxy.getSecret() != null) {
      putEnvironmentVariable("AP_HANDSHAKE", getSecret());
    }
    putAllEnvironmentVariables(interpreter.getEnvironmentVariables());
  }

  protected void setCommand(String command) {
    mCommand = command;
  }

  public Interpreter getInterpreter() {
    return mInterpreter;
  }

  public String getHost() {
    String result = mProxy.getAddress().getHostName();
    if (result.equals("0.0.0.0")) { // Wildcard.
      try {
        return SimpleServer.getPublicInetAddress().getHostName();
      } catch (UnknownHostException e) {
        LogUtil.i("public address", e);
        e.printStackTrace();
      } catch (SocketException e) {
        LogUtil.i("public address", e);
      }
    }
    return result;
  }

  public int getPort() {
    return mProxy.getAddress().getPort();
  }

  public InetSocketAddress getAddress() {
    return mProxy.getAddress();
  }

  public String getSecret() {
    return mProxy.getSecret();
  }

  public RpcReceiverManagerFactory getRpcReceiverManagerFactory() {
    return mProxy.getRpcReceiverManagerFactory();
  }

  @Override
  public void start(Context context, final Runnable shutdownHook) {
    Analytics.track(mInterpreter.getName());
    // NOTE(damonkohler): String.isEmpty() doesn't work on Cupcake.
    if (!mCommand.equals("")) {
      addArgument(mCommand);
    }
    super.start(context,shutdownHook);
  }

  @Override
  public void kill() {
    super.kill();
    mProxy.shutdown();
  }

  @Override
  public String getWorkingDirectory(Context context) {
    return FileUtils.getScriptsRootPath(context);
  }
}