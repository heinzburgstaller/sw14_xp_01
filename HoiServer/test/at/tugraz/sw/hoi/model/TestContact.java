package at.tugraz.sw.hoi.model;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.api.datastore.dev.LocalDatastoreService;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class TestContact {

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig());

	@SuppressWarnings("static-access")
	@Before
	public void setUp() {
		helper.setUp();
		LocalDatastoreService dsService = (LocalDatastoreService) helper
				.getLocalService(LocalDatastoreService.PACKAGE);
		dsService.setNoStorage(true);
	}

	@After
	public void tearDown() {
		helper.tearDown();
	}

	@Test
	public void testCreate() {
		Contact contact = new Contact("heinz.burgstaller@gmail.com", "12345");
		contact.setEmail("clemens.binder@gmail.com");
		contact.setRegId("54321");
		contact.getId();
		Assert.assertTrue(contact != null);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testPersist() {
		String email = "heinz.burgstaller@gmail.com";

		EntityManager em = EMFService.get().createEntityManager();
		Contact contact = new Contact(email, "12345");
		em.persist(contact);
		em.close();

		em = EMFService.get().createEntityManager();

		Query q = em
				.createQuery("select c from Contact c where c.email = :email");
		q.setParameter("email", email);
		List<Contact> result = q.getResultList();

		Assert.assertTrue(result.size() == 1);

		em.close();
	}

}
