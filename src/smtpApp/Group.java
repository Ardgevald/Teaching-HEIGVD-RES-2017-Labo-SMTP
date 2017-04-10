
package smtpApp;

import java.util.ArrayList;

public class Group {
   private final ArrayList<String> mailAddresses = new ArrayList<>();
   
   public void addMail(String mailAddress){
      mailAddresses.add(mailAddress);
   }

   public ArrayList<String> getMailAddresses() {
      return mailAddresses;
   }
}
