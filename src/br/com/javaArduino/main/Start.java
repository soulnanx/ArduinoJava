package br.com.javaArduino.main;

import br.com.javaArduino.core.SerialUtil;

public class Start {

	public static void main(String args[]) {
		SerialUtil s;
		try {
			s = new SerialUtil("/dev/ttyUSB7");

			s.ler();
//			s.escrever("b");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		// System.exit(0);

	}

}
