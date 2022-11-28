package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.models.EMail;
import com.bezkoder.springjwt.models.Tutorial;
import com.bezkoder.springjwt.payload.response.ResponseMessage;
import com.bezkoder.springjwt.repository.ExcelHelper;
import com.bezkoder.springjwt.repository.ExcelService;
import com.bezkoder.springjwt.repository.MailSenderSpring;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        fileService.save(file);

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
    mailSenderSpring.sendEmailWithTemplate(email);
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
}
