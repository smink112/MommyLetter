package com.ssafy.A509.hashtag.model;

import com.ssafy.A509.board.model.Board;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hashtag {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long hashTagId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "board_id")
	@Setter
	private Board board;

	private String content;

	@Builder
	protected Hashtag(String content) {
		this.content = content;
	}
}
