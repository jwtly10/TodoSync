package com.jwtly10.TodoSync.config;

import com.jwtly10.TodoSync.exceptions.MissingConfigException;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConfigReaderTest {
    @Test
    public void testReadConfig() {
        String propUnderTest = ConfigReader.getUserProperty("unit.test");
        assertEquals("unit_test_key", propUnderTest);
    }

    @Test
    public void testMissingProperty() {
        Exception exception = assertThrows(MissingConfigException.class, () -> {
            ConfigReader.getUserProperty("unit.test.missing");
        });

        String partialExpected = "Property unit.test.missing not found";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(partialExpected));
    }
}
