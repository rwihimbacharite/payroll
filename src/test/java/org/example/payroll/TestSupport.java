package org.example.payroll;

import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.Callable;

/** Shared helpers for the plain-Java test programs (no test framework used). */
public class TestSupport {
    /** Fresh system with three sample employees; created for every test case. */
    public static class Ctx {
        public EmployeeManager em = new EmployeeManager();
        public PayrollService ps = new PayrollService(em);
        public ReportService rs = new ReportService(em, ps);
        public Ctx() {
            em.add(new Employee("E001", "Alice", "IT", 50000, 10000));
            em.add(new Employee("E002", "Bob",   "HR", 80000, 10000));
            em.add(new Employee("E003", "Carol", "IT", 120000, 0));
        }
    }
    public record Case(String id, String tech, String req, String feature,
                       String input, String expected, Callable<Object> action) { }

    public static final String IAE = "IllegalArgumentException", NSE = "NoSuchElementException",
                               ISE = "IllegalStateException";
    public static String f(double d) { return String.format("%.2f", d); }
    public static Employee emp(String id, String n, String d, double b, double a) { return new Employee(id, n, d, b, a); }

    /** Runs all cases, prints PASS/FAIL, writes a TSV file, exits with 1 if any case failed. */
    public static void runAll(String title, List<Case> cases, String outFile, String kind) throws Exception {
        int pass = 0, fail = 0;
        System.out.println("=== " + title + " ===");
        try (PrintWriter out = new PrintWriter(outFile)) {
            for (Case c : cases) {
                String actual;
                try { actual = String.valueOf(c.action().call()); }
                catch (Exception ex) { actual = ex.getClass().getSimpleName(); }
                boolean ok = actual.equals(c.expected());
                if (ok) pass++; else fail++;
                out.println(String.join("\t", c.id(), kind, c.tech(), c.req(), c.feature(), c.input(),
                        c.expected(), actual.replace("\t", " ").replace("\n", " "), ok ? "Pass" : "Fail"));
                System.out.printf("%-6s %-5s expected=[%s] actual=[%s]%n", c.id(), ok ? "PASS" : "FAIL", c.expected(), actual);
            }
        }
        System.out.printf("Summary: cases=%d passed=%d failed=%d%n", cases.size(), pass, fail);
        System.exit(fail == 0 ? 0 : 1);
    }
}
