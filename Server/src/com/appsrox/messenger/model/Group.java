package com.appsrox.messenger.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Query;

@Entity
public class Group {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String chatId;
	private String members;
	
	public Group() {}
	
	public Group(String chatId) {
		this.chatId = chatId;
	}
	
	public static Group find(String chatId, EntityManager em) {
		Query q = em.createQuery("select g from Group g where g.chatId = :chatId");
		q.setParameter("chatId", chatId);
		List<Group> result = q.getResultList();
		
		if (!result.isEmpty()) {
			return result.get(0);
		}
		return null;
	}
	
	public Long getId() {
		return id;
	}
	public String getChatId() {
		return chatId;
	}
	public void setChatId(String chatId) {
		this.chatId = chatId;
	}
	public String getMembers() {
		return members;
	}
	public void setMembers(String members) {
		this.members = members;
	}
	
}
