/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smtpProtocol;

import java.util.ArrayList;

/**
 *
 * @author Miguel-Portable
 */
public interface SmtpMail {
   public String getSubject();
   public String getData();
   public String getFrom();
   public ArrayList<String> getTo();
}
