package ru.miroque.iisart;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class MultiThreadedSocketServer {
	ServerSocket myServerSocket;
	boolean ServerOn = true;
	private volatile Map<String, Set<String>> voc;
	private final int WORD = 1;

	public MultiThreadedSocketServer() {
		voc = new HashMap<>();
		try {
			myServerSocket = new ServerSocket(11111);
		} catch (IOException ioe) {
			System.out.println("Could not create server socket on port 11111. Quitting.");
			System.exit(-1);
		}

		Calendar now = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
		System.out.println("It is now : " + formatter.format(now.getTime()));

		// Successfully created Server Socket. Now wait for connections.
		while (ServerOn) {
			try {
				// Accept incoming connections.
				Socket clientSocket = myServerSocket.accept();

				// accept() will block until a client connects to the server.
				// If execution reaches this point, then it means that a client
				// socket has been accepted.

				// For each client, we will start a service thread to
				// service the client requests. This is to demonstrate a
				// Multi-Threaded server. Starting a thread also lets our
				// MultiThreadedSocketServer accept multiple connections
				// simultaneously.

				// Start a Service thread

				ClientServiceThread cliThread = new ClientServiceThread(clientSocket);
				cliThread.setName(String.valueOf(new Random().nextInt(5 + 1)));
				cliThread.start();

			} catch (IOException ioe) {
				System.out.println("Exception encountered on accept. Ignoring. Stack Trace :");
				ioe.printStackTrace();
			}

		}

		try {
			myServerSocket.close();
			System.out.println("Server Stopped");
		} catch (Exception ioe) {
			System.out.println("Problem stopping server socket");
			System.exit(-1);
		}

	}

	public static void main(String[] args) {
		new MultiThreadedSocketServer();
	}

	class ClientServiceThread extends Thread {
		Socket myClientSocket;
		boolean m_bRunThread = true;

		public ClientServiceThread() {
			super();
		}

		ClientServiceThread(Socket s) {
			myClientSocket = s;

		}

		public void run() {
			// Obtain the input stream and the output stream for the socket
			// A good practice is to encapsulate them with a BufferedReader
			// and a PrintWriter as shown below.
			BufferedReader in = null;
			PrintWriter out = null;

			// Print out details of this connection
			System.out.println("Accepted Client Address - " + myClientSocket.getInetAddress().getHostName());

			try {
				in = new BufferedReader(new InputStreamReader(myClientSocket.getInputStream()));
				out = new PrintWriter(new OutputStreamWriter(myClientSocket.getOutputStream()));

				// At this point, we can read for input and reply with
				// appropriate output.

				// Run in a loop until m_bRunThread is set to false
				while (m_bRunThread) {
					// read incoming stream
					String clientCommand = in.readLine();
					System.out.println("[" + this.getName() + "] Client Says: " + clientCommand);
					
					/*************************************************/
					/*** main logic ***/
					String[] args = clientCommand.split(" ");
					
					switch (args[0]) {
					case "add" :
						synchronized (this) {
							if (voc.containsKey(args[WORD])){
								Set<String> defs = voc.get(args[WORD]);
								defs.addAll(chunkDefs(args));
								voc.put(args[WORD], defs);
								out.println("<definition of word successfully added>/<значения слова успешно добавлены>");
								out.flush();
							} else {
								Set<String> defs = new HashSet<>();
								defs.addAll(chunkDefs(args));
								voc.put(args[WORD], defs);
								out.println("<definition of word successfully added>/<значения слова успешно добавлены>");
								out.flush();
							}
						}
						break;
					case "get" :
						synchronized (this) {
							if (voc.containsKey(args[WORD])){
								out.println(voc.get(args[WORD]));
								out.flush();		
							} else {
								String t = "<there is no such word in dictionary>/<слово отсутвует в словаре>";
								out.println(t);
								out.flush();		
							}
						}
						break;
					case "delete" :
						synchronized (this) {
							if (voc.containsKey(args[WORD])){
								Set<String> defs = voc.get(args[1]);
								defs.removeAll(chunkDefs(args));
								voc.put(args[1], defs);
								out.println("<word's defenitions removed>/<значения слова успешно удалены>");
								out.flush();
							} else {
								out.println("<there is no such word>/<такого слова не существует>");
								out.flush();
							}
						}
						break;
						default :
							out.println("<there is no command>/<нет команд от клиента>");
							out.flush();
							break;
					}

					System.out.println(voc);

					/*************************************************/
					if (!ServerOn) {
						// Special command. Quit this thread
						System.out.print("Server has already stopped");
						out.println("Server has already stopped");
						out.flush();
						m_bRunThread = false;

					}

					if (clientCommand.equalsIgnoreCase("quit")) {
						// Special command. Quit this thread
						m_bRunThread = false;
						System.out.print("Stopping client thread for client : ");
					} else if (clientCommand.equalsIgnoreCase("end")) {
						// Special command. Quit this thread and Stop the Server
						m_bRunThread = false;
						System.out.print("Stopping client thread for client : ");
						ServerOn = false;
					} 
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// Clean up
				try {
					in.close();
					out.close();
					myClientSocket.close();
					System.out.println("...Stopped");
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}

		private Collection<String> chunkDefs(String[] args) {
			List<String> defs = new ArrayList<>();
			for (int i = 2; i < args.length; i++) {
				defs.add(args[i].replaceAll(",", "").trim());
			}
			return defs;
		}

	}
}