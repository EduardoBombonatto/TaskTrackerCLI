import Models.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private final Path FILE_PATH = Path.of("tasks.json");
    private List<Task> tasks;

    public TaskManager() {
        this.tasks = new ArrayList<>();
    }

    private List<Task> loadTasks() {
        List<Task> tasks = new ArrayList<>();
        if (!Files.exists(FILE_PATH)) {
            return new ArrayList<>();
        }

        try {
            String fileContent = Files.readString(FILE_PATH);
            String[] tasksList = fileContent.replace("[", "").replace("]", "").split(",");

            for (String taskJson : tasksList) {
                if (!taskJson.endsWith("}")) {
                    taskJson = taskJson + "}";
                }
                tasks.add(Task.jsonToTask(taskJson));
            }
        } catch (IOException e) {
            System.out.println("Error reading file");
            System.exit(0);
        }
        return tasks;
    }

    public void saveTasks() {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(tasks.get(i).taskToJson());
            if (i < tasks.size() - 1) {
                sb.append(",\n");
            }
        }
        sb.append("\n]");

        String jsonContent =  sb.toString();
        try {
            Files.writeString(FILE_PATH, jsonContent);
        } catch (IOException e) {
            System.out.println("Error writing file");
        }
    }

    public void addTask(String description) {
        Task newTask = new Task(description);
        tasks.add(newTask);
        System.out.println("Task added Successfully (ID: " + newTask.getId() + ")");
    }
}
