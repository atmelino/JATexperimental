package jat.core.util;

import java.util.ArrayList;

import javax.swing.JTextArea;

public class jatMessages {

	ArrayList<String> jatMessageList = new ArrayList<String>();
	public boolean changed = false;

//	public void add(String message) {
//		jatMessageList.add(message);
//	}

	public void addln(String message) {
		jatMessageList.add(message + '\n');
		changed = true;
	}

	// public void addln(String className, String message) {
	// jatMessageList.add(message+'\n');
	// }

	public void printMessages() {
		System.out.println("--- jat Messages: ----" );
		for (int i = 0; i < jatMessageList.size(); i++) {
			System.out.print(jatMessageList.get(i));
			changed = false;
		}
	}

	public void printMessagesToTextArea(JTextArea textArea) {
		textArea.setText("");
		for (int i = 0; i < jatMessageList.size(); i++) {
			textArea.append(jatMessageList.get(i));
			changed = false;
			// System.out.print(jatMessageList.get(i));
		}
	}

}
