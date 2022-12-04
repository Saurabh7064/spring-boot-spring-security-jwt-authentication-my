package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.models.Campaign;
import com.bezkoder.springjwt.models.Customer;
import com.bezkoder.springjwt.models.EMail;
import com.bezkoder.springjwt.models.Tutorial;
import com.bezkoder.springjwt.payload.response.ResponseMessage;
import com.bezkoder.springjwt.repository.*;
import com.twilio.Twilio;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
  @GetMapping("/all")
  public String allAccess() {
    return "Public Content.";
  }

  @GetMapping("/user")
  @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
  public String userAccess() {
    return "User Content.";
  }

  @GetMapping("/mod")
  @PreAuthorize("hasRole('MODERATOR')")
  public String moderatorAccess() {
    return "Moderator Board.";
  }

  @GetMapping("/admin")
  @PreAuthorize("hasRole('ADMIN')")
  public String adminAccess() {
    return "Admin Board.";
  }

  @Autowired
  ExcelService fileService;

  @PostMapping("/upload")
  public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
    String message = "";

    if (ExcelHelper.hasExcelFormat(file)) {
      try {
        fileService.saveCustomer(file);

        message = "Uploaded the file successfully: " + file.getOriginalFilename();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
      } catch (Exception e) {
        message = "Could not upload the file: " + file.getOriginalFilename() + "!";
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
      }
    }

    message = "Please upload an excel file!";
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
  }

  @GetMapping("/tutorials")
  public ResponseEntity<List<Tutorial>> getAllTutorials() {
    try {
      List<Tutorial> tutorials = fileService.getAllTutorials();

      if (tutorials.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(tutorials, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/download")
  public ResponseEntity<Resource> getFile() {
    String filename = "tutorials.xlsx";
    InputStreamResource file = new InputStreamResource(fileService.load());

    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
            .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
            .body(file);
  }

  @Autowired
  MailSenderSpring mailSenderSpring;

  @GetMapping("/sendmail/{emailtemplate}")
  public void sendMessage(@PathVariable("email") String emailtemplate) {
    EMail email = new EMail();
    email.setTo("emosrb@gmail.com");
    email.setFrom("emosrb@gmail.com");
    email.setSubject("Learning how to send mail");
    email.setContent("Sending mail");
    Map<String, Object> model = new HashMap<>();
    model.put("firstName", "saurabh");
    model.put("lastName", "kumar");
    email.setModel(model);
    mailSenderSpring.sendEmailWithTemplate(email,"email.flth");
  }

  @GetMapping(value = "/sendSMS")
  public ResponseEntity<String> sendSMS() {

    Twilio.init("ACd8d70e1245e9cc1618357ba03e23ac28", "3cea9f2ba30a65eae57dbd6b9b8e4aa1");

    Message.creator(new PhoneNumber("+16824075301"),
            new PhoneNumber("+12512766023"), "Hello from saurabh 📞").create();

    return new ResponseEntity<String>("Message sent successfully", HttpStatus.OK);
  }
//  @GetMapping("/group/{id}")
//  public ResponseEntity<Group> getGroupById(@PathVariable("id") Long id) {
//    Optional<Group> group = groupRepository.findById(id);
//    Group group1  = group.get();
//    return new ResponseEntity<>(group1,HttpStatus.OK);
//
//  }
//@PostMapping("/group")
//public ResponseEntity<Group> createGroup(@RequestBody Group group, Principal principal) {
//}

  //Campaign Start

  @Autowired
  private CampaignRepositoy campaignRepositoy;

  @PostMapping(value = "/saveCampaign")
  @Transactional
  public ResponseEntity<Campaign> saveCampaign(@RequestBody Campaign campaign) {
     campaignRepositoy.truncateCampaign();
    try {
      Campaign campaign1 = campaignRepositoy.save(campaign);
      return new ResponseEntity<>(campaign1, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

  }

  @PostMapping(value = "/saveTargetAudience")
  @Transactional
  public ResponseEntity<Campaign> saveTargetAudience(@RequestBody Campaign campaign) {
    List<Campaign> campaigns = campaignRepositoy.findAll();

    Campaign saveCampaign = new Campaign();

    saveCampaign.setId(campaigns.get(0).getId());
    saveCampaign.setCampaign_type(campaigns.get(0).getCampaign_type());
    saveCampaign.setCampaign_message(campaigns.get(0).getCampaign_message());

    saveCampaign.setAge(campaign.getAge());
    saveCampaign.setGender(campaign.getGender());
    saveCampaign.setLocation(campaign.getLocation());

     try {
      Campaign campaign1 = campaignRepositoy.save(saveCampaign);
      return new ResponseEntity<>(campaign1, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

  }
  @PostMapping(value = "/saveCampaignDate")
  @Transactional
  public ResponseEntity<Campaign> saveCampaignDate(@RequestBody Campaign campaign) {
    List<Campaign> campaigns = campaignRepositoy.findAll();

    Campaign saveCampaign = new Campaign();

    saveCampaign.setId(campaigns.get(0).getId());
    saveCampaign.setCampaign_type(campaigns.get(0).getCampaign_type());
    saveCampaign.setCampaign_message(campaigns.get(0).getCampaign_message());
    saveCampaign.setAge(campaigns.get(0).getAge());
    saveCampaign.setGender(campaigns.get(0).getGender());
    saveCampaign.setLocation(campaigns.get(0).getLocation());

    saveCampaign.setTime(campaign.getTime());

    try {
      Campaign campaign1 = campaignRepositoy.save(saveCampaign);
      return new ResponseEntity<>(campaign1, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

  }

  @Autowired
  private CustomerRepository customerRepository;

  @GetMapping(value = "/sendCampaign")
  public ResponseEntity<String> sendCampaign() {

    Campaign campaign = campaignRepositoy.findAll().get(0);
    List<Customer> customers = customerRepository.findAll();


    List<Customer> customerList = null;

    if(campaign.getAge().contains(">=")){
       int campaignAge = Integer.parseInt(campaign.getAge().split(">=")[1]);
        customerList = customers.stream().filter(
              customer -> Integer.parseInt(customer.getAge())>=campaignAge
                      && customer.getGender().equals(campaign.getGender())
                      && customer.getLocation().equals(campaign.getLocation())
                      ).collect(Collectors.toList());

    }

    else if(campaign.getAge().contains("<=")){
       int campaignAge = Integer.parseInt(campaign.getAge().split("<=")[1]);
       customerList = customers.stream().filter(
              customer -> Integer.parseInt(customer.getAge())<=campaignAge
                      && customer.getGender().equals(campaign.getGender())
                      && customer.getLocation().equals(campaign.getLocation())
      ).collect(Collectors.toList());
    }

    else if(campaign.getAge().contains("<")){
       int campaignAge = Integer.parseInt(campaign.getAge().split("<")[1]);
       customerList = customers.stream().filter(
              customer -> Integer.parseInt(customer.getAge())<campaignAge
                      && customer.getGender().equals(campaign.getGender())
                      && customer.getLocation().equals(campaign.getLocation())
      ).collect(Collectors.toList());
    }

    else if(campaign.getAge().contains(">")){
       int campaignAge = Integer.parseInt(campaign.getAge().split(">")[1]);
        customerList = customers.stream().filter(
              customer -> Integer.parseInt(customer.getAge())>campaignAge
                      && customer.getGender().equals(campaign.getGender())
                      && customer.getLocation().equals(campaign.getLocation())
      ).collect(Collectors.toList());
    }



    if(campaign.getCampaign_type().equals("sms")){
      for(Customer customer: customerList){
        sendFilteredSMS(customer.getPhone_number(),campaign.getCampaign_message());
      }
    }else {
      for(Customer customer: customerList){
        sendFilteredEmail(customer.getEmail(), campaign.getCampaign_message(), customer.getName());
      }
    }

    return new ResponseEntity<String>("Campaign sent successfully", HttpStatus.OK);
  }


  public void sendFilteredSMS(String ph,String message){

    Twilio.init("ACd8d70e1245e9cc1618357ba03e23ac28", "3cea9f2ba30a65eae57dbd6b9b8e4aa1");

    Message.creator(new PhoneNumber("+1"+ph),
            new PhoneNumber("+12512766023"), message).create();
  }

  public void sendFilteredEmail(String to,String message, String name){
    EMail email = new EMail();
    email.setTo(to);
    email.setFrom("emosrb@gmail.com");
    email.setSubject("Email Subject");
    email.setContent("Email Content");
    Map<String, Object> model = new HashMap<>();
    model.put("firstName", name);
    email.setModel(model);
    mailSenderSpring.sendEmailWithTemplate(email,message);
  }




}
