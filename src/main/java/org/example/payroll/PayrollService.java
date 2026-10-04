package org.example.payroll;

import java.util.*;


public class PayrollService {
    private final EmployeeManager em;
    private final List<Payslip> payments = new ArrayList<>();

    public PayrollService(EmployeeManager em) { this.em = em; }

    
    public double overtimePay(double basic, double hours) {
        if (hours < 0 || hours > 80) throw new IllegalArgumentException("Overtime hours must be 0..80");
        return basic / 160.0 * 1.5 * hours;
    }

    
    public double calculateTax(double gross) {
        if (gross < 0)          throw new IllegalArgumentException("Gross cannot be negative");
        if (gross <= 60000)     return 0;
        if (gross <= 100000)    return (gross - 60000) * 0.20;
        return 8000 + (gross - 100000) * 0.30;
    }

    
    public double pension(double basic) { return basic * 0.05; }

    public Payslip processPayment(String empId, String period, double otHours) {
        if (period == null || !period.matches("\\d{4}-(0[1-9]|1[0-2])"))
            throw new IllegalArgumentException("Period must be YYYY-MM");
        Employee e = em.find(empId);
        if (e == null) throw new NoSuchElementException("No such employee");
        for (Payslip p : payments)
            if (p.empId().equals(empId) && p.period().equals(period))
                throw new IllegalStateException("Already paid for " + period);
        double ot    = overtimePay(e.basic(), otHours);
        double gross = e.basic() + e.allowance() + ot;
        double tax   = calculateTax(gross);
        double pen   = pension(e.basic());
        Payslip p = new Payslip(empId, period, e.basic(), e.allowance(), ot, gross, tax, pen, gross - tax - pen);
        payments.add(p);
        return p;
    }

    public List<Payslip> payments() { return payments; }
}
