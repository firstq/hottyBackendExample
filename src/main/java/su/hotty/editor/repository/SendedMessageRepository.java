package su.hotty.editor.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import su.hotty.editor.domain.SendedMessage;

import java.util.List;

public interface SendedMessageRepository extends MongoRepository<SendedMessage, String> {

    List<SendedMessage> findAll();

}