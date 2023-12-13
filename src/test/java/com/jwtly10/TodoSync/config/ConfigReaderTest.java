package com.jwtly10.TodoSync.config;

import com.jwtly10.TodoSync.exceptions.MissingConfigException;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;

import static org.junit.Assert.*;

public class ConfigReaderTest {
    @Before
    public void setupConfig() {
        String HOME = System.getProperty("user.home");
        File file = new File(HOME + "/.todosync_test");

        if (file.exists()) {
            file.delete();
        }

        try {
            Files.write(file.toPath(), "unit.test=unit_test_key".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadConfig() {
        String propUnderTest = ConfigReader.getUserProperty("unit.test");
        assertEquals("unit_test_key", propUnderTest);
    }

    @Test
    public void testMissingProperty() {
        Exception exception = assertThrows(MissingConfigException.class, () -> ConfigReader.getUserProperty("unit.test.missing"));

        String partialExpected = "Property unit.test.missing not found";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(partialExpected));
    }
}
