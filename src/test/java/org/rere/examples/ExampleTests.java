package org.rere.examples;

import org.rere.examples.arrayExample.ArrayExample;
import org.rere.examples.exception.ThrowExample;
import org.rere.examples.genericTemplate.TemplateExample;
import org.rere.examples.identityFunction.IdentityFunctionExample;
import org.rere.examples.parameterMatching.ParameterMatchingExample;
import org.rere.examples.readme.ReadmeExample;
import org.rere.examples.readmeRecursive.ReadmeRecursiveExample;
import org.rere.examples.recordExample.RecordExample;
import org.rere.examples.sort.SortExample;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
        Path output = Path.of("src/test/java/org/rere/examples/readme/ReadmeExampleExpected.java");
        if (RESET_TESTS) {
            Files.writeString(output, outContent.toString());
            return;
        }
        String expected = Files.readString(output);
        assertThat(outContent.toString()).isEqualTo(expected);
    }

    @Test
    public void testReadmeRecursiveExample() throws IOException {
        String[] args = {};
        ReadmeRecursiveExample.main(args);
        Path output = Path.of("src/test/java/org/rere/examples/readmeRecursive/ReadmeRecursiveExampleExpected.java");
        if (RESET_TESTS) {
            Files.writeString(output, outContent.toString());
            return;
        }
        String expected = Files.readString(output);
        assertThat(outContent.toString()).isEqualTo(expected);
    }
    @Test
    public void testArrayExample() throws IOException {
        String[] args = {};
        ArrayExample.main(args);
        Path output = Path.of("src/test/java/org/rere/examples/arrayExample/ArrayExampleExpected.java");
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
        Path output = Path.of("src/test/java/org/rere/examples/recordExample/RecordExampleExpected.java");
        if (RESET_TESTS) {
            Files.writeString(output, outContent.toString());
            return;
        }
        String expected = Files.readString(output);
        String[] a1 = outContent.toString().split("\n");
        String[] a2 = expected.split("\n");

        for(int i = 0; i < a1.length && i < a2.length; i++) {
            assertThat(a1[i]).isEqualTo(a2[i]);
        }
    }
    @Test
    public void testIdentityFunctionExample() throws IOException {
        String[] args = {};
        IdentityFunctionExample.main(args);
        Path output = Path.of("src/test/java/org/rere/examples/identityFunction/IdentityFunctionExampleExpected.java");
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
        ParameterMatchingExample.main(args);
        Path output = Path.of("src/test/java/org/rere/examples/parameterMatching/ParameterMatchingExampleExpected.java");
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
        Path output = Path.of("src/test/java/org/rere/examples/genericTemplate/TemplateExampleExpected.java");
        if (RESET_TESTS) {
            Files.writeString(output, outContent.toString());
            return;
        }
        String expected = Files.readString(output);
        assertThat(outContent.toString()).isEqualTo(expected);
    }

    @Test
    public void testSortExample() throws IOException {
        String[] args = {};
        SortExample.main(args);
        Path output = Path.of("src/test/java/org/rere/examples/sort/SortExampleExpected.java");
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
        Path output = Path.of("src/test/java/org/rere/examples/exception/ThrowExampleExpected.java");
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
        assertThat(diff <= 1).isTrue();
    }
}
