package com.epam.training.gen.ai.data;

import com.microsoft.semantickernel.services.chatcompletion.AuthorRole;

public record ChatRoleMessage(
        AuthorRole authorRole,
        String message
) {}
