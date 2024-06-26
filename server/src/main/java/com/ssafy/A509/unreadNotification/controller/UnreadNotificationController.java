package com.ssafy.A509.unreadNotification.controller;

import com.ssafy.A509.unreadNotification.dto.NotificationDto;
import com.ssafy.A509.unreadNotification.service.UnreadNotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/noti")
@Tag(name = "Unread", description = "읽지 않은 채팅 알림 API")
public class UnreadNotificationController {

	private final UnreadNotificationService notificationService;

	@Operation(
		summary = "메세지 읽음 -> 알림 삭제",
		description = "알림을 삭제함"
	)
	@DeleteMapping("/{userId}/{dmId}")
	public ResponseEntity<Void> readMessage(@PathVariable Long userId, @PathVariable String dmId) {
		notificationService.markAsRead(userId, dmId);
		return ResponseEntity.ok().build();
	}
	@Operation(
		summary = "읽지 않은 메세지 알림 리스트 조회",
		description = "사용자가 안 읽은 메세지 리스트를 돌려줌"
	)
	@GetMapping("/{userId}")
	public ResponseEntity<List<NotificationDto>> getUnread(@PathVariable Long userId) {
		return ResponseEntity.ok(notificationService.getUnread(userId));
	}
}
