package smtpProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class SmtpClient {

   private final static Logger LOGS = Logger.getLogger(SmtpClient.class.getName());

   private Socket serverSocket;
   private PrintWriter outWriter;
   private BufferedReader inReader;

   private String serverAddress;
   private int serverPort = 25;

   private static final String ENDL = "\r\n";
   private static final String CHARSET = "UTF-8";
   private static final String END_MAIL = ENDL + "." + ENDL;

   private static final String SMTP_OK = "250";

   public SmtpClient(String serverAddress) {
      this.serverAddress = serverAddress;
   }

   public SmtpClient(String serverAddress, int serverPort) {
      this(serverAddress);
      this.serverPort = serverPort;
   }

   public void sendMail(SmtpMail mail) throws IOException {
      // connection to server
      String line;

      serverSocket = new Socket(serverAddress, serverPort);
      inReader = new BufferedReader(new InputStreamReader(serverSocket.getInputStream(), CHARSET));
      outWriter = new PrintWriter(new OutputStreamWriter(serverSocket.getOutputStream(), CHARSET));

      LOGS.log(Level.INFO, inReader.readLine());

      // query to server
      outWriter.write("EHLO user" + ENDL);
      outWriter.flush();

      LOGS.log(Level.INFO, line = inReader.readLine());

      // check connection
      if (!line.startsWith(SMTP_OK)) {
         throw new IOException(line);
      }

      while (line.startsWith(SMTP_OK + "-")) {
         LOGS.log(Level.INFO, line = inReader.readLine());
      }

      // Setting mail sender and receiver
      outWriter.write("MAIL FROM:" + mail.getFrom() + ENDL);
      outWriter.flush();

      LOGS.log(Level.INFO, inReader.readLine());

      for (String to : mail.getTo()) {
         outWriter.write("RCPT TO:" + to + ENDL);
         outWriter.flush();

         LOGS.log(Level.INFO, inReader.readLine());
      }

      // beginning mail
      outWriter.write("DATA" + ENDL);
      outWriter.flush();
      
      LOGS.log(Level.INFO, inReader.readLine());
      
      // writing content of the mail
      outWriter.write("Content-Type: text/plain; charset=\"" + CHARSET.toLowerCase() + "\"" + ENDL);
      outWriter.write("From: " + mail.getFrom() + ENDL);
      
      outWriter.write("To: " + mail.getTo().get(0));
      for (int i = 1; i < mail.getTo().size(); i++) {
         outWriter.write(", " + mail.getTo().get(i));
      }
      outWriter.write(ENDL);
      
      outWriter.write("Subject: " + mail.getSubject() + ENDL + ENDL);
      outWriter.write(mail.getData() + END_MAIL);
      outWriter.flush();
      
      LOGS.log(Level.INFO, inReader.readLine());
      
      // ending connection with server
      outWriter.write("QUIT" + ENDL);
      outWriter.flush();
      
      serverSocket.close();
   }
}
