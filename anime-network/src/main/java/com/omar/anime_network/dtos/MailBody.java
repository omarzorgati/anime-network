package com.omar.anime_network.dtos;

import lombok.Builder;

@Builder
public record MailBody(String to, String subject, String text) {
}
