package org.xujin.moss.client.endpoint.dependency.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PomUtil {

    public static String[] getArtifactIdAndVersion(String fileName) {
        boolean needJudge = false;
        int versionBeginIndex = -1;
        for (int i = 0; i < fileName.length(); i++) {
            char c = fileName.charAt(i);
            if (needJudge && Character.isDigit(c)) {
                versionBeginIndex = i;
                break;
            }
            if (c == '-') {
                needJudge = true;
            } else {
                needJudge = false;
            }
        }

        if (versionBeginIndex > 0) {
            return new String[] {fileName.substring(0, versionBeginIndex - 1), fileName.substring(versionBeginIndex)};
        } else {
            return null;
        }
    }
}
