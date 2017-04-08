package smtpApp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;
import java.util.Scanner;
import smtpProtocol.SmtpClient;

/**
 *
 */
public class PrankMailBuilder {

   private static final String MAIL_SEPARATOR = "####";
   private static final int MIN_VICTIMS = 3;

   private final ArrayList<String> mailList = new ArrayList<>();
   private final ArrayList<Group> groups = new ArrayList<>();

   private final ArrayList<Mail> mails = new ArrayList<>();

   private final SmtpClient client;

   public PrankMailBuilder(int nbGroups, String address, int port) throws IOException, BadConfiguration {

      client = new SmtpClient(address, port);

      BufferedReader reader = new BufferedReader(new FileReader("victims.txt"));
      while (reader.ready()) {
         mailList.add(reader.readLine());
      }

      if (mailList.size() < MIN_VICTIMS) {
         throw new BadConfiguration("Not enough victims, must be at least 3");
      }

      try (Scanner scanner = new Scanner(new File("mails.txt"), "UTF-8")) {
         scanner.useDelimiter(MAIL_SEPARATOR);

         while (scanner.hasNext()) {
            Mail mail = new Mail();

            mail.setSubject(scanner.nextLine());
            mail.setData(scanner.next());

            mails.add(mail);
         }
      }

      if (mails.isEmpty()) {
         throw new BadConfiguration("Must contain at least one mail");
      }

      Collections.shuffle(mailList);

      int nbVictimsPerGroup = mailList.size() / nbGroups;

      // if enough victims, all groups are different
      // else, there can be overlaps
      if (nbVictimsPerGroup >= MIN_VICTIMS) {
         for (int i = 0; i < nbGroups; i++) {
            Group group = new Group();
            for (int j = 0; j < nbVictimsPerGroup; j++) {
               group.addMail(mailList.get(i * nbVictimsPerGroup + j));
            }
            groups.add(group);
         }
      } else {
         for (int i = 0; i < nbGroups; i++) {
            nbVictimsPerGroup = MIN_VICTIMS;
            Collections.shuffle(mailList);
            Group group = new Group();
            for (int j = 0; j < nbVictimsPerGroup; j++) {
               group.addMail(mailList.get(j));
            }
            groups.add(group);
         }
      }

      int mailNumber = 0;

      for (Group group : groups) {
         Mail mail = mails.get(mailNumber++ % mails.size());
         mail.setFrom(group.getMailAddresses().get(0));
         mail.setTo(new ArrayList<>(group.getMailAddresses().subList(1, group.getMailAddresses().size())));

         client.sendMail(mail);
      }
   }

   public static void main(String[] args) {

      Properties prop = new Properties();
      try {
         prop.load(PrankMailBuilder.class.getResourceAsStream("config.properties"));
      } catch (IOException ex) {
         System.err.println("Could not load default properties: " + ex.getMessage());
      }

      String address = prop.getProperty("smtpServerDefaultAddress");
      int port = Integer.parseUnsignedInt(prop.getProperty("smtpServerDefaultPort"));
      int nbGroups = Integer.parseUnsignedInt(prop.getProperty("defaultGroupNumber"));

      if (args.length > 0) {

         try {
            for (int i = 0; i < args.length; i += 2) {
               switch (args[i]) {
                  case "-g":
                  case "-G":
                     nbGroups = Integer.parseInt(args[i + 1]);
                     break;
                  case "-p":
                  case "-P":
                     port = Integer.parseInt(args[i + 1]);
                     break;
                  case "-a":
                  case "-A":
                     address = args[i + 1];
                     break;
               }
            }
         } catch (IndexOutOfBoundsException e) {
            System.out.println("Missing argument value");
         } catch (NumberFormatException e) {
            System.out.println("Bad argument" + e.getMessage());
         }
      }

      try {
         System.out.println("Nb groups : " + nbGroups);
         System.out.println("address : " + address);
         System.out.println("port : " + port);

         new PrankMailBuilder(nbGroups, address, port);
      } catch (IOException | BadConfiguration e) {
         System.err.println(e.getMessage());
      }
   }
}
