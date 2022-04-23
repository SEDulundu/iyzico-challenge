# iyzico coding challenge

# Question 1: Product Management

Product Controller created
Product Service created
Payment Controller created
Payment Service created

Optimistic lock is used for concurrency problem. (ReentrantLock for single instance app)

You can find the details in the attached file. (ReadMe.txt)

# Question 2 : Latency Management

removed @Transactional annotation.

## Requirements

* DB connection pool should stay the same.
* BankService.java, PaymentServiceClients.java and IyzicoPaymentServiceTest.java classes should not be changed.
* In case of an error, there should not be any dirty data in the database.

# Test Coverage Result
 
You can find the details in the attached file. (ReadMe.txt)

# How to start the app

1-
datasource url : jdbc:hsqldb:hsql:/localhost:9001/db default yml.
edited datasource url from me: jdbc:hsqldb:hsql://localhost:9001/db

2- 
hsqldb-2.4.0 is used.

app
PS C:\Users\Emre\Desktop\hsqldb-2.4.0\hsqldb\data> java -classpath ../lib/hsqldb.jar org.hsqldb.server.Server --database.0 file:hsqldb/db --dbname.0 db

test
PS C:\Users\Emre\Desktop\hsqldb-2.4.0\hsqldb\data> java -classpath ../lib/hsqldb.jar org.hsqldb.server.Server --database.0 file:hsqldb/db/test --dbname.0 test

3- 
run application on ide

