package com.robinelvin.sbc.controllers.web;

import com.robinelvin.sbc.controllers.UsersController;
import com.robinelvin.sbc.models.RegistrationRequest;
import com.robinelvin.sbc.models.RegistrationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.validation.Valid;
import java.util.Map;

/**
 * @author Robin Elvin
 */
@Controller
@RequestMapping("/web")
public class WebController extends WebMvcConfigurerAdapter {

//    private Validator validator;
//
//    public WebController() {
//        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
//        validator = validatorFactory.getValidator();
//    }

    @Autowired
    private UsersController usersController;

    @RequestMapping(value = "home")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("title", "Home");
        return modelAndView;
    }

    @RequestMapping(value = "accounts")
    public ModelAndView institutions() {
        ModelAndView modelAndView = new ModelAndView("accounts");
        modelAndView.addObject("title", "Accounts");
        return modelAndView;
    }

    @RequestMapping(value = "register", method = RequestMethod.GET)
    public String register(Map<String, Object> model) {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        model.put("registrationRequest", registrationRequest);
        return "register";
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public String processRegistration(
            //@ModelAttribute("registrationRequest")
            @Valid RegistrationRequest registrationRequest, BindingResult result) {
//        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(registrationRequest);
//
//        for (ConstraintViolation<RegistrationRequest> violation : violations)
//        {
//            String propertyPath = violation.getPropertyPath().toString();
//            String message = violation.getMessage();
//            // Add JSR-303 errors to BindingResult
//            // This allows Spring to display them in view via a FieldError
//            result.addError(new FieldError("registrationRequest", propertyPath,
//
//                    "Invalid "+ propertyPath + "(" + message + ")"));
//        }

        if (result.hasErrors()) {
            return "register";
        }

        // Form OK so try to create the user


        RegistrationResponse registrationResponse = usersController.register(registrationRequest);
        if (registrationResponse.getStatus() != "OK") {
            result.rejectValue("loginName", "error.userexists", new Object[]{"User exists"}, registrationResponse.getStatus());
            //result.addError(new FieldError("registrationRequest", "username", registrationResponse.getStatus()));
            return "register";
        }

        return "registration-success";
    }

}
