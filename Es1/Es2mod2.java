// ok funziona
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
			this.out.println("Connessione stablita , v3");
			this.out.flush();

            String inputLine ;// in.readLine(null);          
            while ((inputLine = in.readLine()) != null){
                try {
                    String[] text = inputLine.split(" ");//
                    if (text.length ==2 && text[0].equals("FREQ")){
                        String[] parts = text[1].split("#");
                        char c = parts[0].charAt(0);
                        String s= parts[1];
                        long count = s.chars().filter(ch -> ch == c).count();// VIP
                        out.println(count);
                    }
                    else 
                    //if(text[0].equals("QUIT")){
                    if(inputLine.equals("QUIT")){//non cambia niente rispetto a sopra
                        out.println("Connessione chiusa");
                        break;//ok
                    }
                    else{
                        out.println("Comando non valido, lanciare eccezione"); //ok funziona
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Errore nel parsing del comando: formato non valido");
                    out.println("Errore: formato comando non valido");
                } catch (StringIndexOutOfBoundsException e) {
                    System.out.println("Errore: stringa vuota o carattere non presente");
                    out.println("Errore: formato parametri non valido");
                }
            }//fine while
	
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
	public static void main(String[] args) throws Exception{
		final int PORT = 30001;
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