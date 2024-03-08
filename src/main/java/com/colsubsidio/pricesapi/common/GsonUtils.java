package com.colsubsidio.pricesapi.common;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

/**
 * The Class GsonUtils creada para agregar utilidades de la libreria gson de
 * google
 */
@Component
public class GsonUtils {

	private static final Gson gson = new Gson();

	/**
	 * Obtiene un objeto, lo coniverte en un Json element y luego lo pasa a string
	 *
	 * @param value el Objeto
	 * @return el objeto en json string
	 */
	public static String getValueJsonString(Object value) {
		String bodyJsonString = "";
		if (value != null) {
			JsonElement obj = new Gson().toJsonTree(value);
			bodyJsonString = obj.toString();
		}

		return bodyJsonString;
	}

	/**
	 * Recibe un json object y una clase y retorna el json object convertido a la
	 * clase enviada.
	 *
	 * @param obj   es el objeto Json que se desea convertir
	 * @param clazz la clase a la que se desa convertir el objeto Json
	 * @return el objeto convertido a la clase
	 */
	public static <T> T toObject(JsonObject obj, Class<T> clazz) {
		return gson.fromJson(obj, clazz);
	}

	/**
	 * Recibe un json string y una clase y retorna el json string convertido a la
	 * clase enviada.
	 *
	 * @param src   es el json string que se desea convertir
	 * @param clazz la clase a la que se desa convertir el json string
	 * @return el string json convertido a la clase
	 */
	public static <T> T toObject(String src, Class<T> clazz) {
		return gson.fromJson(src, clazz);
	}

	/**
	 * Recibe un json string y un Type y retorna el json string convertido al Type.
	 *
	 * @param src   es el json string que se desea convertir
	 * @param clazz el type al que se desea convertir el json String
	 * @return el string json convertido al Type
	 */
	public static <T> T toObject(String src, Type clazz) {
		return gson.fromJson(src, clazz);
	}

	/**
	 * Recibe un json element y una clase y retorna el json element convertido a la
	 * clase enviada.
	 *
	 * @param obj   es el element Json que se desea convertir
	 * @param clazz la clase a la que se desa convertir el objeto Json
	 * @return el objeto convertido a la clase
	 */
	public static <T> T toObject(JsonElement obj, Class<T> clazz) {
		return gson.fromJson(obj, clazz);
	}

	/**
	 * Recibe un objeto y lo convierte en Json y lo retorna en String
	 *
	 * @param src el objeto a serializar
	 * @return el objeto a serializado
	 */
	public static String serialize(Object src) {
		return gson.toJson(src);
	}

	/**
	 * Recibe un Json en String y lo convierte en JsonElement
	 *
	 * @param json el Json en String
	 * @return el Json en String convertido en JsonElement
	 */
	public static JsonElement parseJson(String json) {
		return JsonParser.parseString(json);
	}
}
