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
package com.streamsets.pipeline.container;

import org.slf4j.helpers.MessageFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public final class Utils {

  Utils() {
  }

  public static <T> T checkNotNull(T value, String varName) {
    if (value == null) {
      throw new NullPointerException(format("{} cannot be null", varName));
    }
    return value;
  }

  public static void checkArgument(boolean expression, String msg) {
    if (!expression) {
      throw new IllegalArgumentException(msg);
    }
  }

  public static String format(String template, Object... args) {
    return MessageFormatter.arrayFormat(template, args).getMessage();
  }

  private static final TimeZone UTC = TimeZone.getTimeZone("UTC");
  private static final String ISO8601_UTC_MASK = "yyyy-MM-dd'T'HH:mm'Z'";

  private static DateFormat getISO8601DateFormat() {
    DateFormat dateFormat = new SimpleDateFormat(ISO8601_UTC_MASK);
    // Stricter parsing to prevent dates such as 2011-12-50T01:00Z (December 50th) from matching
    dateFormat.setLenient(false);
    dateFormat.setTimeZone(UTC);
    return dateFormat;
  }

  public static Date parse(String str) throws ParseException {
    return getISO8601DateFormat().parse(str);
  }

}
