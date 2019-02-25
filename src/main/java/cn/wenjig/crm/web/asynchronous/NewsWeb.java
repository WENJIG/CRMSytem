package cn.wenjig.crm.web.asynchronous;

import cn.wenjig.crm.common.annotation.SystemLog;
import cn.wenjig.crm.data.domain.News;
import cn.wenjig.crm.util.JsonUtil;
import cn.wenjig.crm.web.BaseWeb;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.io.IOException;
import java.util.LinkedList;

@RestController
@RequestMapping(value = "/news")
public class NewsWeb extends BaseWeb {

    private final String url = "https://news.qq.com/";

    @RolesAllowed({"ROLE_在职员工"})
    @SystemLog(description = "查看新闻头条", isLogReturn = false)
    @RequestMapping(value = "/")
    public String getNews() {
        try {
            Document document = simulationWebClient();
            Elements titleDom = document.body().select(".linkto");
            LinkedList<News> newsList = new LinkedList<>();
            for (Element element : titleDom) {
                News news = new News();
                news.setTitle(element.text());
                news.setUrl(element.attr("abs:href"));
                newsList.add(news);
            }
            return JsonUtil.toJson(newsList);
        } catch (IOException e) {
            e.printStackTrace();
            return "获取数据失败！";
        }
    }

    /**
     * @Description: 对非静态网页模拟浏览器操作
     * @param
     * @Return org.jsoup.nodes.Document
     */
    private Document simulationWebClient() throws IOException {
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setActiveXNative(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setTimeout(10000);
        HtmlPage htmlPage = null;
        try {
            htmlPage = webClient.getPage(url);
            webClient.waitForBackgroundJavaScript(10000);
            String htmlString = htmlPage.asXml();
            return Jsoup.parse(htmlString);
        } finally {
            webClient.close();
        }

    }

}
