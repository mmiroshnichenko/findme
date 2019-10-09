CREATE TABLE RELATIONSHIP(
    ID NUMBER,
    USER_FROM_ID NUMBER,
    CONSTRAINT RELATIONSHIP_USER_FROM_FK FOREIGN KEY(USER_FROM_ID) REFERENCES USERS(ID),
    USER_TO_ID NUMBER,
    CONSTRAINT RELATIONSHIP_USER_TO_FK FOREIGN KEY(USER_TO_ID) REFERENCES USERS(ID),
    STATUS NVARCHAR2(10) NOT NULL
);

CREATE SEQUENCE RELATIONSHIP_SEQ MINVALUE 1 START WITH 1 INCREMENT BY 1;