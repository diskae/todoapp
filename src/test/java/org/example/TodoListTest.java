package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TodoListTest {

    @Test
    @DisplayName("Should add task and list correctly, trimming whitespace")
    void addAndList() {
        TodoList t = new TodoList();
        t.add(" task1 ");
        assertEquals(1, t.size());
        assertEquals("task1", t.getAll().getFirst());
    }

    @Test
    @DisplayName("Should remove task by index and handle invalid index")
    void remove() {
        TodoList t = new TodoList();
        t.add("a");
        t.add("b");
        assertTrue(t.remove(0));
        assertEquals(1, t.size());
        assertFalse(t.remove(10));
    }

    @Test
    @DisplayName("Should ignore empty and whitespace-only strings")
    void addEmptyIgnored() {
        TodoList t = new TodoList();
        t.add(" ");
        t.add("");
        t.add(null);
        assertEquals(0, t.size());
    }

    @Test
    @DisplayName("Should clear all tasks from the list")
    void clear() {
        TodoList t = new TodoList();
        t.add("Task 1");
        t.add("Task 2");
        assertEquals(2, t.size());

        t.clear();
        assertEquals(0, t.size());
        assertTrue(t.getAll().isEmpty());
    }

    @Test
    @DisplayName("Should mark task as done and prevent duplicate done prefix")
    void markDone() {
        TodoList t = new TodoList();
        t.add("Buy groceries");
        assertTrue(t.markDone(0));
        assertTrue(t.isDone(0));
        assertEquals("[DONE] Buy groceries", t.getAll().getFirst());

        // Marking done again should not duplicate prefix
        assertTrue(t.markDone(0));
        assertEquals("[DONE] Buy groceries", t.getAll().getFirst());
    }

    @Test
    @DisplayName("Should return false when marking done for invalid index")
    void markDoneInvalidIndex() {
        TodoList t = new TodoList();
        t.add("Task");
        assertFalse(t.markDone(-1));
        assertFalse(t.markDone(1));
        assertFalse(t.isDone(-1));
        assertFalse(t.isDone(5));
    }

    @Test
    @DisplayName("Should find tasks matching substring case-insensitively")
    void search() {
        TodoList t = new TodoList();
        t.add("Prepare report for College");
        t.add("Submit CI/CD assignment");
        t.add("Read Java Adoptium guide");

        List<String> results = t.search("report");
        assertEquals(1, results.size());
        assertEquals("Prepare report for College", results.getFirst());

        List<String> caseInsensitiveResults = t.search("COLLEGE");
        assertEquals(1, caseInsensitiveResults.size());
        assertEquals("Prepare report for College", caseInsensitiveResults.getFirst());

        List<String> multipleResults = t.search("a");
        assertTrue(multipleResults.size() >= 2);
    }

    @Test
    @DisplayName("Should return empty list for empty search query or unmatched search")
    void searchEmptyQuery() {
        TodoList t = new TodoList();
        t.add("Task 1");

        assertTrue(t.search("").isEmpty());
        assertTrue(t.search("   ").isEmpty());
        assertTrue(t.search(null).isEmpty());
        assertTrue(t.search("NonExistentTerm123").isEmpty());
    }
}
