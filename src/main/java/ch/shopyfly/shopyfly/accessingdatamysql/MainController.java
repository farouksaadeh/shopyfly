package ch.shopyfly.shopyfly.accessingdatamysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Optional;
import org.springframework.core.io.ClassPathResource;
import java.nio.file.Files;
import java.nio.file.Path;

@Controller // This means that this class is a Controller
@RequestMapping(path="/demo") // This means URL's start with /demo (after Application path)
public class MainController {

    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private UserRepository userRepository;

    @PostMapping(path="/add")
    public @ResponseBody String addNewUser (@RequestParam String name, @RequestParam String email) {
        User n = new User();
        n.setName(name);
        n.setEmail(email);
        userRepository.save(n);
        return "Saved";
        // Irgendwann muss der "String name" und "String email" zu: " String email" und "String password" gewechselt werden.
        //Jedoch muss der Controller bei get.. auch geändert werden! Alle Komponenten beachten!!
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping(path="/register")
    public @ResponseBody String registerUser (@RequestParam String name, @RequestParam String email) {
        if(userRepository.findByName(name).isPresent()){
            return "Benutzername bereits vorhanden.";
        }
        User newUser = new User();
        newUser.setName(name);
        newUser.setEmail(email);
        userRepository.save(newUser);
        return "Benutzer erfolgreich registriert.";
    }

    @GetMapping(path="/register")
    @ResponseBody
    public String showRegistrationForm() {
        try {
            Path path = new ClassPathResource("static/registrationForm.html").getFile().toPath();
            return Files.readString(path);
        } catch (Exception e) {
            e.printStackTrace();
            return "Fehler beim Laden der HTML-Datei";
        }
    }
    @GetMapping(path="/findByName")
    public @ResponseBody String findByName(@RequestParam String name) {
        Optional<User> optionalUser = userRepository.findByName(name);
        if (optionalUser.isPresent()) {
            return optionalUser.get().getName() + ", " + optionalUser.get().getEmail();
        } else {
            return "User not found!";
        }
    }

    @GetMapping(path="/login")
    @ResponseBody
    public String showLoginForm() {
         try {
            Path path = new ClassPathResource("static/loginForm.html").getFile().toPath();
            return Files.readString(path);
        } catch (Exception e) {
            e.printStackTrace();
            return "Fehler beim Laden der HTML-Datei";
        }
    }

    @PostMapping(path="/login")
    public @ResponseBody String loginUser (@RequestParam String name, @RequestParam String password) {
        Optional<User> optionalUser = userRepository.findByName(name);
        if (optionalUser.isPresent()) {
            return "Erfolgreich angemeldet!";
        } else {
            return "Benutzername oder Passwort ist falsch!";
        }
    }

    @PostMapping(path="/delete")
    public @ResponseBody String deleteUser(@RequestParam String emailToDelete) {
        Optional<User> optionalUser = userRepository.findByEmail(emailToDelete);
        if (optionalUser.isPresent()) {
            userRepository.delete(optionalUser.get());
            return "Benutzer erfolgreich gelöscht.";
        } else {
            return "Benutzer mit dieser E-Mail-Adresse wurde nicht gefunden!";

        }
    }
    @GetMapping(path="/delete")
    @ResponseBody
    public String showDeleteForm() {
        try {
            Path path = new ClassPathResource("static/deleteUserForm.html").getFile().toPath();
            return Files.readString(path);
        } catch (Exception e) {
            e.printStackTrace();
            return "Fehler beim Laden der HTML-Datei";
        }
    }

    @PostMapping(path="/deleteAll")
    public @ResponseBody String deleteAllUsers() {
        userRepository.deleteAll();
        return "Alle Benutzer wurden gelöscht";
    }




}
