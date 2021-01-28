

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Calendar;
import java.util.Scanner;


public class Client implements Serializable {
    public static String serverpath="D:/StorageServer";
    
    public static void logs(String mesaj)
    {
        Calendar time = Calendar.getInstance();
        int day = time.get(Calendar.DAY_OF_MONTH);
        int month = time.get(Calendar.MONTH)+1;
        int hour = time.get(Calendar.HOUR_OF_DAY);
        int minute = time.get(Calendar.MINUTE);
        int sec = time.get(Calendar.SECOND);
        String newline = "\n";
        try(FileWriter fw = new FileWriter("D:/StorageServer/Logs/logs.txt", true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw))
           
{
    out.println("["+day+"."+month+"/"+hour+":"+minute+":"+sec+"]"+": "+mesaj+newline);
    //more code
}   catch (IOException e) {
    //exception handling left as an exercise for the reader
}
    }

	public static void main(String[] args) {
                
	        StringBuffer stringBufferOfData = new StringBuffer();
		String environment;
		String hostname;
		int portnumber;
		String clientpath;
		String upload = "upload";
		String download = "download";
		String dir= "dir";
		String mkdir= "mkdir";
		String rmdir= "rmdir";
		String rm= "rm";
                String read = "read";
                String write = "write";
		String shutdown= "shutdown";
		
		
		try{
						
			environment = System.getenv("SERVER_PORT");//preluare variabila din System environment
			System.out.println(environment);
			
			hostname = environment.split(":")[0];//se imparte in 2, una e host-ul
			
			portnumber = Integer.parseInt(environment.split(":")[1]);//ceaalalta portul
			System.out.println("Se cauta conexiune:" + environment);
			
			Registry myreg = LocateRegistry.getRegistry(hostname, portnumber);//localizare registru server				
			RmiInterface inter = (RmiInterface)myreg.lookup("remoteObject");//pentru invocarea metodelor in interfata
			//si cautare obiect registru
			
			if(upload.equals(args[0]))//modul in care se uploadeaza un fisier
			{
				clientpath= args[1];
				serverpath = args[2];//locatie client, server
				
				File clientpathfile = new File(clientpath);
				byte [] mydata=new byte[(int) clientpathfile.length()];
				FileInputStream in=new FileInputStream(clientpathfile);	
					System.out.println("Incarcare pe server...");		
				 in.read(mydata, 0, mydata.length);					 
				 inter.uploadFileToServer(mydata, serverpath, (int) clientpathfile.length());
				//practic pentru incarcarea unui fisier, se preia continutul celui de pe client 
				 in.close();//dupa care se creeaza un nou fisier pe server cu continutul celui de pe client
                                 logs("Uploadare fisier "+clientpath+" de catre "+InetAddress.getLocalHost().getHostAddress());
                                 
			}
			//to download a file
			if(download.equals(args[0]))
			{
				serverpath = args[1];
				clientpath= args[2];

				byte [] mydata = inter.downloadFileFromServer(serverpath);
				System.out.println("downloading...");
				File clientpathfile = new File(clientpath);
				FileOutputStream out=new FileOutputStream(clientpathfile);				
	    		out.write(mydata);
				out.flush();
		    	out.close();
                        logs("Downloadare fisier: "+serverpath+ " de catre "+InetAddress.getLocalHost().getHostAddress());
			}
			
			//to list all the files in a directory
			if(dir.equals(args[0]))
			{
				//serverpath = args[1];
                                
				String[] filelist = inter.listFiles(serverpath);
				for (String i: filelist)
				{
					System.out.println(i);
				}
                                logs("Listare directoare de catre "+InetAddress.getLocalHost().getHostAddress());
                             
                               
			}
                        if(read.equals(args[0]))
                        {
                          //  serverpath=args[1];
                            String file = args[1];
                            try {
                            File myObj = new File(file);
                            Scanner myReader = new Scanner(myObj);
                            while (myReader.hasNextLine()) {
                            String data = myReader.nextLine();
                            System.out.println(data);
                            }
                            myReader.close();
                            } catch (FileNotFoundException e) {
                            System.out.println("An error occurred.");
                            e.printStackTrace();
                            }
                            logs("Citire fisier "+file+" de catre "+InetAddress.getLocalHost().getHostAddress() );
                          }
                        if(write.equals(args[0]))
                        {
                          //  serverpath = args[1];
                            String file= args[1];
                            File writer = new File(file);
                            if (Desktop.isDesktopSupported()) {//daca suporta extensia acelui fisier
                            Desktop.getDesktop().edit(writer);//deschidere in editor
                            } else {
                             // nimic
                            }
                            logs("Deschidere "+file+" pentru scriere de catre "+InetAddress.getLocalHost().getHostAddress());
                        }
			
			//to create a new directory
			if(mkdir.equals(args[0]))
			{
				//serverpath = args[1];
                                String directory = args[1];
				String path = serverpath;
                                File d = new File(serverpath+"/"+directory);
                                if(!d.exists())
                                {
                                    d.mkdirs();
                                    System.out.println("Director creat");
                                }
                                logs("Creare director "+ directory+ " de catre "+InetAddress.getLocalHost().getHostAddress());
			}
			
			//to remove/delete a directory
			if(rmdir.equals(args[0]) || rm.equals(args[0]))
			{
				String directory = args[1];
				String path = serverpath;
                                File d = new File(serverpath+"/"+directory);
                                if(d.exists())
                                {
                                    d.delete();
                                    System.out.println("Director sters");
                                }
                                logs("Stergere director "+d+" de catre "+ InetAddress.getLocalHost().getHostAddress());
				
			}
			//to shutdown the client
			if(shutdown.equals(args[0]))
			{
				System.exit(0);
				System.out.println("Client oprit");
			}
                            
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("error with connection or command. Check your hostname or command");
		}				
		}
		
	}

