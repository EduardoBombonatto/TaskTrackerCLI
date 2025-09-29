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
}
