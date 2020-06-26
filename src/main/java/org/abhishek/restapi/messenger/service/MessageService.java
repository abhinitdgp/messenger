package org.abhishek.restapi.messenger.service;

import java.util.List;

import org.abhishek.restapi.messenger.database.DatabaseClass;
import org.abhishek.restapi.messenger.exception.DataNotFoundException;
import org.abhishek.restapi.messenger.model.Message;

public class MessageService {

	DatabaseClass db;
//	private Map<Long, Message> messages = db.getMessages();
	// private Map<Long, Message> messages = DatabaseClass.getMessages();

	public MessageService() {
		db = new DatabaseClass();
	}

	public List<Message> getAllMessages() {
		List<Message> messages = db.getMessages();
		return messages;
	}

	public Message getMessage(long id) {
		Message msg = db.getMessageById(id);
		if (msg == null) {
			throw new DataNotFoundException("Message with id : " + id + " not found");
		}
		return msg;
	}

	public Message addMessage(Message message) {
		Message msg = db.addMessage(message);
		return msg;
	}

	public boolean updateMessage(Message message) {
		if (message.getId() <= 0)
			return false;
		return db.updateMessage(message);
	}

	public boolean deleteMessage(long id) {
		boolean rowDeleted = db.deleteMessage(id);
		return rowDeleted;
	}

}
