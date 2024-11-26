package com.epam.training.gen.ai.data;

import com.microsoft.semantickernel.services.chatcompletion.AuthorRole;

import javax.annotation.Nullable;

public record ChatRoleMessage(
        AuthorRole authorRole,
        String message
) {}
