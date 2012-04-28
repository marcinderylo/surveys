package org.adaptiveplatform.surveys.controller;

import com.google.common.base.Objects;
import org.adaptiveplatform.adapt.commons.validation.constraints.NonBlank;
import org.adaptiveplatform.surveys.dto.RegisterAccountCommand;
import org.apache.bval.constraints.Email;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Controller
@RequestMapping("signUp")
public class RegistrationController {

    @RequestMapping(method = RequestMethod.GET)
    public String singUpForm(Model model) {
        model.addAttribute("command", new RegisterAccountFormCommand());
        return "registration/signUpForm";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String signUpUser(Model model, @Valid RegisterAccountFormCommand command, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("command", command);
            model.addAttribute("errorsCount", bindingResult.getErrorCount());
            return "registration/signUpForm";
        } else {
            return "redirect:/";
        }
    }

}

class RegisterAccountFormCommand extends RegisterAccountCommand {

    private String emailConfirmation;

    private String passwordConfirmation;

    @NonBlank
    @Email
    public String getEmailConfirmation() {
        return emailConfirmation;
    }

    public void setEmailConfirmation(String emailConfirmation) {
        this.emailConfirmation = emailConfirmation;
    }

    @NonBlank
    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    public boolean emailsMatch() {
        return Objects.equal(getEmail(), emailConfirmation);
    }

    public boolean passwordsMatch() {
        return Objects.equal(getPassword(), passwordConfirmation);
    }
}