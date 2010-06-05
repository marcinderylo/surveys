-- users
INSERT INTO "PUBLIC"."USER_ACCOUNTS" (ID, EMAIL, PASSWORD, USERNAME ) VALUES  ( 1, 'john@doe.com', 'doe', 'john' )
INSERT INTO "PUBLIC"."USER_ACCOUNTS" (ID, EMAIL, PASSWORD, USERNAME ) VALUES  ( 2, 'keyser@soze.com', 'soze', 'keyser' )
INSERT INTO "PUBLIC"."USER_ACCOUNTS" (ID, EMAIL, PASSWORD, USERNAME ) VALUES  ( 3, 'eval@eval.com', 'evaluator', 'evaluator' )
-- user privileges
INSERT INTO "PUBLIC"."USER_ACCOUNTS_PRIVILEGES" (USER_ACCOUNT, PRIVILEGE ) VALUES  ( 1, 'USER' )
INSERT INTO "PUBLIC"."USER_ACCOUNTS_PRIVILEGES" (USER_ACCOUNT, PRIVILEGE ) VALUES  ( 2, 'USER' )
INSERT INTO "PUBLIC"."USER_ACCOUNTS_PRIVILEGES" (USER_ACCOUNT, PRIVILEGE ) VALUES  ( 2, 'ADMINISTRATOR' )
INSERT INTO "PUBLIC"."USER_ACCOUNTS_PRIVILEGES" (USER_ACCOUNT, PRIVILEGE ) VALUES  ( 2, 'EVALUATOR' )
INSERT INTO "PUBLIC"."USER_ACCOUNTS_PRIVILEGES" (USER_ACCOUNT, PRIVILEGE ) VALUES  ( 3, 'USER' )
INSERT INTO "PUBLIC"."USER_ACCOUNTS_PRIVILEGES" (USER_ACCOUNT, PRIVILEGE ) VALUES  ( 3, 'EVALUATOR' )
-- survey templates
INSERT INTO "PUBLIC"."SURVEY_TEMPLATES" (ID, PUBLISHED, TITLE, USER_ID ) VALUES  ( 1, false, 'not published survey', 2 )
INSERT INTO "PUBLIC"."SURVEY_TEMPLATES" (ID, PUBLISHED, TITLE, USER_ID ) VALUES  ( 2, true, 'zajecia z javy', 2 )

-- student groups
INSERT INTO "PUBLIC"."STUDENT_GROUPS" (ID, GROUP_NAME) VALUES (1, 'test group')

-- survey template publications
INSERT INTO "PUBLIC"."SURVEY_PUBLICATIONS" (ID, TEMPLATE_ID, GROUP_ID) VALUES (1,1,1)
INSERT INTO "PUBLIC"."SURVEY_PUBLICATIONS" (ID, TEMPLATE_ID, GROUP_ID) VALUES (2,2,1)

-- question templates
INSERT INTO "PUBLIC"."QUESTION_TEMPLATES" (ID, NUMBER, PUBLISHED, "TEXT", TEMPLATE, QUESTION_TYPE ) VALUES  ( 1, 1, false, 'not published template', 1, 'MULTIPLE_CHOICE' )
INSERT INTO "PUBLIC"."QUESTION_TEMPLATES" (ID, NUMBER, PUBLISHED, "TEXT", TEMPLATE, QUESTION_TYPE ) VALUES  ( 2, 2, false, 'another not published question', 1, 'MULTIPLE_CHOICE' )
INSERT INTO "PUBLIC"."QUESTION_TEMPLATES" (ID, NUMBER, PUBLISHED, "TEXT", TEMPLATE, QUESTION_TYPE ) VALUES  ( 3, 3, true, 'jak oceniasz ''wprowadzenie do jezyka''', 2, 'MULTIPLE_CHOICE' )
INSERT INTO "PUBLIC"."QUESTION_TEMPLATES" (ID, NUMBER, PUBLISHED, "TEXT", TEMPLATE, QUESTION_TYPE ) VALUES  ( 4, 4, true, 'jak oceniasz zajecia z ''swing''', 2, 'MULTIPLE_CHOICE' )
INSERT INTO "PUBLIC"."QUESTION_TEMPLATES" (ID, NUMBER, PUBLISHED, "TEXT", TEMPLATE, QUESTION_TYPE ) VALUES  ( 5, 5, true, 'jak oceniasz wyklady z ''podstawy Java''', 2, 'MULTIPLE_CHOICE' )
-- answer templates
INSERT INTO "PUBLIC"."ANSWER_TEMPLATES" (ID, NUMBER, "TEXT", QUESTION_ID, REQUIRES_COMMENT, DISALLOWS_OTHER_ANSWERS  ) VALUES  ( 1, 1, 'dobrze', 1, false, false )
INSERT INTO "PUBLIC"."ANSWER_TEMPLATES" (ID, NUMBER, "TEXT", QUESTION_ID, REQUIRES_COMMENT, DISALLOWS_OTHER_ANSWERS  ) VALUES  ( 2, 2, 'przecietnie', 1, false, false )
INSERT INTO "PUBLIC"."ANSWER_TEMPLATES" (ID, NUMBER, "TEXT", QUESTION_ID, REQUIRES_COMMENT, DISALLOWS_OTHER_ANSWERS  ) VALUES  ( 3, 3, 'slabo', 1, true, true )

