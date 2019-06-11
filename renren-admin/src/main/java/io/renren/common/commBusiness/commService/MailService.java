package io.renren.common.commBusiness.commService;

import javax.mail.MessagingException;
import java.io.File;

public interface MailService {

    public void sendSimpleMail(String to, String cc, String subject, String content);

    public void sendAttachFileMail(String to, String subject, String content, File file) throws MessagingException;

    public void sendMailWithirng(String to , String subject , String content, String[] srcPath,String[] resids) throws MessagingException;

    public void sendHtmlMail(String to, String subject, String content) throws MessagingException;
}
