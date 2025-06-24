Hibernate Associations (Relationships)**

Hibernate supports mapping **object relationships** to **database foreign keys**. These associations follow typical OOP relationships:

* **One-to-One**
* **One-to-Many**
* **Many-to-One**
* **Many-to-Many**

---

## 🔹 1. **One-to-One Association**

Each entity instance is related to **one and only one** of another entity.

### Example: `User` and `Profile`

```java
@Entity
public class User {
    @Id
    private int id;
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id") // FK to Profile
    private Profile profile;
}

@Entity
public class Profile {
    @Id
    private int id;
    private String email;
    private String address;
}
```

* A `User` has one `Profile`.
* `@JoinColumn` tells Hibernate which column is the FK.

---

## 🔹 2. **One-to-Many / Many-to-One**

These go hand-in-hand:

* **One-to-Many**: One parent, many children.
* **Many-to-One**: Many children link back to one parent.

### Example: `Department` and `Employee`

```java
@Entity
public class Department {
    @Id
    private int id;
    private String name;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    private List<Employee> employees = new ArrayList<>();
}

@Entity
public class Employee {
    @Id
    private int id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "department_id") // FK to Department
    private Department department;
}
```

* `mappedBy` means **`Employee.department` owns the relation**.
* Avoid placing `@JoinColumn` on both sides unless unidirectional.

---

## 🔹 3. **Many-to-Many**

Each entity can relate to **multiple of the other**.

### Example: `Student` and `Course`

```java
@Entity
public class Student {
    @Id
    private int id;
    private String name;

    @ManyToMany
    @JoinTable(
        name = "student_course",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courses = new ArrayList<>();
}

@Entity
public class Course {
    @Id
    private int id;
    private String title;

    @ManyToMany(mappedBy = "courses")
    private List<Student> students = new ArrayList<>();
}
```

* Requires a **join table**.
* You can make it **unidirectional** if you don't need both sides.

---

## 🔹 Cascade Types (important!)

Used to propagate operations:

* `PERSIST`: saves child when parent is saved
* `MERGE`: merges changes
* `REMOVE`: deletes child when parent is deleted
* `ALL`: applies all of the above

Example:

```java
@OneToOne(cascade = CascadeType.ALL)
```

---

## 🔹 Fetch Types

Hibernate can **eagerly** or **lazily** load associations.

| FetchType                             | Behavior                         |
| ------------------------------------- | -------------------------------- |
| `EAGER`                               | Immediately loads related entity |
| `LAZY` (default for @OneToMany, etc.) | Loads only on access             |

Example:

```java
@OneToMany(fetch = FetchType.LAZY)
```

> Lazy loading is more efficient, but you must keep the session open until the collection is accessed.

---

## 🔸 Summary Table

| Relationship | Annotation    | Direction  | Notes                       |
| ------------ | ------------- | ---------- | --------------------------- |
| One-to-One   | `@OneToOne`   | Uni/Bi     | Use `@JoinColumn`           |
| One-to-Many  | `@OneToMany`  | Often Bi   | `mappedBy` = back-reference |
| Many-to-One  | `@ManyToOne`  | Always Uni | Holds FK                    |
| Many-to-Many | `@ManyToMany` | Bi or Uni  | Uses join table             |

---

## 🔸 Common Mistakes to Avoid

* Forgetting `mappedBy` in bidirectional mapping
* Using `EAGER` fetching on large collections (performance issue)
* Not closing session before accessing `LAZY` collections
* Not using `cascade` when saving related entities




//Question.java

package com.map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Question {
	@Id
	@Column(name="question_id")
    private int questionId;
    private String question;
    @OneToOne
    private Answer answer;
	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public Answer getAnswer() {
		return answer;
	}
	public void setAnswer(Answer answer) {
		this.answer = answer;
	}
	public Question(int questionId, String question, Answer answer) {
		super();
		this.questionId = questionId;
		this.question = question;
		this.answer = answer;
	}
	public Question() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
}

