package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EpicTaskTest {
    EpicTask epicTask = new EpicTask("Name","Description");
    SubTask subTask = new SubTask(epicTask, "name", "description", Status.NEW);

    @Test
    void addSubTaskEquals() {
        epicTask.addSubTask(subTask);
        Assertions.assertEquals(subTask, epicTask.getSubTasks().getFirst());
    }
}