INSERT INTO "PUBLIC"."ANSWER_TEMPLATES" (ID, NUMBER, "TEXT", QUESTION_ID, REQUIRES_COMMENT, DISALLOWS_OTHER_ANSWERS  ) VALUES  ( 4, 1, 'dobrze', 2, false, false )
INSERT INTO "PUBLIC"."ANSWER_TEMPLATES" (ID, NUMBER, "TEXT", QUESTION_ID, REQUIRES_COMMENT, DISALLOWS_OTHER_ANSWERS  ) VALUES  ( 5, 2, 'przecietnie', 2, false, false )
INSERT INTO "PUBLIC"."ANSWER_TEMPLATES" (ID, NUMBER, "TEXT", QUESTION_ID, REQUIRES_COMMENT, DISALLOWS_OTHER_ANSWERS  ) VALUES  ( 6, 3, 'slabo', 2, true, true )

INSERT INTO "PUBLIC"."ANSWER_TEMPLATES" (ID, NUMBER, "TEXT", QUESTION_ID, REQUIRES_COMMENT, DISALLOWS_OTHER_ANSWERS  ) VALUES  ( 7, 1, 'dobrze', 3, false, false )
INSERT INTO "PUBLIC"."ANSWER_TEMPLATES" (ID, NUMBER, "TEXT", QUESTION_ID, REQUIRES_COMMENT, DISALLOWS_OTHER_ANSWERS  ) VALUES  ( 8, 2, 'przecietnie', 3, false, false )
INSERT INTO "PUBLIC"."ANSWER_TEMPLATES" (ID, NUMBER, "TEXT", QUESTION_ID, REQUIRES_COMMENT, DISALLOWS_OTHER_ANSWERS  ) VALUES  ( 9, 3, 'slabo', 3, false, false )

INSERT INTO "PUBLIC"."ANSWER_TEMPLATES" (ID, NUMBER, "TEXT", QUESTION_ID, REQUIRES_COMMENT, DISALLOWS_OTHER_ANSWERS  ) VALUES  ( 10, 1, 'dobrze', 4, false, false )
INSERT INTO "PUBLIC"."ANSWER_TEMPLATES" (ID, NUMBER, "TEXT", QUESTION_ID, REQUIRES_COMMENT, DISALLOWS_OTHER_ANSWERS  ) VALUES  ( 11, 2, 'przecietnie', 4, false, false )
INSERT INTO "PUBLIC"."ANSWER_TEMPLATES" (ID, NUMBER, "TEXT", QUESTION_ID, REQUIRES_COMMENT, DISALLOWS_OTHER_ANSWERS  ) VALUES  ( 12, 3, 'slabo', 4, false, false )

INSERT INTO "PUBLIC"."ANSWER_TEMPLATES" (ID, NUMBER, "TEXT", QUESTION_ID, REQUIRES_COMMENT, DISALLOWS_OTHER_ANSWERS  ) VALUES  ( 13, 1, 'dobrze', 5, false, false )
INSERT INTO "PUBLIC"."ANSWER_TEMPLATES" (ID, NUMBER, "TEXT", QUESTION_ID, REQUIRES_COMMENT, DISALLOWS_OTHER_ANSWERS  ) VALUES  ( 14, 2, 'przecietnie', 5, false, false )
INSERT INTO "PUBLIC"."ANSWER_TEMPLATES" (ID, NUMBER, "TEXT", QUESTION_ID, REQUIRES_COMMENT, DISALLOWS_OTHER_ANSWERS  ) VALUES  ( 15, 3, 'slabo', 5, false, false )

