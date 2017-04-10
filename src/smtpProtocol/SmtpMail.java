package smtpProtocol;

import java.util.ArrayList;

public interface SmtpMail {
   public String getSubject();
   public String getData();
   public String getFrom();
   public ArrayList<String> getTo();
}
