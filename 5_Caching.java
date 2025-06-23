Hibernate Caching
Caching is used to reduce the number of database calls by storing frequently accessed data in memory.
caching to improve application performance by reducing database access:
Hibernate supports two main types of caching:


1. First-Level Cache (Session-Level)- 
Enabled by default.
Tied to the Hibernate Session.
Automatically caches entities loaded in the current session.
Only works within the same session.

➤ Example:
Session session = HibernateUtil.getSessionFactory().openSession();

User user1 = session.get(User.class, 1); // DB hit
User user2 = session.get(User.class, 1); // No DB hit — from session cache

session.close();
Even though get() is called twice, only the first call hits the DB. The second call retrieves the object from the session cache.


2. Second-Level Cache (SessionFactory-Level)
Must be enabled manually.
Shared across multiple sessions.
Persists data across sessions using external caching tools (e.g., Ehcache, Infinispan, Hazelcast).
Suitable for read-mostly or reference data (like countries, product categories, etc.)

🛠️ Steps to Enable 2nd Level Cache
✅ 1. Add Dependencies (example with Ehcache):
For Maven:

xml

<dependency>
    <groupId>org.hibernate.orm</groupId>
    <artifactId>hibernate-jcache</artifactId>
    <version>6.4.4.Final</version>
</dependency>

<dependency>
    <groupId>org.ehcache</groupId>
    <artifactId>ehcache</artifactId>
    <version>3.10.8</version>
</dependency>
✅ 2. Configure hibernate.cfg.xml:
xml
Copy
Edit
<property name="hibernate.cache.use_second_level_cache">true</property>
<property name="hibernate.cache.region.factory_class">jcache</property>
<property name="hibernate.javax.cache.provider">org.ehcache.jsr107.EhcacheCachingProvider</property>
✅ 3. Annotate Entity:
java
Copy
Edit
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Employee { ... }
READ_ONLY is best for static data. Use READ_WRITE for updatable entities.

🔹 Query Cache (Optional)
You can cache query results too:

✅ Enable in config:
xml
Copy
Edit
<property name="hibernate.cache.use_query_cache">true</property>
  
✅ Mark query as cacheable:

Query query = session.createQuery("FROM Employee WHERE department = :dept");
query.setParameter("dept", "IT");
query.setCacheable(true);


📌 When to Use Second-Level Cache?
When you have data that's read frequently but rarely updated.
Read-heavy applications
For data shared across users (e.g., country lists, permissions).
Avoid on rapidly changing transactional data.
