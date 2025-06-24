package com.pegination;

import java.util.List;

import org.hibernate.Query.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import com.mavenlearn.Student;

public class HQLPegination {

	public static void main(String[] args) {
		 SessionFactory factory= new Configuration().configure().buildSessionFactory();
		  Session session=factory.openSession();
		Query<Student> query=session.createQuery("from Student");
		
		//Immplementing pegination using hibernate
		// kaha s suru krna h
		query.setFirstResult(0);
		// Kitne result chahiye
		query.setMaxResults(3);
		 List<Student> list=query.list();
		 for(Student st:list) {
			 System.out.println(st.getId()+" : "+st.getName()+" : "+st.getCity());
		 }
		 factory.close();
		 
	}
}











 Pagination in Hibernate (Using `setFirstResult()` and `setMaxResults()`)

Hibernate supports pagination using two key methods:

* `setFirstResult(int startPosition)` → OFFSET
* `setMaxResults(int maxResults)` → LIMIT

These methods work with both **HQL** and **Criteria API**.

---

### 🔸 1. Pagination using HQL

```java
String hql = "FROM Employee ORDER BY id";
Query<Employee> query = session.createQuery(hql, Employee.class);

// Pagination: page 1 (0-based), 10 records per page
query.setFirstResult(0);      // Start index (0 = first row)
query.setMaxResults(10);      // Page size

List<Employee> employees = query.list();
```

### Example: Paginate Page 2 (10 records per page)

```java
int pageNumber = 2;
int pageSize = 10;

query.setFirstResult((pageNumber - 1) * pageSize); // (2 - 1) * 10 = 10
query.setMaxResults(pageSize);
```

---

### 🔸 2. Pagination using Criteria API

```java
CriteriaBuilder cb = session.getCriteriaBuilder();
CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
Root<Employee> root = cq.from(Employee.class);
cq.select(root);

Query<Employee> query = session.createQuery(cq);

// Page 3, 5 records per page
query.setFirstResult(10);  // (3 - 1) * 5
query.setMaxResults(5);

List<Employee> result = query.getResultList();
```

---

### 🔸 3. Pagination with Native SQL (Optional)

```java
Query<Employee> query = session.createNativeQuery("SELECT * FROM employee ORDER BY id", Employee.class);
query.setFirstResult(20);    // OFFSET
query.setMaxResults(10);     // LIMIT
List<Employee> employees = query.list();
```

---

## 🔹 Best Practices

* Always use **`ORDER BY`** with pagination to keep result consistency
* Don't paginate without sorting — you may get unpredictable results
* For large datasets, consider using **indexed columns** for faster access

---

## 🔹 Optional: Total Count for Page Info

If you're building something like a UI table with `Page 1 of 12`, you also need the **total row count**.

```java
String countHql = "SELECT COUNT(e) FROM Employee e";
Long totalRecords = session.createQuery(countHql, Long.class).uniqueResult();
```

You can then calculate:

```java
int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
```

---

## 🔸 Summary

| Method                             | Purpose            |
| ---------------------------------- | ------------------ |
| `setFirstResult(n)`                | Offset (start row) |
| `setMaxResults(n)`                 | Limit (max rows)   |
| Use with HQL, Criteria, Native SQL | ✅ Supported        |

