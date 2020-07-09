package LMS;


import java.io.IOException;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class BookProduct {
	private String title;
	private String subject;
	private String author;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void printInfo() {
		System.out.println(title + "\t\t\t" + author + "\t\t\t" + subject);
	}

	public void changeBookInfo() throws IOException {
		BufferedReader reader = reader();
		Scanner scanner = new Scanner(System.in);
		String input;
		System.out.println("\nUpdate Author? (y/n)");
		input = scanner.next();
		if (input.equals("y")) {
			System.out.println("\nEnter new Author: ");
		}
		System.out.println("\nUpdate Subject? (y/n)");
		input = scanner.next();
		if (input.equals("y")) {
			System.out.println("\nEnter new Subject: ");
		}
		System.out.println("\nUpdate Title? (y/n)");
		input = scanner.next();
		if (input.equals("y")) {
			System.out.println("\nEnter new Title: ");
		}
		System.out.println("\nBook is successfully updated.");
	}

	private BufferedReader reader() throws IOException {
		Scanner scanner = new Scanner(System.in);
		String input;
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		input = scanner.next();
		if (input.equals("y")) {
			author = reader.readLine();
		}
		input = scanner.next();
		if (input.equals("y")) {
			subject = reader.readLine();
		}
		input = scanner.next();
		if (input.equals("y")) {
			title = reader.readLine();
		}
		return reader;
	}
}