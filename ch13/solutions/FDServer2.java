package ch13.solutions;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FDServer2 {
  static int fdPort = 3283; // D-A-T-E on a phonepad :)
  static DateTimeFormatter shortish = DateTimeFormatter.ofPattern("MM/dd/yy hh:mm a");
  public static void serveDateAndTime() {
    for (;;) { // this is sometimes called a "forever" loop
      System.out.println("Waiting ...");
      try (ServerSocket listener = new ServerSocket(fdPort)) {
        Socket fdClient = listener.accept();
        System.out.println("Incoming request from " + fdClient.getInetAddress());
        Thread.startVirtualThread(() -> {
          try {
            OutputStreamWriter osw = new OutputStreamWriter(fdClient.getOutputStream());
            PrintWriter pw = new PrintWriter(osw);
            pw.println(shortish.format(LocalDateTime.now()));
            pw.close();
            fdClient.close();
          } catch(Exception e) {
            System.err.println("Problem handling client: " + e);
          }
        });
      } catch(Exception e) {
        // We're being a bit lazy here, but this server is so
        // simple, there's not much else to do.
        System.err.println("Oh no! Server error: " + e);
      }
    }
  }

  public static void main(String args[]) {
    serveDateAndTime();
  }
}
