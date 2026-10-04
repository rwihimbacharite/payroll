package org.example.payroll;


public record Payslip(String empId, String period, double basic, double allowance,
                      double overtime, double gross, double tax, double pension, double net) { }
