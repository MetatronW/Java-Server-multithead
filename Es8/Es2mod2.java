/////////ok funziona
///  stare attento a quel for out of bounding : for tra 2 num consecutivi
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
			this.out.println("Servizio attivo sulla porta ");
			this.out.flush();

			String inputLine = in.readLine();
			while (inputLine != null){

				try {
					if (inputLine.startsWith("MINDIS")){
						String [] parts = inputLine.substring(7).split(";");
						int [] nums = new int [parts.length]; // 
						for (int i = 0; i<parts.length; i++){
							nums[i]= Integer.parseInt(parts[i].trim());//
						} 	//
						int minDist= Integer.MAX_VALUE;
				
						for (int i = 0; i < nums.length - 1; i++){	//
							if((Math.abs(nums[i]-nums[i+1]))<minDist){//
								minDist = Math.abs(nums[i]-nums[i+1]);
							}
					
						}
						out.println(minDist); //ok va fuori dal FOR, altrimenti lo fa vedere per ogni coppia

					}else if (inputLine.startsWith("MINMAX")){//anche qui si converte in array int come sopra

						String [] parts = inputLine.substring(7).split(";");
						int [] nums = new int [parts.length]; // 
						for (int i = 0; i<parts.length; i++){
							nums[i]= Integer.parseInt(parts[i].trim());//
						}
						//fin qui == a sopra
						int min= Integer.MAX_VALUE;
						int max =Integer.MIN_VALUE;
						for(int i=0; i< nums.length; i++){
							if(nums[i]<min){ //
								min = nums[i];
							}
							if(nums[i]>max){
								max = nums[i];
							}			
						}
						out.println("[" +min +","+max+"]");

					} else if (inputLine.equals("EXIT")){
						out.println("chiudo connessione");
						break;
					}
				} catch (StringIndexOutOfBoundsException e) {
					System.out.println("Errore: comando senza parametri");
					out.println("Errore: formato comando non valido");
				} catch (NumberFormatException e) {
					System.out.println("Errore: parametro non numerico");
					out.println("Errore: parametri non validi");
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("Errore: array vuoto o formato non valido");
					out.println("Errore: numero insufficiente di parametri");
				}
				inputLine = in.readLine(); // ci vuole: ALTRIMENTI NON FUNZIONA
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
	//
	public static void main(String[] args) throws Exception{
		final int PORT = 52000;
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