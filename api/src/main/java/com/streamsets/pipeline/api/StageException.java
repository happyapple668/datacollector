/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.streamsets.pipeline.api;

import com.streamsets.pipeline.container.LocalizableMessage;
import com.streamsets.pipeline.container.Utils;

public class StageException extends Exception {

  public static class ID {
    private final ClassLoader classLoader;
    private final String bundle;
    private final String id;
    private final String msgTemplate;


    public ID(String id, String msgTemplate) {
      this((String) null, id, msgTemplate);
    }

    public ID(String bundle, String id, String msgTemplate) {
      classLoader = Thread.currentThread().getContextClassLoader();
      this.bundle = bundle;
      this.id = Utils.checkNotNull(id, "id");
      this.msgTemplate = Utils.checkNotNull(msgTemplate, "msgTemplate");
    }

    public ID(Class classForBundleName, String id, String msgTemplate) {
      this(Utils.checkNotNull(classForBundleName, "classForBundleName").getName(), id, msgTemplate);
    }

    public String getId() {
      return id;
    }

    public String getMessageTemplate() {
      return msgTemplate;
    }

    public LocalizedString getMessage(Object... args) {
      return new LocalizableMessage(classLoader, bundle, getId(), getMessageTemplate(), args);
    }

  }

  private static Throwable getCause(Object... params) {
    Throwable throwable = null;
    if (params.length > 0 && params[params.length - 1] instanceof Throwable) {
      throwable = (Throwable) params[params.length - 1];
    }
    return throwable;
  }

  private final String id;
  private final LocalizedString localizedString;

  // last parameter can be a cause exception
  public StageException(ID errorId, Object... params) {
    super(getCause(params));
    this.id = Utils.checkNotNull(errorId, "errorId").getId();
    this.localizedString = errorId.getMessage(params);
  }

  public String getId() {
    return id;
  }

  @Override
  public String getMessage() {
    return localizedString.getNonLocalized();
  }

  @Override
  public String getLocalizedMessage() {
    return localizedString.getLocalized();
  }

}
