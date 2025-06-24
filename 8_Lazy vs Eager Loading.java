Hibernate Lazy vs Eager Loading**

Hibernate associations like `@OneToMany`, `@ManyToOne`, etc., have a **fetch type** that determines **when** the associated data is loaded from the database.

---

### 🔹 1. **Lazy Loading** (Default for collections)

> Data is **fetched only when accessed** — not during initial entity load.

```java
@OneToMany(fetch = FetchType.LAZY)
private List<Order> orders;
```

Hibernate **creates a proxy**, and the database query is fired **only when `orders` is accessed**.

#### ✅ Pros:

* Faster startup time
* Saves memory and DB load

#### ❌ Cons:

* Requires session to stay open (else: `LazyInitializationException`)
* Can lead to **N+1 select problem** if not managed well

---

### 🔹 2. **Eager Loading**

> Related data is **fetched immediately**, along with the main entity.

```java
@OneToMany(fetch = FetchType.EAGER)
private List<Order> orders;
```

Hibernate fetches both `Customer` and `Order` tables in one go (usually using **JOIN**).

#### ✅ Pros:

* Simple, less error-prone
* No risk of `LazyInitializationException`

#### ❌ Cons:

* Slower queries if unused data is fetched
* Bad for large associations

---

### 🔹 Lazy vs Eager: Example

```java
Customer customer = session.get(Customer.class, 1);
System.out.println(customer.getName());         // OK

System.out.println(customer.getOrders());       // LAZY: Now triggers a DB query
```

---

### 🔸 LazyInitializationException

Occurs if you:

* Fetch an entity with **lazy-loaded associations**
* Close the session
* Then try to access those associations

#### Example that will fail:

```java
Customer customer = session.get(Customer.class, 1);
session.close();
customer.getOrders(); // ❌ Throws LazyInitializationException
```

---

### 🔸 How to Avoid It?

1. **Access everything within session scope**

```java
try (Session session = factory.openSession()) {
    Customer customer = session.get(Customer.class, 1);
    customer.getOrders().size(); // trigger fetch
}
```

2. **Use JOIN FETCH**

```java
String hql = "FROM Customer c JOIN FETCH c.orders WHERE c.id = :id";
Customer customer = session.createQuery(hql, Customer.class)
                           .setParameter("id", 1)
                           .uniqueResult();
```

3. **Switch to EAGER (not recommended globally)**

Only if you’re sure the related data is always needed.

---

### 🔹 Summary Table

| Fetch Type | When Loaded   | Use Case                        |
| ---------- | ------------- | ------------------------------- |
| `LAZY`     | When accessed | Default; better performance     |
| `EAGER`    | Immediately   | When relationship always needed |

---

## 🔸 Pro Tip: Use LAZY by Default + JOIN FETCH when needed

That way:

* You control what gets fetched
* You avoid huge automatic joins
* You prevent N+1 problems by prefetching correctly

