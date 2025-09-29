package Models;

import java.time.LocalDateTime;

public class Task {
    private static int lastID = 0; // keep track of the last id in the json file, without reading it.
    private int id;
    private String description;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Task(String description){
        this.id = ++lastID; //start in 1
        this.description = description;
        this.status = Status.TODO;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
