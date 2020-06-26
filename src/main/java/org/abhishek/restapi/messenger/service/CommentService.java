package org.abhishek.restapi.messenger.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.abhishek.restapi.messenger.database.DatabaseClass;
import org.abhishek.restapi.messenger.model.Comment;
import org.abhishek.restapi.messenger.model.ErrorMessage;

public class CommentService {
	DatabaseClass db;

	public CommentService() {
		db = new DatabaseClass();
	}

	public List<Comment> getAllComments(long messageId) {
		List<Comment> comments = new ArrayList<>();
		comments = db.getAllComments(messageId);
		return comments;
	}

	public Comment getCommentById(long messageId, long commentId) {
		ErrorMessage errorMessage = new ErrorMessage(404, "Not Found", "https://eclipse-ee4j.github.io/");
		Response response = Response.status(Status.NOT_FOUND).entity(errorMessage).build();
		Comment comment = db.getCommentById(messageId, commentId);
		if (comment == null) {
			throw new NotFoundException(response);
		}
		return comment;
	}

	public Comment addComment(long messageId, Comment comment) {
		Comment pro = db.addComment(messageId, comment);
		return pro;
	}

	public boolean updateComment(long messageId, long commentId, Comment comment) {
		boolean CommentUpdated = db.updateComment(messageId, commentId, comment);
		return CommentUpdated;
	}

	public boolean deleteComment(long messageId, long commentId) {
		boolean CommentDeleted = db.deleteComment(messageId, commentId);
		return CommentDeleted;
	}
}
