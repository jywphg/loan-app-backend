package cyou.oxling.loanappbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class LoanAppBackendApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		
		// 将.env中的变量设置为系统属性，以便Spring能够识别
		System.setProperty("GETUI_APPID", dotenv.get("GETUI_APPID"));
		System.setProperty("GETUI_APPKEY", dotenv.get("GETUI_APPKEY"));
		System.setProperty("GETUI_MASTERSECRET", dotenv.get("GETUI_MASTERSECRET"));
		
		SpringApplication.run(LoanAppBackendApplication.class, args);
	}

}
