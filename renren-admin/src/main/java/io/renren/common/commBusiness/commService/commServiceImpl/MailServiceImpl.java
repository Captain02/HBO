package io.renren.common.commBusiness.commService.commServiceImpl;

import io.renren.common.commBusiness.commService.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")  //发送人的邮箱  比如155156641XX@163.com
    private String from;

    /**
     * 发送普通邮件
     *
     * @param to：收件人
     * @param cc：抄送人
     * @param subject：邮件主题
     * @param content：邮件内容
     */
    @Override
    public void sendSimpleMail(String to, String cc, String subject, String content) {
        SimpleMailMessage simpMsg = new SimpleMailMessage();
        simpMsg.setFrom(from);
        simpMsg.setTo(to);
        simpMsg.setCc(cc);
        simpMsg.setSubject(subject);
        simpMsg.setText(content);
        javaMailSender.send(simpMsg);
    }

    /**
     * 发送带附件的邮件
     *
     * @param to
     * @param subject
     * @param content
     * @param file
     */
    @Override
    public void sendAttachFileMail(String to, String subject, String content, File file) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content);
        helper.addAttachment(file.getName(), file);
        javaMailSender.send(message);
    }

    /**
     * 发送带图片资源的邮件
     *
     * @param to
     * @param subject
     * @param content
     * @param srcPath
     * @param resids
     * @throws MessagingException
     */
    @Override
    public void sendMailWithirng(String to, String subject, String content, String[] srcPath, String[] resids) throws MessagingException {
        if (srcPath.length != resids.length) {
            System.out.println("发送失败");
            return;
        }
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setSubject(subject);
        helper.setText(content, true);
        for (int i = 0; i < srcPath.length; i++) {
            FileSystemResource res = new FileSystemResource(new File(srcPath[i]));
            helper.addInline(resids[i], res);
        }
    }

    /**
     * 发送html格式的邮件
     *
     * @param to 接受者
     * @param subject 主题
     * @param content 内容
     */
    @Override
    public void sendHtmlMail(String to, String subject, String content) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        //true表示需要创建一个multipart message
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setTo(from);
        helper.setSubject(subject);
        helper.setText(content, true);
        javaMailSender.send(message);
    }
}