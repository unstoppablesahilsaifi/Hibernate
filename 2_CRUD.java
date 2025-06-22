CRUD Operations in Hibernate**

Hibernate simplifies database operations using Java objects. CRUD stands for:

* **C**reate
* **R**ead
* **U**pdate
* **D**elete

We’ll use the `User` entity from the last step for examples.

#### Prerequisite:

Make sure you have:

* `hibernate.cfg.xml` properly configured.
* The `User` class annotated with `@Entity`.
* `HibernateUtil` for getting the `SessionFactory`.

---

### 1. **Create (Insert a new record)**

```java
import org.hibernate.Session;
import org.hibernate.Transaction;

User user = new User();
user.setName("Alice");
user.setEmail("alice@example.com");

Session session = HibernateUtil.getSessionFactory().openSession();
Transaction tx = session.beginTransaction();

session.save(user);  // insert

tx.commit();
session.close();
```

---

### 2. **Read (Fetch a record)**

Fetch by ID:

```java
Session session = HibernateUtil.getSessionFactory().openSession();

User user = session.get(User.class, 1);  // 1 is the ID
System.out.println(user.getName() + " - " + user.getEmail());

session.close();
```

---

### 3. **Update (Modify an existing record)**

```java
Session session = HibernateUtil.getSessionFactory().openSession();
Transaction tx = session.beginTransaction();

User user = session.get(User.class, 1);
user.setEmail("alice.new@example.com");

session.update(user);  // or just rely on automatic dirty checking

tx.commit();
session.close();
```

---

### 4. **Delete (Remove a record)**

```java
Session session = HibernateUtil.getSessionFactory().openSession();
Transaction tx = session.beginTransaction();

User user = session.get(User.class, 1);
session.delete(user);  // deletes the row from DB

tx.commit();
session.close();
```

---

### Summary

| Operation | Hibernate Method                                    |
| --------- | --------------------------------------------------- |
| Create    | `session.save()`                                    |
| Read      | `session.get()` or `session.load()`                 |
| Update    | `session.update()` or just modify object and commit |
| Delete    | `session.delete()`                                  |

