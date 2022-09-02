package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {
    Epic epic;

    @BeforeEach
    void createSubtask() {
        final ArrayList<Integer> includeSubtasksIDs = new ArrayList<>();
        includeSubtasksIDs.addAll(List.of(2, 3, 4));

        epic = new Epic("Epic1", "Description epic1", 1, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.of(2022, 9, 01, 00, 00, 00), includeSubtasksIDs);
    }

    @Test
    void getIncludeSubtasksIDs() {
        List<Integer> expectedIncludeSubtasksIDs= List.of(2, 3, 4);
        List<Integer> actualIncludeSubtasksIDs = epic.getIncludeSubtasksIDs();

        assertEquals(expectedIncludeSubtasksIDs, actualIncludeSubtasksIDs,
                "перечень ID входящих подзадач не соответствует" );
    }

    @Test
    void testToString() {
        String expectedString = "1,EPIC,Epic1,NEW,Description epic1,PT1H,2022-09-01T00:00";
        String actualString = epic.toString();
        assertEquals(expectedString, actualString, "Вывод не соответствует ожидаемому");
    }
}