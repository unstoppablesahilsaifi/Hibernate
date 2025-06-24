Hibernate Transactions and Concurrency**

---

### 🔹 What is a Transaction?

A **transaction** is a set of operations that either **all succeed** or **all fail**. It ensures the **ACID** properties:

* **A**tomicity: All operations succeed or none.
* **C**onsistency: DB remains valid after transaction.
* **I**solation: Transactions don’t interfere with each other.
* **D**urability: Once committed, changes persist.

Hibernate uses **`Transaction` API** to handle DB transactions.

---

### 🔸 Basic Transaction Usage in Hibernate

```java
Session session = HibernateUtil.getSessionFactory().openSession();
Transaction tx = null;

try {
    tx = session.beginTransaction();

    // Some DB operations
    User user = new User();
    user.setName("Alice");
    session.save(user);

    tx.commit();  // Persist changes
} catch (Exception e) {
    if (tx != null) tx.rollback();  // Undo changes on error
    e.printStackTrace();
} finally {
    session.close();
}
```

---

### 🔹 Transaction Best Practices

* Always **commit or rollback** the transaction.
* Use `try-catch-finally` to handle exceptions.
* Avoid long transactions — keep them short and efficient.

---

## 🔸 Concurrency in Hibernate

When multiple users or threads access the same data, **concurrency control** is required to avoid:

* Lost updates
* Dirty reads
* Non-repeatable reads

---

### 🔹 Isolation Levels (from JDBC)

Hibernate relies on **JDBC isolation levels**, such as:

* **READ\_COMMITTED** (default)
* **REPEATABLE\_READ**
* **SERIALIZABLE**

Set using:

```xml
<property name="hibernate.connection.isolation">2</property> <!-- READ_COMMITTED -->
```

---

## 🔸 Hibernate Concurrency Control Mechanisms

Hibernate offers **two main strategies**:

---

### 1. **Optimistic Locking** (Recommended)

* Assumes **rare conflicts**.
* Uses a **version number** or timestamp to detect conflicts.
* If two users modify the same record, the second one gets an error.

#### Example:

```java
@Entity
public class Product {
    @Id
    private int id;

    private String name;

    @Version
    private int version; // Hibernate manages this
}
```

Hibernate automatically increments the `version` field on update. If the version doesn’t match the one in DB, it throws `StaleObjectStateException`.

---

### 2. **Pessimistic Locking**

* Assumes **high conflict**.
* Locks rows in the DB to prevent concurrent updates.

#### Example:

```java
Product product = session.get(Product.class, 1, LockMode.PESSIMISTIC_WRITE);
```

This locks the row for writing, so no one else can update it until the current transaction finishes.

You can also use `@Lock` in JPA or add `FOR UPDATE` using native SQL.

---

## 🔸 When to Use What?

| Use Case                      | Strategy                    |
| ----------------------------- | --------------------------- |
| Rare updates, read-heavy      | Optimistic Locking          |
| Frequent updates, write-heavy | Pessimistic Locking         |
| Long-lived sessions           | Avoid – breaks concurrency! |

---

## 🔹 Summary

| Concept                      | Description                   |
| ---------------------------- | ----------------------------- |
| `Transaction`                | Groups DB operations together |
| `tx.commit()`                | Applies all changes           |
| `tx.rollback()`              | Reverts all changes on error  |
| `@Version`                   | Enables optimistic locking    |
| `LockMode.PESSIMISTIC_WRITE` | Prevents other writes         |

