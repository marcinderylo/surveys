package org.adaptiveplatform.surveys.test;

import java.util.Random;

public class RandomStringGenerator {

	private static final int WORD_LENGTH_MIN = 3;
	private static final int WORD_LENGTH_MAX = 10;
	private static final int SENTENCE_LENGTH_MIN = 3;
	private static final int SENTENCE_LENGTH_MAX = 10;
	private static final int PARAGRAPH_LENGTH_MIN = 3;
	private static final int PARAGRAPH_LENGTH_MAX = 10;

	private Random random = new Random();

	public String paragraph() {
		return paragraph(number(PARAGRAPH_LENGTH_MIN, PARAGRAPH_LENGTH_MAX));
	}

	public String paragraph(int sentenceCount) {
		String paragraph = "";
		for (int i = 0; i < sentenceCount; i++) {
			paragraph += sentence() + " ";
		}
		return paragraph;
	}

	public String sentence() {
		return sentence(number(SENTENCE_LENGTH_MIN, SENTENCE_LENGTH_MAX));
	}

	public String sentence(int wordCount) {
		String sentence = "";
		for (int i = 0; i < wordCount; i++) {
			if (i != 0) {
				sentence += " ";
			}
			sentence += word();
		}
		return sentence.substring(0, 1).toUpperCase() + sentence.substring(1) + ".";
	}

	public String word() {
		return word(number(WORD_LENGTH_MIN, WORD_LENGTH_MAX));
	}

	public String word(int length) {
		String word = "";
		for (int i = 0; i < length; i++) {
			word += letter();
		}
		return word;
	}

	public char letter() {
		return (char) number((int) 'a', (int) 'z');
	}

	public int number(int min, int max) {
		return random.nextInt(max - min + 1) + min;
	}

}
