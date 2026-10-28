package org.example;

import java.util.ArrayList;
import java.util.List;

public class TodoList {
    private static final String DONE_PREFIX = "[DONE] ";
    private final List<String> items = new ArrayList<>();

    public void add(String item) {
        if (item != null) {
            item = item.trim();
            if (!item.isEmpty()) {
                items.add(item);
            }
        }
    }

    public boolean remove(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
            return true;
        }
        return false;
    }

    public List<String> getAll() {
        return new ArrayList<>(items);
    }

    public int size() {
        return items.size();
    }

    public void clear() {
        items.clear();
    }

    public boolean markDone(int index) {
        if (index >= 0 && index < items.size()) {
            String item = items.get(index);
            if (!item.startsWith(DONE_PREFIX)) {
                items.set(index, DONE_PREFIX + item);
            }
            return true;
        }
        return false;
    }

    public boolean isDone(int index) {
        if (index >= 0 && index < items.size()) {
            return items.get(index).startsWith(DONE_PREFIX);
        }
        return false;
    }

    public List<String> search(String query) {
        List<String> results = new ArrayList<>();
        if (query == null) {
            return results;
        }
        String normalizedQuery = query.trim().toLowerCase();
        if (normalizedQuery.isEmpty()) {
            return results;
        }
        for (String item : items) {
            if (item.toLowerCase().contains(normalizedQuery)) {
                results.add(item);
            }
        }
        return results;
    }
}
