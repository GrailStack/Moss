/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache license, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */
package org.xujin.moss.client.endpoint.dependency.util;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;

/**
 * @Since 2.9
 */
public class ProcessIdUtil {

    public static final String DEFAULT_PROCESSID = "-";

    public static String getProcessId() {
        try {
            return ManagementFactory.getRuntimeMXBean().getName().split("@")[0]; // likely works on most platforms
        } catch (final Exception ex) {
            try {
                return new File("/proc/self").getCanonicalFile().getName(); // try a Linux-specific way
            } catch (final IOException ignoredUseDefault) {
                // Ignore exception.
            }
        }
        return DEFAULT_PROCESSID;
    }
}
