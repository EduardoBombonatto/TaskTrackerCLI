package Models;

import java.time.LocalDateTime;

public class Task {
    private static int lastID = 0; // keep track of the last id in the json file, without reading it.
    private int id;
    private String description;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Task(String description) {
        this.id = ++lastID; //start in 1
        this.description = description;
        this.status = Status.TODO;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public String taskToJson() {
        return "{\"id\":\"" + id + "\",\n \"description\":\"" + description.strip() + "\",\n \"status\":\"" + status.toString() +
                "\",\n \"createdAt\":\"" + createdAt + "\",\n \"updatedAt\":\"" + updatedAt + "\"\n}";
    }

    public static Task jsonToTask(String json) {
        json = json.replace("{", "").replace("}", "").replace("\"", "");
        String[] field = json.split(","); //id:1, decription:New task ...

        String id = field[0].split(":")[1].strip();
        String description = field[1].split(":")[1].strip();
        String statusStr = field[2].split(":")[1].strip();
        String createdAtStr = field[3].split("[a-z]:")[1].strip();
        String updatedAtStr = field[4].split("[a-z]:")[1].strip();

        Status status = Status.valueOf(statusStr.toUpperCase().replace(" ", "_"));

        Task task = new Task(description);
        task.id = Integer.parseInt(id);
        task.status = status;
        task.createdAt = LocalDateTime.parse(createdAtStr);
        task.updatedAt = LocalDateTime.parse(updatedAtStr);

        if (Integer.parseInt(id) > lastID) {
            lastID = Integer.parseInt(id);
        }

        return task;
    }
}
