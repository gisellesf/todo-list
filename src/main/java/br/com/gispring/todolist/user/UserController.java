package br.com.gispring.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/users")
public class UserController {

  // gerenciamento da estancia do repositorio
  @Autowired
  private UserRepository userRepository;

  /**
   * Body
   * @param userModel
   */
  @PostMapping("/userCreated")
  public ResponseEntity<Object> createUser(@RequestBody UserModel userModel) {

    // checks if the username exists in the database
    var username = this.userRepository.findByUsername(userModel.getUsername());
    if(username != null){ 
      // Error message
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já existe!!\"");
    }

    var passwordHashed = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
    userModel.setPassword(passwordHashed);
    
    var userCreated = this.userRepository.save(userModel);
    return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
  }
}
