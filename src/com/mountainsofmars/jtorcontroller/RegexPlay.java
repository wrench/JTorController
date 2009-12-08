package com.mountainsofmars.jtorcontroller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexPlay {
	public static void main(String[] args) {
		if(args.length < 1) {
			System.out.println("usage java RegexPlay <string_to_test>");
			System.exit(1);
		}
		String toTest = args[0];
		Pattern pattern = Pattern.compile("[^0-9][^0-9][^0-9].*");
		Matcher matcher = pattern.matcher(toTest);
		if(matcher.matches()) {
			System.out.println("MATCH!");
		} else {
			System.out.println("NO MATCH!");
		}
	}
}
