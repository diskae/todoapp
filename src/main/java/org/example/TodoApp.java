package org.example;

import java.util.List;
import java.util.Scanner;

public class TodoApp {
    private static final String COMMANDS_INFO =
            "add <task>, remove <index>, list, clear, done <index>, search <query>, exit";

    public static void main(String[] args) {
        TodoList list = new TodoList();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Simple Todo CLI. Commands: " + COMMANDS_INFO);

        while (true) {
            System.out.print("> ");
            if (!scanner.hasNextLine()) {
                break;
            }
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }
            String[] parts = line.split(" ", 2);
            String cmd = parts[0].toLowerCase();

            switch (cmd) {
                case "add":
                    if (parts.length > 1 && !parts[1].trim().isEmpty()) {
                        list.add(parts[1].trim());
                        System.out.println("Added.");
                    } else {
                        System.out.println("Usage: add <task>");
                    }
                    break;

                case "remove":
                    if (parts.length > 1) {
                        try {
                            int idx = Integer.parseInt(parts[1].trim());
                            if (list.remove(idx)) {
                                System.out.println("Removed.");
                            } else {
                                System.out.println("Index out of range.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid index.");
                        }
                    } else {
                        System.out.println("Usage: remove <index>");
                    }
                    break;

                case "list":
                    List<String> all = list.getAll();
                    for (int i = 0; i < all.size(); i++) {
                        System.out.printf("%d: %s%n", i, all.get(i));
                    }
                    if (all.isEmpty()) {
                        System.out.println("(empty)");
                    }
                    break;

                case "clear":
                    list.clear();
                    System.out.println("All tasks cleared.");
                    break;

                case "done":
                    if (parts.length > 1) {
                        try {
                            int idx = Integer.parseInt(parts[1].trim());
                            if (list.markDone(idx)) {
                                System.out.println("Task marked as done.");
                            } else {
                                System.out.println("Index out of range.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid index.");
                        }
                    } else {
                        System.out.println("Usage: done <index>");
                    }
                    break;

                case "search":
                    if (parts.length > 1 && !parts[1].trim().isEmpty()) {
                        List<String> found = list.search(parts[1].trim());
                        if (found.isEmpty()) {
                            System.out.println("(no matches found)");
                        } else {
                            for (int i = 0; i < found.size(); i++) {
                                System.out.printf("Found %d: %s%n", i, found.get(i));
                            }
                        }
                    } else {
                        System.out.println("Usage: search <query>");
                    }
                    break;

                case "exit":
                    System.out.println("Bye!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Unknown command. Commands: " + COMMANDS_INFO);
            }
        }
    }
}
