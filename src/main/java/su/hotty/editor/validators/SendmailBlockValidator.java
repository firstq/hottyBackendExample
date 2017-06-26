package su.hotty.editor.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.*;
import su.hotty.editor.domain.Block;

@Component
public class SendmailBlockValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Block.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        Block sendmailBlock = (Block) target;

//        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "sendFrom", "Поле не должно быть пустым");
//        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "sendTo", "Поле не должно быть пустым");

        //TODO: Заменить на проверку .getSpecialData.get('sendFrom/To')

/*
        if(!sendmailBlock.getSpecialData.get('sendFrom').matches("[\\w.]+@[\\w.]+\\.\\w+") ){
            errors.rejectValue("sendFrom", "Введённое значение не является email адресом");
        }

        if(!sendmailBlock.getSpecialData.get('sendTo').matches("[\\w.]+@[\\w.]+\\.\\w+") ){
            errors.rejectValue("sendTo", "Введённое значение не является email адресом");
        }
*/
    }

}