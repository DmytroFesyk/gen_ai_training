package com.epam.training.gen.ai.data;

import java.util.List;
import java.util.UUID;

public record ChatWithHistoryResponse(
        UUID chatId,
        List<ChatRoleMessage> messages
) {}