---------------------------------------------
  
  //Answer.java
  
  package com.map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
@Entity
public class Answer {
	@Id
	@Column(name="answer_id")
    private int answerId;
    private String answer;
    @OneToOne(mappedBy = "answer") // Yha per hmne bta diya h ki mapping Question vali class m answer s ho chuki h
                                   // and Answer vali class m mapping vala column yha nhi bnega mapped by ki vjh s
    private Question question;
	public int getAnswerId() {
		return answerId;
	}
	public void setAnswerId(int answerId) {
		this.answerId = answerId;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	public Question getQuestion() {
		return question;
	}
	public void setQuestion(Question question) {
		this.question = question;
	}
	public Answer() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Answer(int answerId, String answer) {
		super();
		this.answerId = answerId;
		this.answer = answer;
	}
    
    
    
}
----------------------------------
  
  MapDemo.java
  
  
  package com.map;

import org.hibernate.SessionFactory;

import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.Session;



public class MapDemo {
    public static void main(String[] args) {
    	  Configuration cfg=new Configuration();
          cfg.configure("hibernate.cfg.xml");
          SessionFactory factory=cfg.buildSessionFactory();
          //Creating question
          
          Question q1=new Question();
          q1.setQuestionId(101);
          q1.setQuestion("What is Java");
          
          //Creating answer
          
          Answer answer=new Answer();
          answer.setAnswerId(201);
          answer.setAnswer("Java is a programming language");
          answer.setQuestion(q1);
          q1.setAnswer(answer);
          
   //Creating question
          
          Question q2=new Question();
          q2.setQuestionId(102);
          q2.setQuestion("What is collection framework");
          
          //Creating answer
          
          Answer answer2=new Answer();
          answer2.setAnswerId(202);
          answer2.setAnswer("API to work with object in java");
          answer2.setQuestion(q2);
          q2.setAnswer(answer2);
          
          
          //we need session to save the data
          
          Session session=factory.openSession();
          Transaction tx=session.beginTransaction();
          
         session.save(q1);
       session.save(q2);
          session.save(answer);
          session.save(answer2);
          
          
         tx.commit();
         
         
         //Fetch the details
         
         Question newQ=(Question)session.get(Question.class,102);
         System.out.println(newQ.getQuestion());
         System.out.println(newQ.getAnswer().getAnswer());
         
          session.close();
          factory.close();
	}
}









Question.java

package com.map;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Question {
	@Id
	@Column(name="question_id")
    private int questionId;
    private String question;
   
    @OneToMany(mappedBy="question")
    private List<Answer> answers;
    
	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	
	public List<Answer> getAnswers() {
		return answers;
	}
	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}
	
	public Question(int questionId, String question, List<Answer> answers) {
		super();
		this.questionId = questionId;
		this.question = question;
		this.answers = answers;
	}
	public Question() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
}
-----------------------------------
  
  Answer.java
  
  package com.map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
@Entity
public class Answer {
	@Id
	@Column(name="answer_id")
    private int answerId;
    private String answer;
    
   @ManyToOne                                
    private Question question;
	public int getAnswerId() {
		return answerId;
	}
	public void setAnswerId(int answerId) {
		this.answerId = answerId;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	public Question getQuestion() {
		return question;
	}
	public void setQuestion(Question question) {
		this.question = question;
	}
	public Answer() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Answer(int answerId, String answer) {
		super();
		this.answerId = answerId;
		this.answer = answer;
	}
    
    
    
}
------------------------------
  
  MapDemo.java
  
