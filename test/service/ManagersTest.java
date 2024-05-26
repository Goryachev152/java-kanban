package service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ManagersTest {

    @Test
    void createManagersNotNull() {
        TaskManager inMemoryTaskManager = Managers.getDefault();
        Assertions.assertNotNull(inMemoryTaskManager);
    }
}