public class TaskCLI {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Insufficient arguments");
            System.exit(0);
        }

        String command = args[0];
        TaskManager taskManager = new TaskManager();

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
                //taskManager.deleteTask(args[1]);
                break;
            case "mark-in-progress":
                if (args.length < 2) {
                    System.out.println("Usage: TaskCLI mark-in-progress <Task ID>");
                }
                //taskManager.markInProgressTask(args[1]);
                break;
            case "mark-done":
                if (args.length < 2) {
                    System.out.println("Usage: TaskCLI mark-done <Task ID>");
                }
                //taskManager.markDoneTask(args[1]);
                break;
            case "list":
                if (args.length < 2) {
                    //taskManager.listTasks("all")
                    System.out.println("All tasks");
                } else {
                    String filterStatus = args[1].toLowerCase();
                    //askManager.listTasks(filterStatus);
                    System.out.println("All taks: " + filterStatus);
                }
                break;
            default:
                System.out.println("Unknown command: " + command);
                break;
        }
        taskManager.saveTasks();
    }
}
