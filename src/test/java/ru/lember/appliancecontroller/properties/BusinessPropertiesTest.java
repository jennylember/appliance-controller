package ru.lember.appliancecontroller.properties;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
class BusinessPropertiesTest {

    public static final String MODEL = "Electrolux_SensiCare_800";

    @Autowired
    private BusinessProperties properties;

    @Test
    public void testAllPropertiesAreExpected() {
        assertNotNull(properties);

        var programNames = properties.getPrograms().stream().map(Program::getName).collect(Collectors.toSet());

        assertTrue(programNames.containsAll(Set.of(
                "ECO 30",
                "ECO 40",
                "ECO 60",
                "NORMAL 30",
                "NORMAL 40",
                "NORMAL 60",
                "HAND WASH 40",
                "FUR 30"
        )));

        Set<Long> programs = properties.getProgramIdsByModel(MODEL);
        assertTrue(programs.containsAll(Set.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L)));
    }
}