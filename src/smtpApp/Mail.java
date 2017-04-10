
package smtpApp;

import java.util.ArrayList;
import smtpProtocol.SmtpMail;

public class Mail implements SmtpMail{
   private String from;
   private ArrayList<String> to;
   
   private String data;
   private String subject;

   @Override
   public String getData() {
      return data;
   }

   @Override
   public String getFrom() {
      return from;
   }

   @Override
   public String getSubject() {
      return subject;
   }
   
   @Override
   public ArrayList<String> getTo() {
      return to;
   }

   public void setData(String data) {
      this.data = data;
   }

   public void setFrom(String from) {
      this.from = from;
   }

   public void setSubject(String subject) {
      this.subject = subject;
   }
   
   public void setTo(ArrayList<String> to) {
      this.to = to;
   }
   
}
