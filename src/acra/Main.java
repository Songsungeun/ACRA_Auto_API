package acra;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.json.simple.JSONObject;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.util.concurrent.Promise;



public class Main {
	private static final String JIRA_URL = "";
	private static final String JIRA_ADMIN_USERNAME = "";
	private static final String JIRA_ADMIN_PASSWORD = "";
	
    public static int bugCount = 1, improvementCount = 1, newFuncCount = 1;
    
    public static ArrayList<String> bugList, improvementList, newFuncList; 
    public static Date today = new Date();
	public static SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat hour = new SimpleDateFormat("hh-mm-ss");
    
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		System.out.println("진행할 프로세스를 선택하세요.");
		System.out.println("0. 종료,  1. 릴리즈 노트 생성,  2. 릴리즈 노트 리넘버링");
		int selectNo = scan.nextInt();
		switch(selectNo) {
		case 0: System.out.println("프로세스를 종료합니다."); System.exit(0); break;
		case 1: create(); break;
		case 2: System.out.println("v2.0에서 추가됩니다. 생성 기능만 쓰세요.^^ 종료할게요 데헷."); System.exit(0); break;
		}
		scan.close();
	}
	
//	private static void reNumber() {
//		
//		// 파일명 입력
//		Scanner reNumberScan = new Scanner(System.in);
//		String reNumFileName;
//		while (true) {
//			System.out.println("리넘버링할 파일명을 입력해주세요. (동일 경로에 있어야 합니다.)");
//			reNumFileName = reNumberScan.nextLine();
//			System.out.println(reNumFileName + "이 맞습니까? (완료:1, 다시입력:2)");
//			int no = reNumberScan.nextInt();
//			if (no == 1) {
//				break;
//			} else {
//				continue;
//			}
//		}
//		reNumberScan.close();
//		
//		if (!reNumFileName.isEmpty() && reNumFileName != null) {
//			try {
//				BufferedWriter out = new BufferedWriter(new FileWriter(reNumFileName + "- reNumber" + date.format(today) + "-" + hour.format(today) + ".txt"));
//				
//				BufferedReader in = new BufferedReader(new FileReader(reNumFileName));
//				String s;
//				
//				int listNo = 1;
//				while ((s = in.readLine()) != null) {
//					// 대분류 만날경우 넘버링 초기화
//					if(s.contains("[버그 수정]") || s.contains("[기능 개선]") || s.contains("[새기능]") || s.contains("|[버그 수정 상세]")) {
//						System.out.println("contains 진입");
//						System.out.println(s);
//						listNo = 0;
//					}
//					
//					// Todo
//					// 클래스 분할 및 리넘버링 함수
////					if (Integer.parseInt(s.substring(0, 1)))
//					out.write(s); out.newLine();
//					listNo++;
//				}
//				
//				
//			} catch (IOException e) {
//				System.out.println("오류가 발생하였습니다.");
//				System.out.println(e);
//			}
//		}
//		
//		
//	}

	public static void create() {
		// 입력 받을 값
		Scanner createScan = new Scanner(System.in);
		System.out.println("버전 명을 입력하세요. :");
		String versionInfo = createScan.nextLine();
//						String versionInfo = "V2.4.6.f5.11";
		System.out.println("Release 날짜를 입력하세요. :");
//						String releaseDate = "2018-10-12";
		String releaseDate = createScan.nextLine();
		System.out.println("JqlQuery를 입력하세요. :");
		String inputJqlQuery = createScan.nextLine();
		createScan.close();
		
		System.out.println(String.format("Logging in to %s with username '%s'", JIRA_URL, JIRA_ADMIN_USERNAME));
        JiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
        try {
        	
        	URI uri = new URI(JIRA_URL);
        	
        	JiraRestClient client = factory.createWithBasicHttpAuthentication(uri, JIRA_ADMIN_USERNAME, JIRA_ADMIN_PASSWORD);
        	
//				        System.out.println("JIRA User 정보 로딩");
//				        Promise<User> promise = client.getUserClient().getUser("hanna");
//				        User user = promise.claim();
        	
//				        for (BasicProject project : client.getProjectClient().getAllProjects().claim()) {
//				            System.out.println(project.getKey() + ": " + project.getName());
//				        }
        	
        	System.out.println("입력된 쿼리 실행");
//        	Promise<SearchResult> searchJqlPromise = client.getSearchClient().searchJql(inputJqlQuery);
        	
        	bugList = new ArrayList<>();
        	improvementList = new ArrayList<>();
        	newFuncList = new ArrayList<>();
        	
//        	totalIssueList = searchJqlPromise.get().getIssues();
        	List<Issue> totalIssueList = new ArrayList<>();
        	Set<String> set = new HashSet<String>();
        	set.add("*all");
        	int start = 0;
        	int maxPerPage = 50;
        	int total = 0;
        	Promise<SearchResult> searchJqlPromise;
        	do {
        		searchJqlPromise = client.getSearchClient().searchJql(inputJqlQuery, maxPerPage, start, set);
        		total = searchJqlPromise.get().getTotal();
        		start += maxPerPage;
        		searchJqlPromise.get().getIssues().iterator().forEachRemaining(totalIssueList::add);
        		
        	} while (total > start);
        			
        	System.out.println("=============================================================");
        	System.out.println("이슈 로딩 완료");
        	System.out.println("해당 쿼리 전체 개수: " + total);
        	System.out.println("받은 개수: " + totalIssueList.size());
        	System.out.println("=============================================================");
        	for (Issue issue : totalIssueList) {
        		switch (issue.getIssueType().getName()) {
        		case "버그": bugList.add(bugCount + ". [" + issue.getKey() + "]" + issue.getSummary()); bugCount++; break;
        		case "개선": improvementList.add(improvementCount + ". [" + issue.getKey() + "]" + issue.getSummary()); improvementCount++; break;
        		case "새 기능": newFuncList.add(newFuncCount + ". [" + issue.getKey() + "]" + issue.getSummary()); newFuncCount++; break;
        		}
        	}
        	
        	// 개수 확인하기 위한 콘솔
        	System.out.println("=============================================================");
        	System.out.println("* 유형별 분류 완료");
        	System.out.println("1. 버그 개수: " + bugList.size() + "개");
        	System.out.println("1. 개선 개수: " + improvementList.size() + "개");
        	System.out.println("1. 새 기능 개수: " + newFuncList.size() + "개");
        	System.out.println("=============================================================");
        	
        	System.out.println("파일 쓰기 시작");
        	try {
        		
        		BufferedWriter out = new BufferedWriter(new FileWriter("Release Note - " + date.format(today) + "-" + hour.format(today) + ".txt"));
        		
        		out.write("Penta Systems Technology Inc. ACRA Point " + versionInfo + " Release Note"); out.newLine();
        		out.write("================================================================================"); out.newLine();
        		out.newLine();
        		
        		// 약관 입력 영역
        		BufferedReader in = new BufferedReader(new FileReader("template.txt"));
        		String s;
        		
        		while ((s = in.readLine()) != null) {
        			out.write(s); out.newLine();
        		}
        		in.close(); out.newLine(); out.newLine();
        		out.write("DATE: " + releaseDate); out.newLine(); out.newLine();
        		// 약관 입력 완료
        		
        		out.write("-------------------------------------------------------------------------------");
        		out.newLine(); out.newLine();
        		out.write("CONTENTS"); out.newLine(); out.newLine();
        		out.write("Release Notes"); out.newLine(); out.newLine();
        		
        		
        		// 버그 리스트 출력
        		out.write("[버그 수정]"); out.newLine();
        		for (String bugContent : bugList) {
        			out.write(bugContent); out.newLine();
        		}
        		out.newLine();
        		out.newLine();
        		
        		// 개선건 출력
        		out.write("[기능 개선]"); out.newLine();
        		for (String improvementContent : improvementList) {
        			out.write(improvementContent); out.newLine();
        		}
        		out.newLine();
        		out.newLine();
        		
        		// 새 기능 건 출력
        		out.write("[새기능]"); out.newLine();
        		for (String newFuncContent : newFuncList) {
        			out.write(newFuncContent); out.newLine();
        		}
        		out.newLine();
        		out.newLine();
        		
        		out.write("-------------------------------------------------------------------------------");
        		out.newLine();
        		out.write("[버그 수정 상세]"); out.newLine();
        		
        		int bugDetailCount = 1;
        		for (Issue eachIssue : totalIssueList) {
        			if (eachIssue.getIssueType().getName().equals("버그")) {
        				out.write(bugDetailCount + ". " + eachIssue.getSummary()); out.newLine();
        				out.write("- Number: [" + eachIssue.getKey()+ "]"); out.newLine();
        				out.write("- Problem: "); out.newLine();
        				if (eachIssue.getDescription() != null) {
        					if (eachIssue.getDescription().contains("현상(상세기입)")) {
        						out.write(eachIssue.getDescription().split("현상\\(상세기입\\):\r\n")[1].replaceAll("#|>", "")); out.newLine();
        					} else {
        						out.write(eachIssue.getDescription()); out.newLine();
        					}
        				}
        				out.write("- Solution: 로직 수정 후 반영"); out.newLine(); out.newLine();
        				bugDetailCount++;
        			}
        		}
        		
        		out.write("-------------------------------------------------------------------------------");
        		out.newLine();
        		out.write("End of Penta Systems Technology Inc. ACRA Point " + versionInfo + " Release Note");
        		out.newLine();
        		
        		out.close();	
        	} catch (IOException e) {
        		System.out.println("파일 쓰기중 오류가 발생하였습니다. 관리자에게 문의 하세요.");
        		System.err.println(e); // 에러가 있다면 메시지 출력
        		System.exit(1);
        	}
        } catch (Exception er) {
        	System.out.println("error 발생");
        	System.out.println(er);
        }
        
        // Done
        System.out.println("파일 생성 완료");
        System.out.println("파일명: Release Note - " + date.format(today) + ":" + hour.format(today) + ".txt");
        System.exit(0);
	}
}

//"objectInfo":{
//	"RuleType":"SC",
//	"RuleBlackOrWhite":"1",
//	"RulePriority":"1",
//	"UserID":"acrauser",
//	"RuleSecret":"",
//	"ValidfromDate":"",
//	"ValidtoDate":"",
//	"ValidWeekDays":"1111111",
//	"ValidHours":"111111111111111111111111"
//}
