package com.findme.dao;

import com.findme.models.Message;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public class MessageDAO extends BaseDAO<Message> {
    public MessageDAO() {
        super(Message.class);
    }
}
