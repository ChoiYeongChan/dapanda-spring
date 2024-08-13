package com.example.dapanda.service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.example.dapanda.domain.email.dto.EmailSenderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailSenderService {

    private final AmazonSimpleEmailService amazonSimpleEmailService;
    private final TemplateEngine customTemplateEngine;

    public void send(final String template, final String subject, final Map<String, Object> variables, final List<String> receivers) {
        Context context = new Context();
        context.setVariables(variables);
        String content = customTemplateEngine.process(template, context);

        final EmailSenderDto senderDto = EmailSenderDto.builder()
                .to(receivers)
                .subject(subject)
                .content(content)
                .build();

        final SendEmailResult sendEmailResult = amazonSimpleEmailService
                .sendEmail(senderDto.toSendRequestDto());
        log.info("{}", sendEmailResult.getSdkResponseMetadata().toString());
        sendingResultMustSuccess(sendEmailResult);
    }

    private void sendingResultMustSuccess(final SendEmailResult sendEmailResult) {
        if (sendEmailResult.getSdkHttpMetadata().getHttpStatusCode() != 200) {
            log.error("{}", sendEmailResult.getSdkResponseMetadata().toString());
        }
    }
}
