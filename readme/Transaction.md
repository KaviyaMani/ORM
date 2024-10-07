# Transaction Management
Need for Transaction Management:
    
    When we have a scenario which involves multiple transactions if transation no 3 fails, it is not good to store T1 and T2.
    By using transaction management instead of storing in database we will store in temporary memory. If all transaction success then only
    it will complete the transaction else it rollbacks.
    with JDBC we handle transation by - con.setAutoCommit(false);    // do something ...     con.commit();
    Spring does this for us we have to just annotate our class/method
## @Transactional - All or nothing
    Way to manage transaction in Spring Boot, used to define scope of transaction
    Can be applied to class level or method level, provides reliability and consistency
    when annotated with @Transactional, particular method should be execute within the context of transaction. 
    Spring **creates proxies** for all the classes annotated with @Transactional, So transactional logic can be applied before or after easily
    Add @EnableTransactionManagement  to main class, without this @transactional won't work
    If we have 2 operations save userDTO, save address with @Transational on method level if save address failed save userDTO won't be committed to db
    without the annotation userDTO will be saved but address won't be saved.
    can be applied to interfaces, classes, or directly on methods, precedence interface< superclass < class < interface method < superclass method < and class method.
### Limitations
    class level - applied to all public methods, private or protected methods **will be ignored** without error
    method level - applies to the method. even if method annotated with @Transactional if called from non transactional method of same class 
                    transactional will not be applied. If it is **called from external it applies**.
        public void method1(){ method2(); }
        @Transactional
        public void method2(){    }
    @Transactional **won't be applied** to method2, since it is **called from non transactional method inside same class**
    If both method and class level available method level takes precedence
## supports 
    Propagation Type - Spring calls TransactionManager::getTransaction to get or create a transaction according to the propagation. How to get the transaction for this particular transaction
    Isolation Level - one of the common ACID properties
    Timeout - transaction failed somewhere did not throw exception and got stuck
    readOnly flag – a hint for the persistence provider that the transaction should be read only
    Rollback rules - Rollback happens only for runtime unchecked exception, for checked exception rollback won't be triggered.

### Propagation 
@Transactional(propagation = Propagation.REQUIRES_NEW)

    REQUIRED - Default, start a new transaction if nothing exists, else use the currently active one
    SUPPORTS - use if one exists. else execute without a transactional context.
    MANDATORY - use if one exists else throw an Exception.
    NEVER - throw an Exception if the method gets called in the context of an active transaction.
    NOT_SUPPORTED - suspend an active transaction and to execute the method without any transactional context.
    REQUIRES_NEW  - Spring suspends the current transaction if it exists, and then creates a new one
    NESTED - to start a new transaction if the method gets called without an active transaction. If it gets called with an active transaction, Spring sets a savepoint and rolls back to that savepoint if an Exception occurs.

### Transaction Isolation
how much a transaction may be impacted by the activities of other concurrent transactions.
**isolation only applies when a new transaction is created**. We can set the isolation level for a method or class
@Transactional(isolation = Isolation.SERIALIZABLE)

    1. Dirty read: read the uncommitted change of a concurrent transaction
    2. Nonrepeatable read: get different value on re-read of a row if a concurrent transaction updates the same row and commits
    3. Phantom read: get different rows after re-execution of a range query if another transaction adds or removes some rows in the range and commits
    DEFAULT - 
    READ_UNCOMMITTED - lowest isolation level and allows for the most concurrent access. It reads uncommitted data of other concurrent transactions. Hence non-repeatable and phantom reads happen
    READ_COMMITTED - second level of isolation, prevents dirty reads, other 2 happens
    REPEATABLE_READ - third level of isolation, prevents dirty, and non-repeatable reads. phantom read happen. It does not allow simultaneous access to same row, Hence concurrent update can't happen
    SERIALIZABLE - highest level of isolation, prevents all. lowest concurrent access. since it executes concurrent calls sequentially

### DB provider level isolation
    Postgres does not support READ_UNCOMMITTED isolation and falls back to READ_COMMITED
    Oracle does not support or allow READ_UNCOMMITTED
    READ_COMMITTED is the default level with Postgres, SQL Server, and Oracle.
    REPEATABLE_READ is the default level in Mysql
    Oracle does not support REPEATABLE_READ.

## Timeout
somewhere in a transaction fails, but it doesn't throw an exception. the thread executing that transaction also stops and holds onto a database connection. 
This increases the wait time for other threads. can add timeout value to transactions

    can specify cancel or rollback
    @Transactional(timeout = 5)

## Read-Only 
transactions that only query data. They do not alter data
    
    @Transactional(readOnly = true) 
    won't throw exception if the method does other than reading, 
    it is just a info to hibernate not to manage some things since it is going to be read only

## Rollback
allows you to control what exceptions should trigger a rollback and which exceptions shouldn't
Change these values with caution and only if necessary

    @Transactional(rollbackFor = {IOException.class, ArithmeticException.class})
    public void rollbackFor() throws IOException, ArithmeticException {

    @Transactional(noRollbackFor = InterruptedException.class)
    public void noRollbackFor() throws InterruptedException{

Final note:

    when you change isolation settings using @Transactional, 
    if no new transaction is created when the method is run using the existing one, 
    Spring will not apply the @Transactional settings on that method to the running transaction. 
    This holds true for timeout, readOnly, rollbackFor, and noRollbackFor. If you set propagation to SUPPORTS, 
    don't expect any other parameters set on the same @Transactional annotation to have any effect.

    Since all this **applied only when new transaction is created** not with existing


https://codingnomads.com/spring-data-jpa-repository-common-issues
