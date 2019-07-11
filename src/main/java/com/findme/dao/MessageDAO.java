package com.findme.dao;

import com.findme.models.Message;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class MessageDAO extends BaseDAO<Message> {
    public MessageDAO() {
        super(Message.class);
    }
}
