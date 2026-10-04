Employee Payroll System - source, tests and fault injection
Build (Maven):   mvn test-compile
Build (javac):   javac -d target/classes src/main/java/org/example/payroll/*.java
                 javac -cp target/classes -d target/test-classes src/test/java/org/example/payroll/*.java
Run tests:       java -cp "target/classes:target/test-classes" org.example.payroll.PayrollServiceBlackBoxTest
                 java -cp "target/classes:target/test-classes" org.example.payroll.PayrollServiceWhiteBoxTest
                 (use ; instead of : on Windows)
Extended suite (58 cases): extended-tests/ - copy the two files over src/test/java/org/example/payroll/ to run it.
