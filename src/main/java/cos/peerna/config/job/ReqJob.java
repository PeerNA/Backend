package cos.peerna.config.job;

import cos.peerna.domain.Category;
import cos.peerna.service.ProblemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.quartz.*;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;

@Component
@Slf4j
@RequiredArgsConstructor
public class ReqJob extends QuartzJobBean implements InterruptableJob {

	private final ProblemService problemService;

	HashMap<String, Category> map = new HashMap<String, Category>(){{
		put("자료구조", Category.DATA_STRUCTURE);
		put("알고리즘", Category.ALGORITHM);
		put("운영체제", Category.OS);
		put("네트워크", Category.NETWORK);
		put("보안", Category.SECURITY);
		put("데이터베이스", Category.RDB);
		put("자바", Category.JAVA);
		put("자바스크립트", Category.JAVASCRIPT);
		put("파이썬", Category.PYTHON);
		put("스프링", Category.SPRING);
		put("리액트", Category.REACT);
		put("노드", Category.NODE_JS);
		put("장고", Category.DJANGO);
		put("암호학/보안(간단한 정도)", Category.SECURITY);
		put("컴파일러", Category.COMPILER);
		put("트러블 슈팅", Category.TROUBLE);
		put("디자인 패턴", Category.DESIGN);
		put("테스트", Category.TEST);
		put("인프라/클라우드", Category.INFRA);
		put("컨테이너", Category.CONTAINER);
		put("DevOps", Category.DEVOPS);
		put("커뮤니케이션", Category.COMMUNICATION);
		put("개인의 역량", Category.WORK);
		put("최신기술에 관심이 있는지", Category.TECH);
		put("코딩테스트", Category.CODING_TEST);
	}};

	@Override
	public void interrupt() throws UnableToInterruptJobException {

	}

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		String dev = jobDataMap.getString("Dev");
		String work = jobDataMap.getString("Work");
		JobKey jobKey = context.getJobDetail().getKey();

		log.info("============================================================================");
		log.info("Developed by {} and Work is {}", dev, work);
		log.info("Jobkey : {}", jobKey);

		final String url = "https://github.com/ksundong/backend-interview-question#%EC%96%B8%EC%96%B4-%EA%B4%80%EB%A0%A8";
		Connection conn = Jsoup.connect(url);

		try {
			Document document = conn.get();
			Element check = document.select("relative-time").first();
			if (check.text().contains("hours ago")) {
				Elements category = document.select("h3");
				for (Element cat : category) {
					//				System.out.println("Category : " + cat.text());
					Elements items = cat.nextElementSiblings().select("summary");
					for (Element summary : items) {
						//					System.out.println("Problem : " + summary.text());
						Elements contents = summary.nextElementSiblings().select("p");
						StringBuilder sb = new StringBuilder();
						for (Element content : contents) {
							sb.append(content.text() + " ");
							//						problemService.make(summary.text(), content.text(), Category.JAVA);
						}
						String result = sb.toString();
						//					System.out.println("Answer : " + result);
						if (!cat.text().equals("과제 전형") && !cat.text().equals("자료구조/알고리즘"))
							//						problemService.make(summary.text(), result, map.getOrDefault(cat.text(), Category.valueOf(cat.text().toUpperCase())));
							problemService.make(summary.text(), result, Category.CODING_TEST);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		log.info("============================================================================");
	}
}
