package com.fcup.utilities;

import com.backblaze.erasure.ReedSolomon;
import org.junit.Test;

import static org.junit.Assert.*;

public class FileEncoderTest {
    @Test
    public void testReedJarInclusion() {
        ReedSolomon reedSolomon = ReedSolomon.create(4, 2);
    }
}