  package com.map;

import org.hibernate.SessionFactory;

import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;

import org.hibernate.Session;



public class MapDemo {
    public static void main(String[] args) {
    	  Configuration cfg=new Configuration();
          cfg.configure("hibernate.cfg.xml");
          SessionFactory factory=cfg.buildSessionFactory();
          //Creating question
          
          Question q1=new Question();
          q1.setQuestionId(101);
          q1.setQuestion("What is Java");
          
          //Creating answer
          
          Answer answer=new Answer();
          answer.setAnswerId(201);
          answer.setAnswer("Java is a programming language");
          answer.setQuestion(q1);
          
          Answer answer1=new Answer();
          answer1.setAnswerId(202);
          answer1.setAnswer("used to create webapp");
          answer1.setQuestion(q1);
          
          Answer answer2=new Answer();
          answer2.setAnswerId(203);
          answer2.setAnswer("It has multiple framework");
          answer2.setQuestion(q1);
          
          
          ArrayList<Answer> list= new ArrayList<Answer>();
          list.add(answer);
          list.add(answer1);
          list.add(answer2);
          
          q1.setAnswers(list);
          
          //we need session to save the data
          
          Session session=factory.openSession();
          Transaction tx=session.beginTransaction();
          
          //save
          session.save(q1);
          session.save(answer);
          session.save(answer1);
          session.save(answer2);
          
          
         tx.commit();
         
         
         //Fetch the details
         Question newQ=(Question)session.get(Question.class,101);
         System.out.println(newQ.getQuestion());
		 for(Answer a:newQ.getAnswers()) {
			 System.out.println(a.getAnswer());
		 }
         
          session.close();
          factory.close();
	}
}










Emp.java

package com.map1;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Emp {
	@Id
 private int eid;
 private String name;
 
 // One Employee can have many Projects.
 @ManyToMany
 private List<Project> projects;

public int getEid() {
	return eid;
}

public void setEid(int eid) {
	this.eid = eid;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public List<Project> getProjects() {
	return projects;
}

public void setProjects(List<Project> projects) {
	this.projects = projects;
}

public Emp(int eid, String name, List<Project> projects) {
	super();
	this.eid = eid;
	this.name = name;
	this.projects = projects;
}

public Emp() {
	super();
	// TODO Auto-generated constructor stub
}
 
 
}
--------------------------------------
  
  Project.java
  
  package com.map1;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Project {
	@Id
   private int pid;
	@Column(name="project_name")
   private String projectName;
	
	//One Project can be assigned to many Employee
	@ManyToMany(mappedBy = "projects")
	private List<Emp> emps;

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public List<Emp> getEmps() {
		return emps;
	}

	public void setEmps(List<Emp> emps) {
		this.emps = emps;
	}

	public Project(int pid, String projectName, List<Emp> emps) {
		super();
		this.pid = pid;
		this.projectName = projectName;
		this.emps = emps;
	}

	public Project() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
---------------------------------
  
  MappingDemo.java
  
  package com.map1;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;

import org.hibernate.Transaction;

public class MappingDemo {

	public static void main(String[] args) {
		Configuration cfg=new Configuration();
		cfg.configure("hibernate.cfg.xml");
		SessionFactory factory=cfg.buildSessionFactory();
		
		Emp e1=new Emp();
		Emp e2=new Emp();
		e1.setEid(1);
		e1.setName("Sahil");
		e2.setEid(2);
		e2.setName("Rishabh");
		
		Project p1=new Project();
		Project p2=new Project();
		
		p1.setPid(11);
		p1.setProjectName("Advia");
		
		p2.setPid(22);
		p2.setProjectName("WBT");
		
		
		ArrayList <Emp> list1=new ArrayList<Emp>();
		ArrayList <Project> list2=new ArrayList<Project>();
		
		list1.add(e1);
		list1.add(e2);
		
		list2.add(p1);
		list2.add(p2);
		
		//Here we assigned two projects to emp1
		e1.setProjects(list2);
		//Here, We assigned 2 employees to project 2
		p2.setEmps(list1);
		
		Session s=factory.openSession();
		Transaction tx=s.beginTransaction();
		
		s.save(e1);
		s.save(e2);
		s.save(p1);
		s.save(p2);
		
		
		
		tx.commit();
		s.close();
		factory.close();
	}
}
