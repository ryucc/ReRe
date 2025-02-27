/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.examples.httpclients;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.rere.examples.httpclients.apache.ApacheHttpClientExample;
import org.rere.examples.httpclients.apache.ApacheMockClientExample;
import org.rere.examples.httpclients.java.JavaHttpClientExample;
import org.rere.examples.readme.ReadmeExample;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class HttpExampleTests {
    private static final boolean RESET_TESTS = false;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    //@Disabled("Google content changes everytime")
    public void testApacheExample() throws Exception {
        String[] args = {};
        ApacheHttpClientExample.main(args);
        Path output = Path.of("src/test/java/org/rere/examples/httpclients/apache/ApacheHttpClientExampleExpected.java");
        if (!RESET_TESTS) {
            Files.writeString(output, outContent.toString());
            return;
        }
        String expected = Files.readString(output);
        assertThat(outContent.toString()).isEqualTo(expected);
    }

    @Test
    @Disabled("Google content changes everytime")
    public void testJavaExample() throws Exception {
        String[] args = {};
        JavaHttpClientExample.main(args);
        Path output = Path.of("src/test/java/org/rere/examples/httpclients/java/JavaHttpClientExampleExpected.java");
        if (!RESET_TESTS) {
            Files.writeString(output, outContent.toString());
            return;
        }
        String expected = Files.readString(output);
        assertThat(outContent.toString()).isEqualTo(expected);
    }
}
