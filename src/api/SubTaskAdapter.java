package api;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import model.EpicTask;
import model.Status;
import model.SubTask;
import model.TaskType;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class SubTaskAdapter extends TypeAdapter<SubTask> {
    protected static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @Override
    public void write(JsonWriter jsonWriter, SubTask subTask) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("name").value(subTask.getName());
        jsonWriter.name("description").value(subTask.getDescription());
        jsonWriter.name("id").value(subTask.getId());
        jsonWriter.name("status").value(subTask.getStatus().toString());
        jsonWriter.name("taskType").value(subTask.getTaskType().toString());
        jsonWriter.name("duration").value(subTask.getDuration().toMinutes());
        jsonWriter.name("startTime").value(subTask.getStartTime().format(TIME_FORMATTER));
        jsonWriter.name("epic").beginObject();
        jsonWriter.name("name").value(subTask.getEpic().getName());
        jsonWriter.name("description").value(subTask.getEpic().getDescription());
        jsonWriter.name("id").value(subTask.getEpic().getId());
        jsonWriter.name("status").value(subTask.getEpic().getStatus().toString());
        jsonWriter.name("taskType").value(subTask.getEpic().getTaskType().toString());
        jsonWriter.endObject();
        jsonWriter.endObject();
    }

    @Override
    public SubTask read(JsonReader jsonReader) throws IOException {
        Integer id = null;
        String name = null;
        String description = null;
        Status  status = null;
        TaskType taskType = null;
        String startTime = null;
        Integer duration = null;
        Status statusEpic = null;
        EpicTask epic = new EpicTask();
        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            switch (jsonReader.nextName()) {
                case "name":
                    name = jsonReader.nextString();
                    break;
                case "description":
                    description = jsonReader.nextString();
                    break;
                case "id":
                    try {
                        id = jsonReader.nextInt();
                    } catch (Exception e) {
                        id = null;
                    }
                    break;
                case "status":
                    status = switch (jsonReader.nextString()) {
                        case "IN_PROGRESS" -> Status.IN_PROGRESS;
                        case "DONE" -> Status.DONE;
                        default -> Status.NEW;
                    };
                    break;
                case "taskType":
                    jsonReader.nextString();
                    taskType = TaskType.SUB_TASK;
                    break;
                case "startTime":
                    startTime = jsonReader.nextString();
                    break;
                case "duration":
                    duration = jsonReader.nextInt();
                    break;
                case "epic":
                    jsonReader.beginObject();
                    while (jsonReader.hasNext()) {
                        switch (jsonReader.nextName()) {
                            case "name":
                                epic.setName(jsonReader.nextString());
                                break;
                            case "description":
                                epic.setDescription(jsonReader.nextString());
                                break;
                            case "id":
                                epic.setId(jsonReader.nextInt());
                                break;
                            case "status":
                                statusEpic = switch (jsonReader.nextString()) {
                                    case "IN_PROGRESS" -> Status.IN_PROGRESS;
                                    case "DONE" -> Status.DONE;
                                    default -> Status.NEW;
                                };
                                epic.setStatus(statusEpic);
                                break;
                            case "taskType":
                                jsonReader.nextString();
                                epic.setTaskType(TaskType.EPIC_TASK);
                                break;
                        }
                    }
                    jsonReader.endObject();
            }
        }
        jsonReader.endObject();
        if (!Objects.isNull(id)) {
            return new SubTask(id, epic, name, description, status, taskType, startTime, duration);
        } else {
            return new SubTask(epic, name , description, status, taskType, startTime, duration);
        }
    }
}
