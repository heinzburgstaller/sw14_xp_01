package at.tugraz.sw.hoi.model;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NoResultException;

@Entity
public class Contact {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String email;

	private String regId;

	public Contact(String email, String regId) {
		super();
		this.email = email;
		this.regId = regId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRegId() {
		return regId;
	}

	public void setRegId(String regId) {
		this.regId = regId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public static Contact findByEmail(String email, EntityManager em) {
		try {
			return (Contact) em
					.createQuery(
							"select c from Contact c where c.email = :email")
					.setParameter("email", email).getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}

}
