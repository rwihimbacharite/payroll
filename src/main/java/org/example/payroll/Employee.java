package org.example.payroll;

/** An employee record: id, name, department, monthly basic salary and fixed allowance. */
public record Employee(String id, String name, String dept, double basic, double allowance) { }
