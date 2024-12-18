package org.ingko.examples;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class ExampleTests {
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
    public void testReadmeExample() throws IOException {
        String[] args = {};
        ReadmeExample.main(args);
        Path output = Path.of("src/test/resources/ReadmeOutput.expected.java");
        if (RESET_TESTS) {
            Files.writeString(output, outContent.toString());
            return;
        }
        String expected = Files.readString(output);
        assertThat(outContent.toString()).isEqualTo(expected);
    }

    @Test
    public void testRecordExample() throws IOException {
        String[] args = {};
        RecordExample.main(args);
        Path output = Path.of("src/test/resources/RecordExample.expected.java");
        if (RESET_TESTS) {
            Files.writeString(output, outContent.toString());
            return;
        }
        String expected = Files.readString(output);
        assertThat(outContent.toString()).isEqualTo(expected);
    }

    @Test
    public void testParameterExample() throws IOException {
        String[] args = {};
        ParameterExample.main(args);
        Path output = Path.of("src/test/resources/ParameterExample.expected.java");
        if (RESET_TESTS) {
            Files.writeString(output, outContent.toString());
            return;
        }
        String expected = Files.readString(output);
        assertThat(outContent.toString()).isEqualTo(expected);
    }

    @Test
    public void testTemplateExample() throws IOException {
        String[] args = {};
        TemplateExample.main(args);
        Path output = Path.of("src/test/resources/TemplateExample.expected.java");
        if (RESET_TESTS) {
            Files.writeString(output, outContent.toString());
            return;
        }
        String expected = Files.readString(output);
        assertThat(outContent.toString()).isEqualTo(expected);
    }

    @Test
    public void testThrowExample() throws Exception {
        String[] args = {};
        ThrowExample.main(args);
        Path output = Path.of("src/test/resources/ThrowExample.expected.java");
        Path actualoutput = Path.of("src/test/resources/ThrowExample.actual.java");
        Files.writeString(actualoutput, outContent.toString());
        if (RESET_TESTS) {
            Files.writeString(output, outContent.toString());
            return;
        }
        String[] expected = Files.readString(output).split("\n");
        String[] actual = outContent.toString().split("\n");
        assertThat(expected.length).isEqualTo(actual.length);
        int diff = 0;
        for (int i = 0; i < expected.length; i++) {
            if (!expected[i].equals(actual[i])) {
                diff++;
            }
        }
        assertThat(diff).isEqualTo(1);
    }
}
