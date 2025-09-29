import Models.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskManager {
    private final Path FILE_PATH = Path.of("tasks.json");
    private List<Task> tasks;

    public TaskManager() {
        this.tasks = loadTasks();
    }

    private List<Task> loadTasks() {
        List<Task> tasks = new ArrayList<>();

        if (!Files.exists(FILE_PATH)) {
            return new ArrayList<>();
        }

        try {
            String fileContent = Files.readString(FILE_PATH).trim();

            if (fileContent.isEmpty()) {
                return new ArrayList<>();
            }

            // Remove to array wrapper
            if (fileContent.startsWith("[")) {
                fileContent = fileContent.substring(1);
            }
            if (fileContent.endsWith("]")) {
                fileContent = fileContent.substring(0, fileContent.length() - 1);
            }
            fileContent = fileContent.trim();

            if (fileContent.isEmpty()) {
                return new ArrayList<>();
            }

            // Parse manual dos objetos JSON
            List<String> jsonObjects = splitJsonObjects(fileContent);

            for (String taskJson : jsonObjects) {
                if (!taskJson.trim().isEmpty()) {
                    tasks.add(Task.jsonToTask(taskJson));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            System.exit(0);
        } catch (Exception e) {
            System.out.println("Error parsing JSON: " + e.getMessage());
            System.exit(0);
        }

        return tasks;
    }

    private List<String> splitJsonObjects(String content) {
        List<String> objects = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        int braceCount = 0;
        boolean inString = false;
        boolean escaped = false;
        boolean objectStarted = false;

        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);

            if (escaped) {
                current.append(c);
                escaped = false;
                continue;
            }

            if (c == '\\' && inString) {
                escaped = true;
                current.append(c);
                continue;
            }

            if (c == '"' && !escaped) {
                inString = !inString;
                current.append(c);
                continue;
            }

            if (!inString) {
                if (c == '{') {
                    braceCount++;
                    objectStarted = true;
                } else if (c == '}') {
                    braceCount--;
                }
            }

            if (objectStarted) {
                current.append(c);
            }

            // Quando terminar um objeto completo
            if (braceCount == 0 && objectStarted && !inString) {
                String obj = current.toString().trim();
                if (!obj.isEmpty()) {
                    objects.add(obj);
                }
                current = new StringBuilder();
                objectStarted = false;
            }
        }

        return objects;
    }

    public void saveTasks() {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append("  ").append(tasks.get(i).taskToJson());
            if (i < tasks.size() - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }
        sb.append("]");

        try {
            Files.writeString(FILE_PATH, sb.toString());
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }

    public void addTask(String description) {
        Task newTask = new Task(description);
        tasks.add(newTask);
        System.out.println("Task added successfully (ID: " + newTask.getId() + ")");
    }

    public void updateTask(String id, String newDescription) {
        Task task = findTask(id).orElseThrow(() ->
                new IllegalArgumentException("Task with ID: " + id + " not found"));
        task.updateDescription(newDescription);
        System.out.println("Task updated successfully (ID: " + task.getId() + ")");
    }

    public void deleteTask(String id) {
        Task task = findTask(id).orElseThrow(() ->
                new IllegalArgumentException("Task with ID: " + id + " not found"));
        tasks.remove(task);
        System.out.println("Task deleted successfully (ID: " + task.getId() + ")");
    }

    public void markInProgressTask(String id) {
        Task task = findTask(id).orElseThrow(() ->
                new IllegalArgumentException("Task with ID " + id + " not found!"));
        task.markInProgress();
    }

    public void markDoneTask(String id) {
        Task task = findTask(id).orElseThrow(() ->
                new IllegalArgumentException("Task with ID " + id + " not found!"));
        task.markDone();
    }

    public void listTasks(String type) {
        for (Task task : tasks) {
            String status = task.getStatus().toString().strip();
            if (type.equals("All") || status.equals(type)) {
                System.out.println(task);
            }
        }
    }

    public Optional<Task> findTask(String id) {
        return tasks.stream()
                .filter((task) -> task.getId() == Integer.parseInt(id))
                .findFirst();
    }
}