package api;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import model.EpicTask;
import model.Status;
import model.SubTask;
import model.TaskType;

import java.io.IOException;
import java.util.Objects;

public class EpicTaskAdapter extends TypeAdapter<EpicTask> {
    @Override
    public void write(JsonWriter jsonWriter, EpicTask epicTask) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("name").value(epicTask.getName());
        jsonWriter.name("description").value(epicTask.getDescription());
        jsonWriter.name("id").value(epicTask.getId());
        jsonWriter.name("status").value(epicTask.getStatus().toString());
        jsonWriter.name("taskType").value(epicTask.getTaskType().toString());
        if (epicTask.getDuration() == null) {
            jsonWriter.name("duration").value("null");
        } else {
            jsonWriter.name("duration").value(epicTask.getDuration().toMinutes());
        }
        if (epicTask.getStartTime() == null) {
            jsonWriter.name("startTime").value("null");
        } else {
            jsonWriter.name("startTime").value(epicTask.getStartTime().format(SubTaskAdapter.TIME_FORMATTER));
        }
        if (epicTask.getEndTime() == null) {
            jsonWriter.name("endTime").value("null");
        } else {
            jsonWriter.name("endTime").value(epicTask.getEndTime().format(SubTaskAdapter.TIME_FORMATTER));
        }
        jsonWriter.name("subTasks").beginArray();
        for (SubTask subTask : epicTask.getSubTasks()) {
            jsonWriter.beginObject();
            jsonWriter.name("name").value(subTask.getName());
            jsonWriter.name("description").value(subTask.getDescription());
            jsonWriter.name("id").value(subTask.getId());
            jsonWriter.name("status").value(subTask.getStatus().toString());
            jsonWriter.name("taskType").value(subTask.getTaskType().toString());
            jsonWriter.name("duration").value(subTask.getDuration().toMinutes());
            jsonWriter.name("startTime").value(subTask.getStartTime().format(SubTaskAdapter.TIME_FORMATTER));
            jsonWriter.endObject();
        }
        jsonWriter.endArray();
        jsonWriter.endObject();
    }

    @Override
    public EpicTask read(JsonReader jsonReader) throws IOException {
        Integer id = null;
        String name = null;
        String description = null;
        Status status = null;
        String startTime = null;
        Integer duration = null;

        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String field = jsonReader.nextName();

            if (field.equals("id")) {
                try {
                    id = jsonReader.nextInt();
                } catch (Exception ex) {
                    id = null;
                }

            } else if (field.equals("name")) {
                name = jsonReader.nextString();

            } else if (field.equals("description")) {
                description = jsonReader.nextString();

            } else if (field.equals("status")) {
                status = switch (jsonReader.nextString()) {
                    case "IN_PROGRESS" -> Status.IN_PROGRESS;
                    case "DONE" -> Status.DONE;
                    default -> Status.NEW;
                };

            } else if (field.equals("duration")) {
                try {
                    duration = Integer.parseInt(jsonReader.nextString());
                } catch (Exception e) {
                    duration = null;
                }
            } else if (field.equals("startTime")) {
                    startTime = jsonReader.nextString();
            } else {
                jsonReader.skipValue();
            }
        }
        jsonReader.endObject();

        if (Objects.isNull(id) || id == 0) {
            return new EpicTask(name, description, status, TaskType.EPIC_TASK);
        } else {
            return new EpicTask(id, name, description, status, TaskType.EPIC_TASK, startTime, duration);
        }
    }
}
