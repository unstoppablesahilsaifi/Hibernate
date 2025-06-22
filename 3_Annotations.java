Annotations:

@Entity- use to mark any class as entity
@Table- use to change the table details.
@Id- use to mark column as id(Primary key)
@GeneratedValue- hibernate will automatically generate values for that using an internal sequence. Therefore, we don't have to set it manually.
@Column- can be used to specify column mapping. For example, to change the column name in the associated table in database.
@Transient- This tells hibernate not to save the fields.
@Temporal- @Temporal over a date field tells hibernate the format in which the data needs to be saved.
@Lob- this tells hibernate that this is a large object, not a simple object.

 Hibernate Annotations and Mapping**

Hibernate uses **annotations** to map Java classes to database tables, replacing older XML-based mappings.

We’ll cover:

1. `@Entity`, `@Table`
2. `@Id`, `@GeneratedValue`
3. Column mapping: `@Column`
4. Relationships:

   * `@OneToOne`
   * `@OneToMany`
   * `@ManyToOne`
   * `@ManyToMany`
5. Transient and embedded fields

---

### 1. **Basic Entity Mapping**

```java
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username", nullable = false, length = 100)
    private String name;

    @Column(unique = true)
    private String email;

    // getters and setters
}
```

* `@Entity`: Marks class as a persistent Hibernate entity.
* `@Table`: Specifies DB table name (optional).
* `@Id`: Marks primary key.
* `@GeneratedValue`: Auto-generates primary key values.
* `@Column`: Maps a field to a column, and lets you define constraints.

---

### 2. **Relationship Mappings**

#### a) One-to-One

```java
@Entity
public class Passport {
    @Id
    private int id;

    private String passportNumber;

    @OneToOne(mappedBy = "passport")
    private User user;
}
```

```java
@Entity
public class User {
    @Id
    private int id;

    private String name;

    @OneToOne
    @JoinColumn(name = "passport_id")
    private Passport passport;
}
```

#### b) One-to-Many and Many-to-One

One user has many orders.

```java
@Entity
public class User {
    @Id
    private int id;

    private String name;

    @OneToMany(mappedBy = "user")
    private List<Order> orders;
}

@Entity
public class Order {
    @Id
    private int id;

    private String product;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
```

#### c) Many-to-Many

Users can have many roles, and roles can belong to many users.

```java
@Entity
public class User {
    @Id
    private int id;

    private String name;

    @ManyToMany
    @JoinTable(name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;
}

@Entity
public class Role {
    @Id
    private int id;

    private String roleName;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;
}
```

---

### 3. **Other Annotations**

* `@Transient`: Marks a field to be ignored by Hibernate.
* `@Embedded`: Embeds a value object (reused structure).

Example:

```java
@Embeddable
public class Address {
    private String city;
    private String state;
}

@Entity
public class User {
    @Id
    private int id;

    private String name;

    @Embedded
    private Address address;
}
