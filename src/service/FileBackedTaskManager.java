package service;



import model.EpicTask;
import model.ManagerSaveException;
import model.Status;
import model.SubTask;
import model.Task;
import model.TaskType;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class FileBackedTaskManager extends InMemoryTaskManager {

    private static final String HEADER = "id,type,name,status,description,startTime,duration,epic";
    private final File file;

    public FileBackedTaskManager(String path, HistoryManager historyManager) {
        super(historyManager);
        this.file = new File(path);
    }

    private void save() {
     try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
         bufferedWriter.write(HEADER);
         bufferedWriter.newLine();
         for (Task task : getTasks().values()) {
             bufferedWriter.write(toString(task));
             bufferedWriter.newLine();
         }
         for (EpicTask epicTask : getEpics().values()) {
             bufferedWriter.write(toString(epicTask));
             bufferedWriter.newLine();
         }
         for (SubTask subTask : getSubTasks().values()) {
             bufferedWriter.write(toString(subTask));
             bufferedWriter.newLine();
         }
     } catch (IOException e) {
         throw new ManagerSaveException("Не удалость сохранить данные в файл");
     }
    }

    private Task fromString(String value) {
        String[] split = value.split(",");
        int id = Integer.parseInt(split[0]);
        TaskType typeTask = TaskType.valueOf(split[1]);
        String name = split[2];
        Status status = Status.valueOf(split[3]);
        String description = split[4];
        //String startTime = split[5];
        //String duration = split[6];
        if (typeTask == TaskType.TASK) {
            String startTime = split[5];
            String duration = split[6];
            return new Task(id, name, description, status, typeTask, startTime, Integer.parseInt(duration));
        } else if (typeTask == TaskType.EPIC_TASK) {
            return new EpicTask(id,name, description, status, TaskType.EPIC_TASK);
        } else if (typeTask == TaskType.SUB_TASK) {
            String startTime = split[5];
            String duration = split[6];
            return new SubTask(id, new EpicTask(Integer.parseInt(split[7])), name, description, status, typeTask, startTime, Integer.parseInt(duration));
        }
        return null;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file.getPath(),
                Managers.getDefaultHistory());
        StringBuilder builder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            bufferedReader.readLine();
            while (bufferedReader.ready()) {
                builder.append(bufferedReader.readLine());
                builder.append(System.lineSeparator());
            }
            putData(fileBackedTaskManager, builder);
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось загрузить файл");
        }
        return fileBackedTaskManager;
    }

    private static List<Task> getAllTaskFromFile(StringBuilder builder, FileBackedTaskManager fileBackedTaskManager) {
        String[] split = builder.toString().split(System.lineSeparator());
        List<Task> tasks = new ArrayList<>();
        for (String task : split) {
            tasks.add(fileBackedTaskManager.fromString(task));
        }
        return tasks;
    }

    private static void putData(FileBackedTaskManager fileBackedTaskManager, StringBuilder builder) {
        List<Task> tasks = getAllTaskFromFile(builder, fileBackedTaskManager);
        int maxId = 0;
        for (Task task : tasks) {
            if (task.getId() > maxId) {
                maxId = task.getId();
            }
            fileBackedTaskManager.setId(maxId);
            switch (task.getTaskType()) {
                case TASK -> fileBackedTaskManager.getTasks().put(task.getId(), task);
                case SUB_TASK -> fileBackedTaskManager.getSubTasks().put(task.getId(), (SubTask) task);
                case EPIC_TASK -> fileBackedTaskManager.getEpics().put(task.getId(), (EpicTask) task);
            }
        }
        for (SubTask subTask : fileBackedTaskManager.getSubTasks().values()) {
            int epicId = subTask.getEpic().getId();
            EpicTask epicTask = fileBackedTaskManager.getEpics().get(epicId);
            epicTask.addSubTask(subTask);
            subTask.setEpic(epicTask);
        }
        for (EpicTask epicTask : fileBackedTaskManager.getAllEpicTask()) {
            if (!epicTask.getSubTasks().isEmpty()) {
                epicTask.getStartTime();
                epicTask.getEndTime();
                epicTask.getDuration();
            }
        }
    }



    @Override
    public Task createTask(Task task) {
        super.createTask(task);
        save();
        return task;
    }

    private String toString(Task task) {
        return task.toCsvString();
    }

    @Override
    public Task updateTask(Task task) {
        super.updateTask(task);
        save();
        return task;
    }

    public void removeAllTask() {
        super.removeAllTask();
        save();
    }

    @Override
    public void removeTask(int id) {
        super.removeTask(id);
        save();
    }

    @Override
    public EpicTask createEpic(EpicTask epicTask) {
        super.createEpic(epicTask);
        save();
        return epicTask;
    }

    @Override
    public void removeEpicTask(int id) {
        super.removeEpicTask(id);
        save();
    }

    @Override
    public void removeAllEpicTask() {
        super.removeAllEpicTask();
        save();
    }

    @Override
    public void updateEpicTask(EpicTask epicTask) {
        super.updateEpicTask(epicTask);
        save();
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        super.createSubTask(subTask);
        save();
        return subTask;
    }

    @Override
    public void removeSubTask(int id) {
        super.removeSubTask(id);
        save();
    }

    @Override
    public void removeAllSubTask() {
        super.removeAllSubTask();
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }
}
