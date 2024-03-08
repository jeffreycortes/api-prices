package com.colsubsidio.pricesapi.infrastructure.adapters.mail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

import com.colsubsidio.pricesapi.common.EnvironmentService;
import com.colsubsidio.pricesapi.common.GsonUtils;
import com.colsubsidio.pricesapi.common.telemetry.LogsManager;
import com.colsubsidio.pricesapi.tmp.domain.CuentaUsuarioEmpresaAggregateDto;
import com.colsubsidio.pricesapi.tmp.domain.email.Email;
import com.colsubsidio.pricesapi.tmp.domain.email.EmailNotifierService;
import com.colsubsidio.pricesapi.tmp.domain.email.EmailRequestDto;
import com.colsubsidio.pricesapi.tmp.domain.email.EmailRes;
import com.colsubsidio.pricesapi.infrastructure.adapters.RestTemplateUtil;
import com.microsoft.applicationinsights.boot.dependencies.google.common.base.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {

    private final RestTemplateUtil restTemplateUtil;
    private final LogsManager log;
    private final GsonUtils gson;
    private final EnvironmentService environmentService;
    private final HttpHeaders httpHeaders = new HttpHeaders();
    private final EmailNotifierService emailNotifierService;
    private final MailContentBuilder mailContentBuilder;
    public static final String SENT_EMAIL = "Envio de correo";


    private ResponseEntity<EmailRes> sent(Email email, String name) {
        UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(environmentService.getEmailUrl());
        uri.queryParam("name", name);
        ResponseEntity<EmailRes> response = restTemplateUtil.sendRequest(uri, HttpMethod.POST, email, EmailRes.class,
                httpHeaders, false, SENT_EMAIL);
        return response;

    }

    private ResponseEntity<EmailRes> sent(Email email) {
        UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(environmentService.getEmailUrl());
        ResponseEntity<EmailRes> response = restTemplateUtil.sendRequest(uri, HttpMethod.POST, email, EmailRes.class,
                httpHeaders, false, SENT_EMAIL);
        return response;
    }

    @Async
    public void sentFromRequest(CuentaUsuarioEmpresaAggregateDto cuentaUsuarioEmpresaAggregateDto, byte[] file) {

        String content = "";
        String fileName = "";
        String template = "";
        String subjet = "";
        String copy = environmentService.getEmailCopy();
        String replyHidden = environmentService.getEmailReplyHidden();
        String to = "";
        String fileText = "";
        EmailRequestDto emailRequestDto = EmailRequestDto.builder().build();

        ResponseEntity<EmailRes> emailRes = null;
        List<EmailRequestDto.SendMailReqFiles> files = new ArrayList<>();

        try {

            template = "./confirmation.html";
            subjet = "RESPUESTA CONFIRMACIÓN GENERACIÓN DE CLAVE";
            fileName = "afiliacion.pdf";
            fileText = file != null ? Base64.getEncoder().encodeToString(file) : "";
            to = cuentaUsuarioEmpresaAggregateDto.usuario.getCorreoEletronico();
            if (template != null && template != "") {
                content = new String(mailContentBuilder.buildEmail(cuentaUsuarioEmpresaAggregateDto, template).getBytes(),
                        "UTF-8");
            }

            Email email = Email.builder().text(content).subject(subjet).to(to).fileText(fileText).build();
            if (copy != null) {
                email.setReply(copy);
            } else {
                email.setReply("");
            }

            if (replyHidden != null)
                email.setReplyHidden(replyHidden);

            if(!Strings.isNullOrEmpty(fileText)) {
                files = Arrays.asList(EmailRequestDto.SendMailReqFiles
                        .builder()
                        .name(fileName)
                        .base64(fileText)
                        .build());
            }

            emailRequestDto = EmailRequestDto.builder()
                    .text(content)
                    .subject(subjet)
                    .to(to)
                    .cc(!Strings.isNullOrEmpty(copy) ? copy : "")
                    .bcc(!Strings.isNullOrEmpty(replyHidden) ? replyHidden : "")
                    .isTextHtml(true)
                    .files(files)
                    .params(new ArrayList<>())
                    .build();

            ResponseEntity<String> response = emailNotifierService.sendEmail(emailRequestDto);
            log.info("MailService.sentFromRequest response: {}", response);

            if(Objects.isNull(response) || response.getStatusCode() !=HttpStatus.OK) {
                log.error("MailService.sentFromRequest usuarioCuenta {}, emailRes {}", gson.serialize(cuentaUsuarioEmpresaAggregateDto), gson.serialize(response));
            }
        }  catch (Exception e) {
            log.error("MailService.sentFromRequest error {}, usuarioCuenta {}", gson.serialize(e), gson.serialize(cuentaUsuarioEmpresaAggregateDto));
        }
    }
}
