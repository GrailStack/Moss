package org.xujin.moss.client.endpoint.dependency.util;

import org.junit.Assert;
import org.junit.Test;

public class PomUtilTest {
    @Test
    public void testGetArtifactIdAndVersion() {
        Assert.assertArrayEquals(
            new String[] {"", "٢９٢٢٢٢٢٢٢٢٢٢٢٢٢٢"},
            PomUtil.getArtifactIdAndVersion("-٢９٢٢٢٢٢٢٢٢٢٢٢٢٢٢"));
        Assert.assertArrayEquals(
            new String[] {"", "9-"},
            PomUtil.getArtifactIdAndVersion("-9-"));
        Assert.assertArrayEquals(
            new String[] {"-", "9"},
            PomUtil.getArtifactIdAndVersion("--9"));
        Assert.assertNull(PomUtil.getArtifactIdAndVersion("3"));
        Assert.assertNull(PomUtil.getArtifactIdAndVersion("-"));
        Assert.assertNull(PomUtil.getArtifactIdAndVersion("--"));
    }
}
