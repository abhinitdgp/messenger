package org.abhishek.restapi.messenger.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.*;

import org.abhishek.restapi.messenger.model.Comment;
import org.abhishek.restapi.messenger.service.CommentService;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CommentResource {
	CommentService commentService;

	public CommentResource() {
		commentService = new CommentService();
	}

	@GET
	public List<Comment> getAllComments(@PathParam("messageId") long message_id) {
		List<Comment> comments = commentService.getAllComments(message_id);
		return comments;
	}

	@POST
	public Comment addComment(@PathParam("messageId") long messageId, Comment comment) {
		Comment newComment = commentService.addComment(messageId, comment);
		return newComment;
	}

	@GET
	@Path("/{commentId}")
	public Comment getCommentById(@PathParam("messageId") long messageId, @PathParam("commentId") long commentId) {
		Comment comment = commentService.getCommentById(messageId, commentId);
		return comment;
	}

	@PUT
	@Path("/{commentId}")
	public boolean updateComment(@PathParam("messageId") long messageId, @PathParam("commentId") long commentId,
			Comment comment) {
		comment.setId(commentId);
		// Comment com = commentService.updateComment(messageId, commentId, comment);
		boolean com = commentService.updateComment(messageId, commentId, comment);
		return com;
	}

	@DELETE
	@Path("/{commentId}")
	public boolean deleteComment(@PathParam("messageId") long messageId, @PathParam("commentId") long commentId) {
		return commentService.deleteComment(messageId, commentId);
	}

}
