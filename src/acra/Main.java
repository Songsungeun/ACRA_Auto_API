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
		System.out.println("������ ���μ����� �����ϼ���.");
		System.out.println("0. ����,  1. ������ ��Ʈ ����,  2. ������ ��Ʈ ���ѹ���");
		int selectNo = scan.nextInt();
		switch(selectNo) {
		case 0: System.out.println("���μ����� �����մϴ�."); System.exit(0); break;
		case 1: create(); break;
		case 2: System.out.println("v2.0���� �߰��˴ϴ�. ���� ��ɸ� ������.^^ �����ҰԿ� ����."); System.exit(0); break;
		}
		scan.close();
	}
	
//	private static void reNumber() {
//		
//		// ���ϸ� �Է�
//		Scanner reNumberScan = new Scanner(System.in);
//		String reNumFileName;
//		while (true) {
//			System.out.println("���ѹ����� ���ϸ��� �Է����ּ���. (���� ��ο� �־�� �մϴ�.)");
//			reNumFileName = reNumberScan.nextLine();
//			System.out.println(reNumFileName + "�� �½��ϱ�? (�Ϸ�:1, �ٽ��Է�:2)");
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
//					// ��з� ������� �ѹ��� �ʱ�ȭ
//					if(s.contains("[���� ����]") || s.contains("[��� ����]") || s.contains("[�����]") || s.contains("|[���� ���� ��]")) {
//						System.out.println("contains ����");
//						System.out.println(s);
//						listNo = 0;
//					}
//					
//					// Todo
//					// Ŭ���� ���� �� ���ѹ��� �Լ�
////					if (Integer.parseInt(s.substring(0, 1)))
//					out.write(s); out.newLine();
//					listNo++;
//				}
//				
//				
//			} catch (IOException e) {
//				System.out.println("������ �߻��Ͽ����ϴ�.");
//				System.out.println(e);
//			}
//		}
//		
//		
//	}

	public static void create() {
		// �Է� ���� ��
		Scanner createScan = new Scanner(System.in);
		System.out.println("���� ���� �Է��ϼ���. :");
		String versionInfo = createScan.nextLine();
//						String versionInfo = "V2.4.6.f5.11";
		System.out.println("Release ��¥�� �Է��ϼ���. :");
//						String releaseDate = "2018-10-12";
		String releaseDate = createScan.nextLine();
		System.out.println("JqlQuery�� �Է��ϼ���. :");
		String inputJqlQuery = createScan.nextLine();
		createScan.close();
		
		System.out.println(String.format("Logging in to %s with username '%s'", JIRA_URL, JIRA_ADMIN_USERNAME));
        JiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
        try {
        	
        	URI uri = new URI(JIRA_URL);
        	
        	JiraRestClient client = factory.createWithBasicHttpAuthentication(uri, JIRA_ADMIN_USERNAME, JIRA_ADMIN_PASSWORD);
        	
//				        System.out.println("JIRA User ���� �ε�");
//				        Promise<User> promise = client.getUserClient().getUser("hanna");
//				        User user = promise.claim();
        	
//				        for (BasicProject project : client.getProjectClient().getAllProjects().claim()) {
//				            System.out.println(project.getKey() + ": " + project.getName());
//				        }
        	
        	System.out.println("�Էµ� ���� ����");
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
        	System.out.println("�̽� �ε� �Ϸ�");
        	System.out.println("�ش� ���� ��ü ����: " + total);
        	System.out.println("���� ����: " + totalIssueList.size());
        	System.out.println("=============================================================");
        	for (Issue issue : totalIssueList) {
        		switch (issue.getIssueType().getName()) {
        		case "����": bugList.add(bugCount + ". [" + issue.getKey() + "]" + issue.getSummary()); bugCount++; break;
        		case "����": improvementList.add(improvementCount + ". [" + issue.getKey() + "]" + issue.getSummary()); improvementCount++; break;
        		case "�� ���": newFuncList.add(newFuncCount + ". [" + issue.getKey() + "]" + issue.getSummary()); newFuncCount++; break;
        		}
        	}
        	
        	// ���� Ȯ���ϱ� ���� �ܼ�
        	System.out.println("=============================================================");
        	System.out.println("* ������ �з� �Ϸ�");
        	System.out.println("1. ���� ����: " + bugList.size() + "��");
        	System.out.println("1. ���� ����: " + improvementList.size() + "��");
        	System.out.println("1. �� ��� ����: " + newFuncList.size() + "��");
        	System.out.println("=============================================================");
        	
        	System.out.println("���� ���� ����");
        	try {
        		
        		BufferedWriter out = new BufferedWriter(new FileWriter("Release Note - " + date.format(today) + "-" + hour.format(today) + ".txt"));
        		
        		out.write("Penta Systems Technology Inc. ACRA Point " + versionInfo + " Release Note"); out.newLine();
        		out.write("================================================================================"); out.newLine();
        		out.newLine();
        		
        		// ��� �Է� ����
        		BufferedReader in = new BufferedReader(new FileReader("template.txt"));
        		String s;
        		
        		while ((s = in.readLine()) != null) {
        			out.write(s); out.newLine();
        		}
        		in.close(); out.newLine(); out.newLine();
        		out.write("DATE: " + releaseDate); out.newLine(); out.newLine();
        		// ��� �Է� �Ϸ�
        		
        		out.write("-------------------------------------------------------------------------------");
        		out.newLine(); out.newLine();
        		out.write("CONTENTS"); out.newLine(); out.newLine();
        		out.write("Release Notes"); out.newLine(); out.newLine();
        		
        		
        		// ���� ����Ʈ ���
        		out.write("[���� ����]"); out.newLine();
        		for (String bugContent : bugList) {
        			out.write(bugContent); out.newLine();
        		}
        		out.newLine();
        		out.newLine();
        		
        		// ������ ���
        		out.write("[��� ����]"); out.newLine();
        		for (String improvementContent : improvementList) {
        			out.write(improvementContent); out.newLine();
        		}
        		out.newLine();
        		out.newLine();
        		
        		// �� ��� �� ���
        		out.write("[�����]"); out.newLine();
        		for (String newFuncContent : newFuncList) {
        			out.write(newFuncContent); out.newLine();
        		}
        		out.newLine();
        		out.newLine();
        		
        		out.write("-------------------------------------------------------------------------------");
        		out.newLine();
        		out.write("[���� ���� ��]"); out.newLine();
        		
        		int bugDetailCount = 1;
        		for (Issue eachIssue : totalIssueList) {
        			if (eachIssue.getIssueType().getName().equals("����")) {
        				out.write(bugDetailCount + ". " + eachIssue.getSummary()); out.newLine();
        				out.write("- Number: [" + eachIssue.getKey()+ "]"); out.newLine();
        				out.write("- Problem: "); out.newLine();
        				if (eachIssue.getDescription() != null) {
        					if (eachIssue.getDescription().contains("����(�󼼱���)")) {
        						out.write(eachIssue.getDescription().split("����\\(�󼼱���\\):\r\n")[1].replaceAll("#|>", "")); out.newLine();
        					} else {
        						out.write(eachIssue.getDescription()); out.newLine();
        					}
        				}
        				out.write("- Solution: ���� ���� �� �ݿ�"); out.newLine(); out.newLine();
        				bugDetailCount++;
        			}
        		}
        		
        		out.write("-------------------------------------------------------------------------------");
        		out.newLine();
        		out.write("End of Penta Systems Technology Inc. ACRA Point " + versionInfo + " Release Note");
        		out.newLine();
        		
        		out.close();	
        	} catch (IOException e) {
        		System.out.println("���� ������ ������ �߻��Ͽ����ϴ�. �����ڿ��� ���� �ϼ���.");
        		System.err.println(e); // ������ �ִٸ� �޽��� ���
        		System.exit(1);
        	}
        } catch (Exception er) {
        	System.out.println("error �߻�");
        	System.out.println(er);
        }
        
        // Done
        System.out.println("���� ���� �Ϸ�");
        System.out.println("���ϸ�: Release Note - " + date.format(today) + ":" + hour.format(today) + ".txt");
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
