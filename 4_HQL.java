== HQL does not support INSERT into tables directly. You must use Java entity and session.save() instead.

package com.hql;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.mavenlearn.Student;

public class HqlExample {

	public static void main(String[] args) {
		Configuration cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");
		SessionFactory factory = cfg.buildSessionFactory();
		Session s = factory.openSession();

		// HQL Syntax

		String query = "from Student";
		// It will return the object of query
		Query q = s.createQuery(query);
		
		//Single- Unique result
		//Multiple- List
		
		//It will give the list of Student
		List<Student> list=q.list();
		
		for(Student student:list) {
			 System.out.println(student.getName());
		}
		System.out.println("__________________________________________________");
		
		Transaction tx=s.beginTransaction();
//      Delete Query		
//		Query q2=s.createQuery("delete from Student where city=:c");
//		q2.setParameter("c","Bhjnpura");
//		int r= q2.executeUpdate();
//		System.out.println("Deleted");
//		System.out.println(r);

//      Update Query
		Query q2=s.createQuery("update Student set city=:c where name=:n");
		q2.setParameter("c", "Gzb");
		q2.setParameter("n", "Rishabh");
		int r= q2.executeUpdate();
		System.out.println(r+ "Object Updated");
		tx.commit();
		
		
		// How to Use Join in HQL
		Query q3=s.createQuery("select q.question,q.questionId,a.answer from Question as q INNER JOIN q.answers as a");
		
		List<Object []> list3=q3.getResultList();
		for(Object[] arr:list3) {
			System.out.println(Arrays.toString(arr));
		}
       
		s.close();
		factory.close();
	}

}
