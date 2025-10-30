import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Es2mod2 extends Thread{
	private Socket client; 			// socket for the communication
	private BufferedReader in;		// input reader
	private PrintWriter out;		// output writer

	public Es2mod2 (Socket client) {
		this.client = client;
	}

	public void run() {
		try {
			this.out = new PrintWriter(new OutputStreamWriter(this.client.getOutputStream(), "UTF-8"),true);
			this.in = new BufferedReader(new InputStreamReader(this.client.getInputStream(), "UTF-8"));
			this.out.println("Connessione stabilita  -");
			this.out.flush();

			String inputLine = in.readLine();
			while (inputLine != null) {// 
				try {
					String [] rows = inputLine.split("#");
					StringBuilder response = new StringBuilder(); //si poteva fare con una lista,vedi altri esempi

					for(int i =0; i<rows.length; i++){
						String [] elements = rows[i].split(",");
						int maxLen = 0;
						for (int j=0; j<elements.length; j++){
							if(elements[j].length() >maxLen ){
								maxLen = elements[j].length(); // 
							}
						}/*
						for (String elem : elements) {
							maxLen = Math.max(maxLen, elem.length());
						}*/
						response.append(maxLen); // oppurecomein altri casi dopo aapend(maxlen + "#")
						if (i < rows.length - 1) { //
							response.append("#");
						}

					}
					out.println(response.toString());
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("Errore: formato input non valido");
					out.println("Errore: formato non valido");
				}
				client.close(); //
			} //fine while			
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
			      this.out.println("Connessione chiusa  -");
			      this.out.close();
			  }
		  } catch (Exception e) {
			  System.out.println(e);
		  }
		  try {
			  if (this.client != null && !this.client.isClosed())
			      this.client.close();
		  } catch (IOException e) {
			  System.out.println(e);
		  }
		}
	}

	// main module
	//
	public static void main(String[] args) throws Exception{
		final int PORT = 37004;
		ServerSocket server = null;
		try {
			server = new ServerSocket(PORT);
			System.out.println("Server initialized "+PORT);
			while(true){
				Es2mod2 sms = new Es2mod2(server.accept());
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