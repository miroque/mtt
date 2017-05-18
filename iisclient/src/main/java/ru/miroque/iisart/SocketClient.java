package ru.miroque.iisart;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class SocketClient {
	public static void main(String[] args) {
		// First parameter has to be Server host name or IP address
		if (args.length == 0) {
			System.out.println("Usage : SocketClient <serverName>");
			return;
		}

		Socket s = null;

		// Create the socket connection to the MultiThreadedSocketServer port
		// 11111
		try {
			s = new Socket(args[0], 11111);
		} catch (UnknownHostException uhe) {
			// Server Host unreachable
			System.out.println("Unknown Host :" + args[0]);
			s = null;
		} catch (IOException ioe) {
			// Cannot connect to port on given server host
			System.out.println("Cant connect to server at 11111. Make sure it is running.");
			s = null;
		}

		if (s == null)
			System.exit(-1);

		BufferedReader in = null;
		PrintWriter out = null;

		try {
			// Create the streams to send and receive information
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));

			// Since this is the client, we will initiate the talking.
			// Send a string data and flush
			out.println("What is going on Server?");
			out.flush();
			// Receive the reply.
			System.out.println(in.readLine());

//			String stopMark = "";
//			while (!stopMark.equalsIgnoreCase("done")) {
//				System.in.
//			}
			
			// *************************
			Scanner scanner = new Scanner(System.in);

	        while (true) {

	            System.out.print("Enter something : ");
	            String input = scanner.nextLine();

	            if ("done".equalsIgnoreCase(input)) {
	                System.out.println("Exit!");
	                break;
	            }

	            System.out.println("input : " + input);
	            out.println(input);
	            out.flush();
	            System.out.println("-----------\n");
	        }

	        scanner.close();
			// *************************
			
			// Send the special string to tell server to quit.
//			out.println("end");
//			out.flush();
		} catch (IOException ioe) {
			System.out.println("Exception during communication. Server probably closed connection.");
		} finally {
			try {
				// Close the input and output streams
				out.close();
				in.close();
				// Close the socket before quitting
				s.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
