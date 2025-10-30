import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Es2mod22 extends Thread{
	private Socket client; 			// socket for the communication
	private BufferedReader in;		// input reader
	private PrintWriter out;		// output writer

	public Es2mod22 (Socket client) {
		this.client = client;
	}

	public void run() {
		try {
			this.out = new PrintWriter(new OutputStreamWriter(this.client.getOutputStream(), "ASCII"),true);
			this.in = new BufferedReader(new InputStreamReader(this.client.getInputStream(), "ASCII"));
			this.out.println("Connection established, ");
			out.println("inserire sequenza di numeri");
			this.out.flush();

			String inputLine = in.readLine();
			System.out.println("ricevuto: " + inputLine);

			String [] numbers = inputLine.split("#");

			try {
				int sum = 0;
				for (int i =0; i<numbers.length;i++){ //prova con for classico
					sum+= Integer.parseInt(numbers[i]); //
					
				}
				double mean = sum/numbers.length;

				out.println(mean);
			} catch (NumberFormatException e) {
				System.out.println("Errore: formato numero non valido");
				out.println("Errore: uno o più numeri non validi");
			} catch (ArithmeticException e) {
				System.out.println("Errore: divisione per zero");
				out.println("Errore: nessun numero inserito");
			}
/*			//altra forma di fare il for
            for (String numStr : numbers) {
                sum += Integer.parseInt(numStr); //och: da usare parseInt per fare operazioni con interi
            }
            double mean = sum / numbers.length;
*/
		
		} catch (IOException e) {
			System.out.println("Problem with the client - It will be disconnected");
			System.out.println(e);
		} finally {
		  try {
			  if (this.in != null)
			      this.in.close();
		  } catch (IOException e) {
			  System.out.println(e);
		  }
		  try {
			  if (this.out != null) {
			      out.println("Connessione terminata");
			      this.out.close();
			  }
		  } catch (Exception e) {
			  System.out.println(e);
		  }
		  try {
			  if (this.client != null)
			      this.client.close();
		  } catch (IOException e) {
			  System.out.println(e);
		  }
		}
	}

	// main module
	
	public static void main(String[] args) throws Exception{
		final int PORT = 20001;
		ServerSocket server = null;
		try {
			server = new ServerSocket(PORT);
			System.out.println("Server initialized sulla porta "+PORT);
			while(true){
				Es2mod22 sms = new Es2mod22(server.accept());
				sms.start();
				System.out.println("new user connected");
			}
		} catch (IOException e) {
			System.out.println(e);
		} finally {
			if (server!=null) {
				try {
					server.close();
				} catch (IOException e) {
					System.out.println(e);
				}
			}
		}//end try-catch

	}

}