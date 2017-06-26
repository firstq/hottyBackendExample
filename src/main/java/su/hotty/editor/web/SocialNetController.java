package su.hotty.editor.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import su.hotty.editor.config.API;
import su.hotty.editor.domain.SocialNet;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;
import su.hotty.editor.service.SocialNetService;
import su.hotty.editor.web.SocialNetController;


@Controller
@RequestMapping(API.PATH+"/socialnets")
public class SocialNetController {

    @Autowired
    SocialNetService socialNetService;
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") String id) {
        SocialNet socialNet = socialNetService.findSocialNet(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (socialNet == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(socialNet.toJson(), headers, HttpStatus.OK);
    }
    
    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<SocialNet> result = socialNetService.findAllSocialNets();
        return new ResponseEntity<String>(SocialNet.toJsonArray(result), headers, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json, UriComponentsBuilder uriBuilder) {
        SocialNet socialNet = SocialNet.fromJsonToSocialNet(json);
        socialNetService.saveSocialNet(socialNet);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        RequestMapping a = (RequestMapping) getClass().getAnnotation(RequestMapping.class);
        headers.add("Location",uriBuilder.path(a.value()[0]+"/"+socialNet.getId().toString()).build().toUriString());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
        for (SocialNet socialNet: SocialNet.fromJsonArrayToSocialNets(json)) {
            socialNetService.saveSocialNet(socialNet);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJson(@RequestBody String json, @PathVariable("id") String id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        SocialNet socialNet = SocialNet.fromJsonToSocialNet(json);
        socialNet.setId(id);
        if (socialNetService.updateSocialNet(socialNet) == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteFromJson(@PathVariable("id") String id) {
        SocialNet socialNet = socialNetService.findSocialNet(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (socialNet == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        socialNetService.deleteSocialNet(socialNet);
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
}