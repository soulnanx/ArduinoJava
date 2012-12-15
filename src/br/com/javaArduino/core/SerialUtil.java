package br.com.javaArduino.core;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;

import br.com.javaArduino.exception.PortNotFoundException;

public class SerialUtil implements SerialPortEventListener {

    @SuppressWarnings("rawtypes")
	static Enumeration portList;
    static CommPortIdentifier portId;
    Integer tamanhoByte;

    static SerialPort serialPort;
    static OutputStream outputStream;
    static boolean outputBufferEmptyFlag = false;
    static boolean portFound = false;

    InputStream inputStream;

    public SerialUtil(String portaSerial) throws PortNotFoundException {
//    	this.tamanhoByte = tamanhoByte;
        boolean portFound = false;
        
        portList = CommPortIdentifier.getPortIdentifiers();
        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();

            if (portId.getName().equals(portaSerial)) {
            	portFound = true; 
                break;
            }
        }
        
        if (portFound){
        	// Inicializa porta
            try {
                serialPort = (SerialPort) portId.open("SimpleReadApp", 2000);
                serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                serialPort.notifyOnDataAvailable(true);
            } catch (PortInUseException e) {
                e.printStackTrace();
            } catch (UnsupportedCommOperationException e) {
				e.printStackTrace();
			}
        } else {
        	throw new PortNotFoundException("A porta " + portaSerial + " n�o foi encontrada!");
        }
    }

    @Override
//    public void serialEvent(SerialPortEvent event) {
//        switch (event.getEventType()) {
//        case SerialPortEvent.BI:
//        case SerialPortEvent.OE:
//        case SerialPortEvent.FE:
//        case SerialPortEvent.PE:
//        case SerialPortEvent.CD:
//        case SerialPortEvent.CTS:
//        case SerialPortEvent.DSR:
//        case SerialPortEvent.RI:
//        case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
//            break;
//        case SerialPortEvent.DATA_AVAILABLE:
//            // caso tenha dados disponiveis
//            byte[] readBuffer = new byte[tamanhoByte];
//            //int numBytes;
//            
//            // inicio da leitura dos dados##############
//            try {
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//            
//            try {
//				while (inputStream.available() > 0) {
//				    //numBytes = inputStream.read(readBuffer);
//				    new String(readBuffer);
//				    System.out.println("leu "+readBuffer);
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//            // fim da leitura dos dados#################
//            
//            break;
//        }
//    }
    public synchronized void serialEvent(SerialPortEvent oEvent) {
		
    	if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				int available = inputStream.available();
				byte chunk[] = new byte[available];
				inputStream.read(chunk, 0, available);

				// Os resultados mostrados são dependentes do codepage
				System.out.print(new String(chunk));
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
		// Ignore todos os outros eventTypes, mas você deve considerar o resto.
	}
    public void escrever(String texto) throws InterruptedException, IOException, UnsupportedCommOperationException {
        outputStream = serialPort.getOutputStream();
        serialPort.notifyOnOutputEmpty(true);
        outputStream.write(texto.getBytes());
        System.out.println("Escreveu \"" + texto + "\" na " + serialPort.getName());
        
        Thread.sleep(200); 
    }

    public void ler() throws IOException, TooManyListenersException, UnsupportedCommOperationException {
        inputStream = serialPort.getInputStream();
        serialPort.addEventListener(this);
    }
}