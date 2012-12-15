package br.com.javaArduino.exception;

public class PortNotFoundException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public PortNotFoundException() {
		super();
	}
	
	public PortNotFoundException(String erro) {
		super(erro);
	}
}
