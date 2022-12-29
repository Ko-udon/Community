package community.project.community.components;

import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor

public class MailComponents {
  //dlfmafjzhzihocih
  private final JavaMailSender javaMailSender;

  public boolean sendMail(String mail,String subject,String text){
    boolean result=false;

    MimeMessagePreparator msg=new MimeMessagePreparator() {
      @Override
      public void prepare(MimeMessage mimeMessage) throws Exception {
        MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimeMessage, true,"UTF-8");
        mimeMessageHelper.setTo(mail);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(text,true);

      }
    };
    try{
      javaMailSender.send(msg);
      result=true;

    }catch (Exception e){
      System.out.println(e.getMessage());
    }
    return result;

  }

}
