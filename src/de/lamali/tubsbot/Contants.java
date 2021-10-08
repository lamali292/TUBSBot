package de.lamali.tubsbot;

public class Contants {
	public static final String JDA_TOKEN = System.getenv("JDA_TOKEN");
	public static final String AWS_ACCESS_KEY_ID = System.getenv("S3_KEY");
	public static final String AWS_SECRET_ACCESS_KEY = System.getenv("S3_SECRET");
	public static final String BUCKET_NAME = System.getenv("BUCKET_NAME");
}
