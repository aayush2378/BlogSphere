package com.northeastern.choreless.controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.northeastern.choreless.dao.ChoreDAO;
import com.northeastern.choreless.dao.GroupDAO;
import com.northeastern.choreless.dao.RoommateChoreDebtorDAO;
import com.northeastern.choreless.dao.RoommateDAO;
//import com.northeastern.choreless.service.MailService;
import com.northeastern.choreless.model.*;
import com.northeastern.choreless.service.MailService;

import jakarta.mail.MessagingException;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class DumpController {

	private int currentNumber = 0;
	private String roommates[] = { "Sachin", "Ruthvik", "Shashank", "Anirudh", "Rishikesh" };
	private String emails[] = { "baskar.sa@northeastern.edu", "ravi.ru@northeastern.edu",
			"bharadwajramachand.s@northeastern.edu", "yadhukumar.a@northeastern.edu", "cherodath.r@northeastern.edu" };

	@Autowired
	ServletContext servletContext;
//	
	@Autowired
	private MailService mailService;

	@Autowired
	private GroupDAO group_dao;

	@Autowired
	private RoommateDAO roommateDao;
	
	@Autowired
	private ChoreDAO choredao;
	
	@Autowired
	private RoommateChoreDebtorDAO rcd_dao;
	
	
	@RequestMapping("/")
	public String landingPage() {
		
		return "landing";
	}
	
	public void sendEmailOnSubmit(List<Roommate> roommates, String name, String chore) throws MessagingException {
		
		for (Roommate roommate: roommates)
			mailService.mailWithAttachment(roommate.getEmail(),

					"<p>"+name+" has done chore:"+ chore+"!</p>"+"<p><a href=\"https://localhost:8080/approve\">Click here to approve</a></p>",
					"ChoreLess alert: "+ name+"did chore:"+chore, "");
	}
	
	
	@RequestMapping("/register")
	public String group() {
		return "group";
	}
	
	
	
	
	
	
	
	@RequestMapping("/home")
	public String home(@RequestParam(name = "groupId") int groupId, Model model) {
		ChoreGroup group = group_dao.getById(groupId);
		
		model.addAttribute("welcome", "Welcome " + group.getGroupName() + "!");
		model.addAttribute("chores", group.getChores());
		
		
		model.addAttribute("roommates", group.getRoommates());

		
		return "home";
//		
//		if (adminId == user.getId()) {
//
//			return "admin_home";
//		}
//		return "home";
	}
	
	
	
	
	@RequestMapping("/chore")
	public String chores() {
		
		return "add-chore";
	}
	


	@RequestMapping("/main")
	public String main() {

		return "main";
	}

	@RequestMapping("/delete")
	public String delete() {

//		group_dao.deleteById(2);
		return "group";
	}

	

	
	
	@RequestMapping("/db")
	public String insertingDbValues() {
//    	List<Roommate> mates = new ArrayList<>();
//    	
//    	for(int i=0;i<roommates.length;i++) {
//                mates.add(new Roommate(roommates[i],emails[i]));
//    	}
//    	ChoreGroup group =new ChoreGroup(mates,"Jvue 314",4,"30:11:2023 22:32:34","1,0,0,0,0");
//    	
//    	
//    	group_dao.save(group);

		return "home";
	}

	@GetMapping("/choreDetails/{id}")
	public String doGet(@PathVariable("id") int id, HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		System.out.println("DO GET() executing");

		Chore chore = choredao.getById(id);
		List<Roommate> mate_list= roommateDao.getByGroupId(chore.getChoregroup().getGroupId());
		servletContext.setAttribute("chore_name", chore.getChoreName());
		ChoreGroup group = group_dao.getById(chore.getChoregroup().getGroupId());
		
		int index = chore.getCurrIndex();
		
		List<String> names = new ArrayList<>();
		List<Integer> roommate_ids = new ArrayList<>();
		
		for (Roommate roommate : mate_list) {
			names.add(roommate.getName());
			roommate_ids.add(roommate.getId());
		}
		String lastSubmissionTime = chore.getLast_chore_sub_time();

		String mates[] = names.toArray(String[]::new);

		int sacrifice_count[] = convertCsvToIntArray(chore.getSacrifice());

		
		
		
		
		
		
		
		servletContext.setAttribute("roommates", mates);
		servletContext.setAttribute("index", index);
		servletContext.setAttribute("lastSubmissionTime", lastSubmissionTime);
		servletContext.setAttribute("roomate_list", mate_list);
		servletContext.setAttribute("debtors_next_in_line", chore.getDebtors_next_in_line());
		servletContext.setAttribute("sacrifice", sacrifice_count);
		servletContext.setAttribute("groupId", group.getGroupId());
		servletContext.setAttribute("choreId", id);
		servletContext.setAttribute("roommate_ids", roommate_ids);

		return "random";
	}
	
//	
	@GetMapping("/getDebtors/{id}")
    @ResponseBody
    public ResponseEntity<List<Integer>> getDebtors(@RequestParam int index,@PathVariable("id") int groupId) {
        // Assuming 'roommate_list' is available in your GroupDAO
		
		int choreId = (int)servletContext.getAttribute("choreId");
		List<Integer> debtors = rcd_dao.getDebtors(choreId,group_dao.getById(groupId).getRoommates().get(index).getId()).getDebtors();
        return ResponseEntity.ok(debtors);
    }
	
	
	public static String convertIntArrayToCSV(int[] array) {
		if (array == null || array.length == 0) {
			return "";
		}

		StringBuilder csvBuilder = new StringBuilder();

		csvBuilder.append(array[0]);

		for (int i = 1; i < array.length; i++) {
			csvBuilder.append(",").append(array[i]);
		}

		return csvBuilder.toString();
	}

	public static int[] convertCsvToIntArray(String csvString) {
		if (csvString == null || csvString.isEmpty()) {
			return new int[0];
		}

		String[] stringValues = csvString.split(",");
		int[] intArray = new int[stringValues.length];

		for (int i = 0; i < stringValues.length; i++) {
			try {
				intArray[i] = Integer.parseInt(stringValues[i].trim());
			} catch (NumberFormatException e) {

				System.err.println("Error converting to int: " + stringValues[i]);
			}
		}

		return intArray;
	}

	@RequestMapping("/forgotPassword")
	public String forgotPass() {

		return "forgotPassword";

	}

	@RequestMapping("/check")
	public String check() {

		return "check";
	}

	@RequestMapping("/skip/{id}")
	public String skip(@PathVariable("id") int groupId,HttpServletRequest request,
			@RequestParam(name = "skipCount", required = false) Integer skipCount) {

		if (skipCount != null) {
			System.out.println(skipCount);
		}
		ChoreGroup group = group_dao.getById(groupId);
		
		int choreId = (int) servletContext.getAttribute("choreId");
		Chore chore = choredao.getById(choreId);
		
		int sacrifice_count[] = convertCsvToIntArray(chore.getSacrifice());
		int curr_index = chore.getCurrIndex();

		List<String> names = new ArrayList<>();
		for (Roommate roommate : group.getRoommates()) {
			names.add(roommate.getName());
		}

		String mates[] = names.toArray(String[]::new);
		int roommate_length = mates.length;
		
		
		
		List<Integer> debtorsInLine = chore.getDebtors_next_in_line();
		curr_index = (debtorsInLine == null || debtorsInLine.isEmpty())?(curr_index) :(debtorsInLine.get(0)) ;
		
		
		
		
		Roommate roommate  = group.getRoommates().get(curr_index);         //? for what ??
		
//		Roommate roommate = roommateDao.getById(curr_index + 1);
		
		RoommateChoreDebtor rcd = rcd_dao.getDebtors(choreId, roommate.getId());
		List<Integer> debtors  = rcd.getDebtors();
		
//		List<Integer> debtors = roommate.getDebtors();
		
		
		
		if (sacrifice_count[curr_index] > 0) {

			
			
			sacrifice_count[curr_index] = sacrifice_count[curr_index] - skipCount;
			chore.setSacrifice(convertIntArrayToCSV(sacrifice_count));
			boolean isDebtor = true;

			if (debtorsInLine == null || debtorsInLine.isEmpty()) {
				servletContext.setAttribute("index", (curr_index + 1) % roommate_length);
				chore.setCurrIndex((curr_index + 1) % roommate_length);
				isDebtor = false;
			} else {
				debtorsInLine.remove(0);
				chore.setDebtors_next_in_line(debtorsInLine);
			}

			if (skipCount != null) {
				int i = 0;
				while ((skipCount--) > 0) {

					if (chore.getDebtors_next_in_line() != null) {
						if (isDebtor) {
							chore.getDebtors_next_in_line().add(i++, debtors.remove(0)); //error
						} else
							chore.getDebtors_next_in_line().add(debtors.remove(0));
					} else
						chore.setDebtors_next_in_line(new ArrayList<Integer>(Arrays.asList(debtors.remove(0))));
				}
			} else
				System.out.println("skipcount is null. Choose a value ");

			
			
			rcd.setDebtors(debtors);
			

			choredao.save(chore);
			rcd_dao.save(rcd);

//			group_dao.save(group);
//			roommateDao.save(roommate);

			servletContext.setAttribute("debtors_in_line", chore.getDebtors_next_in_line());
			servletContext.setAttribute("roommate_list", group_dao.getById(groupId).getRoommates());
			servletContext.setAttribute("sacrifice", sacrifice_count);
			servletContext.setAttribute("skip", mates[curr_index] + " skipped their turn using their skipping points");
			

		} else {
			request.setAttribute("error", mates[curr_index] + "\'s skipping points are 0! XD");
		}
		return "random";
	}
//
	
//
//	@RequestMapping("/confirm")
//	public String Register_confirm() {
//
//		return "registration-confirmation";
//	}
//	
////	public String[] debtors(List<Integer> debtors, String mates[]) {
////		String str[] =;
////		int k=0;
////		for(int i : debtors) {
////			str[k++]= mates[i];
////		}
////		return str;
////	}
//
	@PostMapping("/dump/{id}")
	public String doPost(@PathVariable("id") int groupId,HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name = "sacrificer", required = false) Integer sacrificer) throws IOException, MessagingException {
		System.out.println("DO POST() executing");
		response.setContentType("text/html;charset=UTF-8");

		Date date = new Date();
		String pattern = "dd:MM:yyyy HH:mm:ss";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String formattedDate = simpleDateFormat.format(date);

		ChoreGroup group = group_dao.getById(groupId);
		int choreId = (int) servletContext.getAttribute("choreId");
		
		Chore chore = choredao.getById(choreId);
		
		List<String> names = new ArrayList<>();
		for (Roommate roommate : group.getRoommates()) {
			names.add(roommate.getName());
		}
		
		String mates[] = names.toArray(String[]::new);
		int roommate_length =  mates.length;
		int sacrifice_count[] = convertCsvToIntArray(chore.getSacrifice());

		int curr_index = chore.getCurrIndex();

		RoommateChoreDebtor rcd = null;
		
		
		if (sacrificer != null) {
			Roommate sacrifice_mate = roommateDao.getById((int)sacrificer);
			rcd = rcd_dao.getDebtors(choreId,(int)sacrificer);
			List<Integer> debtors = rcd.getDebtors();
					
					if(debtors == null) {
						rcd.setDebtors(new ArrayList<Integer>(Arrays.asList(curr_index)));
					}
					else {
						rcd.getDebtors().add(curr_index);
						rcd.getDebtors().forEach(System.out::println);
						
					}
					
					List<Roommate> roommate_list = group.getRoommates();
					int j=-1;
					for(int i=0;i<roommate_list.size();i++) {
						if(roommate_list.get(i).getId() == (int)sacrificer) {
							j = i;
							break;
						}
					}
					
					sacrifice_count[j]++;
					chore.setSacrifice(convertIntArrayToCSV(sacrifice_count));
					
					
					
					
//			for (int i = 0; i < mates.length; i++) {
//				if (mates[i].equals(sacrificer)) {
//					
//					
//					
//					roommate = roommateDao.getById(i + 1);
//
//					if (roommate.getDebtors() == null) {
//						roommate.setDebtors(new ArrayList<Integer>(Arrays.asList(curr_index)));
//					} else {
//						roommate.getDebtors().add(curr_index);
//						roommate.getDebtors().forEach(System.out::println); // print
//					}
//
//					sacrifice_count[i]++;
//					group.setSacrifice(convertIntArrayToCSV(sacrifice_count));
//					break;
//				}
//			}

		}
		else {
			try {
				sendEmailOnSubmit(group.getRoommates(),mates[curr_index],chore.getChoreName());
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
				
		
		

		int index = curr_index;

		List<Integer> debtors_next_in_line = chore.getDebtors_next_in_line();
		if (debtors_next_in_line == null || debtors_next_in_line.isEmpty())
			index = (curr_index + 1) % roommate_length;
		else {
			debtors_next_in_line.remove(0);
			chore.setDebtors_next_in_line(debtors_next_in_line);
		}
		
		chore.setCurrIndex(index);
		chore.setLast_chore_sub_time(formattedDate);

//		group_dao.save(group);      // ???????????????
		choredao.save(chore);

		if (rcd != null) {
			rcd_dao.save(rcd);
			servletContext.setAttribute("debtors", rcd.getDebtors());
		}
		System.out.println("///////////////////////// \nCurrent Index: "+ chore.getCurrIndex());
		System.out.println("/////////////////////////");
		servletContext.setAttribute("debtors_in_line", debtors_next_in_line);
		servletContext.setAttribute("index", index);
		servletContext.setAttribute("sacrifice", sacrifice_count);
		servletContext.setAttribute("lastSubmissionTime", formattedDate);
		servletContext.setAttribute("skip", "");
		servletContext.setAttribute("choreId", choreId);
//		servletContext.setAttribute("roomate_list", group_dao.getById(1).getRoommates());
		return "random";
	}
}

//Skip drop down fix(shows number of skips)
// roommate registration drop down fix 
// promise object fix (alert) after AJAX
// provide link for next 50/100 turns

// email approval(from admin account) linked with registration page

// admin account: special privileges

