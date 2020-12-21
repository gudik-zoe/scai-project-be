package com.luv2code.springboot.cruddemo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.luv2code.springboot.cruddemo.entity.Message;
import com.luv2code.springboot.cruddemo.service.MessagesService;
import com.luv2code.springboot.cruddemo.service.StorageService;
import com.luv2code.utility.IdExtractor;
import com.luv2code.utility.ImageUrl;

public class ChatWebSocketHandler extends TextWebSocketHandler {


	private final Map<Integer, WebSocketSession> webSocketSessions = new HashMap<Integer, WebSocketSession>();

	@Autowired
	private MessagesService messageService;
	
	@Autowired
	private StorageService storageService;

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {


	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		
		if (message.getPayload().contains("Bearer ")) {

			try {
				IdExtractor idExtractor = new IdExtractor(message.getPayload());
				webSocketSessions.put(idExtractor.getIdFromToken(), session);
			} catch (Exception e) {
				e.printStackTrace();
				session.sendMessage(new TextMessage("token expired"));
			}
		} else {
			JSONObject obj;
			try {
				int senderId = 0;
				for (Map.Entry entry : webSocketSessions.entrySet()) {
					if (session == entry.getValue()) {
						senderId = (int) entry.getKey();
					}
				}
				obj = new JSONObject(message.getPayload());
				obj.append("idSender", senderId);
				int idReceiver = obj.getInt("idReceiver");
				String messageText = obj.getString("message");
				
				

				TextMessage newMessage = new TextMessage(obj.toString());

				if (webSocketSessions.containsKey(idReceiver)) {
					webSocketSessions.get(idReceiver).sendMessage(newMessage);
				}
				session.sendMessage(newMessage);

				Message theMessage = new Message(senderId, idReceiver, messageText, new Date(System.currentTimeMillis()), false  );
				messageService.sendMessage(theMessage);

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		webSocketSessions.values().remove(session);
	}

}
