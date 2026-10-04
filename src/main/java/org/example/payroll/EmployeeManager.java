package org.example.payroll;

import java.util.*;

/** FR1-FR4: add, update, delete, search employees. */
public class EmployeeManager {
    private final Map<String, Employee> store = new LinkedHashMap<>();

    /** Validation rules for an employee (used by add and update). */
    public static void validate(Employee e) {
        if (e.id() == null || e.id().isBlank())     throw new IllegalArgumentException("Id required");
        if (e.name() == null || e.name().isBlank()) throw new IllegalArgumentException("Name required");
        if (e.dept() == null || e.dept().isBlank()) throw new IllegalArgumentException("Dept required");
        if (e.basic() <= 0)                         throw new IllegalArgumentException("Basic must be > 0");
        if (e.allowance() < 0)                      throw new IllegalArgumentException("Allowance must be >= 0");
    }

    public void add(Employee e) {                       // FR1
        validate(e);
        if (store.containsKey(e.id())) throw new IllegalArgumentException("Duplicate id");
        store.put(e.id(), e);
    }

    public void update(Employee e) {                    // FR2
        validate(e);
        if (!store.containsKey(e.id())) throw new NoSuchElementException("No such employee");
        store.put(e.id(), e);
    }

    public void remove(String id) {                     // FR3
        if (store.remove(id) == null) throw new NoSuchElementException("No such employee");
    }

    public Employee find(String id) { return store.get(id); }   // FR4 (null if absent)
    public List<Employee> list()    { return new ArrayList<>(store.values()); }
    public int count()              { return store.size(); }
}
