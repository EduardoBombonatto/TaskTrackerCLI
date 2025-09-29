import Models.Status;

public class TaskCLI {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        if (args.length < 1) {
            System.out.println("Insufficient arguments");
            System.exit(0);
        }

        String command = args[0];

        switch (command) {
            case "add":
                if (args.length < 2) {
                    System.out.println("Usage: TaskCLI add <Task>");
                    System.exit(0);
                }
                taskManager.addTask(args[1]);
                break;
            case "update":
                if (args.length < 3) {
                    System.out.println("Usage: TaskCLI update <Task ID> <Description Update");
                    System.exit(0);
                }
                taskManager.updateTask(args[1], args[2]);
                break;
            case "delete":
                if (args.length < 2) {
                    System.out.println("Usage: TaskCLI delete <Task ID>");
                }
                taskManager.deleteTask(args[1]);
                break;
            case "mark-in-progress":
                if (args.length < 2) {
                    System.out.println("Usage: TaskCLI mark-in-progress <Task ID>");
                }
                taskManager.markInProgressTask(args[1]);
                break;
            case "mark-done":
                if (args.length < 2) {
                    System.out.println("Usage: TaskCLI mark-done <Task ID>");
                }
                taskManager.markDoneTask(args[1]);
                break;
            case "list":
                if (args.length < 2) {
                    taskManager.listTasks("All");
                } else {
                    Status filterStatus;
                    try {
                        filterStatus = Status.valueOf(args[1].toUpperCase().replace("-", "_"));
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid status: " + args[1]);
                        return;
                    }
                    taskManager.listTasks(filterStatus.toString());
                }
                break;
            default:
                System.out.println("Unknown command: " + command);
                break;
        }
        taskManager.saveTasks();
    }
}
