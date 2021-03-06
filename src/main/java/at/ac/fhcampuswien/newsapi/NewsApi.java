package at.ac.fhcampuswien.newsapi;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import at.ac.fhcampuswien.newsapi.beans.NewsResponse;
import at.ac.fhcampuswien.newsapi.enums.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.NoSuchElementException;


public class NewsApi {

    public static final String DELIMITER = "&";

    /**
     * For detailed documentation of the API see: https://newsapi.org/docs
     *
     * %s is a filler for endpoint like top-headlines, everything (see /newsapi/enums/Endpoint)
     * q=%s is a filler for specified query
     *
     * Example URL: https://newsapi.org/v2/top-headlines?country=us&apiKey=myKey
     */
    public static final String NEWS_API_URL = "http://newsapi.org/v2/%s?q=%s&apiKey=%s";

    private Endpoint endpoint;
    private String q;
    private String qInTitle;
    private Country sourceCountry;
    private Category sourceCategory;
    private String domains;
    private String excludeDomains;
    private String from;
    private String to;
    private Language language;
    private SortBy sortBy;
    private String pageSize;
    private String page;
    private String apiKey;

    public Endpoint getEndpoint() {
        return endpoint;
    }

    public String getQ() {
        return q;
    }

    public String getqInTitle() {
        return qInTitle;
    }

    public Country getSourceCountry() {
        return sourceCountry;
    }

    public Category getSourceCategory() {
        return sourceCategory;
    }

    public String getDomains() {
        return domains;
    }

    public String getExcludeDomains() {
        return excludeDomains;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public Language getLanguage() {
        return language;
    }

    public SortBy getSortBy() {
        return sortBy;
    }

    public String getPageSize() {
        return pageSize;
    }

    public String getPage() {
        return page;
    }

    public String getApiKey() {
        return apiKey;
    }

    public NewsApi(String q, String qInTitle, Country sourceCountry, Category sourceCategory, String domains, String excludeDomains, String from, String to, Language language, SortBy sortBy, String pageSize, String page, String apiKey, Endpoint endpoint) {
        this.q = q;
        this.qInTitle = qInTitle;
        this.sourceCountry = sourceCountry;
        this.sourceCategory = sourceCategory;
        this.domains = domains;
        this.excludeDomains = excludeDomains;
        this.from = from;
        this.to = to;
        this.language = language;
        this.sortBy = sortBy;
        this.pageSize = pageSize;
        this.page = page;
        this.apiKey = apiKey;
        this.endpoint = endpoint;
    }

    protected String requestData() {
        String url = null;
        try {
            url = buildURL();
        } catch (NewsApiException e) {
            System.out.println("URL not working");
        }
        System.out.println("URL: " + url);
        URL obj = null;
        try {
            obj = new URL(url);
        } catch (MalformedURLException e) {
            // TODO improve ErrorHandling
            //e.printStackTrace();

            System.out.println("Error: "+e.getMessage());
            return null;

        }
        HttpURLConnection con;
        StringBuilder response = new StringBuilder();
        try {
            con = (HttpURLConnection) obj.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (NullPointerException e){
            System.out.println("Error: Could not open connection");
        } catch (IOException e) {
            // TODO improve ErrorHandling
            System.out.println("Error "+e.getMessage());
        }
        return response.toString();
    }

    protected String buildURL() throws NewsApiException {
        // TODO ErrorHandling
        String urlbase = null;
        StringBuilder sb = null;

        try{
            urlbase = String.format(NEWS_API_URL,getEndpoint().getValue(),getQ(),getApiKey());
            sb = new StringBuilder(urlbase);
        } catch (Exception e){
            //System.out.println("URL not working");
            return "Error: URL not working";
        }



        System.out.println(urlbase);

        try{
            if(getFrom() != null){
                sb.append(DELIMITER).append("from=").append(getFrom());
            }
            else{
                throw new NewsApiException("From is null");
            }
            if(getTo() != null){
                sb.append(DELIMITER).append("to=").append(getTo());
            }
            else{
                throw new NewsApiException("To is null");
            }
            if(getPage() != null){
                sb.append(DELIMITER).append("page=").append(getPage());
            }
            else{
                throw new NewsApiException("Page is null");
            }
            if(getPageSize() != null){
                sb.append(DELIMITER).append("pageSize=").append(getPageSize());
            }
            else{
                throw new NewsApiException("getPageSize is null");
            }
            if(getLanguage() != null){
                sb.append(DELIMITER).append("language=").append(getLanguage());
            }
            else{
                throw new NewsApiException("getLanguage is null");
            }
            if(getSourceCountry() != null){
                sb.append(DELIMITER).append("country=").append(getSourceCountry());
            }
            else{
                throw new NewsApiException("SourceCountry is null");
            }
            if(getSourceCategory() != null){
                sb.append(DELIMITER).append("category=").append(getSourceCategory());
            }
            else{
                throw new NewsApiException("SourceCategory is null");
            }
            if(getDomains() != null){
                sb.append(DELIMITER).append("domains=").append(getDomains());
            }
            else{
                throw new NewsApiException("Domains is null");
            }
            if(getExcludeDomains() != null){
                sb.append(DELIMITER).append("excludeDomains=").append(getExcludeDomains());
            }
            else{
                throw new NewsApiException("ExcludeDomains is null");
            }
            if(getqInTitle() != null){
                sb.append(DELIMITER).append("qInTitle=").append(getqInTitle());
            }
            else{
                throw new NewsApiException("qInTitle is null");
            }
            if(getSortBy() != null){
                sb.append(DELIMITER).append("sortBy=").append(getSortBy());
            }
            else{
                throw new NewsApiException("SortBy is null");
            }
        } catch(NewsApiException e){
            System.out.println(e.getMessage());
        }

        return sb.toString();
    }

    public NewsResponse getNews() {
        NewsResponse newsReponse = null;
        String jsonResponse = requestData();
        if(jsonResponse != null && !jsonResponse.isEmpty()){

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                newsReponse = objectMapper.readValue(jsonResponse, NewsResponse.class);
                if(!"ok".equals(newsReponse.getStatus())){
                    System.out.println("Error: "+newsReponse.getStatus());
                }
            } catch (JsonProcessingException e) {
                System.out.println("Error: "+e.getMessage());

            }
        }
        //TODO improve Errorhandling
        return newsReponse;
    }
}




