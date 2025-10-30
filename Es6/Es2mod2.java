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
			this.out.println("connessione presente ,   ");
			this.out.flush();

			String inputLine = in.readLine();
			while(inputLine != null){

				try {
					if(inputLine.startsWith("MIN")){
						String[] parts = inputLine.substring(4).split("#");//
						int minLen = Integer.MAX_VALUE;
						for (String s : parts) {
							minLen = Math.min(minLen, s.length());
						}
						
						out.println(minLen);
					} else
					if(inputLine.startsWith("VOC")){
						String [] parts= inputLine.substring(4).split("#");
						int totalVowel =0;
						for (String s : parts) {
							for (char c : s.toLowerCase().toCharArray()) {
								if ("aeiou".indexOf(c) != -1) {
									totalVowel++;
								}
							}
						}
						out.println(totalVowel);
					} else
					if(inputLine.equals("EXIT")){
						break;//non fa altro
					} else{
						out.println("comando invalido");
						throw new IOException("Invalid Command");
					}
				} catch (StringIndexOutOfBoundsException e) {
					System.out.println("Errore: comando troppo corto o formato non valido");
					out.println("Errore: formato comando non valido");
				}
				inputLine = in.readLine(); //ok ilcomando exit per uscire, va dentro il while però, quindi break

			}

			
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
			  if (this.out != null)
			      this.out.close();
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
	//
	public static void main(String[] args) throws Exception{
		final int PORT = 41001;
		ServerSocket server = null;
		try {
			server = new ServerSocket(PORT);
			System.out.println("Server initialized sulla porta "+PORT);
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