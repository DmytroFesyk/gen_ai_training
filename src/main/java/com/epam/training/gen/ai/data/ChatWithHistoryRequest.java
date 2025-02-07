package com.epam.training.gen.ai.data;

import java.util.UUID;

public record ChatWithHistoryRequest(
        UUID chatId,
        PromptParameters promptParameters,
        String input
) {
}
