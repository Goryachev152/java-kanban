package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EpicTaskTest {

    private static EpicTask epicTask;
    private static SubTask subTask;

    @BeforeEach
    public void beforeEach() {
        epicTask = new EpicTask("Name", "Description", Status.NEW, TaskType.EPIC_TASK);
        subTask = new SubTask(epicTask, "name", "description", Status.NEW, TaskType.SUB_TASK);
    }

    @Test
    void addSubTaskEquals() {
        epicTask.addSubTask(subTask);
        Assertions.assertEquals(subTask, epicTask.getSubTasks().getFirst());
    }
}