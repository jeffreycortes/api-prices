package com.colsubsidio.pricesapi.infrastructure.adapters.mail;

import com.colsubsidio.pricesapi.common.CryptoService;
import com.colsubsidio.pricesapi.common.EnvironmentService;
import com.colsubsidio.pricesapi.tmp.domain.CuentaUsuarioEmpresaAggregateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

@Component
public class MailContentBuilder {

	private final TemplateEngine templateEngine;
	@Autowired
	private EnvironmentService environmentService;
	private Map<Integer, String> copy = new HashMap<Integer, String>();
	private static final String CARACTERES = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	private final SecureRandom RANDOM = new SecureRandom();

	@Autowired
	public MailContentBuilder(TemplateEngine templateEngine) {
		this.templateEngine = templateEngine;
	}

	public String generarCadenaAlfanumerica() {
		int longitud = 4;
		StringBuilder sb = new StringBuilder(longitud);
		for (int i = 0; i < longitud; i++) {
			int indice = RANDOM.nextInt(CARACTERES.length());
			sb.append(CARACTERES.charAt(indice));
		}
		return sb.toString();
	}


	public String buildEmail(CuentaUsuarioEmpresaAggregateDto cuentaUsuarioEmpresaAggregateDto, String nameTemplate) {
		Context context = new Context();
		String cadenaAleatoria = generarCadenaAlfanumerica();
		String contraseniaOriginal = CryptoService.decifrarConAES(cuentaUsuarioEmpresaAggregateDto.usuario.getContrasenha());
		context.setVariable("contrasenia", contraseniaOriginal+"_"+cadenaAleatoria);
		context.setVariable("urlColsubsidio",environmentService.getUrlColsubsidioEmpresas());
		context.setVariable("ayudaAfiGrupoFamiliar",environmentService.getUrlAyudaAfiliacionGrupoFamiliar());
		context.setVariable("ayudaSubsidioFamiliar",environmentService.getUrlAyudaSubsidioFamiliar());
		context.setVariable("ayudaAfiColaboradores",environmentService.getUrlAyudaAfiliacionColaboradores());
		context.setVariable("correoColsubsidio",environmentService.getCorreoAfiliaciones());
		context.setVariable("correoNovedades",environmentService.getCorreoNovedadesAfiliacion());
		return templateEngine.process(nameTemplate, context);
	}

}
