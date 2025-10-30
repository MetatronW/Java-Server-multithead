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
			this.out = new PrintWriter(new OutputStreamWriter(this.client.getOutputStream(), "ASCII"),true);
			this.in = new BufferedReader(new InputStreamReader(this.client.getInputStream(), "ASCII"));
			this.out.println("Connessione stablita ");
			this.out.flush();

			int total = 0;
			int conVocal = 0;

            String line;
            while((line=in.readLine())!=null){
                if(line.equals(".")){
                    break;
                }
                total++; //

                try {
                    char first = line.toLowerCase().charAt(0);  //per convertire in char 
                    if (first == 'a' || first == 'e' || first == 'i' || first == 'o' || first == 'u' )  {
                        conVocal++; //
                    }
                } catch (StringIndexOutOfBoundsException e) {
                    System.out.println("Errore: stringa vuota ricevuta dal client");
                }
                
                out.println(total + "#" +conVocal);    

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
              System.out.println("Connessione terminata");//
		  } catch (IOException e) {
			  System.out.println(e);
		  }
		}
	}

	// main module
	
	public static void main(String[] args) throws Exception{
		final int PORT = 20003;
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