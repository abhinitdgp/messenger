package org.abhishek.restapi.messenger.resources;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import org.abhishek.restapi.messenger.model.Message;
import org.abhishek.restapi.messenger.service.MessageService;
import org.glassfish.jersey.server.Uri;

@Path("/messages")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MessageResource {

	MessageService messageService = new MessageService();

	@GET
	public List<Message> getJsonMessages() {
		return messageService.getAllMessages();
	}

	@GET
	@Produces(MediaType.APPLICATION_XML)
	public List<Message> getXmlMessages() {
		return messageService.getAllMessages();
	}

	@GET
	@Path("/{messageId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Message getMessageById(@PathParam("messageId") long messageId, @Context UriInfo uriInfo) {
		Message message = messageService.getMessage(messageId);
		message.addLink(getUriForSelf(message, uriInfo), "self");
		message.addLink(getUriForProfile(message, uriInfo), "profile");
		message.addLink(getUriForComment(message, uriInfo), "comments");
		return message;
	}

	private String getUriForComment(Message message, UriInfo uriInfo) {
		String uri = uriInfo.getBaseUriBuilder().path(MessageResource.class)
				.path(MessageResource.class, "getCommentResource").resolveTemplate("messageId", message.getId())
				.path(CommentResource.class).build().toString();
		return uri;
	}

	private String getUriForProfile(Message message, UriInfo uriInfo) {
		String uri = uriInfo.getBaseUriBuilder().path(ProfileResource.class).path(message.getAuthor()).build()
				.toString();
		return uri;
	}

	private String getUriForSelf(Message message, UriInfo uriInfo) {
		String uri = uriInfo.getBaseUriBuilder().path(MessageResource.class).path(String.valueOf(message.getId()))
				.build().toString();
		return uri;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addMessage(Message message, @Context UriInfo uriInfo) {
		Message newMsg = messageService.addMessage(message);
//		return Response.created(new URI("messenger/webapi/messages/" + newMsg.getId()))
//						.entity(newMsg)
//						.build();
		String newId = String.valueOf(newMsg.getId());
		URI uri = uriInfo.getAbsolutePathBuilder().path(newId).build();
		return Response.created(uri).entity(newMsg).build();
	}

	@PUT
	@Path("/{messageId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String updateMessage(@PathParam("messageId") long messageId, Message message) {
		message.setId(messageId);
		String out = "";
		boolean updated = messageService.updateMessage(message);
		if (updated) {
			out = "Message updated";
		} else {
			out = "Failed to update message";
		}
		return out;
	}

	@DELETE
	@Path("/{messageId}")
	public void deleteMessage(@PathParam("messageId") long messageId) {
		messageService.deleteMessage(messageId);
	}

	@Path("/{messageId}/comments")
	public CommentResource getCommentResource() {
		return new CommentResource();
	}
}
