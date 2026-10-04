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
        add("BB-02","Equivalence partition (invalid name)","FR1","EmployeeManager.add","add(E010,\"\",IT,50000,5000)",IAE,
           () -> { new Ctx().em.add(emp("E010","","IT",50000,5000)); return "accepted"; });
        add("BB-03","Equivalence partition (duplicate id)","FR1","EmployeeManager.add","add(E001,Dave,IT,50000,5000)",IAE,
           () -> { new Ctx().em.add(emp("E001","Dave","IT",50000,5000)); return "accepted"; });
        add("BB-04","Boundary value (basic = 0)","FR1","EmployeeManager.add","add(E010,Dave,IT,0,0)",IAE,
           () -> { new Ctx().em.add(emp("E010","Dave","IT",0,0)); return "accepted"; });
        add("BB-05","Boundary value (basic = 0.01)","FR1","EmployeeManager.add","add(E010,Dave,IT,0.01,0)","count=4",
           () -> { Ctx c = new Ctx(); c.em.add(emp("E010","Dave","IT",0.01,0)); return "count=" + c.em.count(); });
        add("BB-06","Boundary value (allowance = -1)","FR1","EmployeeManager.add","add(E010,Dave,IT,50000,-1)",IAE,
           () -> { new Ctx().em.add(emp("E010","Dave","IT",50000,-1)); return "accepted"; });
        add("BB-07","Equivalence partition (existing id)","FR2","EmployeeManager.update","update(E001,Alice,IT,55000,10000)","basic=55000.00",
           () -> { Ctx c = new Ctx(); c.em.update(emp("E001","Alice","IT",55000,10000)); return "basic=" + f(c.em.find("E001").basic()); });
        add("BB-08","Equivalence partition (unknown id)","FR2","EmployeeManager.update","update(E999,Zed,IT,55000,0)",NSE,
           () -> { new Ctx().em.update(emp("E999","Zed","IT",55000,0)); return "updated"; });
        add("BB-09","Equivalence partition (existing id)","FR3","EmployeeManager.remove","remove(E002)","count=2",
           () -> { Ctx c = new Ctx(); c.em.remove("E002"); return "count=" + c.em.count(); });
        add("BB-10","Equivalence partition (unknown id)","FR3","EmployeeManager.remove","remove(E999)",NSE,
           () -> { new Ctx().em.remove("E999"); return "removed"; });
        add("BB-11","Equivalence partition (existing id)","FR4","EmployeeManager.find","find(E003)","Carol",
           () -> new Ctx().em.find("E003").name());
        add("BB-12","Equivalence partition (unknown id)","FR4","EmployeeManager.find","find(E999)","null",
           () -> String.valueOf(new Ctx().em.find("E999")));
        add("BB-13","Boundary value (gross = 60,000)","FR5","PayrollService.calculateTax","calculateTax(60000)","0.00",
           () -> f(new Ctx().ps.calculateTax(60000)));
        add("BB-14","Boundary value (gross = 60,001)","FR5","PayrollService.calculateTax","calculateTax(60001)","0.20",
           () -> f(new Ctx().ps.calculateTax(60001)));
        add("BB-15","Boundary value (gross = 100,000)","FR5","PayrollService.calculateTax","calculateTax(100000)","8000.00",
           () -> f(new Ctx().ps.calculateTax(100000)));
        add("BB-16","Boundary value (gross = 100,001)","FR5","PayrollService.calculateTax","calculateTax(100001)","8000.30",
           () -> f(new Ctx().ps.calculateTax(100001)));
        add("BB-17","Boundary value (gross = -1)","FR5","PayrollService.calculateTax","calculateTax(-1)",IAE,
           () -> f(new Ctx().ps.calculateTax(-1)));
        add("BB-18","Equivalence partition (valid hours)","FR5","PayrollService.overtimePay","overtimePay(16000, 10)","1500.00",
           () -> f(new Ctx().ps.overtimePay(16000, 10)));
        add("BB-19","Boundary value (hours = 80)","FR5","PayrollService.overtimePay","overtimePay(16000, 80)","12000.00",
           () -> f(new Ctx().ps.overtimePay(16000, 80)));
        add("BB-20","Boundary value (hours = 81)","FR5","PayrollService.overtimePay","overtimePay(16000, 81)",IAE,
           () -> f(new Ctx().ps.overtimePay(16000, 81)));
        add("BB-21","Decision table (no overtime, tax 0%)","FR6","PayrollService.processPayment","processPayment(E001, 2026-10, 0)","net=57500.00",
           () -> "net=" + f(new Ctx().ps.processPayment("E001","2026-10",0).net()));
        add("BB-22","Decision table (overtime, tax 20%)","FR6","PayrollService.processPayment","processPayment(E002, 2026-10, 10)","net=86000.00",
           () -> "net=" + f(new Ctx().ps.processPayment("E002","2026-10",10).net()));
        add("BB-23","Decision table (tax 30% band)","FR6","PayrollService.processPayment","processPayment(E003, 2026-10, 0)","net=100000.00",
           () -> "net=" + f(new Ctx().ps.processPayment("E003","2026-10",0).net()));
        add("BB-24","Equivalence partition (invalid period)","FR6","PayrollService.processPayment","processPayment(E001, 2026-13, 0)",IAE,
           () -> new Ctx().ps.processPayment("E001","2026-13",0));
        add("BB-25","Error guessing (double payment)","FR6","PayrollService.processPayment","processPayment(E001, 2026-10, 0) twice",ISE,
           () -> { Ctx c = new Ctx(); c.ps.processPayment("E001","2026-10",0); return c.ps.processPayment("E001","2026-10",0); });
        add("BB-26","Equivalence partition (unknown employee)","FR6","PayrollService.processPayment","processPayment(E999, 2026-10, 0)",NSE,
           () -> new Ctx().ps.processPayment("E999","2026-10",0));
        add("BB-27","Equivalence partition (no data)","FR7","ReportService.payrollReport","payrollReport(2026-10) with no payments","No payments for 2026-10",
           () -> new Ctx().rs.payrollReport("2026-10"));
        add("BB-28","Equivalence partition (with data)","FR7","ReportService.payrollReport","pay E001 and E003 for 2026-10, then payrollReport(2026-10)","Total net: 157500.00",
           () -> { Ctx c = new Ctx(); c.ps.processPayment("E001","2026-10",0); c.ps.processPayment("E003","2026-10",0);
                   String[] l = c.rs.payrollReport("2026-10").split("\\R"); return l[l.length-1]; });
        add("BB-29","Equivalence partition (grouping)","FR7","ReportService.departmentNetTotals","pay E001, E002, E003 for 2026-10; get totals","IT=157500.00; HR=80000.00",
           () -> { Ctx c = new Ctx(); c.ps.processPayment("E001","2026-10",0); c.ps.processPayment("E002","2026-10",0);
                   c.ps.processPayment("E003","2026-10",0); var m = c.rs.departmentNetTotals("2026-10");
                   return "IT=" + f(m.get("IT")) + "; HR=" + f(m.get("HR")); });

        
        add("BB-30","Error guessing (same employee, different period)","FR6","PayrollService.processPayment","processPayment(E001, 2026-10, 0) then processPayment(E001, 2026-11, 0)","payments=2",
           () -> { Ctx c = new Ctx(); c.ps.processPayment("E001","2026-10",0); c.ps.processPayment("E001","2026-11",0);
                   return "payments=" + c.ps.payments().size(); });

        runAll("Black-box functional tests (specification based)", cases, "results-blackbox.tsv", "Black-box");
    }
}
