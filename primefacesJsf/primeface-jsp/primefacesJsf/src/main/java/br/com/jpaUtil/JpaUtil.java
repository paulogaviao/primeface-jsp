package br.com.jpaUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaUtil {

	private static EntityManagerFactory factory =null;
	/*toda vez que chamado se não tiver sido criado ele o fara*/
	static {
		if(factory==null) {
			factory = Persistence.createEntityManagerFactory("primefacesJsf");
		}
	}
	
	public static EntityManager getEntityManager() {
		return factory.createEntityManager();
	}
	
}
