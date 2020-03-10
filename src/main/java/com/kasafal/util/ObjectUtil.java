package com.kasafal.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

public abstract class ObjectUtil {
	public static final Logger LOGGER = Logger.getLogger(ObjectUtil.class);

	/**
	 * Read a file from the system
	 * 
	 * @param path
	 *            {@link String}
	 * @throws IOException
	 **/
	public static String read(String path) throws IOException {
		/***
		 * Way to read file using scanner
		 */
		/*
		 * File f= new File(path); Scanner sc = new Scanner(f); //Different way to read
		 * file sc.useDelimiter("\\A"); System.out.println(sc.next());
		 */

		/***
		 * Way to read file using BufferedReader
		 */
		// BufferedReader bf = new BufferedReader(new InputStreamReader(new
		// FileInputStream(path)));

		String result = new String(Files.readAllBytes(Paths.get(path)));
		System.out.println("Reading content from the file");
		result = result + "new Result";
		return result;

	}

	/**
	 * Write a file to system
	 * 
	 * @param path
	 *            {@link String}
	 * @param content
	 *            {@link String}
	 **/
	public static void write(String path, String content) throws IOException {
		File file = new File(path);
		FileWriter writer = new FileWriter(file);
		LOGGER.info("Writing content to " + path);
		writer.write(content);
		writer.flush();
		writer.close();
	}

	public static <T> void serializeObject(T obj, OutputStream output) throws IOException {
		ObjectOutputStream objectOutput = new ObjectOutputStream(output);
		System.out.println("Serializing the object");
		objectOutput.writeObject(obj);
		objectOutput.flush();
		objectOutput.close();
	}

	public static Object deselializeObject(InputStream input) throws IOException {
		ObjectInputStream objectInput = new ObjectInputStream(input);
		try {
			System.out.println("Deserializing the object");
			return objectInput.readObject();
		} catch (ClassNotFoundException e) {

			System.out.println("No class found for the input stream ");
		}
		return null;

	}

	public static <T> String toXml(T obj) throws JAXBException {
		StringWriter sw = new StringWriter();
		JAXBContext jaxbContext = JAXBContext.newInstance(obj.getClass());
		Marshaller marshaller = jaxbContext.createMarshaller();
		System.out.println("Converting object to xml");
		marshaller.marshal(obj, sw);
		return sw.toString();

	}

	public static <T> T xmltoObject(InputStream is, Class<T> className) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(className);
		Unmarshaller unmarsheller = jaxbContext.createUnmarshaller();
		LOGGER.info("Converting xml to object of  Type"+ className.getName() );
		return (className.cast(unmarsheller.unmarshal(is)));
	}

	public static <T> T xmltoObject(String fileName, Class<T> className) throws JAXBException {
		FileInputStream fileInput;
		try {
			fileInput = new FileInputStream(fileName);
			return (T) xmltoObject(fileInput, className);
		} catch (FileNotFoundException e) {
			LOGGER.error("File not found " + fileName);
		}
		return null;
	}

}
