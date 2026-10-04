package org.example.payroll;

import java.util.*;
import java.util.concurrent.Callable;
import org.example.payroll.TestSupport.Ctx;
import static org.example.payroll.TestSupport.*;


public class PayrollServiceWhiteBoxTest {
    static final List<Case> cases = new ArrayList<>();
    static void add(String id, String tech, String req, String feat, String in, String exp, Callable<Object> a) {
        cases.add(new Case(id, tech, req, feat, in, exp, a)); }

    public static void main(String[] args) throws Exception {

        add("WB-01","Basis path 2: gross<=60000","FR5","PayrollService.calculateTax","calculateTax(30000)","0.00",
           () -> f(new Ctx().ps.calculateTax(30000)));
        add("WB-02","Basis path 3: gross<=100000","FR5","PayrollService.calculateTax","calculateTax(80000)","4000.00",
           () -> f(new Ctx().ps.calculateTax(80000)));
        add("WB-03","Basis path 4: gross>100000","FR5","PayrollService.calculateTax","calculateTax(150000)","23000.00",
           () -> f(new Ctx().ps.calculateTax(150000)));
        add("WB-04","Condition coverage: hours>80","FR5","PayrollService.overtimePay (C2)","overtimePay(16000, 100)",IAE,
           () -> f(new Ctx().ps.overtimePay(16000,100)));
        add("WB-05","Loop testing: 0 iterations matching","FR7","ReportService.payrollReport","payrollReport(2026-10) no payments","No payments for 2026-10",
           () -> new Ctx().rs.payrollReport("2026-10"));

        runAll("White-box structural tests (code based)", cases, "results-whitebox.tsv", "White-box");
    }
}
