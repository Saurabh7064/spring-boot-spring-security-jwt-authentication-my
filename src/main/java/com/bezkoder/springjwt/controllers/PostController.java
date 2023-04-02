package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.models.*;
import com.bezkoder.springjwt.models.Post;
import com.bezkoder.springjwt.repository.*;
import com.bezkoder.springjwt.response.PostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class PostController {
    @Autowired
    GroupRepository groupRepository;

    @Autowired
    PostRepository postRepository;


    @PostMapping("/post")
    public ResponseEntity<Post> createPost(@RequestBody Post post, Principal principal) {
        try {
           Optional<Group> group =  groupRepository.findById(post.getGroup().getId());
           Group group1 = group.get();

            Post post1 = new Post(principal.getName(),post.getPostMessage(),post.getHasFile());

            post1.setGroup(group1);

            Post post2 = postRepository.save(post1);
            post2.setGroup(null);
            return new ResponseEntity<>(post2, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Autowired
    private FileDBRepository fileDBRepository;

    @GetMapping("posts")
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        List<Post> postList = postRepository.findAll();

        List<PostResponse> posts = new ArrayList<>();

        for(Post post: postList){

            PostResponse postResponse = new PostResponse();
            if(post.getPostMessage().equals("Customers.xlsx")){
                int x =0;
                System.out.println("hi");
            }
            if(post.getHasFile()){
                List<FileDB> fileDB = fileDBRepository.findByName(post.getPostMessage());
                try{
                String fileDownloadUri = ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/api/test/files/")
                        .path(fileDB.get(0).getId())
                        .toUriString();
                    postResponse.setUrl(fileDownloadUri);
                    postResponse.setPostMessage(fileDB.get(0).getName());

                }

                catch (Exception e){
                    e.printStackTrace();
                 }
            }else {
            postResponse.setPostMessage(post.getPostMessage());
            }
            postResponse.setId(post.getId());
            postResponse.setGroupName(post.getGroup().getGroupName());
            postResponse.setCreatedBy(post.getCreatedBy());
            posts.add(postResponse);
        }
        return new ResponseEntity<>(posts,HttpStatus.OK);
    }

    @DeleteMapping("/post/{id}")
    public ResponseEntity<String> deletePost(@PathVariable("id") long id){
        try {
            postRepository.deleteById(id);
            return new ResponseEntity<>("Successfully Deleted",HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
