//ok funziona
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
			this.out.println("connessione stabilita");
			this.out.flush(); //
			String inputLine = in.readLine();
			while(inputLine != null){ ///   
				try {
					String [] rows = inputLine.split("#");
					int size = rows.length;
					int matrix [][] = new int [size][size];

					for (int i = 0; i<size; i++){
						String [] col = rows[i].split(","); ///
						for(int j=0; j<size; j++){
							matrix[i][j]= Integer.parseInt(col[j]);
						}
					}
					//calcolo max e somma diagonale
					int max= 0;//
					int sum = 0;

					for(int i =0; i<size; i++){ //ok come logica
						int diagValue = matrix[i][i];
						if (diagValue>max){
							max = diagValue;						
						}
						sum +=diagValue;
					}
					this.out.println(max + "#" +sum);
				} catch (NumberFormatException e) {
					System.out.println("Errore: formato numero non valido nella matrice");
					this.out.println("Errore: formato numero non valido");
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("Errore: matrice non quadrata o formato non valido");
					this.out.println("Errore: formato matrice non valido");
				}
				
				this.in.close();
			  this.out.close();
              System.out.println("connessione chiusa");
				//inputLine = in.readLine(); // ci va per forza sul while

				//pero si deve chiudere la connessione, cosi non finisce
				//se non faccio il while si chiude subito

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
		final int PORT = 22001;
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