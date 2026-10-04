package org.example.payroll;

import java.util.*;


public class ReportService {
    private final EmployeeManager em;
    private final PayrollService ps;

    public ReportService(EmployeeManager em, PayrollService ps) { this.em = em; this.ps = ps; }

    public String payrollReport(String period) {
        StringBuilder sb = new StringBuilder();
        double total = 0;
        int n = 0;
        for (Payslip p : ps.payments()) {
            if (!p.period().equals(period)) continue;
            Employee e = em.find(p.empId());
            sb.append(String.format("%s %s net=%.2f%n", p.empId(), e == null ? "?" : e.name(), p.net()));
            total += p.net();
            n++;
        }
        if (n == 0) return "No payments for " + period;
        sb.append(String.format("Total net: %.2f", total));
        return sb.toString();
    }

    public Map<String, Double> departmentNetTotals(String period) {
        Map<String, Double> m = new TreeMap<>();
        for (Payslip p : ps.payments()) {
            if (!p.period().equals(period)) continue;
            Employee e = em.find(p.empId());
            String d = (e == null) ? "UNKNOWN" : e.dept();
            m.merge(d, p.net(), Double::sum);
        }
        return m;
    }
}
