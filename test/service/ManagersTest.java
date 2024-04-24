package service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void createManagersNotNull() {
        TaskManager inMemoryTaskManager = Managers.getDefault();
        Assertions.assertNotNull(inMemoryTaskManager);
    }
}