INSERT INTO "PUBLIC"."FILLED_SURVEYS" (ID, PUBL_TEMPLATE_ID, START_DATE, FINISH_DATE, USER_ID) VALUES (1,1,NOW(),NOW(),1)
INSERT INTO "PUBLIC"."FILLED_SURVEYS" (ID, PUBL_TEMPLATE_ID, START_DATE, FINISH_DATE, USER_ID) VALUES (2,1,NOW(),NOW(),2)

INSERT INTO "PUBLIC"."ANSWERED_QUESTIONS" (ID,SURVEY_ID, QUESTION_ID,COMMENT, NUMBER) VALUES (1,1,1,'comment',1)
INSERT INTO "PUBLIC"."ANSWERED_QUESTIONS" (ID,SURVEY_ID, QUESTION_ID,COMMENT, NUMBER) VALUES (2,1,2,'comment',2)
INSERT INTO "PUBLIC"."ANSWERED_QUESTIONS" (ID,SURVEY_ID, QUESTION_ID,COMMENT, NUMBER) VALUES (3,2,1,'comment',1)
INSERT INTO "PUBLIC"."ANSWERED_QUESTIONS" (ID,SURVEY_ID, QUESTION_ID,COMMENT, NUMBER) VALUES (4,2,2,'comment',2)

INSERT INTO "PUBLIC"."ANSWERED_QUESTIONS_ANSWERS" (ID,ANSWER_TEMPLATE_ID,ANSWERED_QUESTION_ID,SELECTED, NUMBER) VALUES (1,1,1,true,1)
INSERT INTO "PUBLIC"."ANSWERED_QUESTIONS_ANSWERS" (ID,ANSWER_TEMPLATE_ID,ANSWERED_QUESTION_ID,SELECTED, NUMBER) VALUES (2,2,1,false,1)
INSERT INTO "PUBLIC"."ANSWERED_QUESTIONS_ANSWERS" (ID,ANSWER_TEMPLATE_ID,ANSWERED_QUESTION_ID,SELECTED, NUMBER) VALUES (3,3,1,true,1)

INSERT INTO "PUBLIC"."ANSWERED_QUESTIONS_ANSWERS" (ID,ANSWER_TEMPLATE_ID,ANSWERED_QUESTION_ID,SELECTED, NUMBER) VALUES (7,1,3,true,1)
INSERT INTO "PUBLIC"."ANSWERED_QUESTIONS_ANSWERS" (ID,ANSWER_TEMPLATE_ID,ANSWERED_QUESTION_ID,SELECTED, NUMBER) VALUES (8,2,3,false,1)
INSERT INTO "PUBLIC"."ANSWERED_QUESTIONS_ANSWERS" (ID,ANSWER_TEMPLATE_ID,ANSWERED_QUESTION_ID,SELECTED, NUMBER) VALUES (9,3,3,true,1)

INSERT INTO "PUBLIC"."ANSWERED_QUESTIONS_ANSWERS" (ID,ANSWER_TEMPLATE_ID,ANSWERED_QUESTION_ID,SELECTED, NUMBER) VALUES (4,4,2,true,2)
INSERT INTO "PUBLIC"."ANSWERED_QUESTIONS_ANSWERS" (ID,ANSWER_TEMPLATE_ID,ANSWERED_QUESTION_ID,SELECTED, NUMBER) VALUES (5,5,2,false,2)
INSERT INTO "PUBLIC"."ANSWERED_QUESTIONS_ANSWERS" (ID,ANSWER_TEMPLATE_ID,ANSWERED_QUESTION_ID,SELECTED, NUMBER) VALUES (6,6,2,false,2)

INSERT INTO "PUBLIC"."ANSWERED_QUESTIONS_ANSWERS" (ID,ANSWER_TEMPLATE_ID,ANSWERED_QUESTION_ID,SELECTED, NUMBER) VALUES (10,4,4,true,2)
INSERT INTO "PUBLIC"."ANSWERED_QUESTIONS_ANSWERS" (ID,ANSWER_TEMPLATE_ID,ANSWERED_QUESTION_ID,SELECTED, NUMBER) VALUES (11,5,4,false,2)
INSERT INTO "PUBLIC"."ANSWERED_QUESTIONS_ANSWERS" (ID,ANSWER_TEMPLATE_ID,ANSWERED_QUESTION_ID,SELECTED, NUMBER) VALUES (12,6,4,false,2)
