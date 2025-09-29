package Models;

import java.time.LocalDateTime;

public class Task {
    private static int lastID = 0;
    private int id;
    private String description;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Task(String description) {
        this.id = ++lastID;
        this.description = description;
        this.status = Status.TODO;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public void updateDescription(String description) {
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }

    public void markInProgress() {
        this.status = Status.IN_PROGRESS;
        this.updatedAt = LocalDateTime.now();
    }

    public void markDone() {
        this.status = Status.DONE;
        this.updatedAt = LocalDateTime.now();
    }

    public String taskToJson() {
        return "{\"id\":" + id +
                ",\"description\":\"" + escapeJson(description) +
                "\",\"status\":\"" + status.toString() +
                "\",\"createdAt\":\"" + createdAt +
                "\",\"updatedAt\":\"" + updatedAt + "\"}";
    }

    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private static String unescapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\\"", "\"")
                .replace("\\\\", "\\")
                .replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\t", "\t");
    }

    public static Task jsonToTask(String json) {
        json = json.trim();
        if (json.startsWith("{")) json = json.substring(1);
        if (json.endsWith("}")) json = json.substring(0, json.length() - 1);

        int id = extractInt(json, "id");
        String description = extractString(json, "description");
        String statusStr = extractString(json, "status");
        String createdAtStr = extractString(json, "createdAt");
        String updatedAtStr = extractString(json, "updatedAt");

        Status status = Status.valueOf(statusStr.toUpperCase().replace(" ", "_"));

        Task task = new Task(description);
        task.id = id;
        task.status = status;
        task.createdAt = LocalDateTime.parse(createdAtStr);
        task.updatedAt = LocalDateTime.parse(updatedAtStr);

        if (id > lastID) {
            lastID = id;
        }

        return task;
    }

    private static int extractInt(String json, String key) {
        String pattern = "\"" + key + "\":";
        int startIdx = json.indexOf(pattern);
        if (startIdx == -1) {
            throw new IllegalArgumentException("Key not found: " + key);
        }
        startIdx += pattern.length();

        // Pula espaços em branco
        while (startIdx < json.length() && Character.isWhitespace(json.charAt(startIdx))) {
            startIdx++;
        }

        int endIdx = startIdx;
        while (endIdx < json.length() &&
                (Character.isDigit(json.charAt(endIdx)) || json.charAt(endIdx) == '-')) {
            endIdx++;
        }

        String numberStr = json.substring(startIdx, endIdx).trim();
        if (numberStr.isEmpty()) {
            throw new IllegalArgumentException("Empty value for key: " + key);
        }

        return Integer.parseInt(numberStr);
    }

    private static String extractString(String json, String key) {
        String pattern = "\"" + key + "\":\"";
        int startIdx = json.indexOf(pattern);
        if (startIdx == -1) {
            throw new IllegalArgumentException("Key not found: " + key + " in JSON: " + json);
        }
        startIdx += pattern.length();

        int endIdx = startIdx;
        boolean escaped = false;
        while (endIdx < json.length()) {
            char c = json.charAt(endIdx);

            if (escaped) {
                escaped = false;
                endIdx++;
                continue;
            }

            if (c == '\\') {
                escaped = true;
                endIdx++;
                continue;
            }

            if (c == '\"') {
                break;
            }
            endIdx++;
        }

        if (endIdx >= json.length()) {
            throw new IllegalArgumentException("Unterminated string for key: " + key);
        }

        String value = json.substring(startIdx, endIdx);
        return unescapeJson(value);
    }

    @Override
    public String toString() {
        return "Task{id=" + id +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt + '}';
    }
}