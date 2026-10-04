package org.example.payroll;

import java.util.*;
import java.util.concurrent.Callable;
import org.example.payroll.TestSupport.Ctx;
import static org.example.payroll.TestSupport.*;

/** White-box structural tests (code based) */
public class PayrollServiceWhiteBoxTest {
    static final List<Case> cases = new ArrayList<>();
    static void add(String id, String tech, String req, String feat, String in, String exp, Callable<Object> a) {
        cases.add(new Case(id, tech, req, feat, in, exp, a)); }

    public static void main(String[] args) throws Exception {
// ---------------- WHITE-BOX (structure based) ----------------
        add("WB-01","Condition coverage: id==null","FR1","EmployeeManager.validate (D1a)","add(Employee id=null)",IAE,
           () -> { new Ctx().em.add(emp(null,"Dave","IT",1,0)); return "accepted"; });
        add("WB-02","Condition coverage: id.isBlank()","FR1","EmployeeManager.validate (D1b)","add(Employee id=\"  \")",IAE,
           () -> { new Ctx().em.add(emp("  ","Dave","IT",1,0)); return "accepted"; });
        add("WB-03","Condition coverage: name==null","FR1","EmployeeManager.validate (D2a)","add(Employee name=null)",IAE,
           () -> { new Ctx().em.add(emp("E010",null,"IT",1,0)); return "accepted"; });
        add("WB-04","Condition coverage: name.isBlank()","FR1","EmployeeManager.validate (D2b)","add(Employee name=\" \")",IAE,
           () -> { new Ctx().em.add(emp("E010"," ","IT",1,0)); return "accepted"; });
        add("WB-05","Condition coverage: dept==null","FR1","EmployeeManager.validate (D3a)","add(Employee dept=null)",IAE,
           () -> { new Ctx().em.add(emp("E010","Dave",null,1,0)); return "accepted"; });
        add("WB-06","Condition coverage: dept.isBlank()","FR1","EmployeeManager.validate (D3b)","add(Employee dept=\"\")",IAE,
           () -> { new Ctx().em.add(emp("E010","Dave","",1,0)); return "accepted"; });
        add("WB-07","Decision coverage: basic<=0 true","FR1","EmployeeManager.validate (D4)","add(Employee basic=-500)",IAE,
           () -> { new Ctx().em.add(emp("E010","Dave","IT",-500,0)); return "accepted"; });
        add("WB-08","Decision coverage: allowance<0 true","FR1","EmployeeManager.validate (D5)","add(Employee allowance=-10)",IAE,
           () -> { new Ctx().em.add(emp("E010","Dave","IT",100,-10)); return "accepted"; });
        add("WB-09","Basis path: all decisions false","FR1","EmployeeManager.validate (path 9)","add(E010,Dave,IT,100,10)","count=4",
           () -> { Ctx c = new Ctx(); c.em.add(emp("E010","Dave","IT",100,10)); return "count=" + c.em.count(); });
        add("WB-10","Basis path 1: gross<0","FR5","PayrollService.calculateTax","calculateTax(-100)",IAE,
           () -> f(new Ctx().ps.calculateTax(-100)));
        add("WB-11","Basis path 2: gross<=60000","FR5","PayrollService.calculateTax","calculateTax(30000)","0.00",
           () -> f(new Ctx().ps.calculateTax(30000)));
        add("WB-12","Basis path 3: gross<=100000","FR5","PayrollService.calculateTax","calculateTax(80000)","4000.00",
           () -> f(new Ctx().ps.calculateTax(80000)));
        add("WB-13","Basis path 4: gross>100000","FR5","PayrollService.calculateTax","calculateTax(150000)","23000.00",
           () -> f(new Ctx().ps.calculateTax(150000)));
        add("WB-14","Condition coverage: hours<0","FR5","PayrollService.overtimePay (C1)","overtimePay(16000, -5)",IAE,
           () -> f(new Ctx().ps.overtimePay(16000,-5)));
        add("WB-15","Condition coverage: hours>80","FR5","PayrollService.overtimePay (C2)","overtimePay(16000, 100)",IAE,
           () -> f(new Ctx().ps.overtimePay(16000,100)));
        add("WB-16","Path: both conditions false","FR5","PayrollService.overtimePay","overtimePay(32000, 40)","12000.00",
           () -> f(new Ctx().ps.overtimePay(32000,40)));
        add("WB-17","Condition coverage: period==null","FR6","PayrollService.processPayment (P1a)","processPayment(E001, null, 0)",IAE,
           () -> new Ctx().ps.processPayment("E001",null,0));
        add("WB-18","Condition coverage: period fails regex","FR6","PayrollService.processPayment (P1b)","processPayment(E001, 202610, 0)",IAE,
           () -> new Ctx().ps.processPayment("E001","202610",0));
        add("WB-19","Decision coverage: employee == null","FR6","PayrollService.processPayment (P2)","processPayment(E404, 2026-10, 0)",NSE,
           () -> new Ctx().ps.processPayment("E404","2026-10",0));
        add("WB-20","Decision coverage: duplicate in loop","FR6","PayrollService.processPayment (P3)","processPayment(E002, 2026-10, 0) twice",ISE,
           () -> { Ctx c = new Ctx(); c.ps.processPayment("E002","2026-10",0); return c.ps.processPayment("E002","2026-10",0); });
        add("WB-21","Path: success, all decisions false","FR6","PayrollService.processPayment","processPayment(E002, 2026-11, 0); count stored","net=80000.00; stored=1",
           () -> { Ctx c = new Ctx(); var p = c.ps.processPayment("E002","2026-11",0);
                   return "net=" + f(p.net()) + "; stored=" + c.ps.payments().size(); });
        add("WB-22","Decision coverage: add duplicate branch","FR1","EmployeeManager.add","add(E002 Bob) again",IAE,
           () -> { new Ctx().em.add(emp("E002","Bob","HR",1,0)); return "accepted"; });
        add("WB-23","Decision coverage: update not-found branch","FR2","EmployeeManager.update","update(E404,...)",NSE,
           () -> { new Ctx().em.update(emp("E404","X","IT",1,0)); return "updated"; });
        add("WB-24","Decision coverage: remove not-found branch","FR3","EmployeeManager.remove","remove(E404)",NSE,
           () -> { new Ctx().em.remove("E404"); return "removed"; });
        add("WB-25","Loop testing: 0 iterations matching","FR7","ReportService.payrollReport","payrollReport(2026-10) no payments","No payments for 2026-10",
           () -> new Ctx().rs.payrollReport("2026-10"));
        add("WB-26","Loop testing: payments of another period skipped (continue branch)","FR7","ReportService.payrollReport","pay E001 for 2026-09; payrollReport(2026-10)","No payments for 2026-10",
           () -> { Ctx c = new Ctx(); c.ps.processPayment("E001","2026-09",0); return c.rs.payrollReport("2026-10"); });
        add("WB-27","Loop testing: 1 iteration","FR7","ReportService.payrollReport","pay E001 for 2026-10; payrollReport(2026-10)","Total net: 57500.00",
           () -> { Ctx c = new Ctx(); c.ps.processPayment("E001","2026-10",0);
                   String[] l = c.rs.payrollReport("2026-10").split("\\R"); return l[l.length-1]; });
        add("WB-28","Loop testing: many iterations + map merge branch","FR7","ReportService.departmentNetTotals","pay E001 and E003 (both IT) for 2026-10","IT=157500.00",
           () -> { Ctx c = new Ctx(); c.ps.processPayment("E001","2026-10",0); c.ps.processPayment("E003","2026-10",0);
                   return "IT=" + f(c.rs.departmentNetTotals("2026-10").get("IT")); });

        
        runAll("White-box structural tests (code based)", cases, "results-whitebox.tsv", "White-box");
    }
}
