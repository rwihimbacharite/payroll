package org.example.payroll;

import java.util.*;
import java.util.concurrent.Callable;
import org.example.payroll.TestSupport.Ctx;
import static org.example.payroll.TestSupport.*;

/** Black-box functional tests (specification based) */
public class PayrollServiceBlackBoxTest {
    static final List<Case> cases = new ArrayList<>();
    static void add(String id, String tech, String req, String feat, String in, String exp, Callable<Object> a) {
        cases.add(new Case(id, tech, req, feat, in, exp, a)); }

    public static void main(String[] args) throws Exception {
// ---------------- BLACK-BOX (specification based) ----------------
        add("BB-01","Equivalence partition (valid)","FR1","EmployeeManager.add","add(E010,Dave,IT,50000,5000)","count=4",
           () -> { Ctx c = new Ctx(); c.em.add(emp("E010","Dave","IT",50000,5000)); return "count=" + c.em.count(); });
        add("BB-02","Equivalence partition (existing id)","FR2","EmployeeManager.update","update(E001,Alice,IT,55000,10000)","basic=55000.00",
           () -> { Ctx c = new Ctx(); c.em.update(emp("E001","Alice","IT",55000,10000)); return "basic=" + f(c.em.find("E001").basic()); });
        add("BB-03","Decision table (overtime, tax 20%)","FR6","PayrollService.processPayment","processPayment(E002, 2026-10, 10)","net=86000.00",
           () -> "net=" + f(new Ctx().ps.processPayment("E002","2026-10",10).net()));
        add("BB-04","Error guessing (double payment)","FR6","PayrollService.processPayment","processPayment(E001, 2026-10, 0) twice",ISE,
           () -> { Ctx c = new Ctx(); c.ps.processPayment("E001","2026-10",0); return c.ps.processPayment("E001","2026-10",0); });
        add("BB-05","Equivalence partition (with data)","FR7","ReportService.payrollReport","pay E001 and E003 for 2026-10, then payrollReport(2026-10)","Total net: 157500.00",
           () -> { Ctx c = new Ctx(); c.ps.processPayment("E001","2026-10",0); c.ps.processPayment("E003","2026-10",0);
                   String[] l = c.rs.payrollReport("2026-10").split("\\R"); return l[l.length-1]; });

        runAll("Black-box functional tests (specification based)", cases, "results-blackbox.tsv", "Black-box");
    }
}
