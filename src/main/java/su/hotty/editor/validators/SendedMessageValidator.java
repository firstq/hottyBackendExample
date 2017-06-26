package su.hotty.editor.validators;

import su.hotty.editor.domain.SendedMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.*;

@Component
public class SendedMessageValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return SendedMessage.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        SendedMessage message = (SendedMessage) target;

		/*
		*	TO DO: Вынести в validationMessages.properties
		*/

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "Поле не должно быть пустым");
/*
        if(message.getParentId() != null && message.getParent().getCapchaEnable()){
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "captcha", "Поле не должно быть пустым");
        }

*/
        if(!message.getEmail().matches("[\\w.]+@[\\w.]+\\.\\w+") ){
            errors.rejectValue("email", "Введённое значение не является email адресом");
        }

    }

}